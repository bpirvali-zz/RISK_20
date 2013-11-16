package com.intuit.psd.risk.risk2.configmgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.intuit.psd.risk.risk2.configmgr.interfaces.ConfigMgr;

public class ConfigMgrImpl implements ConfigMgr {

	private JdbcTemplate jdbcTemplate;
	private final Map<String, String> mapConfig;


	public ConfigMgrImpl(JdbcTemplate jdbc) {
		this.jdbcTemplate = jdbc;
		mapConfig = new HashMap<String, String>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM BRE.RISK_2_CONF");

		for (Map<String, Object>row: rows) {
			String key = (String)row.get("KEY");
			String val = (String)row.get("VALUE");
			mapConfig.put(key.toLowerCase(), val);
		}
	}

	/*
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	*/

	/**
	 * @param args
	 */
	/*
	public static void main(String[] args) {
    	ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "ApplicationContext.xml" });
    	ConfigMgrImpl cfgMgr = (ConfigMgrImpl)context.getBean("configMgrImpl");
    	String nBolts = cfgMgr.get("STorm_risk_events_no_bolts ");
    	System.out.println("No of Bollts:" + nBolts);
    	//cfgMgr.put("Test_Key", "Test_Val");
	}
	*/

	public String get(String key) {
		String s = mapConfig.get(key.toLowerCase());
		if (s==null)
			throw new RuntimeException("Key:" + key + " does not exist!");
		return s;
	}

	public void put(String key, String val) {
		mapConfig.put(key.toLowerCase(), val);
	}

	public void putInDB(String key, String val) {
		String sql = "INSERT INTO BRE.RISK_2_CONF VALUES (?, ?)";
		jdbcTemplate.update(sql, new Object[] { key, val } );
		put(key,val);
	}

	public int getInt(String key) {
		String s = get(key);
		return Integer.valueOf(s);
	}

	public double getDouble(String key) {
		String s = get(key);
		return Double.valueOf(s);
	}

	public boolean getBoolean(String key) {
		String s = get(key);
		return Boolean.valueOf(s);
	}

	public Map<String, String> getMapConfig() {
		return mapConfig;
	}

	public void refreshCache() {
		mapConfig.clear();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM BRE.RISK_2_CONF");

		for (Map<String, Object>row: rows) {
			String key = (String)row.get("KEY");
			String val = (String)row.get("VALUE");
			mapConfig.put(key.toLowerCase(), val);
		}
	}

}
