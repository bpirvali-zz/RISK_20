package com.intuit.psd.risk.risk2.topology.tuples;

import java.io.Serializable;
import java.sql.Timestamp;

//@multi-threaded
public class CardBatchEvent implements Serializable {
	//private static final Logger logger = LoggerFactory.getLogger(CardBatchEvent.class);
	private static final long serialVersionUID = 1L;
	public final String accNo;
	public final Timestamp eventTimestamp;
	public final int systemID;
	public final int eventCatID;
	public final int eventTypeID;
	public final int eventID;
	public final String key;
	private long timeStartProcessing;
	//public volatile boolean processed; 
	private volatile int retries; 
	
	public CardBatchEvent(String accNo, Timestamp eventTimestamp, int systemID, int eventCatID, int eventTypeID, int eventID) {
		this.accNo = accNo;
		this.eventTimestamp = eventTimestamp;
		this.systemID = systemID;
		this.eventCatID = eventCatID;
		this.eventTypeID = eventTypeID;
		this.eventID = eventID;
		//this.processed = false;
		this.retries = 0;
		this.timeStartProcessing = System.currentTimeMillis();
		this.key = 
			(accNo==null ? "" : accNo) + "," + 
			(eventTimestamp==null ? "" : eventTimestamp.toString()) + "," + 
			systemID + "," + 
			eventCatID + "," + 
			eventTypeID + "," + 
			eventID;
	}

	@Override
	public String toString() {
		return "CardBatchEvent [accNo=" + accNo + ", eventTimestamp=" + eventTimestamp
				+ ", timeStartProcessing=" + timeStartProcessing + ", retries=" + retries + "]";
	}

//	public CardBatchEvent mapRow(ResultSet rs) {
//		try {
//			return new CardBatchEvent(
//					rs.getString(1),
//					rs.getDate(2)
//					);
//		} catch( SQLException e) {
//			logger.error("mapRow Exception", e);
//		}
//		return null;
//	}
	
//	public String createKey() {
//		return (accNo==null ? "" : accNo) + "," + (eventTimestamp==null ? "" : eventTimestamp.toString());
//	}

	public String getKey() {
		return key;
	}
	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public long getTimeStartProcessing() {
		return timeStartProcessing;
	}

	public void setTimeStartProcessing(long timeStartProcessing) {
		this.timeStartProcessing = timeStartProcessing;
	}	
}
