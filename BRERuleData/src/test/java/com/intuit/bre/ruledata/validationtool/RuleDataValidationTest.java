package com.intuit.bre.ruledata.validationtool;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.hsqldb_voltpatches.Types;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.intuit.bre.ruledata.Configuration;

public class RuleDataValidationTest {

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
	public void inputValidation() throws Exception {
		InputStream inStream = RuleDataValidationTest.class.getResourceAsStream("/testruledatavalidation.txt");
		RuleDataValidation ruleDataValidation = new RuleDataValidation();
		String results = ruleDataValidation.processValidation(inStream);
		System.out.println(results);
		if (results.contains("FAIL")) {
			Assert.fail("Some tests failed");
		}
		if (!results.contains("SUCCESS")) {
			Assert.fail("No tests were successful");
		}
	}
	
	@Test
	public void testDataValidationTestGetIntegerHappyPath() throws Exception {
		ValidationInput input = new ValidationInput();
		input.setMerchantAccountNumber("1234567890123456");
		input.setBatchCycleDate("08/08/2013");
		input.setKey("test.int");
		input.setExpectedValue("12345");
		
		RuleDataValidation ruleDataValidation = new RuleDataValidation();
		ruleDataValidation.validate(input);
	}
	
	@Test
	public void testDataValidationTestGetDoubleHappyPath() throws Exception {
		ValidationInput input = new ValidationInput();
		input.setMerchantAccountNumber("1234567890123456");
		input.setBatchCycleDate("08/08/2013");
		input.setKey("test.double");
		input.setExpectedValue("123.45");
		
		RuleDataValidation ruleDataValidation = new RuleDataValidation();
		ruleDataValidation.validate(input);
	}
	
	@Test
	public void testDataValidationTestGetLongHappyPath() throws Exception {
		ValidationInput input = new ValidationInput();
		input.setMerchantAccountNumber("1234567890123456");
		input.setBatchCycleDate("08/08/2013");
		input.setKey("test.long");
		input.setExpectedValue("123");
		
		RuleDataValidation ruleDataValidation = new RuleDataValidation();
		ruleDataValidation.validate(input);
	}
	
	@Test
	public void testDataValidationTestGetDateHappyPath() throws Exception {
		ValidationInput input = new ValidationInput();
		input.setMerchantAccountNumber("1234567890123456");
		input.setBatchCycleDate("08/08/2013");
		input.setKey("test.date");
		input.setExpectedValue("08/08/2013");
		
		RuleDataValidation ruleDataValidation = new RuleDataValidation();
		ruleDataValidation.validate(input);
	}
	
	@Test
	public void testDataValidationTestGetStringHappyPath() throws Exception {
		ValidationInput input = new ValidationInput();
		input.setMerchantAccountNumber("1234567890123456");
		input.setBatchCycleDate("08/08/2013");
		input.setKey("test.string");
		input.setExpectedValue("OPEN");
		
		RuleDataValidation ruleDataValidation = new RuleDataValidation();
		ruleDataValidation.validate(input);
	}
	
	@Test
	public void testBadExpectedIntValue() throws Exception {
		RuleDataValidation ruleDataValidation = new RuleDataValidation();

		ValidationInput input = new ValidationInput();
		input.setMerchantAccountNumber("1234567890123456");
		input.setBatchCycleDate("08/08/2013");
		input.setKey("test.int");
		input.setExpectedValue("dog");
		
		try {
			ruleDataValidation.validate(input);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// exception expected
		}
				
	}
	
	@Test
	public void testExpectedIntValueMismatch() throws Exception {
		RuleDataValidation ruleDataValidation = new RuleDataValidation();

		ValidationInput input = new ValidationInput();
		input.setMerchantAccountNumber("1234567890123456");
		input.setBatchCycleDate("08/08/2013");
		input.setKey("test.int");
		input.setExpectedValue("54321");
		
		try {
			ruleDataValidation.validate(input);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// exception expected
		}
				
	}
	
	@Test
	public void testBadExpectedDoubleValue() throws Exception {
		RuleDataValidation ruleDataValidation = new RuleDataValidation();

		ValidationInput input = new ValidationInput();
		input.setMerchantAccountNumber("1234567890123456");
		input.setBatchCycleDate("08/08/2013");
		input.setKey("test.double");
		input.setExpectedValue("dog");
		
		try {
			ruleDataValidation.validate(input);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// exception expected
		}
				
	}
	
	@Test
	public void testExpectedDoubleValueMismatch() throws Exception {
		RuleDataValidation ruleDataValidation = new RuleDataValidation();

		ValidationInput input = new ValidationInput();
		input.setMerchantAccountNumber("1234567890123456");
		input.setBatchCycleDate("08/08/2013");
		input.setKey("test.double");
		input.setExpectedValue("54321.0");
		
		try {
			ruleDataValidation.validate(input);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// exception expected
		}
				
	}

	@Test
	public void testBadExpectedLongValue() throws Exception {
		RuleDataValidation ruleDataValidation = new RuleDataValidation();

		ValidationInput input = new ValidationInput();
		input.setMerchantAccountNumber("1234567890123456");
		input.setBatchCycleDate("08/08/2013");
		input.setKey("test.long");
		input.setExpectedValue("dog");
		
		try {
			ruleDataValidation.validate(input);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// exception expected
		}
				
	}
	
	@Test
	public void testExpectedLongValueMismatch() throws Exception {
		RuleDataValidation ruleDataValidation = new RuleDataValidation();

		ValidationInput input = new ValidationInput();
		input.setMerchantAccountNumber("1234567890123456");
		input.setBatchCycleDate("08/08/2013");
		input.setKey("test.long");
		input.setExpectedValue("54321");
		
		try {
			ruleDataValidation.validate(input);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// exception expected
		}
				
	}

	@Test
	public void testBadExpectedDateValue() throws Exception {
		RuleDataValidation ruleDataValidation = new RuleDataValidation();

		ValidationInput input = new ValidationInput();
		input.setMerchantAccountNumber("1234567890123456");
		input.setBatchCycleDate("08/08/2013");
		input.setKey("test.date");
		input.setExpectedValue("dog");
		
		try {
			ruleDataValidation.validate(input);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// exception expected
		}
				
	}
	
	@Test
	public void testExpectedDateValueMismatch() throws Exception {
		RuleDataValidation ruleDataValidation = new RuleDataValidation();

		ValidationInput input = new ValidationInput();
		input.setMerchantAccountNumber("1234567890123456");
		input.setBatchCycleDate("08/08/2013");
		input.setKey("test.date");
		input.setExpectedValue("02/02/1987");
		
		try {
			ruleDataValidation.validate(input);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// exception expected
		}
				
	}
	@Test
	public void testBadValidationInput() throws Exception {
		RuleDataValidation ruleDataValidation = new RuleDataValidation();

		ValidationInput input = new ValidationInput();
		input.setMerchantAccountNumber("1234567890123456");
		input.setBatchCycleDate("08/08/2013");
		input.setKey("test.int");
		input.setExpectedValue("12345");
		
		input.setMerchantAccountNumber(null);
		
		try {
			ruleDataValidation.validate(input);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// exception expected
		}

		input.setMerchantAccountNumber("1234567890123456");
		input.setBatchCycleDate(null);
		
		try {
			ruleDataValidation.validate(input);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// exception expected
		}

		input.setBatchCycleDate("45/bob/2013");

		try {
			ruleDataValidation.validate(input);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// exception expected
		}

		input.setBatchCycleDate("08/08/2013");
		input.setKey("nonexistantkey");

		try {
			ruleDataValidation.validate(input);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// exception expected
		}

		input.setKey(null);

		try {
			ruleDataValidation.validate(input);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// exception expected
		}

		input.setKey("test.int");
		input.setExpectedValue(null);

		try {
			ruleDataValidation.validate(input);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// exception expected
		}

	}
	
}
