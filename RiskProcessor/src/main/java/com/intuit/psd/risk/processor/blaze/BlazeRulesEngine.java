package com.intuit.psd.risk.processor.blaze;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blazesoft.server.base.NdServerException;
import com.blazesoft.server.base.NdServiceException;
import com.blazesoft.server.base.NdServiceSessionException;
import com.blazesoft.server.local.NdLocalServerException;
import com.intuit.psd.risk.interfaces.BusinessObjectModel;
import com.intuit.psd.risk.processor.RuleFlowType;
import com.intuit.psd.risk.processor.RulesEngine;
import com.intuit.psd.risk.processor.card.CardBatchBOMImpl;
import com.intuit.psd.risk.processor.exceptions.RiskException;

public class BlazeRulesEngine implements RulesEngine {

    private static Logger logger = LoggerFactory.getLogger(BlazeRulesEngine.class);

    private volatile RiskServer server;
    private final static String ruleService = "RiskRuleService";
    
    private final static String cardBatchEntryPoint = "processCardDailyBatchRules";

    public synchronized void startup() throws RiskException {
		try {
		    // Create the server
			if (server == null) {
			    server = (RiskServer) RiskServer.createServer("/opt/oasis/config/RiskRules/RiskServer.server");
			    logger.info("Blaze has been started!");
			} 
//			else {
//				throw new RiskException("Server has already been started");
//			}
		} catch (NdLocalServerException e1) {
		    e1.printStackTrace();
		    throw new RiskException(
			    "Error occurred while creating Blaze server: "
				    + e1.getMessage());
		}    	
    }
    
    public synchronized void shutdown() throws RiskException {
    	if (server!=null) {
			try {
				server.shutdown();
				logger.info("Blaze has been stopped!");
			} catch (NdServerException e1) {
			    e1.printStackTrace();
			    throw new RiskException(
				    "Error occurred while shutting down Blaze server: "
					    + e1.getMessage());
			} finally {
				server = null;
			}
    	}
    }

    public void processRules(BusinessObjectModel bom, RuleFlowType ruleFlowType)
    	    throws RiskException {
        	

    	try {
	    	switch (ruleFlowType) {
	    		case CARD_BATCH:
	    			server.processCardBatchRules((CardBatchBOMImpl)bom, ruleService, cardBatchEntryPoint);
	    			//server.processCardBatchRulesFake((CardBatchBOMImpl)bom, ruleService, cardBatchEntryPoint);
	    			break;
	    		default:
	    			throw new RiskException("Unsupported rule flow type " + ruleFlowType);
	    	}
    	} catch (NdServiceSessionException e) {
    	    logger.error("Exception occurred: " + e.getMessage());
    	    throw new RiskException(
    		    "NdServiceSessionException occurred while processing rules: "
    			    + e.getMessage());
    	} catch (NdServiceException e) {
    	    logger.error("Exception occurred: " + e.getMessage());
    	    throw new RiskException(
    		    "NdServiceException occurred while processing rules: "
    			    + e.getMessage());
    	} catch (NdServerException e) {
    	    logger.error("Exception occurred: " + e.getMessage());
    	    throw new RiskException(
    		    "NdServerException occurred while processing rules: "
    			    + e.getMessage());
    	}
    	logger.info("rules engine call done");

    }
    
    
}
