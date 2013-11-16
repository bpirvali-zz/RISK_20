package com.intuit.psd.risk.risk2.topology.interfaces;

import java.util.List;
import java.util.Map;


/**
 * This is the data storage facing DAO interface
 */
public interface DataStorageDAO {
	/**
	 * This function returns a map of rows of type T.
	 * 
	 * @param query			The SQL query to be executed.
	 * @param rowMapper		The row-mapper<T> class is similar to JDBCTemplate one except with 
	 * 						the advantage of being type safe.
	 * @param pkIndex		The pk-column/any unique-column of the table of type T2.
	 * @return				A map of rows representing the result set table. 
	 * 						The keys are of type T and values are of type T2
	 */
	public <T2, T> Map<T2, T> executeQuery(String query, RowMapper<T> rowMapper, int pkIndex);
	
	/**
	 * This function returns a list of rows of type T.
	 * 
	 * @param query			The SQL query to be executed.
	 * @param rowMapper		The row-mapper<T> class is similar to JDBCTemplate one except with 
	 * 						the advantage of being type safe.
	 * @return				A list of rows representing the result set table. 
	 * 						The rows are of type T2
	 */
	public <T> List<T> executeQuery(String query, RowMapper<T> rowMapper);
	
	/**
	 * This function tests the connectivity to the database.
	 * 
	 * @return true if connection succeeded and false otherwise.
	 */
	public boolean testConnection();
	
	/**
	 * It closes the connection and statement object!
	 */
	public void close();
}

