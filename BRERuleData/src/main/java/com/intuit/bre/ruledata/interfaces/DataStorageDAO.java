package com.intuit.bre.ruledata.interfaces;

import java.util.List;
import java.util.Map;

import com.intuit.bre.ruledata.DataElement;

/*
 * This is the data storage facing DAO interface
 * */
public interface DataStorageDAO {
	public List<Map<String, DataElement>> executeQuery(String query);
	public List<Map<String, DataElement>> executeProc(String query);
	//public boolean testConnection();
	public void close();
}
