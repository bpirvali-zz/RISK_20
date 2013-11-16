package com.intuit.bre.ruledata;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.intuit.bre.ruledata.interfaces.RuleData;

public class RuleDataImpTest {

	@BeforeClass
	public void setUp() throws Exception {
		Configuration.setQueryFile("/TestRuleDataQueries.xml");
		Configuration.setJdbcPropertiesFile("/testriskprocessorjdbc.properties");		
	}

	@Test
	public  void testRuleDataImplGetInstance() throws Exception {
		RuleData ruleData = RuleDataImpl.getInstance();
		Assert.assertNotNull(ruleData);
	}
	
}
