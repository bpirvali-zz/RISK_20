package com.intuit.psd.risk.processor;

import com.intuit.psd.risk.interfaces.BusinessObjectModel;
import com.intuit.psd.risk.processor.exceptions.RiskException;

/**
 * @author asookazian
 *
 */
public interface RulesEngine {
	
	public void startup() throws RiskException;
	
	public void shutdown() throws RiskException;

    public void processRules(BusinessObjectModel bom, RuleFlowType ruleFlowType)
    	    throws RiskException;
	
}
