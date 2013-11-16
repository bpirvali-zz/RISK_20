package com.intuit.bre.ruledata.validationtool;

public class ValidationInput {

	private String merchantAccountNumber;
	private String batchCycleDate;
	private String key;
	private String expectedValue;
	
	public String getMerchantAccountNumber() {
		return merchantAccountNumber;
	}
	public void setMerchantAccountNumber(String merchantAccountNumber) {
		this.merchantAccountNumber = merchantAccountNumber;
	}
	public String getBatchCycleDate() {
		return batchCycleDate;
	}
	public void setBatchCycleDate(String batchCycleDate) {
		this.batchCycleDate = batchCycleDate;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getExpectedValue() {
		return expectedValue;
	}
	public void setExpectedValue(String expectedValue) {
		this.expectedValue = expectedValue;
	}
	
}
