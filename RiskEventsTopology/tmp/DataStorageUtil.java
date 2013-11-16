package com.intuit.psd.risk.risk2.topology.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//@multi-threaded

public class DataStorageUtil {
	private static  Map<String, DBInfo> mapDBInfo = null;
	private static String[] supportedDBInfo = {"oracle","voltdb","mssql","hsqldb"};
	public static class DBInfo {
		private String dbName;
		private String Driver;
		private String URL;
		private String UserName;
		private String PassWord;
		
		public DBInfo(String dbName, Properties prop) {
			this.dbName = dbName;
			
			this.Driver 	= prop.getProperty(dbName + ".jdbc.driverClassName");;
			this.URL 		= prop.getProperty(dbName + ".jdbc.url");;
			this.UserName 	= prop.getProperty(dbName + ".jdbc.userName");
			
			// TODO:BP modify this code to go through a password server
			this.PassWord 	= prop.getProperty(dbName + ".jdbc.password");
		}
		public String getDbName() {
			return dbName;
		}
		public String getDriver() {
			return Driver;
		}

		public String getURL() {
			return URL;
		}

		public String getUserName() {
			return UserName;
		}

		public String getPassWord() {
			return PassWord;
		}
	}
	
	private static class DataStorageUtilHolder {
		private static final DataStorageUtil INSTANCE = createPropertiesUtil();

		private static DataStorageUtil createPropertiesUtil() {
			Properties prop = new Properties();
			DataStorageUtil dsUtil = null;
			try {
				prop.load(DataStorageUtil.class.getResourceAsStream("/jdbc.properties"));
				dsUtil = new DataStorageUtil();
				for (String db: supportedDBInfo) {
					DBInfo dbInfo = new DBInfo(db,prop);
					if (dbInfo.getDriver()!=null && dbInfo.getDriver().length()>0) {
						mapDBInfo.put(db, dbInfo);
						System.out.println("DB-Info added for:" + db);
					}
				}
			} catch (IOException e) {
				// TODO:BP log exception
				e.printStackTrace();
			}
			return dsUtil;
		}
	}
	
	private DataStorageUtil() {
		mapDBInfo = new HashMap<String, DBInfo>();
	}
	
	public static DataStorageUtil getInstance() {
		return DataStorageUtilHolder.INSTANCE;
	}
	
	public DBInfo getDBInfo(String dbName) {
		DBInfo dbInfo = mapDBInfo.get(dbName.toLowerCase());
		//if (dbInfo==null)
		//	throw new RuntimeException("Data Storage Name is not recognized!");
		
		return dbInfo;
	}
}
