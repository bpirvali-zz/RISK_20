package com.intuit.bre.ruledata;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.OracleTypes;

import com.intuit.bre.ruledata.interfaces.DataStorageDAO;

public class DataStorageDAOOasisImpl extends AbstractJDBCDAO implements
		DataStorageDAO {

	String orclDriver = "";
	String uRL = "";
	String userName = "";
	String password = "";

	public DataStorageDAOOasisImpl(DataStorageUtil.DBInfo dbInfo) {
		super(dbInfo.getDriver(), dbInfo.getURL(),
				dbInfo.getUserName(), dbInfo.getPassWord());
	}

	/*
	@Override
	public boolean testConnection() {
		return testConn("SELECT 1 AS C1 FROM DUAL");
	}
	*/
	
	/*
	// EXECdbo.usp_BRE_Agg_Credit'4266961000000339','AUTH','SUM', 19,'DAY'
	public void testCallTestProc() throws SQLException {
		String sProc = "{CALL SYSTEM.TEST_PROC2(?, ?, ?, ?, ?, ?)}";
		CallableStatement cStmt = getConn().prepareCall(sProc);

		// IN parms
		cStmt.setString(1, "'4266961000000339'");
		cStmt.setString(2, "'AUTH'");
		cStmt.setString(3, "'SUM'");
		cStmt.setInt(4, 19);
		cStmt.setString(5, "'DAY'");

		// OUT parm
		cStmt.registerOutParameter(6, OracleTypes.CURSOR);

		cStmt.executeQuery();
		double d1=-1, d2=-1;
		ResultSet rs = (ResultSet) cStmt.getObject(6);
		while (rs.next()) {
			d1 = rs.getDouble(1);
			d2 = rs.getDouble(2);
		}
		System.out.println("stored proc returns:" + d1 + ", " + d2);
	}
	*/
	
}