package com.intuit.bre.ruledata.interfaces;

import java.util.Date;

import com.intuit.bre.ruledata.DataElement;
import com.intuit.bre.ruledata.RuleDataType;
/*
 * This is the internal caching interface
 * */
public interface MerchantCache  {
	public  String getString(	String key, String merchantAccountNumber, Date batchCycleDate);
	public  Integer getInteger(	String key, String merchantAccountNumber, Date batchCycleDate);
	public  Long getLong(		String key, String merchantAccountNumber, Date batchCycleDate);
	public  Double getDouble(	String key, String merchantAccountNumber, Date batchCycleDate);
	public  Date getDate(		String key, String merchantAccountNumber, Date batchCycleDate);
	
	/*
	void putString(String merchantAccountNumber, Date BCD, String key, String val);
	void putInteger(String merchantAccountNumber, Date BCD, String key, int val);
	void putLong(String merchantAccountNumber, Date BCD, String key, long val);
	void putDouble(String merchantAccountNumber, Date BCD, String key, double val);	
	void putDate(String merchantAccountNumber, Date BCD, String key, Date val);	
	*/
	void putValue(String merchantAccountNumber, Date BCD, String key, RuleDataType type, DataElement val);	
	
	boolean isCachedStr(String merchantAccountNumber, Date BCD, String key);
	boolean isCachedInt(String merchantAccountNumber, Date BCD, String key);
	boolean isCachedLong(String merchantAccountNumber, Date BCD, String key);
	boolean isCachedDouble(String merchantAccountNumber, Date BCD, String key);
	boolean isCachedDate(String merchantAccountNumber, Date BCD, String key);
}
