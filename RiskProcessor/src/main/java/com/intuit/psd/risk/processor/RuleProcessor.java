package com.intuit.psd.risk.processor;

import com.intuit.psd.risk.interfaces.BusinessObjectModel;import com.intuit.psd.risk.processor.exceptions.RiskException;

public interface RuleProcessor {

	public void processRules(BusinessObjectModel bom) throws RiskException;
	
}
