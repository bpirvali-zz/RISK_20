package com.intuit.psd.risk.risk2.topology;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.intuit.psd.risk.risk2.configmgr.interfaces.ConfigMgr;

/**
 * 
 * @author bpirvali
 * 
 */
public class RiskEventsTopologyTest  {
	
	//private ApplicationContext appCtx = null;
	//private ApplicationContext appCtx2 = null;
	@Test(groups = "a")
	public void testAcks() {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringObjectMgr.getInstance().getBean("jdbcTemplate");
		int nRows = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM bre.incoming_risk_events");
		Assert.assertEquals(12, nRows);
		
		int nAckedRows = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM bre.incoming_risk_events WHERE processing_status=2 AND retries=0");
		Assert.assertEquals(11, nAckedRows);
	}

	@Test(groups = "a")
	public void testFails() {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringObjectMgr.getInstance().getBean("jdbcTemplate");

		// publishing the table after the test
		List<Map<String, Object>> l = jdbcTemplate.queryForList("select MERCHANT_ACCOUNT_NUMBER, RETRIES, PROCESSING_STATUS, error_msg from bre.incoming_risk_events");		
		Util.printList(l, "TABLE incoming_risk_events after testFails");
		
		List<String> lRetries = jdbcTemplate.queryForList("SELECT value FROM bre.risk_2_conf WHERE key='sre_retries'", String.class);
		String failCondition = "processing_status=3 AND retries=" + lRetries.get(0); 
		int nFailedRows = jdbcTemplate.queryForInt(
				"SELECT COUNT(*) FROM bre.incoming_risk_events WHERE " + failCondition);
		Assert.assertEquals(1, nFailedRows);

		List<String> error_msgs = jdbcTemplate.queryForList("SELECT error_msg FROM bre.incoming_risk_events WHERE " + failCondition, String.class);
		Assert.assertEquals("[ ]Test Exception for merchant: '111'![ ]Test Exception for merchant: '111'!", error_msgs.get(0));
	}

	@Test(dependsOnGroups = "a")
	public void testHang() {
		//String[] springFiles = { "AppCtx-JdbcTemplate-Test.xml", "AppCtx-RiskEvents.xml", "AppCtx-RiskProcessor-Test.xml" };
		//SpringObjectMgr.setSpringAppCtxFiles(springFiles);
		JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringObjectMgr.getInstance().getBean("jdbcTemplate");
		jdbcTemplate.update("DELETE FROM bre.incoming_risk_events WHERE merchant_account_number!='100'");
		jdbcTemplate.update("UPDATE bre.incoming_risk_events SET processing_status=0, retries=-1, error_msg='', time_processed=null " + 
				"WHERE merchant_account_number IN ('100')" );

		jdbcTemplate.update("UPDATE bre.risk_2_conf SET value=300 WHERE key='sre_fake_hang_secs'");
		jdbcTemplate.update("UPDATE bre.risk_2_conf SET value=5 WHERE key='sre_topology_message_timeout_secs'");
		
		jdbcTemplate.update("COMMIT");
    	ConfigMgr cfg = (ConfigMgr)SpringObjectMgr.getInstance().getBean("configMgrImpl");
		cfg.refreshCache();
		
//		List<Map<String, Object>> l = jdbcTemplate.queryForList("select MERCHANT_ACCOUNT_NUMBER, RETRIES, PROCESSING_STATUS, error_msg from bre.incoming_risk_events");
//		Util.printList(l, "TABLE incoming_risk_events");
//		
//		l = jdbcTemplate.queryForList("select * from bre.risk_2_conf");
//		Util.printList(l, "TABLE risk_2_conf");
		
		//String[] args = { "local_cluster", "AppCtx-JdbcTemplate-Test.xml", "AppCtx-RiskEvents.xml", "AppCtx-RiskProcessor-Test.xml" };
		String[] args = { "local_cluster" };
		try {
			Main.main(args);
		} catch (InterruptedException e) {
		}

		// publishing the table after test!!!
		List<Map<String, Object>> l = jdbcTemplate.queryForList("select MERCHANT_ACCOUNT_NUMBER, RETRIES, PROCESSING_STATUS, error_msg from bre.incoming_risk_events");
		Util.printList(l, "TABLE incoming_risk_events after testHang");
		
		//JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringObjectMgr.getInstance().getBean("jdbcTemplate");
		List<String> lRetries = jdbcTemplate.queryForList("SELECT value FROM bre.risk_2_conf WHERE key='sre_retries'", String.class);
		
		String failCondition = "processing_status=3 AND retries=" + lRetries.get(0); 
		int nFailedRows = jdbcTemplate.queryForInt(
				"SELECT COUNT(*) FROM bre.incoming_risk_events WHERE " + failCondition);
		Assert.assertEquals(1, nFailedRows);
		
		List<String> error_msgs = jdbcTemplate.queryForList("SELECT error_msg FROM bre.incoming_risk_events WHERE " + failCondition, String.class);
		Assert.assertEquals("[ ]BOLT HANG!!![ ]BOLT HANG!!!", error_msgs.get(0));

	}
	
	
	@BeforeClass
	public void beforeClass() {
		String[] args = { "local_cluster", "AppCtx-JdbcTemplate-Test.xml", "AppCtx-RiskEvents.xml", "AppCtx-RiskProcessor-Test.xml" };
		try {
			Main.main(args);
		} catch (InterruptedException e) {
			// ignore interrupted exceptions
		}
	}

	@AfterClass
	public void afterClass() {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringObjectMgr.getInstance().getBean("jdbcTemplate");
		List<Map<String, Object>> l = jdbcTemplate.queryForList("select MERCHANT_ACCOUNT_NUMBER, RETRIES, PROCESSING_STATUS, error_msg from bre.incoming_risk_events");
		Util.printList(l, "TABLE incoming_risk_events");
	}

}
