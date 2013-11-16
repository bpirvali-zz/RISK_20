package com.intuit.psd.risk.risk2.topology;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SpringObjectMgrTest {
  @Test
  public void testGetObject() {
		JdbcTemplate jdbcTemplate = (JdbcTemplate)SpringObjectMgr.getInstance().getBean("jdbcTemplate");
		
		int i = 0;
		try {
			i = jdbcTemplate.queryForInt("SELECT 1 FROM DUAL");
		} catch(DataAccessException e) {
			Assert.fail(e.getMessage(), e);
		}
		Assert.assertEquals(i, 1);
	  
  }
}
