package com.intuit.psd.risk.processor.card;

import java.sql.Timestamp;
import java.util.Date;

import com.intuit.psd.risk.processor.RiskEvent;

/**
 * @author asookazian
 * 
 */
public class CardBatchEventImpl extends RiskEvent {
	private String merchantAccountNumber;
	private Date batchCycleDate;
	private Timestamp eventTimestamp;
	private int systemID;
	private int eventCatID;
	private int eventTypeID;
	private int eventID;

	public CardBatchEventImpl() {
		super();
	}

	public CardBatchEventImpl(String merchantAccountNumber,
			Timestamp eventTimestamp, int systemID, int eventCatID,
			int eventTypeID, int eventID, String eventType) {
		super();
		this.merchantAccountNumber = merchantAccountNumber;
		this.batchCycleDate = eventTimestamp;
		this.eventTimestamp = eventTimestamp;
		this.systemID = systemID;
		this.eventCatID = eventCatID;
		this.eventTypeID = eventTypeID;
		this.eventID = eventID;
		setEventType(eventType);
	}

	public String getMerchantAccountNumber() {
		return merchantAccountNumber;
	}

	public void setMerchantAccountNumber(String merchantAccountNumber) {
		this.merchantAccountNumber = merchantAccountNumber;
	}

	public Date getBatchCycleDate() {
		return batchCycleDate;
	}

	public void setBatchCycleDate(Date batchCycleDate) {
		this.batchCycleDate = batchCycleDate;
	}

}
