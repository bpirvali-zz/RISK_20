package com.intuit.bre.ruledata;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intuit.bre.ruledata.interfaces.DataStorageDAO;

public class DataStorageDAOMSSQLImpl extends AbstractJDBCDAO implements DataStorageDAO {
    private static Logger logger = LoggerFactory.getLogger(DataStorageDAOMSSQLImpl.class);
    
    //CallableStatement cstmtBRE_AGG_CREDIT = null;
	public DataStorageDAOMSSQLImpl(DataStorageUtil.DBInfo dbInfo) {
		super(dbInfo.getDriver(), dbInfo.getURL(),
				dbInfo.getUserName(), dbInfo.getPassWord());
	}

	/*
	@Override
	public boolean testConnection() {
		return testConn("SELECT 1 AS C1");
	}
	*/
	
	/*
	protected boolean testConnection(String testSQL) {
		List<Map<String, DataElement>> l = executeQuery(testSQL);
		DataElement[] de = l.get(0).values().toArray(new DataElement[0]);
		Integer i = (Integer)de[0].getValue();
		if (i==1)
			return true;
			
		return false;
	}
	*/

	/*
	@Override
	public List<Map<String, DataElement>> executeProc(String query, Object procInputParameters) {
		if (query.contains("BRE_AGG_CREDIT")) {
			if (procInputParameters instanceof MSSQLProcBREAggCreditInputParameters==false)
				throw new RuntimeException("Wrong object type: expected: MSSQLProcBREAggCreditInputParameters");
			
			return executeProcBRE_AGG_CREDIT(query, (MSSQLProcBREAggCreditInputParameters)procInputParameters);
		}
		throw new RuntimeException("Error: Failed to identify the stored procedure!");
	}
	
	// dbo.usp_BRE_Agg_Credit '4266961000000339', 'TRaN', 'SuM', 35, 'mONTH', 'AMEX', '2013-08-01'
	private List<Map<String, DataElement>> executeProcBRE_AGG_CREDIT(String sSQL,
			MSSQLProcBREAggCreditInputParameters input) {
		ResultSet rs = null;
		List<Map<String, DataElement>> list = null;
		try {
			if (cstmtBRE_AGG_CREDIT==null)			
				cstmtBRE_AGG_CREDIT = getConn().prepareCall(sSQL);
			cstmtBRE_AGG_CREDIT.setString(1, input.getAccountNumber());
			cstmtBRE_AGG_CREDIT.setString(2, input.getAggType());
			cstmtBRE_AGG_CREDIT.setString(3, input.getAggFunction());
			cstmtBRE_AGG_CREDIT.setInt(4, input.getDays());
			cstmtBRE_AGG_CREDIT.setString(5, input.getTimeUnit());
			cstmtBRE_AGG_CREDIT.setString(6, input.getCardType());
			cstmtBRE_AGG_CREDIT.setDate(7, input.getBatchCycleDate());
			rs = cstmtBRE_AGG_CREDIT.executeQuery();
			list = resultSetToArrayList(rs);
		} catch (SQLException e) {
			logger.error("Error: executeProcBRE_AGG_CREDIT failed!", e);
			throw new RuntimeException("Error: executeProcBRE_AGG_CREDIT failed!", e);
		} finally {
			try {
				if (rs!=null) 
					rs.close();
			} catch (SQLException e) {
				logger.error("Error while closing result set", e);
			}
			rs = null;
			
		}
		return list;
	}
	
	@Override
	public void close() {
		try {
			if (cstmtBRE_AGG_CREDIT != null) 
				cstmtBRE_AGG_CREDIT.close();			
		} catch (SQLException e) {
			logger.error("Error while closing cstmtBRE_AGG_CREDIT", e);
		} finally {
			cstmtBRE_AGG_CREDIT = null;
			super.close();
		}
	}
	*/
	
}