//
// Blaze Advisor Server Deployment.
// Created by the Blaze Advisor Generate Deployment Wizard
//
// Copyright (1997-2013),Fair Isaac Corporation. All Rights Reserved.
// 
//

package com.intuit.psd.risk.processor.blaze;

import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import com.blazesoft.server.base.NdServerException;
import com.blazesoft.server.base.NdServiceException;
import com.blazesoft.server.base.NdServiceSessionException;
import com.blazesoft.server.config.NdServerConfig;
import com.blazesoft.server.deploy.NdStatelessServer;
import com.blazesoft.server.local.NdLocalServerException;
import com.intuit.psd.risk.processor.card.ViolationAlert;

//==> Import Application specific code here

/**
 *	This class implements a stateless server based on RiskServer
 *	It is a subclass of <code>com.blazesoft.server.deploy.NdStatelessServer</code>.
 */
public class RiskServer extends NdStatelessServer

{
	/**
	 *	Constructor. Calls the constructor of the superclass.
	 *	@param	serverConfig	Server configuration to use
	 *
 	 *	@exception NdLocalServerException if construction of the server fails.
	 */
	private boolean firstTry = true;
	public RiskServer(NdServerConfig serverConfig)
			throws NdLocalServerException
	{
		super(serverConfig);
	}

	/**
	 *	Invokes a server through the entry point [entryPoint] in the service [ruleService].
	 *
	 *	@param	arg0		==> Enter a description here
	 *	@param  ruleService
	 *  @param  entryPoint
	 */
	public void processCardBatchRules(com.intuit.psd.risk.processor.card.CardBatchBOMImpl arg0, String ruleService, String entryPoint)
			throws NdServerException, NdServiceException, NdServiceSessionException
	{
		// Build the argument list
		Object[] applicationArgs = new Object[1];
		applicationArgs[0] = arg0;
			

		// Invoke the service and returns its result, if any.
		invokeService(ruleService, entryPoint, null, applicationArgs);
		
	}
	
	public void processCardBatchRulesFake(com.intuit.psd.risk.processor.card.CardBatchBOMImpl bom, String ruleService, String entryPoint)
			throws NdServerException, NdServiceException, NdServiceSessionException
	{
		List<ViolationAlert> violationAlerts = new ArrayList<ViolationAlert>();
		ViolationAlert va = new ViolationAlert();
		va.setAlertDate(bom.getBatchCycleDate());
		va.setAlertStatus("Alert Status");
		va.setAlertType("Alert Type");
		va.setBatchCycleDate(bom.getBatchCycleDate());
		va.setCaseType("Case Type");
		va.setDecision("Decision");
		va.setMerchantAccountNumber(bom.getAccountNumber());
		va.setRuleVersionId("Rule Version ID");
		va.setViolatedRuleActualValue("Violated Rule Actual Value");
		va.setViolatedRuleDescription("Violated Rule Description");
		va.setViolatedRuleName("Violated Rule Name");
		va.setViolatedRuleSet("Violated Rule Set");
		va.setViolationLevel("Violation Level");
		va.setWorkQueueCategory("Work Queue Category");
		
		violationAlerts.add(va);
		bom.setViolationAlerts(violationAlerts);
		if (firstTry) {
			firstTry=false;
			throw new RuntimeException("This is a test Run Time Exception");
		} 
		//System.out.println(bom.toString());
	}

}
