package com.intuit.psd.risk.processor;

import java.text.SimpleDateFormat;

import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.intuit.psd.risk.interfaces.BusinessObjectModel;
import com.intuit.psd.risk.processor.blaze.BlazeRulesEngine;
import com.intuit.psd.risk.processor.card.CardBatchEventImpl;
import com.intuit.psd.risk.processor.card.CardBatchFactoryImpl;
import com.intuit.psd.risk.processor.card.CardBatchRuleFlowImpl;
import com.intuit.psd.risk.processor.card.CardBatchStatusDAOImpl;
import com.intuit.psd.risk.processor.exceptions.RiskException;

public class RiskProcessorTest {

	@Test
	public void processCardEventHappyPath() throws Exception {
		// mock rules engine
		RulesEngine rulesEngine = EasyMock.createMock(RulesEngine.class);
		rulesEngine.processRules(EasyMock.anyObject(BusinessObjectModel.class),
				EasyMock.anyObject(RuleFlowType.class));

		// mock CardBatchStatusDAO
		CardBatchStatusDAOImpl batchCardStatusDAO = EasyMock
				.createMock(CardBatchStatusDAOImpl.class);
		batchCardStatusDAO.markStatusSuccessfullyCompleted(
				EasyMock.anyObject(BusinessObjectModel.class),
				EasyMock.anyInt());

		// prepare input

		SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
		java.util.Date batchDate = dateFormat.parse("07/28/2012");

		CardBatchEventImpl cardEvent = new CardBatchEventImpl();

		cardEvent.setMerchantAccountNumber("1234567890123456");
		cardEvent.setBatchCycleDate(batchDate);
		cardEvent.setEventType("CARD_BATCH");

		// wire it all up
		CardBatchRuleFlowImpl ruleProcessor = new CardBatchRuleFlowImpl();
		ruleProcessor.setRulesEngine(rulesEngine);

		CardBatchFactoryImpl cardBatchFactoryImpl = new CardBatchFactoryImpl();
		cardBatchFactoryImpl.setRuleFlow(ruleProcessor);
		cardBatchFactoryImpl.setStatusDAO(batchCardStatusDAO);

		RiskAbstractFactory riskAbstractFactory = new RiskAbstractFactory();
		riskAbstractFactory.setCardBatchFactoryImpl(cardBatchFactoryImpl);

		RiskProcessorImpl processor = new RiskProcessorImpl();
		processor.setRiskAbstractFactory(riskAbstractFactory);
		processor.setRulesEngine(rulesEngine);

		// run test

		EasyMock.replay(rulesEngine);
		EasyMock.replay(batchCardStatusDAO);

		RiskProcessorResponse response = processor.execute(cardEvent);

		Assert.assertNull(response);

		EasyMock.verify(rulesEngine);
		EasyMock.verify(batchCardStatusDAO);
	}
	
	@Test
	public void processCardEventIsNull() throws Exception {
		// mock rules engine
		RulesEngine rulesEngine = EasyMock.createMock(RulesEngine.class);
		rulesEngine.processRules(EasyMock.anyObject(BusinessObjectModel.class),
				EasyMock.anyObject(RuleFlowType.class));

		// mock CardBatchStatusDAO
		CardBatchStatusDAOImpl batchCardStatusDAO = EasyMock
				.createMock(CardBatchStatusDAOImpl.class);
		batchCardStatusDAO.markStatusSuccessfullyCompleted(
				EasyMock.anyObject(BusinessObjectModel.class),
				EasyMock.anyInt());

	
		// wire it all up
		CardBatchRuleFlowImpl ruleProcessor = new CardBatchRuleFlowImpl();
		ruleProcessor.setRulesEngine(rulesEngine);

		CardBatchFactoryImpl cardBatchFactoryImpl = new CardBatchFactoryImpl();
		cardBatchFactoryImpl.setRuleFlow(ruleProcessor);
		cardBatchFactoryImpl.setStatusDAO(batchCardStatusDAO);

		RiskAbstractFactory riskAbstractFactory = new RiskAbstractFactory();
		riskAbstractFactory.setCardBatchFactoryImpl(cardBatchFactoryImpl);

		RiskProcessorImpl processor = new RiskProcessorImpl();
		processor.setRiskAbstractFactory(riskAbstractFactory);
		processor.setRulesEngine(rulesEngine);

		// run test

		EasyMock.replay(rulesEngine);
		EasyMock.replay(batchCardStatusDAO);

		//CardEvent is null
		RiskProcessorResponse response = processor.execute(null);

		Assert.assertEquals(1, response.getErrorCode());
		Assert.assertNotNull(response.getErrorMessage());

	}


	@Test(expectedExceptions = Exception.class)
	public void processCardEventRulesEngineException() throws Exception {
		// mock rules engine
		RulesEngine rulesEngine = EasyMock.createMock(RulesEngine.class);
		rulesEngine.processRules(EasyMock.anyObject(BusinessObjectModel.class),
				EasyMock.anyObject(RuleFlowType.class));
		EasyMock.expectLastCall()
				.andThrow(
						new Exception(
								"Rules engine unable to process rules because it is being attacked by ninjas!"));

		// mock CardBatchStatusDAO
		CardBatchStatusDAOImpl cardBatchStatusDAO = EasyMock
				.createMock(CardBatchStatusDAOImpl.class);
		EasyMock.expect(
				cardBatchStatusDAO.markStatusFailed(
						EasyMock.anyObject(BusinessObjectModel.class),
						EasyMock.anyObject(RiskException.class),
						EasyMock.anyInt())).andReturn(true);

		// prepare input

		SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
		java.util.Date batchDate = dateFormat.parse("07/28/2012");

		CardBatchEventImpl cardEvent = new CardBatchEventImpl();

		cardEvent.setMerchantAccountNumber("1234567890123456");
		cardEvent.setBatchCycleDate(batchDate);
		cardEvent.setEventType("CARD_BATCH");

		// wire it all up
		CardBatchRuleFlowImpl ruleProcessor = new CardBatchRuleFlowImpl();
		ruleProcessor.setRulesEngine(rulesEngine);

		CardBatchFactoryImpl cardBatchFactoryImpl = new CardBatchFactoryImpl();
		cardBatchFactoryImpl.setRuleFlow(ruleProcessor);
		cardBatchFactoryImpl.setStatusDAO(cardBatchStatusDAO);

		RiskAbstractFactory riskAbstractFactory = new RiskAbstractFactory();
		riskAbstractFactory.setCardBatchFactoryImpl(cardBatchFactoryImpl);

		RiskProcessorImpl processor = new RiskProcessorImpl();
		processor.setRiskAbstractFactory(riskAbstractFactory);
		processor.setRulesEngine(rulesEngine);

		// run test

		EasyMock.replay(rulesEngine);
		EasyMock.replay(cardBatchStatusDAO);

		RiskProcessorResponse response = processor.execute(cardEvent);
		Assert.assertEquals(1, response.getErrorCode());
		Assert.assertNotNull(response.getErrorMessage());

		EasyMock.verify(rulesEngine);
		EasyMock.verify(cardBatchStatusDAO);
	}

	@Test(expectedExceptions = Exception.class)
	public void processCardEventSQLException() throws Exception {
		// mock rules engine
		RulesEngine rulesEngine = EasyMock.createMock(RulesEngine.class);
		rulesEngine.processRules(EasyMock.anyObject(BusinessObjectModel.class),
				EasyMock.anyObject(RuleFlowType.class));

		// mock BatchCardStatusDAO
		CardBatchStatusDAOImpl cardBatchStatusDAO = EasyMock
				.createMock(CardBatchStatusDAOImpl.class);
		cardBatchStatusDAO.markStatusSuccessfullyCompleted(
				EasyMock.anyObject(BusinessObjectModel.class),
				EasyMock.anyInt());
		EasyMock.expectLastCall().andThrow(
				new Exception("Database is being attacked by ninjas!"));
		EasyMock.expect(
				cardBatchStatusDAO.markStatusFailed(
						EasyMock.anyObject(BusinessObjectModel.class),
						EasyMock.anyObject(RiskException.class),
						EasyMock.anyInt())).andReturn(true);
		EasyMock.expectLastCall().andThrow(
				new Exception("Database is being attacked by ninjas!"));

		// prepare input
		SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
		java.util.Date batchDate = dateFormat.parse("07/28/2012");

		CardBatchEventImpl cardEvent = new CardBatchEventImpl();

		cardEvent.setMerchantAccountNumber("1234567890123456");
		cardEvent.setBatchCycleDate(batchDate);
		cardEvent.setEventType("CARD_BATCH");

		// wire it all up
		CardBatchRuleFlowImpl ruleProcessor = new CardBatchRuleFlowImpl();
		ruleProcessor.setRulesEngine(rulesEngine);

		CardBatchFactoryImpl cardBatchFactoryImpl = new CardBatchFactoryImpl();
		cardBatchFactoryImpl.setRuleFlow(ruleProcessor);
		cardBatchFactoryImpl.setStatusDAO(cardBatchStatusDAO);

		RiskAbstractFactory riskAbstractFactory = new RiskAbstractFactory();
		riskAbstractFactory.setCardBatchFactoryImpl(cardBatchFactoryImpl);

		RiskProcessorImpl processor = new RiskProcessorImpl();
		processor.setRiskAbstractFactory(riskAbstractFactory);
		processor.setRulesEngine(rulesEngine);

		// run test
		EasyMock.replay(rulesEngine);
		EasyMock.replay(cardBatchStatusDAO);

		try {
			RiskProcessorResponse response = processor.execute(cardEvent);
			Assert.assertEquals(1, response.getErrorCode());
			Assert.assertNotNull(response.getErrorMessage());
		} catch (Exception e) {
			Assert.fail("execute should not throw an exception");
		}

		EasyMock.verify(rulesEngine);
		EasyMock.verify(cardBatchStatusDAO);
	}

	@Test
	public void cleanupTest() {
		// mock rules engine
		RulesEngine rulesEngine = EasyMock.createMock(RulesEngine.class);
		rulesEngine.shutdown();

		RiskProcessorImpl processor = new RiskProcessorImpl();

		processor.setRulesEngine(rulesEngine);

		// run test
		EasyMock.replay(rulesEngine);

		processor.cleanUp();

		EasyMock.verify(rulesEngine);
	}
	
	@Test
	public void getRulesEngine() {
		RiskProcessorImpl processor = new RiskProcessorImpl();
		BlazeRulesEngine rulesEngine = new BlazeRulesEngine();
		processor.setRulesEngine(rulesEngine);
		Assert.assertEquals(processor.getRulesEngine(), rulesEngine);
	}

	@Test
	public void getRiskAbstractFactory() {
		RiskProcessorImpl processor = new RiskProcessorImpl();
		RiskAbstractFactory riskAbstractFactory = new RiskAbstractFactory();
		processor.setRiskAbstractFactory(riskAbstractFactory);
		Assert.assertEquals(processor.getRiskAbstractFactory(),
				riskAbstractFactory);
	}

}
