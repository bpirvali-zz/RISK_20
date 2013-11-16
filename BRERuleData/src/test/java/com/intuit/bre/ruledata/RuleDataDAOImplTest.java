package com.intuit.bre.ruledata;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

public class RuleDataDAOImplTest {

	@Test
	public void testDateReplacementMSSQL() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		RuleDataQuery queryInfo = new RuleDataQuery();
		queryInfo.setDbConnection("MSSQL");
		queryInfo.setQuery("{CALL dbo.usp_BRE_AGG_CREDIT('[MA_NUM]', 'TRAN', 'SUM', 120, 'DAY', 'CreditTotal', '[BCD]')}");
		String merchantAccountNumber = "123";
		Date batchDate = dateFormat.parse("08/08/2013");
		
		String sql = RuleDataDAOImpl.replaceQueryTemplates(queryInfo, merchantAccountNumber, batchDate);
		
		Assert.assertEquals(sql, "{CALL dbo.usp_BRE_AGG_CREDIT('123', 'TRAN', 'SUM', 120, 'DAY', 'CreditTotal', '2013-08-08')}");
	}
	
	@Test
	public void testDateReplacementOracle() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		RuleDataQuery queryInfo = new RuleDataQuery();
		queryInfo.setDbConnection("ORACLE");
		queryInfo.setQuery("{CALL dbo.usp_BRE_AGG_CREDIT('[MA_NUM]', 'TRAN', 'SUM', 120, 'DAY', 'CreditTotal', [BCD])}");
		String merchantAccountNumber = "123";
		Date batchDate = dateFormat.parse("08/08/2013");
		
		String sql = RuleDataDAOImpl.replaceQueryTemplates(queryInfo, merchantAccountNumber, batchDate);
		
		Assert.assertEquals(sql, "{CALL dbo.usp_BRE_AGG_CREDIT('123', 'TRAN', 'SUM', 120, 'DAY', 'CreditTotal', to_date('08/08/2013','MM/DD/YYYY'))}");
	}
	
}
