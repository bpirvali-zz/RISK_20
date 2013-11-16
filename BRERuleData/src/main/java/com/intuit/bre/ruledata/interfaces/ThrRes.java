package com.intuit.bre.ruledata.interfaces;

import com.intuit.bre.ruledata.ThrState;

/*
 * Interface of thread resource
 * */
public interface ThrRes {
	public MerchantCache getMerchantCache();
	//public void setMerchantCache(MerchantCache merchantCache);
	//public ThrState getThrState();
	//public void setThrState(ThrState thrState);
	//public StringBuilder getStrLogBuffer();
	public DataStorageDAO getDao(String daoName);
	//public void closeDao(String daoName);
	public void releaseResources();

}