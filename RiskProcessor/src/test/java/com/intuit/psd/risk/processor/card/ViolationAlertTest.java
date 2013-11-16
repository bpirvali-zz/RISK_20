package com.intuit.psd.risk.processor.card;

import java.text.SimpleDateFormat;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.intuit.psd.risk.processor.PojoValidatorTest;
import com.intuit.psd.risk.processor.card.ViolationAlert;

public class ViolationAlertTest extends PojoValidatorTest {
	
	@Test
	public void testGetterAndSetterMethods() throws Exception {
		ViolationAlert violationAlert = new ViolationAlert();  
		SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
  	  	java.util.Date myDate = dateFormat.parse("07/28/2012");
  	  	
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
    	
    	Assert.assertEquals(violationAlert.getViolatedRuleName(), "test rule name");
    	Assert.assertEquals(violationAlert.getViolatedRuleSet(), "test ruleset");
    	Assert.assertEquals(violationAlert.getViolatedRuleActualValue(), "test ruleactualvalue");
    	Assert.assertEquals(violationAlert.getViolatedRuleDescription(), "test violationruledescription");
    	Assert.assertEquals(violationAlert.getAlertDate().getTime(), myDate.getTime()); 
    	Assert.assertEquals(violationAlert.getBatchCycleDate().getTime(), myDate.getTime()); 
    	Assert.assertEquals(violationAlert.getMerchantAccountNumber(), "123456789");
    	Assert.assertEquals(violationAlert.getCaseType(), "test casetype");
    	Assert.assertEquals(violationAlert.getAlertType(), "test alerttype");
    	Assert.assertEquals(violationAlert.getViolationLevel(), "test violationlevel");
    	Assert.assertEquals(violationAlert.getRuleVersionId(), "test ruleversionid");
    	Assert.assertEquals(violationAlert.getAlertStatus(), "test alertstatus");
    	Assert.assertEquals(violationAlert.getWorkQueueCategory(), "test workqueuecategory");
    	Assert.assertEquals(violationAlert.getDecision(), "test decision");
    	
	}
	
	
}
