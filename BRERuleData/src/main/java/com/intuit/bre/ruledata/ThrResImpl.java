package com.intuit.bre.ruledata;

//import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.intuit.bre.ruledata.interfaces.DataStorageDAO;
import com.intuit.bre.ruledata.interfaces.MerchantCache;
import com.intuit.bre.ruledata.interfaces.ThrRes;

public class ThrResImpl implements ThrRes {
	public static final String DAO_NAME_VOLTDB = "VOLTDB";
	public static final String DAO_NAME_OASIS = "OASIS";
	public static final String DAO_NAME_MSSQL = "MSSQL";
	public static final String DAO_NAME_HSQL = "HSQLDB";
	
	private MerchantCache merchantCache;
	private ThrState thrState;
	private StringBuilder strLogBuffer;
	private Map<String, DataStorageDAO> mapDaos;
	
	public ThrResImpl() throws ClassNotFoundException, SQLException {
		thrState 	= ThrState.IDLE;
		strLogBuffer= new StringBuilder();
	    
		DataStorageUtil dsUtil = DataStorageUtil.getInstance();
		
	    // TODO:BP These values should be set by Spring
		merchantCache = new BasicMerchantCacheImpl();
	    
		mapDaos = new HashMap<String, DataStorageDAO>();
		
		if (dsUtil.getDBInfo("oracle")!=null)
			mapDaos.put(DAO_NAME_OASIS, new DataStorageDAOOasisImpl(dsUtil.getDBInfo("oracle")));
		if (dsUtil.getDBInfo("voltdb")!=null)
			mapDaos.put(DAO_NAME_VOLTDB, new DataStorageDAOVoltImpl(dsUtil.getDBInfo("voltdb")));	    
		if (dsUtil.getDBInfo("mssql")!=null)
			mapDaos.put(DAO_NAME_MSSQL, new DataStorageDAOMSSQLImpl(dsUtil.getDBInfo("mssql")));	
		if (dsUtil.getDBInfo("hsqldb")!=null)
			mapDaos.put(DAO_NAME_HSQL, new DataStorageDAOMSSQLImpl(dsUtil.getDBInfo("hsqldb")));	
	}

	/* (non-Javadoc)
	 * @see com.intuit.bre.ruledata.ThrRes#getMerchantCache()
	 */
	@Override
	public MerchantCache getMerchantCache() {
		return merchantCache;
	}

	/* (non-Javadoc)
	 * @see com.intuit.bre.ruledata.ThrRes#setMerchantCache(com.intuit.bre.ruledata.interfaces.MerchantCache)
	 */
	/*
	@Override
	public void setMerchantCache(MerchantCache merchantCache) {
		this.merchantCache = merchantCache;
	}
	*/

	/* (non-Javadoc)
	 * @see com.intuit.bre.ruledata.ThrRes#getThrState()
	 */
	/*
	@Override
	public ThrState getThrState() {
		return thrState;
	}
	*/

	/* (non-Javadoc)
	 * @see com.intuit.bre.ruledata.ThrRes#setThrState(com.intuit.bre.ruledata.ThrState)
	 */
	/*
	@Override
	public void setThrState(ThrState thrState) {
		this.thrState = thrState;
	}
	*/

	/* (non-Javadoc)
	 * @see com.intuit.bre.ruledata.ThrRes#getStrLogBuffer()
	 */
	/*
	@Override
	public StringBuilder getStrLogBuffer() {
		return strLogBuffer;
	}
	*/

	/* (non-Javadoc)
	 * @see com.intuit.bre.ruledata.ThrRes#getDao(java.lang.String)
	 */
	@Override
	public DataStorageDAO getDao(String daoName) {
		DataStorageDAO d = mapDaos.get(daoName);
		if (d==null) {
			String supported_dbs = "";
			for (Map.Entry<String, DataStorageDAO> e : mapDaos.entrySet()) 
				supported_dbs = supported_dbs + e.getKey() + " "; 
			throw new RuntimeException("DB-Name:'" + daoName + "' is not in the list of supported dbs:{" + supported_dbs + "}!");
		}
		return d;
	}

	@Override
	public void releaseResources() {
		DataStorageDAO dao;
		for (Map.Entry<String, DataStorageDAO> entry: mapDaos.entrySet()) {
			dao = entry.getValue();
			dao.close();
		}
		this.strLogBuffer  = null;
		this.merchantCache = null;
		this.mapDaos       = null;
	}

//	@Override
//	public void closeDao(String daoName) {
//		DataStorageDao dao = mapDaos.get(daoName);
//		dao.close();	
//	}
	
	/*
	private Connection getVoltDBConnection() {
        try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection conn = DriverManager.getConnection("jdbc:voltdb://localhost:21212", "user", "pw");
                return conn;
        } catch (Exception e) {
                e.printStackTrace();
                return null;
        }
	}
	*/
}
