package com.intuit.psd.risk.processor;

import java.text.SimpleDateFormat;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.intuit.psd.risk.processor.card.CardBatchEventImpl;

public class CardBatchEventTest extends PojoValidatorTest {
	
	@Test
	public void testGetterAndSetter() throws Exception {
		CardBatchEventImpl cardBatchEvent = new CardBatchEventImpl();  
    	SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
  	  	java.util.Date batchCycleDate = dateFormat.parse("07/28/2012");
  	  	String merchantAccountNumber = "123456789";
    	cardBatchEvent.setBatchCycleDate(batchCycleDate);    	
    	cardBatchEvent.setMerchantAccountNumber(merchantAccountNumber);
    	
    	Assert.assertEquals(cardBatchEvent.getBatchCycleDate().getTime(), batchCycleDate.getTime()); 
    	Assert.assertEquals(cardBatchEvent.getMerchantAccountNumber(), merchantAccountNumber);
	}

}
