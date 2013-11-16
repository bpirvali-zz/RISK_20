package com.intuit.psd.risk.processor.card;

import java.util.Date;

/**
 * This class is used to encapsulate the violation alert info that are produced by the Blaze engine.
 * @author asookazian
 *
 */
public class ViolationAlert {
	
	private String violatedRuleName;
	private String violatedRuleSet;
	private String violatedRuleActualValue;
	private String violatedRuleDescription;
	private Date alertDate;
	private Date batchCycleDate;
	private String merchantAccountNumber;
	private String caseType;
	private String alertType;
	//private String workQueue;
	private String violationLevel;
	//private String workQueueCategory;
	private String ruleVersionId;
	private String alertStatus ;
	private String workQueueCategory;
	private String decision ;

	
	public String getViolatedRuleName() {
		return violatedRuleName;
	}
	public void setViolatedRuleName(String violatedRuleName) {
		this.violatedRuleName = violatedRuleName;
	}
	public String getViolatedRuleSet() {
		return violatedRuleSet;
	}
	public void setViolatedRuleSet(String violatedRuleSet) {
		this.violatedRuleSet = violatedRuleSet;
	}
	public String getViolatedRuleActualValue() {
		return violatedRuleActualValue;
	}
	public void setViolatedRuleActualValue(String violatedRuleActualValue) {
		this.violatedRuleActualValue = violatedRuleActualValue;
	}
	public String getViolatedRuleDescription() {
		return violatedRuleDescription;
	}
	public void setViolatedRuleDescription(String violatedRuleDescription) {
		this.violatedRuleDescription = violatedRuleDescription;
	}
	public Date getAlertDate() {
		return alertDate;
	}
	public void setAlertDate(Date alertDate) {
		this.alertDate = alertDate;
	}
	public Date getBatchCycleDate() {
		return batchCycleDate;
	}
	public void setBatchCycleDate(Date batchCycleDate) {
		this.batchCycleDate = batchCycleDate;
	}
	public String getMerchantAccountNumber() {
		return merchantAccountNumber;
	}
	public void setMerchantAccountNumber(String merchantAccountNumber) {
		this.merchantAccountNumber = merchantAccountNumber;
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public String getViolationLevel() {
		return violationLevel;
	}
	public void setViolationLevel(String violationLevel) {
		this.violationLevel = violationLevel;
	}
	public String getRuleVersionId() {
		return ruleVersionId;
	}
	public void setRuleVersionId(String ruleVersionId) {
		this.ruleVersionId = ruleVersionId;
	}	
	public String getAlertStatus() {
	    return alertStatus;
	}
	public void setAlertStatus(String alertStatus) {
	    this.alertStatus = alertStatus;
	}
	public String getWorkQueueCategory() {
	    return workQueueCategory;
	}
	public void setWorkQueueCategory(String workQueueCategory) {
	    this.workQueueCategory = workQueueCategory;
	}
	public String getDecision() {
	    return decision;
	}
	public void setDecision(String decision) {
	    this.decision = decision;
	}
	/*@Override
	public String toString() {
		return "violatedRuleName: "+violatedRuleName+
		"\n, violatedRuleSet: "+violatedRuleSet+
		"\n, violatedRuleActualValue: "+violatedRuleActualValue+
		"\n, violatedRuleDescription: "+violatedRuleDescription+
		"\n, alertDate: "+alertDate+
		"\n, batchCycleDate: "+batchCycleDate+
		"\n, merchantAccountNumber: "+merchantAccountNumber+
		"\n, caseType: "+caseType+
		"\n, alertType: "+alertType+
		"\n, violationLevel: "+violationLevel+
		"\n, ruleVersionId: "+ruleVersionId +
		"\n, alertStatus: "+alertStatus+
                "\n, workQueueCategory: " + workQueueCategory +
                "\n, decision: " + decision;
	}*/
	
}
