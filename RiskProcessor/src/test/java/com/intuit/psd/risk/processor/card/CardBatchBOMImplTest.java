package com.intuit.psd.risk.processor.card;

import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import com.intuit.psd.risk.processor.PojoValidatorTest;
import com.intuit.psd.risk.processor.card.CardBatchBOMImpl;
import com.intuit.psd.risk.processor.card.ViolationAlert;

public class CardBatchBOMImplTest extends PojoValidatorTest {
	
	@Test
	public void testGetterAndSetterMethods() throws Exception {
		
		CardBatchBOMImpl cardBatchBOMImpl = new CardBatchBOMImpl();
		cardBatchBOMImpl.setAccountNumber("123456789");
		SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
  	  	java.util.Date myDate = dateFormat.parse("07/28/2012");
		cardBatchBOMImpl.setBatchCycleDate(myDate);
		
		ViolationAlert violationAlert = new ViolationAlert();    	  	
		violationAlert.setViolatedRuleName("test rule name");
		violationAlert.setViolatedRuleSet("test ruleset");
		violationAlert.setViolatedRuleActualValue("test ruleactualvalue");
		violationAlert.setViolatedRuleDescription("test violationruledescription");
    	violationAlert.setAlertDate(myDate);
    	violationAlert.setBatchCycleDate(myDate);
    	violationAlert.setMerchantAccountNumber("123456789");
    	violationAlert.setCaseType("test casetype");
    	violationAlert.setAlertType("test alerttype");
    	violationAlert.setViolationLevel("test violationlevel");
    	violationAlert.setRuleVersionId("test ruleversionid");
    	violationAlert.setAlertStatus("test alertstatus");
    	violationAlert.setWorkQueueCategory("test workqueuecategory");
    	violationAlert.setDecision("test decision");
    	
    	List<ViolationAlert> violationAlerts = new ArrayList<ViolationAlert>();
    	violationAlerts.add(violationAlert);
    	cardBatchBOMImpl.setViolationAlerts(violationAlerts);
    	
    	Assert.assertEquals(cardBatchBOMImpl.getAccountNumber(), "123456789");
    	Assert.assertEquals(cardBatchBOMImpl.getBatchCycleDate().getTime(), myDate.getTime()); 
    	Assert.assertEquals(cardBatchBOMImpl.getViolationAlerts().get(0).getViolatedRuleName(), "test rule name");
       	Assert.assertEquals(cardBatchBOMImpl.getViolationAlerts().get(0).getViolatedRuleSet(), "test ruleset");
    	Assert.assertEquals(cardBatchBOMImpl.getViolationAlerts().get(0).getViolatedRuleActualValue(), "test ruleactualvalue");
    	Assert.assertEquals(cardBatchBOMImpl.getViolationAlerts().get(0).getViolatedRuleDescription(), "test violationruledescription");
    	Assert.assertEquals(cardBatchBOMImpl.getViolationAlerts().get(0).getAlertDate().getTime(), myDate.getTime()); 
    	Assert.assertEquals(cardBatchBOMImpl.getViolationAlerts().get(0).getBatchCycleDate().getTime(), myDate.getTime()); 
    	Assert.assertEquals(cardBatchBOMImpl.getViolationAlerts().get(0).getMerchantAccountNumber(), "123456789");
    	Assert.assertEquals(cardBatchBOMImpl.getViolationAlerts().get(0).getCaseType(), "test casetype");
    	Assert.assertEquals(cardBatchBOMImpl.getViolationAlerts().get(0).getAlertType(), "test alerttype");
    	Assert.assertEquals(cardBatchBOMImpl.getViolationAlerts().get(0).getViolationLevel(), "test violationlevel");
    	Assert.assertEquals(cardBatchBOMImpl.getViolationAlerts().get(0).getRuleVersionId(), "test ruleversionid");
    	Assert.assertEquals(cardBatchBOMImpl.getViolationAlerts().get(0).getAlertStatus(), "test alertstatus");
    	Assert.assertEquals(cardBatchBOMImpl.getViolationAlerts().get(0).getWorkQueueCategory(), "test workqueuecategory");
    	Assert.assertEquals(cardBatchBOMImpl.getViolationAlerts().get(0).getDecision(), "test decision");

    	/*String temp ="CardBatchBOMImpl [accountNumber=123456789, batchCycleDate=Sat Jul 28 00:00:00 PDT 2012, violationAlerts=violatedRuleName: test rule name"+
    			", violatedRuleSet: test ruleset"+
    			", violatedRuleActualValue: test ruleactualvalue"+
    			", violatedRuleDescription: test violationruledescription"+
    			", alertDate: Sat Jul 28 00:00:00 PDT 2012"+
    			", batchCycleDate: Sat Jul 28 00:00:00 PDT 2012"+
    			", merchantAccountNumber: 123456789"+
    			", caseType: test casetype"+
    			", alertType: test alerttype"+
    			", violationLevel: test violationlevel"+
    			", ruleVersionId: test ruleversionid"+
    			", alertStatus: test alertstatus"+
    			", workQueueCategory: test workqueuecategory"+
    			", decision: test decision]";
    	System.out.println("Test toString()"+cardBatchBOMImpl.toString().trim().compareTo(temp));
    	Assert.assertEquals(cardBatchBOMImpl.toString().trim(),temp.trim());*/
    	
	}
	
	
}
