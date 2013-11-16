package com.intuit.psd.risk.risk2.topology.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.intuit.psd.risk.risk2.topology.interfaces.RowMapper;

public class IncomingRiskEvents implements RowMapper<IncomingRiskEvents> {
	private int EVENT_TYPE_ID;
	private int WORKER_ID;
	private String MERCHANTACCOUNTNUMBER;
	private Date EVENT_DATE;
	private int EVENT_ID;
	private String EVENT_DETAILS;
	private int RETRIES;
	private int PROCESSING_STATUS;
	private String ERROR_MSG;
	
	public IncomingRiskEvents() {
		
	}
	public IncomingRiskEvents(int eVENT_TYPE_ID, int wORKER_ID,
			String mERCHANTACCOUNTNUMBER, Date eVENT_DATE, int eVENT_ID,
			String eVENT_DETAILS, int rETRIES, int pROCESSING_STATUS,
			String eRROR_MSG) {
		super();
		EVENT_TYPE_ID = eVENT_TYPE_ID;
		WORKER_ID = wORKER_ID;
		MERCHANTACCOUNTNUMBER = mERCHANTACCOUNTNUMBER;
		EVENT_DATE = eVENT_DATE;
		EVENT_ID = eVENT_ID;
		EVENT_DETAILS = eVENT_DETAILS;
		RETRIES = rETRIES;
		PROCESSING_STATUS = pROCESSING_STATUS;
		ERROR_MSG = eRROR_MSG;
	}

	@Override
	public String toString() {
		return "IncomingRiskEvents [EVENT_TYPE_ID=" + EVENT_TYPE_ID
				+ ", WORKER_ID=" + WORKER_ID + ", MERCHANTACCOUNTNUMBER="
				+ MERCHANTACCOUNTNUMBER + ", EVENT_DATE=" + EVENT_DATE
				+ ", EVENT_ID=" + EVENT_ID + ", EVENT_DETAILS=" + EVENT_DETAILS
				+ ", RETRIES=" + RETRIES + ", PROCESSING_STATUS="
				+ PROCESSING_STATUS + ", ERROR_MSG=" + ERROR_MSG + "]";
	}

	/**
	 * @param args
	 */

	public IncomingRiskEvents mapRow(ResultSet rs) {
		try {
			return new IncomingRiskEvents(
					rs.getInt(1),
					rs.getInt(2),
					rs.getString(3),
					rs.getDate(4),
					rs.getInt(5),
					rs.getString(6),
					rs.getInt(7),
					rs.getInt(8),
					rs.getString(9)
					);
		} catch( SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setPreparedStatement(PreparedStatement ps) {
		throw new RuntimeException("Not implemented for this class!");
	}
}
