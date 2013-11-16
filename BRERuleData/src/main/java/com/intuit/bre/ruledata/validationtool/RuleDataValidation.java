package com.intuit.bre.ruledata.validationtool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import com.intuit.bre.ruledata.Configuration;
import com.intuit.bre.ruledata.RuleDataConfig;
import com.intuit.bre.ruledata.RuleDataElement;
import com.intuit.bre.ruledata.RuleDataImpl;
import com.intuit.bre.ruledata.RuleDataType;

public class RuleDataValidation {

	static private final String EOL = System.getProperty("line.separator");
	
	public static void main(String args[]) {
		RuleDataValidation ruleDataValidation = new RuleDataValidation();
		String inputFileName = args[0];
		String outputFileName = args[1];
		FileInputStream fIn = null;
		FileWriter fWriter = null;
		try {
			fIn = new FileInputStream(inputFileName);
			fWriter = new FileWriter(outputFileName);
			String results = ruleDataValidation.processValidation(fIn);
			fWriter.write(results);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {fWriter.close();} catch (Exception e) {}
			try {fIn.close();} catch (Exception e) {}
		}
	}
	
	public String processValidation(InputStream inStream) throws Exception {
		StringBuilder sBuilder = new StringBuilder();
		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(new InputStreamReader(inStream));
			String oneline = null;
			while ((oneline = bReader.readLine()) != null) {
				StringTokenizer tokenizer = new StringTokenizer(oneline, "\t");
				int tokenCount = tokenizer.countTokens();
				if (tokenCount == 0) {
					continue;
				}
				if (tokenCount < 4) {
					throw new Exception("Incomplete row found: " + oneline);
				}
				int column = 0;
				ValidationInput validationInput = new ValidationInput();
				while (tokenizer.hasMoreTokens()) {
					if (column == 0) {
						validationInput.setKey(tokenizer.nextToken());
					} else if (column == 1) {
						validationInput.setMerchantAccountNumber(tokenizer.nextToken());
					} else if (column == 2) {
						validationInput.setBatchCycleDate(tokenizer.nextToken());
					} else if (column == 3) {
						validationInput.setExpectedValue(tokenizer.nextToken());
					}
					column++;
				}
				try {
					long elapsedTime = validate(validationInput);
					sBuilder.append(validationInput.getKey() + "\t"
									   + validationInput.getMerchantAccountNumber() + "\t"
									   + validationInput.getBatchCycleDate() + "\t"
									   + validationInput.getExpectedValue() + "\t"
									   + "SUCCESS" + "\t"
									   + Long.toString(elapsedTime) + EOL);
				} catch (Exception e) {
					sBuilder.append(validationInput.getKey() + "\t"
							   + validationInput.getMerchantAccountNumber() + "\t"
							   + validationInput.getBatchCycleDate() + "\t"
							   + validationInput.getExpectedValue() + "\t"
							   + "FAIL" + "\t\t"
							   + e.getMessage() + EOL);
				}
			}
		} catch (Exception e) {
			sBuilder.append(e.getMessage());
		} finally {
			try {bReader.close();} catch (Exception e) {}
		}
		return sBuilder.toString();
	}
	
	public long validate(ValidationInput input) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yy");
		
		Date batchDate = null;
		try {
			batchDate = dateFormat.parse(input.getBatchCycleDate());
		} catch (Exception e) {
			throw new Exception("Invalid date " + input.getBatchCycleDate());
		}
		
		if (input.getMerchantAccountNumber() == null || input.getMerchantAccountNumber().length() == 0) {
			throw new Exception("Missing merchant account number");
		}
		
		if (input.getKey() == null || input.getKey().length() == 0) {
			throw new Exception("Missing key");
		}
		
		if (input.getExpectedValue() == null || input.getExpectedValue().length() == 0) {
			throw new Exception("Missing expected value");
		}
		
		RuleDataConfig config = RuleDataConfig.getInstance();
		if (config == null) {
			throw new Exception("Unable to parse " + Configuration.getQueryFile());
		}
		
		RuleDataElement ruleDataElement = config.getElementMap().get(input.getKey());
		if (ruleDataElement == null) {
			throw new Exception("Unable to find key " + input.getKey() + " in " + Configuration.getQueryFile());
		}
		
		String testName = input.getKey() + "/" + input.getMerchantAccountNumber() + "/" + input.getBatchCycleDate();
		boolean checkExpected = !"?".equals(input.getExpectedValue());
		
		long startTime = (new java.util.Date()).getTime();
		
		RuleDataType ruleDataType = ruleDataElement.getType();
		if (RuleDataType.INTEGER == ruleDataType) {
			Integer expected = null;
			try {
				if (checkExpected) {
					expected = new Integer(input.getExpectedValue());
				}
			} catch (Exception e) {
				throw new Exception("Unable to convert " + input.getExpectedValue() + " to an integer");
			}
			Integer result = RuleDataImpl.getInstance().getInteger(input.getKey(), input.getMerchantAccountNumber(), batchDate);
			if (checkExpected && !expected.equals(result)) {
				throw new Exception("Test " + testName + " failed.  Expected " + expected + " but received " + result);
			}
		} else if (RuleDataType.DOUBLE == ruleDataType) {
			Double expected = null;
			try {
				if (checkExpected) {
					expected = new Double(input.getExpectedValue());
				}
			} catch (Exception e) {
				throw new Exception("Unable to convert " + input.getExpectedValue() + " to a double");
			}
			Double result = RuleDataImpl.getInstance().getDouble(input.getKey(), input.getMerchantAccountNumber(), batchDate);
			result = Math.floor((result * 10000) + 0.5) / 10000;
			if (checkExpected && !expected.equals(result)) {
				throw new Exception("Test " + testName + " failed.  Expected " + expected + " but received " + result);
			}
			
		} else if (RuleDataType.LONG == ruleDataType) {
			Long expected = null;
			try {
				if (checkExpected) {
					expected = new Long(input.getExpectedValue());
				}
			} catch (Exception e) {
				throw new Exception("Unable to convert " + input.getExpectedValue() + " to a long");
			}
			Long result = RuleDataImpl.getInstance().getLong(input.getKey(), input.getMerchantAccountNumber(), batchDate);
			if (checkExpected && !expected.equals(result)) {
				throw new Exception("Test " + testName + " failed.  Expected " + expected + " but received " + result);
			}
			
		} else if (RuleDataType.DATE == ruleDataType) {
			Date expected = null;
			try {
				if (checkExpected) {
					expected = dateFormat.parse(input.getExpectedValue());
				}
			} catch (Exception e) {
				throw new Exception("Unable to convert " + input.getExpectedValue() + " to a date");
			}
			Date result = RuleDataImpl.getInstance().getDate(input.getKey(), input.getMerchantAccountNumber(), batchDate);
			if (checkExpected && !expected.equals(result)) {
				throw new Exception("Test " + testName + " failed.  Expected " + expected + " but received " + result);
			}
			
		} else if (RuleDataType.STRING == ruleDataType) {
			String expected = null;
			if (checkExpected) {
				expected = input.getExpectedValue();
			}
			String result = RuleDataImpl.getInstance().getString(input.getKey(), input.getMerchantAccountNumber(), batchDate);
			if (checkExpected && !expected.equals(result)) {
				throw new Exception("Test " + testName + " failed.  Expected " + expected + " but received " + result);
			}
			
		}
		
		long endTime = (new java.util.Date()).getTime();
		long elapsedTime = endTime - startTime;
		return elapsedTime;
		
	}
	
}
