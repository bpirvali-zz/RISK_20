package com.intuit.bre.ruledata;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.intuit.bre.ruledata.interfaces.ResourceManagement;
import com.intuit.bre.ruledata.interfaces.RuleData;
import com.intuit.bre.ruledata.interfaces.RuleDataInternal;
import com.intuit.bre.ruledata.interfaces.ThrResMgr;

/**
 * @author bpirvali
 * @Multi-Threaded
 *
 * This class is replacing the ThreadLocal for more portability and thread-pool friendliness 
 * 
 */

public class ThrResMgrImpl implements ThrResMgr {
	private Map<String, RuleDataInternal> mapRuleData = null;	
	private ThrResMgrImpl() {
		mapRuleData = new ConcurrentHashMap<String, RuleDataInternal>();
	}
	
	private static class ThrResMgrHolder {
		private static final ThrResMgr INSTANCE = new ThrResMgrImpl();
	}
	
	public static ThrResMgr getInstance() {
		return ThrResMgrHolder.INSTANCE;		
	}
	
	@Override
	public RuleData getRuleData() {
		String thrName =Thread.currentThread().getName(); 		
		return mapRuleData.get(thrName);
	}
	
	@Override
	public void putRuleData(RuleDataInternal ruleData) {
		String thrName =Thread.currentThread().getName(); 		
		mapRuleData.put(thrName, ruleData);
	}
	
	
	/* 
	 * This function is called by the maintenance thread
	 * */
	/*
	@Override
	public void removeRuleData(String sThreadName) {
		ResourceManagement rd = mapRuleData.get(sThreadName);
		if (rd!=null) {
			rd.releaseResources();
			mapRuleData.remove(sThreadName);
		}
	}
	*/

	/*
	 * This function is called by the maintenance thread!
	 * */
	/*
	@Override
	public void removeAllRuleData() {
		Set<String> keys = mapRuleData.keySet();
		for (String key: keys) {
			removeRuleData(key);
		}		
	}
	*/
	
}
