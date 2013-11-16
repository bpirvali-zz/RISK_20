package com.intuit.psd.risk.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.intuit.psd.risk.interfaces.BusinessObjectModel;
import com.intuit.psd.risk.interfaces.RiskFactory;
import com.intuit.psd.risk.interfaces.StatusDAO;
import com.intuit.psd.risk.processor.exceptions.RiskException;

/**
 * This is the main Risk Processor class responsible for processing risk events
 * per merchant. The execute method is invoked by the Storm bolt. In case of
 * exception, return 1 to the caller (do not throw any exceptions back to the
 * caller).
 * 
 * @author asookazian
 * 
 */

public class RiskProcessorImpl implements RiskProcessor {

    private static Logger logger = LoggerFactory.getLogger(RiskProcessorImpl.class);
    private final static int failCode = 1;
    private int workerID = -1;
	private RulesEngine rulesEngine;
    private RiskAbstractFactory riskAbstractFactory;
    
    public RiskAbstractFactory getRiskAbstractFactory() {
		return riskAbstractFactory;
	}

	public void setRiskAbstractFactory(RiskAbstractFactory riskAbstractFactory) {
		this.riskAbstractFactory = riskAbstractFactory;
	}

    public void prepare(int workerID) throws RiskException {
    	logger.debug("Entering RiskProcessor.prepare(workerID=" + workerID + ")...");
    	this.workerID = workerID;
    	rulesEngine.startup();
    }
    
	/**
	 * Get instance of RiskProcessorImpl
	 * @return instance
	 * @throws RiskException if unable to get instance
	 */
	static public RiskProcessorImpl getInstance() throws RiskException {
		ApplicationContext context =
			    new ClassPathXmlApplicationContext(new String[] {"RiskProcessorApplicationContext.xml"});
				//NOTE: Spring will dependency inject RiskAbstractFactory and RulesEngine instances.
			
		RiskProcessorImpl riskProcessor = (RiskProcessorImpl)context.getBean("riskProcessorImpl", RiskProcessorImpl.class);
		RiskAbstractFactory riskAbstractFactory = (RiskAbstractFactory)context.getBean("riskAbstractFactory");
		riskProcessor.setRiskAbstractFactory(riskAbstractFactory);
		logger.info("Spring container is now bootstrapped!");
		return riskProcessor;
	}



    /**
     * This method is called form STORM bolt.execute() method. Purpose of this
     * method is to process the merchant. Method returns 0 on success and 1 for
     * error.
     * 
     * @param riskEvent
     * @param eventType
     * @return
     */
    //TODO: add a null check for riskEvent
    public RiskProcessorResponse execute(RiskEvent riskEvent) {
		logger.info("begin RiskProcessor.execute()");
		// use abstract factory pattern to get classes specific for event type
		RiskFactory riskFactory = null;
		if (riskEvent != null) {
			riskFactory = riskAbstractFactory.getRiskFactory(riskEvent.getEventType());
		}
		else {
			RiskProcessorResponse response = new RiskProcessorResponse();
			response.setErrorCode(failCode);
			response.setErrorMessage("riskEvent is null in RiskProcessor.execute()");
			response.setError(new RiskException("riskEvent is null in RiskProcessor.execute()"));
			return response;
		}
			
		RuleProcessor ruleFlow = riskFactory.getRuleFlowInstance();
		StatusDAO statusDAO = riskFactory.getStatusDAOInstance();
		BusinessObjectModel bom = null;
		RiskProcessorResponse response = null;
		
		try {
			//create BOM
		    bom = riskFactory.createBOM(riskEvent);
	
		    //process the rules via rules engine
		    ruleFlow.processRules(bom);
	
		    // insert into merchantsalerts and update Incoming Risk Events tables
		    statusDAO.markStatusSuccessfullyCompleted(bom, workerID);
		    return response;
		} catch (Throwable error) {
		    try {
		    	statusDAO.markStatusFailed(bom, error, workerID);
		    } catch (RiskException e) {
		    	logger.error("Exception occurred: " + e.getMessage());
		    }
		    response = new RiskProcessorResponse();
			response.setErrorCode(failCode);
			response.setError(error);
			return response;
		}
    }

    public void cleanUp() throws RiskException {
    	rulesEngine.shutdown();
    }
    
	public RulesEngine getRulesEngine() {
		return rulesEngine;
	}

	public void setRulesEngine(RulesEngine rulesEngine) {
		this.rulesEngine = rulesEngine;
	}
    
  
}
