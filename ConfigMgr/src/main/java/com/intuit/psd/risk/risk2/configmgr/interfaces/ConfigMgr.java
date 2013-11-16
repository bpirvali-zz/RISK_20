package com.intuit.psd.risk.risk2.configmgr.interfaces;

import java.util.Map;

public interface ConfigMgr {
	public String get(String key);
	public int getInt(String key);
	public double getDouble(String key);
	public boolean getBoolean(String key);
	public void put(String key, String val);
	public void putInDB(String key, String val);
	public Map<String, String> getMapConfig();
	public void refreshCache();
}
