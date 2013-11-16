package com.intuit.bre.ruledata;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intuit.bre.ruledata.interfaces.DataStorageDAO;


public abstract class AbstractJDBCDAO implements DataStorageDAO{
    private static Logger logger = LoggerFactory.getLogger(AbstractJDBCDAO.class);

    private Connection conn;
	private Statement stmt;

	private final String driverClassName;
	private final String jdbcURL;
	private final String User;
	private final String pw;

	public Connection getConn() {
		return conn;
	}

	/*
	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public String getJdbcURL() {
		return jdbcURL;
	}

	public String getUser() {
		return User;
	}

	public String getPw() {
		return pw;
	}
	*/

	AbstractJDBCDAO(String driverClassName, String jdbcURL, String User,
			String pw) {
		this.driverClassName = driverClassName;
		this.jdbcURL = jdbcURL;
		this.User = User;
		this.pw = pw;

		conn = null;
		stmt = null;
		try { 
			 Class.forName(driverClassName); 
			conn = DriverManager.getConnection(jdbcURL, User, pw);
			//conn = getConnection();
			stmt = conn.createStatement();
		} catch (Exception e) {
			logger.error("Error: Failed to open the DB connection!", e);
			throw new RuntimeException(
					"Error: Failed to open the DB connection!");
		}
	}

	@Override
	public List<Map<String, DataElement>> executeQuery(String query) {
		ResultSet rs = null;
		List<Map<String, DataElement>> list = null;
		try {
			logger.debug("Executing query: " + query);
			rs = stmt.executeQuery(query);
			list = resultSetToArrayList(rs);
		} catch (SQLException e) {
			logger.error("Error: excuting query", e);
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("Error while attempting to close result set", e);
			}
		}
		return list;
	}	

	@Override
	public List<Map<String, DataElement>> executeProc(String query) {
		CallableStatement cStmt = null;
		ResultSet rs = null;
		List<Map<String, DataElement>> list = null;
		try {
			logger.debug("Executing proc: " + query);
			cStmt = getConn().prepareCall(query);
			rs = cStmt.executeQuery();
			list = resultSetToArrayList(rs);
			return list;
		} catch (SQLException e) {
			logger.error("Error while calling " + query, e);
			throw new RuntimeException("Error: " + query + " failed!", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				logger.error("Error while closing result set", e);
			}
		}
	}
	
	
	/*
	@Override
	public boolean testConnection() {
		throw new RuntimeException("Not implemented at the abstract level!");
	}
	*/

	@Override
	public void close() {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error("Error while attempting to close statement", e);
			}
			stmt = null;
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Error while attempting to close connection", e);
			}
			conn = null;
		}
	}

	private static class ValueObj {
		private final String type;
		private final Object value;

		public ValueObj(String type, Object val) {
			this.type = type;
			this.value = val;
		}

		public String getType() {
			return type;
		}

		public Object getValue() {
			return value;
		}
	}

	/*
	protected boolean testConn(String testSQL) {
		//String testSQL="SELECT 1 AS C1";
		List<Map<String, DataElement>> l = executeQuery(testSQL);
		DataElement[] de = l.get(0).values().toArray(new DataElement[0]);
		Integer i = (Integer)de[0].getValue();
		if (i==1)
			return true;
		//for (Map.Entry<String, DataElement> e: l.get(0).entrySet())
			
		return false;
	}
	*/
	
	protected static List<Map<String, DataElement>> resultSetToArrayList(
			ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();

		List<Map<String, DataElement>> list = new ArrayList<Map<String, DataElement>>();
		while (rs.next()) {
			Map<String, DataElement> row = new HashMap<String, DataElement>(
					columns);
			for (int i = 1; i <= columns; ++i) {
				// if (md.getColumnName(i).equals("BUSINESSMODEINTERNET"))
				// d = rs.getDouble(i);
				String sColName = md.getColumnName(i).toUpperCase();
				ValueObj v = normalizeSQLTypes(md.getColumnTypeName(i).toUpperCase(), rs, i);
				DataElement element = new DataElement(sColName,
						v.getType(), v.getValue());
				//System.out.println(element.toString());
				row.put(sColName, element);
			}
			list.add(row);
		}
		
		// if no result set was returned then fill all values with NULLs
		if (list.size() == 0) {
			Map<String, DataElement> row = new HashMap<String, DataElement>(
					columns);
			for (int i = 1; i <= columns; ++i) {
				String sColName = md.getColumnName(i).toUpperCase();
				ValueObj v = getNullValueForSQLType(md.getColumnTypeName(i).toUpperCase());
				DataElement element = new DataElement(sColName,
						v.getType(), v.getValue());
				row.put(sColName, element);
			}
			list.add(row);
		}
		
		return list;
	}

	private static ValueObj getNullValueForSQLType(String sqlType) {
		if (sqlType.contains("CHAR"))
			return new ValueObj(DataElement.TYPE_STRING, null);

		if (sqlType.equals("BIGINT")) {
			return new ValueObj(DataElement.TYPE_LONG, null);
		}
		
		if (sqlType.equals("SMALLINT") || sqlType.equals("INT") || sqlType.equals("INTEGER")) {
			return new ValueObj(DataElement.TYPE_INT, null);
		}
		if (sqlType.equals("DATE"))
			return new ValueObj(DataElement.TYPE_DATE, null);
		
		if (sqlType.equals("TIMESTAMP"))
			return new ValueObj(DataElement.TYPE_DATE, null);

		if (sqlType.equals("FLOAT") || sqlType.equals("DOUBLE") || sqlType.equals("REAL") || sqlType.equals("NUMBER")
				|| sqlType.equals("MONEY") || sqlType.equals("SMALLMONEY") ) {
			return new ValueObj(DataElement.TYPE_DOUBLE, null);
		}
		throw new RuntimeException("Error: Failed to convert SQL Type");
	}
	
	private static ValueObj normalizeSQLTypes(String sqlType, ResultSet rs,
			int i) {
		try {
			if (sqlType.contains("CHAR"))
				return new ValueObj(DataElement.TYPE_STRING, rs.getString(i));

			if (sqlType.equals("BIGINT")) {
				long val = rs.getLong(i);
				if (rs.wasNull()) {
					return new ValueObj(DataElement.TYPE_LONG, null);
				} else {
					return new ValueObj(DataElement.TYPE_LONG, new Long(val));					
				}
			}
			
			if (sqlType.equals("SMALLINT") || sqlType.equals("INT") || sqlType.equals("INTEGER")) {
				int val = rs.getInt(i);
				if (rs.wasNull()) {
					return new ValueObj(DataElement.TYPE_INT, null);
				} else {
					return new ValueObj(DataElement.TYPE_INT, new Integer(val));					
				}
			}
			if (sqlType.equals("DATE"))
				return new ValueObj(DataElement.TYPE_DATE, rs.getDate(i));
			
			if (sqlType.equals("TIMESTAMP"))
				return new ValueObj(DataElement.TYPE_DATE, rs.getDate(i));

			if (sqlType.equals("FLOAT") || sqlType.equals("DOUBLE") || sqlType.equals("REAL") || sqlType.equals("NUMBER")
					|| sqlType.equals("MONEY") || sqlType.equals("SMALLMONEY") ) {
				double val = rs.getDouble(i);
				if (rs.wasNull()) {
					return new ValueObj(DataElement.TYPE_DOUBLE, null);
				} else {
					return new ValueObj(DataElement.TYPE_DOUBLE, new Double(val));					
				}
			}

	} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
		throw new RuntimeException("Error: Failed to convert SQL Type");
	}

}
