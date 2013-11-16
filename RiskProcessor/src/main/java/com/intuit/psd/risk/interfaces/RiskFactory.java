package com.intuit.psd.risk.interfaces;

import com.intuit.psd.risk.processor.RiskEvent;
import com.intuit.psd.risk.processor.RuleProcessor;
import com.intuit.psd.risk.processor.exceptions.RiskException;

/**
 * @author asookazian
 *
 */
public interface RiskFactory {
	
	public StatusDAO getStatusDAOInstance();
	public RuleProcessor getRuleFlowInstance();
	public BusinessObjectModel createBOM(RiskEvent riskEvent) throws RiskException;
	
}
