package com.intuit.psd.risk.risk2.topology.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intuit.psd.risk.risk2.topology.interfaces.DataStorageDAO;
import com.intuit.psd.risk.risk2.topology.interfaces.RowMapper;

public abstract class AbstractJDBCDAO implements DataStorageDAO {
	private Connection conn;
	private Statement stmt;

	private final String driverClassName;
	private final String jdbcURL;
	private final String User;
	private final String pw;

	public Connection getConn() {
		return conn;
	}

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
			e.printStackTrace();
			throw new RuntimeException(
					"Error: Failed to open the DB connection!");
		}
	}

	public <T2, T> Map<T2, T> executeQuery(String query, RowMapper<T> rowMapper, int pkIndex) {
		ResultSet rs = null;
		Map<T2, T> map = new HashMap<T2, T>();
		try {
			System.out.println("Executing query: " + query);
			rs = stmt.executeQuery(query);
			//ResultSetMetaData md = rs.getMetaData();
			while(rs.next()) {
				T t = rowMapper.mapRow(rs);
				@SuppressWarnings("unchecked")
				T2 t2 = (T2) rs.getObject(pkIndex);
				map.put(t2, t);
			}
		} catch (SQLException e) {
		// TODO:BP log exception and optionally deal with it!
		e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO:BP Log exception
				e.printStackTrace();
			}
		}
		return map;
	}
	
	public <T> List<T> executeQuery(String query, RowMapper<T> rowMapper) {
		ResultSet rs = null;
		List<T> list = new ArrayList<T>();
		try {
			System.out.println("Executing query: " + query);
			rs = stmt.executeQuery(query);
			//ResultSetMetaData md = rs.getMetaData();
			while(rs.next()) {
				T t = rowMapper.mapRow(rs);
				list.add(t);
			}
		} catch (SQLException e) {
		// TODO:BP log exception and optionally deal with it!
		e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO:BP Log exception
				e.printStackTrace();
			}
		}
		return list;
	}

	
	
	/**
	 * This is a helper function to test the database connection.
	 * 
	 * @param testSql	This SQL statement should return 1 
	 * @return 			True if connection test succeeded and false otherwise
	 */
	protected boolean testConnection(String testSql) {
		try {
			ResultSet rs = stmt.executeQuery(testSql);
			while (rs.next()) {
				int i = rs.getInt(1);
				return (i==1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void close() {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO:BP log exception
				e.printStackTrace();
			}
			stmt = null;
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO:BP log exception
				e.printStackTrace();
			}
			conn = null;
		}
	}
}
