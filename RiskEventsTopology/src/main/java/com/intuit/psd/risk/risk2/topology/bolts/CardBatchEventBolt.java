package com.intuit.psd.risk.risk2.topology.bolts;

import java.util.Map;

//import org.easymock.EasyMock;
//import static org.easymock.EasyMock.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import com.intuit.psd.risk.processor.RiskAbstractFactory;
import com.intuit.psd.risk.processor.RiskEvent;
import com.intuit.psd.risk.processor.RiskProcessor;
import com.intuit.psd.risk.processor.RiskProcessorResponse;
import com.intuit.psd.risk.processor.card.CardBatchEventImpl;
import com.intuit.psd.risk.risk2.configmgr.AppConsts;
import com.intuit.psd.risk.risk2.topology.SpringObjectMgr;
import com.intuit.psd.risk.risk2.topology.Util;
import com.intuit.psd.risk.risk2.topology.spouts.CardBatchEventSpout;
import com.intuit.psd.risk.risk2.topology.tuples.CardBatchEvent;

public class CardBatchEventBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(CardBatchEventBolt.class);

	// @SuppressWarnings("rawtypes")
	// private Map mapStormConfig = null;
	private TopologyContext topologyCtx = null;
	private OutputCollector collector = null;
	private transient RiskProcessor riskProcessor = null;
	//private static ApplicationContext springCtx = null;
	//private long timeOutMiliSecs;
	private long fakeHangMiliSecs;
	
	private int boltID;

	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		// save input
		// mapStormConfig = stormConf;
		topologyCtx = context;
		this.collector = collector;
		//this.timeOutMiliSecs		= (Long)stormConf.get(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS) * 1000;
		this.fakeHangMiliSecs		= Long.valueOf((String)stormConf.get(AppConsts.SRE_FAKE_HANG_SECS)) * 1000;
		this.boltID		= topologyCtx.getThisTaskIndex();
		Util.logStartupInfo("Bolt", context);
		//Util.logClassPath();

		if (riskProcessor==null) 
			riskProcessor = (RiskProcessor)SpringObjectMgr.getInstance().getBean("riskProcessorImpl");
			//riskProcessor = (RiskProcessor)createMockedProcessor();
		
		riskProcessor.prepare(boltID);
	}
	
//	private static synchronized ApplicationContext getContext(String ctxFile) {
//		if (CardBatchEventBolt.springCtx == null)
//			CardBatchEventBolt.springCtx = new ClassPathXmlApplicationContext(
//					"RiskProcessorApplicationContext.xml");
//		return CardBatchEventBolt.springCtx;
//	}

	public void execute(Tuple input) {
		Object obj = input.getValue(0);
		
		// check object type
		if (!(obj instanceof CardBatchEvent))
			throw new RuntimeException("input tuple:[" + obj.toString()
					+ "] not instance of MerchantTuble");
		
		// check the tuple type declared matches
		if (obj != input.getValueByField(CardBatchEventSpout.CARD_BATCH_RISK_EVENT_TUPLE))
			throw new RuntimeException("obj!=input.getValueByField");

		CardBatchEvent event = (CardBatchEvent) obj;
		RiskEvent cardBatchEvent = new CardBatchEventImpl(event.accNo,
				event.eventTimestamp, 
				event.systemID,
				event.eventCatID,
				event.eventTypeID,
				event.eventID,
				RiskAbstractFactory.EVENT_TYPE_RISK_CARD);
		fakeBoltHang(event);
		if (fakeHangMiliSecs>0)
			return;
		RiskProcessorResponse response = riskProcessor.execute(cardBatchEvent);		
		if (response == null) {
			logger.info("Bolt[{}]:Thread[{}]:ACK:[{}]", boltID, Thread.currentThread().getId(),
					event.accNo);
			collector.ack(input);
		} else {
			logger.error("Bolt[{}]:Thread[{}]:FAIL:[{}]", boltID, Thread.currentThread().getId(),
					event.toString() );
			logger.error(response.getErrorMessage(), response.getError());
			collector.fail(input);
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		logger.trace("entering Bolt:declareOutputFields...");
	}
	
	//@SuppressWarnings("unused")
	private void fakeBoltHang(CardBatchEvent event) {
		if (fakeHangMiliSecs>0) {
			logger.info("-------------------------");
			logger.info("-- FAKE BOLT HANG --");
			logger.info("retries  : {}", event.getRetries());
			logger.info("sleep for: {} [msec]", fakeHangMiliSecs);
			logger.info("-------------------------");
			try {
				Thread.sleep(fakeHangMiliSecs);
			} catch (InterruptedException e) {
				logger.info("Faked hang sleep interrupted!");
				//logger.error(e.getMessage(), e);
			}
		}
	}

//	public RiskProcessor getRiskProcessor() {
//		return riskProcessor;
//	}

	public void setRiskProcessor(RiskProcessor riskProcessor) {
		this.riskProcessor = riskProcessor;
	}

//	public OutputCollector getCollector() {
//		return collector;
//	}

	public void setCollector(OutputCollector collector) {
		this.collector = collector;
	}
}
