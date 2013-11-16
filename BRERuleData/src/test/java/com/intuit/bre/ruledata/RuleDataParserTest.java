package com.intuit.bre.ruledata;

import java.io.InputStream;
import java.text.SimpleDateFormat;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.intuit.bre.ruledata.interfaces.RuleData;

public class RuleDataParserTest {	

	@Test
	public void testSimpleXML() throws Exception {	
		Configuration.setQueryFile("/TestRuleDataQueries.xml");
		Configuration.setJdbcPropertiesFile("/testriskprocessorjdbc.properties");		

		InputStream stream = RuleDataParserTest.class.getResourceAsStream("/TestRuleDataQueries.xml");
		
		Assert.assertNotNull(stream);

		RuleDataParser parser = new RuleDataParser();
		RuleDataConfig config = parser.parseRuleDataConfig(stream);
		
		Assert.assertNotNull(config);
		
		String query1Name = "query.AggDaily";
		String query2Name = "query.MerchantProfile";
		String spCall1Name = "storedproc.120day.tran.credittotal";
		String ruleData1Key = "aggregates.DepositCountDaily";
		String ruleData2Key = "merchantProfile.AccountStatus";
		String ruleData3Key = "aggregates.SalesCount120Day";
		String ruleData4Key = "aggregates.SalesAmount120Day";
		
		RuleDataQuery ruleDataQuery1 = config.getQueryMap().get(query1Name);
		Assert.assertNotNull(ruleDataQuery1);

		RuleDataQuery ruleDataQuery2 = config.getQueryMap().get(query2Name);
		Assert.assertNotNull(ruleDataQuery2);

		RuleDataQuery spCall1 = config.getQueryMap().get(spCall1Name);
		Assert.assertNotNull(spCall1);

		RuleDataElement ruleData1 = config.getElementMap().get(ruleData1Key);
		Assert.assertNotNull(ruleData1);

		RuleDataElement ruleData2 = config.getElementMap().get(ruleData2Key);
		Assert.assertNotNull(ruleData2);

		RuleDataElement ruleData3 = config.getElementMap().get(ruleData3Key);
		Assert.assertNotNull(ruleData3);

		RuleDataElement ruleData4 = config.getElementMap().get(ruleData4Key);
		Assert.assertNotNull(ruleData4);
		
		Assert.assertEquals(ruleDataQuery1.getName(), "query.AggDaily");
		Assert.assertEquals(ruleDataQuery1.getDbConnection(), "MSSQL");
		Assert.assertTrue(ruleDataQuery1.getQuery().contains("select 1 as DepositCountDaily"));
		Assert.assertFalse(ruleDataQuery1.isStoredProc());
		
		Assert.assertEquals(spCall1.getName(), "storedproc.120day.tran.credittotal");
		Assert.assertEquals(spCall1.getDbConnection(), "MSSQL");
		Assert.assertTrue(spCall1.getQuery().contains("{CALL dbo.usp_BRE_AGG_CREDIT('[MA_NUM]', 'TRAN', 'SUM', 120, 'DAY', 'CreditTotal', '[BCD]')}"));
		Assert.assertTrue(spCall1.isStoredProc());
		
		Assert.assertEquals(ruleData1.getKey(), "aggregates.DepositCountDaily");
		Assert.assertEquals(ruleData1.getQuery(), ruleDataQuery1);
		Assert.assertEquals(ruleData1.getColumn(), "DepositCountDaily");
		Assert.assertEquals(ruleData1.getType(), RuleDataType.DOUBLE);
		
		Assert.assertEquals(spCall1.getRuleDataElements().size(), 2);
		Assert.assertEquals(ruleDataQuery1.getRuleDataElements().size(), 1);

	}
	
	private void testBadRuleDataQueries(String fileName) throws Exception {
		Configuration.setQueryFile(fileName);
		Configuration.setJdbcPropertiesFile("/testriskprocessorjdbc.properties");		

		InputStream stream = RuleDataParserTest.class.getResourceAsStream(fileName);
		
		Assert.assertNotNull(stream);

		RuleDataParser parser = new RuleDataParser();
		try {
			RuleDataConfig config = parser.parseRuleDataConfig(stream);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// exception expected
		}		
	}
	
	@Test
	public void testBadRuleDataQueries1() throws Exception {
		testBadRuleDataQueries("/BadRuleDataQueries1.xml");
	}

	@Test
	public void testBadRuleDataQueries2() throws Exception {
		testBadRuleDataQueries("/BadRuleDataQueries2.xml");
	}

	@Test
	public void testBadRuleDataQueries3() throws Exception {
		testBadRuleDataQueries("/BadRuleDataQueries3.xml");
	}

	@Test
	public void testBadRuleDataQueries4() throws Exception {
		testBadRuleDataQueries("/BadRuleDataQueries4.xml");
	}

	@Test
	public void testBadRuleDataQueries5() throws Exception {
		testBadRuleDataQueries("/BadRuleDataQueries5.xml");
	}


}