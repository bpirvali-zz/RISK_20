package com.intuit.bre.ruledata;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.h2.tools.SimpleResultSet;

public class H2StoredProc {

	private static String merchantId;
	private static Date batchCycleDate;
	private static String cardType;
	private static String aggregateOperation;
	private static String authOrTran;
	private static String dayOrMonth;
	private static int interval;
	private static int cnt;
	private static double amt;
	private static int saleCnt;
	private static double saleAmt;
	private static int returnCnt;
	private static double returnAmt;
	private static int saleKeyedCnt;
	private static double saleKeyedAmt;
	private static int returnKeyedCnt;
	private static double returnKeyedAmt;
	
	public static ResultSet mockBREAggCreditSP(String merchantAccount, String tranOrAuth, String aggOp, int interval,
			String dayOrMonth, String cardType, String dateStr) throws SQLException {
		SimpleResultSet rs = new SimpleResultSet();
		rs.addColumn("MerchantID", Types.VARCHAR, 16, 0);
		rs.addColumn("BatchCycleDate", Types.DATE, 10, 0);
		rs.addColumn("CardType", Types.VARCHAR, 11, 0);
		rs.addColumn("AggregateOperation", Types.VARCHAR, 3, 0);
		rs.addColumn("AuthOrTran", Types.VARCHAR, 4, 0);
		rs.addColumn("DayOrMonth", Types.VARCHAR, 3, 0);
		rs.addColumn("Interval", Types.INTEGER, 4, 0);
		rs.addColumn("Cnt", Types.INTEGER , 10, 0);
		rs.addColumn("Amt", Types.DOUBLE, 19, 4);
		rs.addColumn("Sale_Cnt", Types.INTEGER, 10, 0);
		rs.addColumn("Sale_Amt", Types.DOUBLE, 19, 4);
		rs.addColumn("Return_Cnt", Types.INTEGER, 10, 0);
		rs.addColumn("Return_Amt", Types.DOUBLE, 19, 4);
		rs.addColumn("Sale_Keyed_Cnt", Types.INTEGER, 10, 0);
		rs.addColumn("Sale_Keyed_Amt", Types.DOUBLE, 19, 4);
		rs.addColumn("Return_Keyed_Cnt", Types.INTEGER, 10, 0);
		rs.addColumn("Return_Keyed_Amt", Types.DOUBLE, 19, 4);
		
		rs.addRow(merchantId, batchCycleDate, cardType, aggregateOperation, authOrTran, dayOrMonth, interval,
				cnt, amt, saleCnt, saleAmt, returnCnt, returnAmt, saleKeyedCnt, saleKeyedAmt, returnKeyedCnt, returnKeyedAmt);
		
		return rs;
	}

	public static String getMerchantId() {
		return merchantId;
	}

	public static void setMerchantId(String merchantId) {
		H2StoredProc.merchantId = merchantId;
	}

	public static Date getBatchCycleDate() {
		return batchCycleDate;
	}

	public static void setBatchCycleDate(Date batchCycleDate) {
		H2StoredProc.batchCycleDate = batchCycleDate;
	}

	public static String getCardType() {
		return cardType;
	}

	public static void setCardType(String cardType) {
		H2StoredProc.cardType = cardType;
	}

	public static String getAggregateOperation() {
		return aggregateOperation;
	}

	public static void setAggregateOperation(String aggregateOperation) {
		H2StoredProc.aggregateOperation = aggregateOperation;
	}

	public static String getAuthOrTran() {
		return authOrTran;
	}

	public static void setAuthOrTran(String authOrTran) {
		H2StoredProc.authOrTran = authOrTran;
	}

	public static String getDayOrMonth() {
		return dayOrMonth;
	}

	public static void setDayOrMonth(String dayOrMonth) {
		H2StoredProc.dayOrMonth = dayOrMonth;
	}

	public static int getInterval() {
		return interval;
	}

	public static void setInterval(int interval) {
		H2StoredProc.interval = interval;
	}

	public static int getCnt() {
		return cnt;
	}

	public static void setCnt(int cnt) {
		H2StoredProc.cnt = cnt;
	}

	public static double getAmt() {
		return amt;
	}

	public static void setAmt(double amt) {
		H2StoredProc.amt = amt;
	}

	public static int getSaleCnt() {
		return saleCnt;
	}

	public static void setSaleCnt(int saleCnt) {
		H2StoredProc.saleCnt = saleCnt;
	}

	public static double getSaleAmt() {
		return saleAmt;
	}

	public static void setSaleAmt(double saleAmt) {
		H2StoredProc.saleAmt = saleAmt;
	}

	public static int getReturnCnt() {
		return returnCnt;
	}

	public static void setReturnCnt(int returnCnt) {
		H2StoredProc.returnCnt = returnCnt;
	}

	public static double getReturnAmt() {
		return returnAmt;
	}

	public static void setReturnAmt(double returnAmt) {
		H2StoredProc.returnAmt = returnAmt;
	}

	public static int getSaleKeyedCnt() {
		return saleKeyedCnt;
	}

	public static void setSaleKeyedCnt(int saleKeyedCnt) {
		H2StoredProc.saleKeyedCnt = saleKeyedCnt;
	}

	public static double getSaleKeyedAmt() {
		return saleKeyedAmt;
	}

	public static void setSaleKeyedAmt(double saleKeyedAmt) {
		H2StoredProc.saleKeyedAmt = saleKeyedAmt;
	}

	public static int getReturnKeyedCnt() {
		return returnKeyedCnt;
	}

	public static void setReturnKeyedCnt(int returnKeyedCnt) {
		H2StoredProc.returnKeyedCnt = returnKeyedCnt;
	}

	public static double getReturnKeyedAmt() {
		return returnKeyedAmt;
	}

	public static void setReturnKeyedAmt(double returnKeyedAmt) {
		H2StoredProc.returnKeyedAmt = returnKeyedAmt;
	}
	
}
