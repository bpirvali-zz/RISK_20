package com.intuit.bre.ruledata.interfaces;


/*
 * Interface of the thread resource manager
 * */
public interface ThrResMgr {

	public RuleData getRuleData();
	public void putRuleData(RuleDataInternal ruleData);

	/*
	public void removeRuleData(String sThreadName);
	public void removeAllRuleData();
	*/
}