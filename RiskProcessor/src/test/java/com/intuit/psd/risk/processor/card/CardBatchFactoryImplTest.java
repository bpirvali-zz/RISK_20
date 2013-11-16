package com.intuit.psd.risk.processor.card;

import java.text.SimpleDateFormat;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.testng.annotations.Test;

import com.intuit.psd.risk.interfaces.StatusDAO;
import com.intuit.psd.risk.processor.RuleProcessor;

public class CardBatchFactoryImplTest {

    @Test
    public void createBOM() throws Exception {
	SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
	java.util.Date batchDate = dateFormat.parse("07/28/2012");

	CardBatchFactoryImpl riskFactory = new CardBatchFactoryImpl();
	CardBatchEventImpl riskEvent = new CardBatchEventImpl();
	riskEvent.setMerchantAccountNumber("1234567890123456");
	riskEvent.setBatchCycleDate(batchDate);
	riskEvent.setEventType("RISK_CARD");
	
	StatusDAO statusDAO = EasyMock.createMock(StatusDAO.class);
	RuleProcessor ruleFlow = EasyMock.createMock(RuleProcessor.class);
	riskFactory.setRuleFlow(ruleFlow);
	riskFactory.setStatusDAO(statusDAO);

	CardBatchBOMImpl bom = (CardBatchBOMImpl) riskFactory
		.createBOM(riskEvent);
	
	Assert.assertEquals(riskEvent.getMerchantAccountNumber(),
		bom.getAccountNumber());
	Assert.assertNotNull(riskFactory.getRuleFlow());
	Assert.assertNotNull(riskFactory.getStatusDAO());
	Assert.assertEquals(riskEvent.getBatchCycleDate(),
		bom.getBatchCycleDate());
	Assert.assertNotNull(bom.getViolationAlerts());
	Assert.assertEquals(0, bom.getViolationAlerts().size());
    }
}
