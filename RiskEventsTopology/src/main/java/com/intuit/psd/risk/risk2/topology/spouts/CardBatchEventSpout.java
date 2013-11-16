package com.intuit.psd.risk.risk2.topology.spouts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.intuit.psd.risk.risk2.configmgr.AppConsts;
import com.intuit.psd.risk.risk2.topology.Util;
import com.intuit.psd.risk.risk2.topology.interfaces.EventFeederDAO;
import com.intuit.psd.risk.risk2.topology.tuples.CardBatchEvent;


public class CardBatchEventSpout extends BaseRichSpout {
	private static final long 	serialVersionUID = 1;		
	private static final Logger logger = LoggerFactory.getLogger(CardBatchEventSpout.class);
	public static final String CARD_BATCH_RISK_EVENT_TUPLE = "CardBatchEvent";
	
	private int 				workerID;
	private int 				maxRetries;
	private int 				batchSize;
	private int                 spoutSleep;
	private int                 spoutSleepSecs;
	private long				timeOutSecs;
	
	private EventFeederDAO 		eventFeeder;
	
	//@SuppressWarnings("rawtypes") 
	//private Map mapStormConfig 				= null;
	private TopologyContext topologyCtx 	= null;
	private SpoutOutputCollector collector	= null;
	
	private Map<String, CardBatchEvent> toProcessEvents = new HashMap<String, CardBatchEvent>();
	private Map<String, CardBatchEvent> toReplayEvents = new HashMap<String, CardBatchEvent>();

	//private boolean firstLoopDone=false;
//	public EventFeederDAO getEventFeeder() {
//		return eventFeeder;
//	}

	public void seteventFeeder(EventFeederDAO eventFeeder) {
		this.eventFeeder = eventFeeder;
	}

	public void open(@SuppressWarnings("rawtypes") Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		
		// save input 
		//this.mapStormConfig 	= conf;
		this.topologyCtx 		= context;
		this.collector 			= collector;
		this.workerID 			= topologyCtx.getThisTaskIndex();		
		this.maxRetries			= Integer.valueOf((String)conf.get(AppConsts.SRE_MAX_RETRIES));
		this.batchSize 			= Integer.valueOf((String)conf.get(AppConsts.SRE_BATCH_SIZE));
		this.spoutSleepSecs		= Integer.valueOf((String)conf.get(AppConsts.SRE_SPOUT_SLEEP_SECS));
		this.spoutSleep 		= spoutSleepSecs * 1000;
		this.timeOutSecs		= (Long)conf.get(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS);
		//this.spoutHBFrequency 	= Integer.valueOf((String)conf.get(AppConsts.SRE_SPOUT_HEART_BEAT_SLEEPS));
		//this.counterHB			= this.spoutHBFrequency;
		
		Util.logStartupInfo("Spout", context);
		List<Object> l = new ArrayList<Object>();
		l.add(workerID);
		l.add(maxRetries);		
		l.add(batchSize);
		eventFeeder.init(l);
	}

	@SuppressWarnings("unchecked")
	public void nextTuple() {
		// Read from Spout Source
		if (toReplayEvents.isEmpty()) {
			toReplayEvents = (Map<String,CardBatchEvent>)eventFeeder.getNextRiskEventList();
			if (toReplayEvents!=null && toReplayEvents.size()>0) 
				toProcessEvents.putAll(toReplayEvents);
		} 
		
		// Do I have anything to send?
		if (toProcessEvents.size()>0) {
			for (Map.Entry<String, CardBatchEvent> entry: toProcessEvents.entrySet()) {
				CardBatchEvent event = entry.getValue();
				logger.trace("Spout[{}]:Thread[{}]:emitting:[{}]...", workerID, Thread.currentThread().getId(), event.toString());
				collector.emit(new Values(event), event);
			} 
			toProcessEvents.clear();
		// I do not have anything to send --> Send the heart beat go to sleep and exit!
		} else {
			try {
				// TODO:BP Send a heart beat PLEASE, PLEASE, PLEEEEEEEEEEASE!!!
				logger.trace("Spout[{}]:Thread[{}]:Nothing to emit sleeping for {} [sec]...", workerID, Thread.currentThread().getId(), spoutSleepSecs);
				Thread.sleep(spoutSleep);
			} catch (InterruptedException e) {
				logger.info("Spout[{}]:Thread[{}]:Interrupted for shutdown...", workerID, Thread.currentThread().getId());
			}
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(CARD_BATCH_RISK_EVENT_TUPLE));
	}

    @Override
    public void ack(Object msgId) {
		CardBatchEvent event = (CardBatchEvent)msgId;
		logger.trace("Spout[{}]:Thread[{}]:ACK:{}", workerID, Thread.currentThread().getId(), event.accNo);
		toReplayEvents.remove(event.getKey());		
	}
    
    @Override
    public void fail(Object msgId) {
		logger.error("Spout[{}]:Thread[{}]:FAIL:[{}]:HashCode:{}", workerID, Thread.currentThread().getId(), msgId, msgId.hashCode());    
		CardBatchEvent e = (CardBatchEvent)msgId;
		CardBatchEvent event = toReplayEvents.get(e.getKey());
		if (event==null)
			return;
		
		// check whether Hang happened and handle the extra work for Hang!
		if (isMessageTimeOut(event)) {
    		displayHangError(event);
    		eventFeeder.updateForMerchantHang(event);
		}
		
		// Handle retries!!!
		int retries = event.getRetries();
		if (retries<maxRetries) {
			event.setRetries(retries+1);
			event.setTimeStartProcessing(System.currentTimeMillis());
			toProcessEvents.put(event.getKey(), event);
		} else {
			logger.info("------------------------------");
			logger.info("!!! MAX Retry reached !!!"); 
			logger.info("Removing   : {}", event.accNo); 
			logger.info("retries    : {}", retries); 
			logger.info("max retries: {}", maxRetries); 
			logger.info("------------------------------");
			toReplayEvents.remove(event.getKey());
		}
    }	
    
    private boolean isMessageTimeOut(CardBatchEvent e) {
    	long timeTooktoProcessSecs = (System.currentTimeMillis() - e.getTimeStartProcessing())/1000;
    	if (timeTooktoProcessSecs>timeOutSecs) 
    		return true;
    	
    	return false;
    }
    
    private void displayHangError(CardBatchEvent e) {
    	long timeTooktoProcessSecs = (System.currentTimeMillis() - e.getTimeStartProcessing())/1000;
		logger.error("------------------------------");
		logger.error("------------------------------");
		logger.error("!!! Oops BOLT HANG !!!"); 
		logger.error("Timeout time [sec]     :{}", timeOutSecs); 
		logger.error("Hang detected in [sec] :{}", timeTooktoProcessSecs); 
		logger.error("Current Retries        :{}",e.getRetries());
		logger.error("------------------------------");
		logger.error("------------------------------");    	
    }
}
