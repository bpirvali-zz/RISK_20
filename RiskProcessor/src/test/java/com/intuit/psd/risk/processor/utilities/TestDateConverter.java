package com.intuit.psd.risk.processor.utilities;

import java.text.SimpleDateFormat;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.beust.jcommander.ParameterException;

public class TestDateConverter {

  @Test
  public void convertGoodDate() throws Exception {
	  String goodDateString = "07/28/2012";
	  SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	  java.util.Date expectedDate = dateFormat.parse(goodDateString);
	  
	  DateConverter converter = new DateConverter();
	  java.util.Date actualDate = converter.convert(goodDateString);
	  
	  Assert.assertEquals(actualDate, expectedDate);
  }
  
  @Test
  public void convertGoodDateWithoutLeadingZeros() throws Exception {
	  String goodDateString = "7/3/2012";
	  SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	  java.util.Date expectedDate = dateFormat.parse(goodDateString);
	  
	  DateConverter converter = new DateConverter();
	  java.util.Date actualDate = converter.convert(goodDateString);
	  
	  Assert.assertEquals(actualDate, expectedDate);
  }
  
  @Test
  public void convertBadDate() throws Exception {
	  String badDateString = "02/34/2011";
	  
	  try {
		  DateConverter converter = new DateConverter();
		  java.util.Date actualDate = converter.convert(badDateString);
		  Assert.fail("Expected an expection to be thrown for a bad date"+actualDate.toString());
	  } catch (ParameterException pe) {
		  // code should arrive here
	  }
  }
  
  @Test
  public void convertReallyBadDate() throws Exception {
	  String badDateString = "abcdefg";
	  
	  try {
		  DateConverter converter = new DateConverter();
		  java.util.Date actualDate = converter.convert(badDateString);
		  Assert.fail("Expected an expection to be thrown for a bad date "+actualDate.toString());
	  } catch (ParameterException pe) {
		  // code should arrive here
	  }
  }
}
