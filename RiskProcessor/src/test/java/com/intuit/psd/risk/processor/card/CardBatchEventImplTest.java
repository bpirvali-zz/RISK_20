package com.intuit.psd.risk.processor.card;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import junit.framework.Assert;

import org.testng.annotations.Test;

public class CardBatchEventImplTest {
	@Test
	public void testCardBatchEvent() throws Exception {
		
		String merchantAccountNumber = "1234567890123456";		
		SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
		java.util.Date myDate = dateFormat.parse("07/28/2012");
		String eventType = "testEvent";
		
		CardBatchEventImpl cardBatchEventImpl = new CardBatchEventImpl(merchantAccountNumber,
				new Timestamp(myDate.getTime()), 
				0,0,0,0,
				eventType);
		Assert.assertEquals(eventType, cardBatchEventImpl.getEventType());
		Assert.assertEquals(merchantAccountNumber, cardBatchEventImpl.getMerchantAccountNumber());
		Assert.assertEquals(myDate, cardBatchEventImpl.getBatchCycleDate());
	}
	
	@Test
	public void testCardBatchEventNoArgs() throws Exception {
		
		CardBatchEventImpl cardBatchEventImpl = new CardBatchEventImpl();
		Assert.assertEquals(null, cardBatchEventImpl.getEventType());
		Assert.assertEquals(null, cardBatchEventImpl.getMerchantAccountNumber());
		Assert.assertEquals(null, cardBatchEventImpl.getBatchCycleDate());
	}
}
