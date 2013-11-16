package com.intuit.psd.risk.risk2.topology.bolts;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intuit.psd.risk.interfaces.BusinessObjectModel;
import com.intuit.psd.risk.processor.RuleFlowType;
import com.intuit.psd.risk.processor.RulesEngine;
import com.intuit.psd.risk.processor.card.CardBatchBOMImpl;
import com.intuit.psd.risk.processor.card.ViolationAlert;
import com.intuit.psd.risk.processor.exceptions.RiskException;

public class BlazeRulesEngine_Fake implements RulesEngine {
	private static final Logger logger = LoggerFactory.getLogger(BlazeRulesEngine_Fake.class);

	@Override
	public void startup() throws RiskException {
		logger.info("Starting the <<< FAKE >>> Rule-Engine...");

	}

	@Override
	public void shutdown() throws RiskException {
		logger.info("Shutting down the <<< FAKE >>> Rule-Engine...");

	}

	@Override
	public void processRules(BusinessObjectModel bom, RuleFlowType ruleFlowType)
			throws RiskException {
		logger.info("Process Rules with the <<< FAKE >>> Rule-Engine...");
		
		CardBatchBOMImpl cbm = (CardBatchBOMImpl)bom;
		if (cbm.getAccountNumber().equals("111"))
			throw new RuntimeException("Test Exception for merchant: '111'!");
		List<ViolationAlert> violationAlerts = new ArrayList<ViolationAlert>();
		ViolationAlert va = new ViolationAlert();
		va.setAlertDate(cbm.getBatchCycleDate());
		va.setAlertStatus("Alert Status");
		va.setAlertType("Alert Type");
		va.setBatchCycleDate(cbm.getBatchCycleDate());
		va.setCaseType("Case Type");
		va.setDecision("Decision");
		va.setMerchantAccountNumber(cbm.getAccountNumber());
		va.setRuleVersionId("Rule Version ID");
		va.setViolatedRuleActualValue("Violated Rule Actual Value");
		va.setViolatedRuleDescription("Violated Rule Description");
		va.setViolatedRuleName("Violated Rule Name");
		va.setViolatedRuleSet("Violated Rule Set");
		va.setViolationLevel("Violation Level");
		va.setWorkQueueCategory("Work Queue Category");
		
		violationAlerts.add(va);
		cbm.setViolationAlerts(violationAlerts);
	}

}
