package com.intuit.bre.ruledata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.hsqldb_voltpatches.Types;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DataRequestTest {

	@BeforeClass
	public void setUp() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		Configuration.setQueryFile("/TestRuleDataQueries2.xml");
		Configuration.setJdbcPropertiesFile("/testriskprocessorjdbc.properties");
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
		Statement stmt = conn.createStatement();
		
		try {
			stmt.execute("DROP ALL OBJECTS");
			System.out.println("TESTTABLE dropped");
		} catch (Exception e) {
			System.out.println("TESTTABLE not found, not dropping");
		}
		
		stmt.execute("SET MODE MSSQLServer");
		
		stmt.execute("CREATE TABLE TESTTABLE (ACCOUNTNUM VARCHAR(100), BATCHDATE TIMESTAMP, TESTSTRING VARCHAR(100), TESTINT INT, TESTREAL REAL, TESTDATE TIMESTAMP, TESTBIGINT BIGINT)");
		
		PreparedStatement pStmt = conn.prepareStatement("INSERT INTO TESTTABLE "
								+ "(ACCOUNTNUM, BATCHDATE, TESTSTRING, TESTINT, TESTREAL, TESTDATE, TESTBIGINT) "
								+ "values (?, ?, ?, ?, ?, ?, ?)");
		
		pStmt.setString(1, "1234567890123456");
		pStmt.setTimestamp(2, new java.sql.Timestamp(batchDate.getTime()));
		pStmt.setString(3, "OPEN");
		pStmt.setInt(4, 12345);
		pStmt.setDouble(5, 123.45);
		pStmt.setTimestamp(6, new java.sql.Timestamp(batchDate.getTime()));
		pStmt.setLong(7, 123);
		pStmt.executeUpdate();
		
		pStmt.setString(1, "6543210987654321");
		pStmt.setTimestamp(2, new java.sql.Timestamp(batchDate.getTime()));
		pStmt.setNull(3, Types.VARCHAR);
		pStmt.setNull(4, Types.INTEGER);
		pStmt.setNull(5, Types.REAL);
		pStmt.setNull(6, Types.TIMESTAMP);
		pStmt.setNull(7, Types.BIGINT);
		pStmt.executeUpdate();
				
		pStmt.setString(1, "7777777777777777");
		pStmt.setTimestamp(2, new java.sql.Timestamp(batchDate.getTime()));
		pStmt.setString(3, "123");
		pStmt.setInt(4, 123);
		pStmt.setDouble(5, 123.00);
		pStmt.setTimestamp(6, new java.sql.Timestamp(batchDate.getTime()));
		pStmt.setLong(7, 123);
		pStmt.executeUpdate();
				
		pStmt.close();
		
		stmt.execute("CREATE ALIAS usp_BRE_AGG_CREDIT FOR \"com.intuit.bre.ruledata.H2StoredProc.mockBREAggCreditSP\"");
		
		conn.commit();
		conn.close();
		System.out.println("TESTTABLE HAS BEEN PREPPED");		
	}
	
	@Test
	public void getString() throws Exception {
		String key = "test.string";
		String merchantAccountNumber = "1234567890123456";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		String result = RuleDataImpl.getInstance().getString(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, "OPEN");
		
		// get it again to test the caching mechanisms
		result = RuleDataImpl.getInstance().getString(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, "OPEN");
		
	}
		
	@Test
	public void getInt() throws Exception {
		String key = "test.int";
		String merchantAccountNumber = "1234567890123456";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		int result = RuleDataImpl.getInstance().getInteger(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, 12345);
		
		// get it again to test the caching mechanisms
		result = RuleDataImpl.getInstance().getInteger(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, 12345);
		
	}
		
	@Test
	public void getLong() throws Exception {
		String key = "test.long";
		String merchantAccountNumber = "1234567890123456";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		long result = RuleDataImpl.getInstance().getLong(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, 123);
		
		// get it again to test the caching mechanisms
		result = RuleDataImpl.getInstance().getLong(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, 123);
		
	}
		
	@Test
	public void getDouble() throws Exception {
		String key = "test.double";
		String merchantAccountNumber = "1234567890123456";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		double result = RuleDataImpl.getInstance().getDouble(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(Math.floor((result * 100) + 0.5)/100, 123.45);
		
		// get it again to test the caching mechanisms
		result = RuleDataImpl.getInstance().getDouble(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(Math.floor((result * 100) + 0.5)/100, 123.45);
		
	}
	
	@Test
	public void getDate() throws Exception {
		String key = "test.date";
		String merchantAccountNumber = "1234567890123456";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		java.util.Date result = RuleDataImpl.getInstance().getDate(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, batchDate);
		
		// get it again to test the caching mechanisms
		result = RuleDataImpl.getInstance().getDate(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, batchDate);
		
	}
		
	@Test
	public void testSPCall_getString() throws Exception {
		H2StoredProc.setCardType("CreditTotal");
		
		String key = "testsp.string";
		String merchantAccountNumber = "1234567890123456";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		String result = RuleDataImpl.getInstance().getString(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, "CreditTotal");

		// get it again to test caching mechanism
		result = RuleDataImpl.getInstance().getString(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, "CreditTotal");
	}
		
	@Test
	public void testSPCall_getInteger() throws Exception {
		H2StoredProc.setSaleCnt(123);
		
		String key = "testsp.SalesCount120Day";
		String merchantAccountNumber = "1234567890123456";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		int result = RuleDataImpl.getInstance().getInteger(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, 123);

		// get it again to test caching mechanism
		result = RuleDataImpl.getInstance().getInteger(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, 123);
	}
		
	@Test
	public void getDateAsString() throws Exception {
		String key = "test.date";
		String merchantAccountNumber = "1234567890123456";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		try {
			String result = RuleDataImpl.getInstance().getString(key, merchantAccountNumber, batchDate);
			Assert.fail("Exception was expected");
		} catch (Exception e) {
			// exception expected
		}
		
	}
		
	@Test
	public void getIntAsString() throws Exception {
		String key = "test.int";
		String merchantAccountNumber = "1234567890123456";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		try {
			String result = RuleDataImpl.getInstance().getString(key, merchantAccountNumber, batchDate);
			Assert.fail("Exception was expected");
		} catch (Exception e) {
			// exception expected
		}
		
	}
		
	@Test
	public void getDoubleAsString() throws Exception {
		String key = "test.double";
		String merchantAccountNumber = "1234567890123456";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		try {
			String result = RuleDataImpl.getInstance().getString(key, merchantAccountNumber, batchDate);
			Assert.fail("Exception was expected");
		} catch (Exception e) {
			// exception expected
		}
		
	}
		
	@Test
	public void getLongAsString() throws Exception {
		String key = "test.long";
		String merchantAccountNumber = "1234567890123456";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		try {
			String result = RuleDataImpl.getInstance().getString(key, merchantAccountNumber, batchDate);
			Assert.fail("Exception was expected");
		} catch (Exception e) {
			// exception expected
		}
		
	}
		
	@Test
	public void getStringAsLong() throws Exception {
		String key = "test.string";
		String merchantAccountNumber = "1234567890123456";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		try {
			long result = RuleDataImpl.getInstance().getLong(key, merchantAccountNumber, batchDate);
			Assert.fail("Exception was expected");
		} catch (Exception e) {
			// exception expected
		}
		
	}
		
	@Test
	public void getNonexistantKey() throws Exception {
		String key = "test.imaginary";
		String merchantAccountNumber = "1234567890123456";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		try {
			long result = RuleDataImpl.getInstance().getLong(key, merchantAccountNumber, batchDate);
			Assert.fail("Exception was expected");
		} catch (Exception e) {
			// exception expected
		}
		
	}
		
	@Test
	public void getWithNullKey() throws Exception {
		String key = null;
		String merchantAccountNumber = "1234567890123456";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		try {
			java.util.Date result = RuleDataImpl.getInstance().getDate(key, merchantAccountNumber, batchDate);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// exception expected
		}
	}
		
	@Test
	public void getWithNullMerchant() throws Exception {
		String key = "test.date";
		String merchantAccountNumber = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		try {
			java.util.Date result = RuleDataImpl.getInstance().getDate(key, merchantAccountNumber, batchDate);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// exception expected
		}
	}
		
	@Test
	public void getWithNullBatchCycleDate() throws Exception {
		String key = "test.date";
		String merchantAccountNumber = "1234567890123456";
		java.util.Date batchDate = null;
		
		try {
			java.util.Date result = RuleDataImpl.getInstance().getDate(key, merchantAccountNumber, batchDate);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// exception expected
		}
	}
		
	@Test
	public void getNullString() throws Exception {
		String key = "test.string";
		String merchantAccountNumber = "6543210987654321";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		String result = RuleDataImpl.getInstance().getString(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, null);
		
		// get it again to test the caching mechanisms
		result = RuleDataImpl.getInstance().getString(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, null);
		
	}
		
	@Test
	public void getNullInt() throws Exception {
		String key = "test.int";
		String merchantAccountNumber = "6543210987654321";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		Integer result = RuleDataImpl.getInstance().getInteger(key, merchantAccountNumber, batchDate);
		Assert.assertNull(result);
		
		// get it again to test the caching mechanisms
		result = RuleDataImpl.getInstance().getInteger(key, merchantAccountNumber, batchDate);
		Assert.assertNull(result);
		
	}
		
	@Test
	public void getNullLong() throws Exception {
		String key = "test.long";
		String merchantAccountNumber = "6543210987654321";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		Long result = RuleDataImpl.getInstance().getLong(key, merchantAccountNumber, batchDate);
		Assert.assertNull(result);
		
		// get it again to test the caching mechanisms
		result = RuleDataImpl.getInstance().getLong(key, merchantAccountNumber, batchDate);
		Assert.assertNull(result);
		
	}
		
	@Test
	public void getNullDouble() throws Exception {
		String key = "test.double";
		String merchantAccountNumber = "6543210987654321";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		Double result = RuleDataImpl.getInstance().getDouble(key, merchantAccountNumber, batchDate);
		Assert.assertNull(result);
		
		// get it again to test the caching mechanisms
		result = RuleDataImpl.getInstance().getDouble(key, merchantAccountNumber, batchDate);
		Assert.assertNull(result);
		
	}
	
	@Test
	public void getNullDate() throws Exception {
		String key = "test.date";
		String merchantAccountNumber = "6543210987654321";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		java.util.Date result = RuleDataImpl.getInstance().getDate(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, null);
		
		// get it again to test the caching mechanisms
		result = RuleDataImpl.getInstance().getDate(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, null);
		
	}

	@Test
	public void getStringFromMissingResultSet() throws Exception {
		String key = "test.string";
		String merchantAccountNumber = "55555";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		String result = RuleDataImpl.getInstance().getString(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, null);
		
		// get it again to test the caching mechanisms
		result = RuleDataImpl.getInstance().getString(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, null);
		
	}
		
	@Test
	public void getIntFromMissingResultSet() throws Exception {
		String key = "test.int";
		String merchantAccountNumber = "55555";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		Integer result = RuleDataImpl.getInstance().getInteger(key, merchantAccountNumber, batchDate);
		Assert.assertNull(result);
		
		// get it again to test the caching mechanisms
		result = RuleDataImpl.getInstance().getInteger(key, merchantAccountNumber, batchDate);
		Assert.assertNull(result);
		
	}
		
	@Test
	public void getLongFromMissingResultSet() throws Exception {
		String key = "test.long";
		String merchantAccountNumber = "55555";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		Long result = RuleDataImpl.getInstance().getLong(key, merchantAccountNumber, batchDate);
		Assert.assertNull(result);
		
		// get it again to test the caching mechanisms
		result = RuleDataImpl.getInstance().getLong(key, merchantAccountNumber, batchDate);
		Assert.assertNull(result);
		
	}
		
	@Test
	public void getDoubleFromMissingResultSet() throws Exception {
		String key = "test.double";
		String merchantAccountNumber = "55555";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		Double result = RuleDataImpl.getInstance().getDouble(key, merchantAccountNumber, batchDate);
		Assert.assertNull(result);
		
		// get it again to test the caching mechanisms
		result = RuleDataImpl.getInstance().getDouble(key, merchantAccountNumber, batchDate);
		Assert.assertNull(result);
		
	}
	
	@Test
	public void getDateFromMissingResultSet() throws Exception {
		String key = "test.date";
		String merchantAccountNumber = "55555";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		java.util.Date result = RuleDataImpl.getInstance().getDate(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, null);
		
		// get it again to test the caching mechanisms
		result = RuleDataImpl.getInstance().getDate(key, merchantAccountNumber, batchDate);
		Assert.assertEquals(result, null);
		
	}
	
	@Test
	public void conversionToIntTest() throws Exception {
		String merchantAccountNumber = "7777777777777777";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		int result = RuleDataImpl.getInstance().getInteger("conv.testint1", merchantAccountNumber, batchDate);
		Assert.assertEquals(result, 123);		
		
		result = RuleDataImpl.getInstance().getInteger("conv.testint2", merchantAccountNumber, batchDate);
		Assert.assertEquals(result, 123);		
		
		result = RuleDataImpl.getInstance().getInteger("conv.testint3", merchantAccountNumber, batchDate);
		Assert.assertEquals(result, 123);		
		
		result = RuleDataImpl.getInstance().getInteger("conv.testint4", merchantAccountNumber, batchDate);
		Assert.assertEquals(result, 123);				

	}
	
	@Test
	public void conversionToDoubleTest() throws Exception {
		String merchantAccountNumber = "7777777777777777";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		double result = RuleDataImpl.getInstance().getDouble("conv.testdouble1", merchantAccountNumber, batchDate);
		Assert.assertEquals(result, 123.0);		
		
		result = RuleDataImpl.getInstance().getDouble("conv.testdouble2", merchantAccountNumber, batchDate);
		Assert.assertEquals(result, 123.0);		
		
		result = RuleDataImpl.getInstance().getDouble("conv.testdouble3", merchantAccountNumber, batchDate);
		Assert.assertEquals(result, 123.0);		
		
		result = RuleDataImpl.getInstance().getDouble("conv.testdouble4", merchantAccountNumber, batchDate);
		Assert.assertEquals(result, 123.0);				

	}
	
	@Test
	public void conversionToLongTest() throws Exception {
		String merchantAccountNumber = "7777777777777777";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		long result = RuleDataImpl.getInstance().getLong("conv.testlong1", merchantAccountNumber, batchDate);
		Assert.assertEquals(result, 123);		
		
		result = RuleDataImpl.getInstance().getLong("conv.testlong2", merchantAccountNumber, batchDate);
		Assert.assertEquals(result, 123);		
		
		result = RuleDataImpl.getInstance().getLong("conv.testlong3", merchantAccountNumber, batchDate);
		Assert.assertEquals(result, 123);		
		
		result = RuleDataImpl.getInstance().getLong("conv.testlong4", merchantAccountNumber, batchDate);
		Assert.assertEquals(result, 123);				

	}
	
	@Test
	public void conversionToStringTest() throws Exception {
		String merchantAccountNumber = "7777777777777777";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date batchDate = dateFormat.parse("08/08/2013");
		
		String result = RuleDataImpl.getInstance().getString("conv.teststr1", merchantAccountNumber, batchDate);
		Assert.assertEquals(result, "123");		
		
		result = RuleDataImpl.getInstance().getString("conv.teststr2", merchantAccountNumber, batchDate);
		Assert.assertEquals(result, "123");		
		
		result = RuleDataImpl.getInstance().getString("conv.teststr3", merchantAccountNumber, batchDate);
		Assert.assertEquals(result, "123");		
		
		result = RuleDataImpl.getInstance().getString("conv.teststr4", merchantAccountNumber, batchDate);
		Assert.assertEquals(result, "123.0");				

	}
	
}
