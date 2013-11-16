package com.intuit.psd.risk.risk2.topology.bolts;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import backtype.storm.Config;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;

import com.intuit.psd.risk.processor.RiskProcessor;
import com.intuit.psd.risk.processor.card.CardBatchEventImpl;
import com.intuit.psd.risk.risk2.configmgr.AppConsts;
import com.intuit.psd.risk.risk2.topology.spouts.CardBatchEventSpout;
import com.intuit.psd.risk.risk2.topology.tuples.CardBatchEvent;

public class CardBatchEventBoltTest {
	/*
	 * public void prepare(Map stormConf, TopologyContext context,
	 * OutputCollector collector) {
	 * 
	 * StockMarket marketMock = EasyMock.createMock(StockMarket.class);
	 * EasyMock.expect(marketMock.getPrice("EBAY")).andReturn(42.00);
	 * EasyMock.replay(marketMock); logger.trace("getThisComponentId: " +
	 * context.getThisComponentId()); logger.trace("getThisTaskId:      " +
	 * context.getThisTaskId()); logger.info("Thread-ID:          " +
	 * Thread.currentThread().getId()); logger.info("{}-Index:         {}",
	 * title, context.getThisTaskIndex());
	 */

	@Test
	public void testPrepare() {
		CardBatchEventBolt bolt = new CardBatchEventBolt();

		Config conf = EasyMock.createNiceMock(Config.class);
		EasyMock.expect((String) conf.get(AppConsts.SRE_FAKE_HANG_SECS)).andReturn("0");

		TopologyContext context = EasyMock.createNiceMock(TopologyContext.class);
		EasyMock.expect(context.getThisComponentId()).andReturn("CardBatchEventBolt").once();
		EasyMock.expect(context.getThisTaskId()).andReturn(1).once();
		EasyMock.expect(context.getThisTaskIndex()).andReturn(0).once();
		
		RiskProcessor riskProc = EasyMock.createNiceMock(RiskProcessor.class);
		riskProc.prepare(EasyMock.anyInt());
		EasyMock.expectLastCall().once();
		//EasyMock.expect(riskProc.execute(EasyMock.anyObject(CardBatchEventImpl.class))).andReturn(null).once();		
		bolt.setRiskProcessor(riskProc);
		EasyMock.replay(conf, context, riskProc);
		
		try {
			bolt.prepare(conf, context, null);
		} catch (Throwable t) {
			Assert.fail(t.getMessage(), t);
		}
		EasyMock.verify(conf, context, riskProc);
	}

	@Test
	public void testExecuteHappyPath() {
		CardBatchEventBolt bolt = new CardBatchEventBolt();
		
		Timestamp t1 = getTimestamp("2013-09-18 13:35:43.044");
		CardBatchEvent event = new CardBatchEvent("100", t1, 0, 0, 0, 0);
		
		Tuple input = EasyMock.createNiceMock(Tuple.class);
		EasyMock.expect(input.getValue(0)).andReturn(event).once();
		EasyMock.expect(input.getValueByField(CardBatchEventSpout.CARD_BATCH_RISK_EVENT_TUPLE)).andReturn(event).once();

		RiskProcessor riskProc = EasyMock.createNiceMock(RiskProcessor.class);
		EasyMock.expect(riskProc.execute(EasyMock.anyObject(CardBatchEventImpl.class))).andReturn(null).once();		
		bolt.setRiskProcessor(riskProc);
		
		OutputCollector collector = EasyMock.createNiceMock(OutputCollector.class);
		collector.ack(EasyMock.anyObject(Tuple.class));
		EasyMock.expectLastCall().once();
		bolt.setCollector(collector);
		
		EasyMock.replay(input, riskProc, collector);

		try {
			bolt.execute(input);
		} catch (Throwable t) {
			Assert.fail(t.getMessage(), t);
		}
		EasyMock.verify(input, riskProc);		
	}

	// 2013-09-18 13:35:43.044
	private static Timestamp getTimestamp(String s) {
		Timestamp timestamp = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		java.util.Date parsedDate = null;
		try {
			parsedDate = sdf.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (parsedDate != null)
			timestamp = new Timestamp(parsedDate.getTime());
		return timestamp;
	}

	@BeforeMethod
	public void beforeMethod() {
	}

	@BeforeClass
	public void beforeClass() {
	}

}
