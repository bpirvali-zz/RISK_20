package com.intuit.bre.ruledata.interfaces;

import java.util.Date;

/*
 * @EXTERNAL_INTERFACE
 * 
 * This is the only external interface of the library
 * '4266961000000339', 'TRaN', 'SuM', 35, 'mONTH', 'AMEX', '2013-08-01'
 * */
public interface RuleData {

	public  String getString(	String key, String merchantAccountNumber, Date batchCycleDate) throws Exception;
	public  Integer getInteger(	String key, String merchantAccountNumber, Date batchCycleDate) throws Exception;
	public  Long getLong(		String key, String merchantAccountNumber, Date batchCycleDate) throws Exception;
	public  Double getDouble(	String key, String merchantAccountNumber, Date batchCycleDate) throws Exception;
	public  Date getDate(		String key, String merchantAccountNumber, Date batchCycleDate) throws Exception;

}