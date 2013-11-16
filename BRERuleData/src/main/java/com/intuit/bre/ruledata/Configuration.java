package com.intuit.bre.ruledata;

/**
 * This class contains the names of the configuration files.  It was created
 * so that the defaults can be overriden to make testing easier.
 * @author Ross Mills
 *
 */
public class Configuration {

	static private String queryFile = "/RuleDataQueries.xml";
	static private String jdbcPropertiesFile = "/riskprocessorjdbc.properties";
	
	public static String getQueryFile() {
		return queryFile;
	}
	
	public static void setQueryFile(String queryFile) {
		Configuration.queryFile = queryFile;
	}
	
	public static String getJdbcPropertiesFile() {
		return jdbcPropertiesFile;
	}
	
	public static void setJdbcPropertiesFile(String jdbcPropertiesFile) {
		Configuration.jdbcPropertiesFile = jdbcPropertiesFile;
	}
		
}
