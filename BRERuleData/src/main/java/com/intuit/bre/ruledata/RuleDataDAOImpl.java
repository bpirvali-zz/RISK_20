package com.intuit.bre.ruledata;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intuit.bre.ruledata.interfaces.DataStorageDAO;
import com.intuit.bre.ruledata.interfaces.MerchantCache;
import com.intuit.bre.ruledata.interfaces.RuleDataDAO;
import com.intuit.bre.ruledata.interfaces.ThrRes;

public class RuleDataDAOImpl implements RuleDataDAO {

    private static Logger logger = LoggerFactory.getLogger(RuleDataDAOImpl.class);

    private final ThrRes thrRes;
	private final RuleDataConfig config;
	private final MerchantCache merchantCache;
	
	public RuleDataDAOImpl(RuleDataConfig config, ThrRes thrRes) {
		this.thrRes = thrRes;
		this.config = config;
		this.merchantCache = thrRes.getMerchantCache();
	}
	
	/*
	 * This function could use some performance optimizations if needed
	 * */
	static String replaceQueryTemplates(RuleDataQuery query, String merchantAccountNumber, Date BCD) {
		SimpleDateFormat mssqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat oracleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
		String sSQL = query.getQuery();
		sSQL = sSQL.replace("[MA_NUM]", merchantAccountNumber);
		
		String dbConn = query.getDbConnection();
		if ("ORACLE".equals(dbConn)) {
			sSQL = sSQL.replace("[BCD]", "to_date('"+oracleDateFormat.format(BCD)+"','MM/DD/YYYY')");			
		} else if ("MSSQL".equals(dbConn)) {
			sSQL = sSQL.replace("[BCD]", mssqlDateFormat.format(BCD));
		} else if ("VOLTDB".equals(dbConn)) {
			
		} else if ("HSQLDB".equals(dbConn)) {
			
		}
		
		return sSQL;		
	}
	
	/*
	private static String replaceAggQueryTemplates(String sSQL, String merchantAccountNumber, Date BCD, AggDataElement aggElement, int days ) {		
		//SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Calendar cal = Calendar.getInstance();
		cal.setTime(BCD);
		cal.add(Calendar.DATE, (-1) * (days - 1));
		String startDateStr = "'" + dateFormat.format(cal.getTime()) + "'";
		String endDateStr = "'" + dateFormat.format(BCD) + "'";
		
		// build column names list
		String sColNames="";
		List<RuleDataElement> rdElements = aggElement.getRdElements();
		for (RuleDataElement el: rdElements) {
			sColNames += el.getColumn() + ",";
		}
		if (sColNames!=null)
			sColNames = sColNames.substring(0, sColNames.length()-1);
		
		sSQL = sSQL.replace("[MA_NUM]","'" + merchantAccountNumber + "'");
		sSQL = sSQL.replace("[AGGREGATE_FUNCTIONS]", sColNames);
		sSQL = sSQL.replace("[DATE_START_BCD]", startDateStr);
		sSQL = sSQL.replace("[DATE_END_BCD]", endDateStr);
		return sSQL;		
	}
	
	private static void printRow(Map<String,DataElement> row) {
		for (Map.Entry<String, DataElement> entry : row.entrySet())
		    System.out.println(entry.getKey() + "/" + entry.getValue());		
	}
	*/

	private void getAllLookupValues(String key, String merchantAccountNumber, Date BCD) {
	    RuleDataElement element = config.getElementMap().get(key);
		RuleDataQuery query = element.getQuery();
		Set<RuleDataElement> rdElements = query.getRuleDataElements();
		
	    String connectionType = element.getQuery().getDbConnection();
		String sSQL = replaceQueryTemplates(query, merchantAccountNumber, BCD);
		DataStorageDAO dsDao = thrRes.getDao(connectionType);
		MerchantCache cache = thrRes.getMerchantCache();
		
		List<Map<String, DataElement>> dataElements = null;
		if (query.isStoredProc()) {
			dataElements = dsDao.executeProc(sSQL);
		} else {
			dataElements = dsDao.executeQuery(sSQL);
		}
		
		if (dataElements.size()>1)
			throw new RuntimeException("Returned resultset more than 1 rows is not supported!");
		
		// TODO:BP log the fact that no rows came backS
		if (dataElements.size()==1) {
		
			Map<String,DataElement> row = dataElements.get(0);
			//printRow(row);
			for (RuleDataElement e: rdElements) {
				String colName = e.getColumn().toUpperCase();
				String k = e.getKey();
				RuleDataType type = e.getType();
				DataElement de = row.get(colName);
				if (de==null) {
					String msg = "column:" + colName + " was not returned from query:" + sSQL;
					throw new RuntimeException(msg);
				}
				//System.out.println(k + "," + type + ":" +de.toString());
				cache.putValue(merchantAccountNumber, BCD, k, type, de);
			}
		}
	}

	/*
	private void getAllAggValues(String key, Object merchantInfo) {
		if (merchantInfo instanceof MSSQLProcBREAggCreditInputParameters==false)  
			throw new RuntimeException("Object passed is not 'MSSQLProcBREAggCreditInputParameters'!");
		
		MSSQLProcBREAggCreditInputParameters input = (MSSQLProcBREAggCreditInputParameters)merchantInfo;
	    AggDataElement aggElement = config.getAggMap().get(key);
		RuleDataQuery query = aggElement.getQuery();
		
		// Getting con-type, sSQL, db-connetion, and cache
	    String connectionType = aggElement.getQuery().getDbConnection();
		String sSQL = query.getQuery().trim();
		DataStorageDAO dsDao = thrRes.getDao(connectionType);
		MerchantCache cache = thrRes.getMerchantCache();
		
		List<Map<String, DataElement>> dataElements = null;
		if (connectionType.equals(ThrResImpl.DAO_NAME_MSSQL)) {
			dataElements = dsDao.executeProc(sSQL, input);
		} else { 
			// The same for VOLTDB as well as ORACLE
			// sSQL = replaceAggQueryTemplates(sSQL, merchantAccountNumber, BCD, aggElement, days);
			// dataElements = dsDao.executeQuery(sSQL);
		}
		
		if (dataElements.size()>1)
			throw new RuntimeException("Returned resultset more than 1 rows is not supported!");
		
		// TODO:BP log the fact that no rows came backs
		if (dataElements.size()==0) {
			System.out.println("No rows was returned for the query:" + sSQL);
			return;
		}
		
		Map<String,DataElement> row = dataElements.get(0);
		//printRow(row);
		int i=1;
		for (RuleDataElement e: aggElement.getRdElements()) {
			String colName = e.getColumn().toUpperCase();
			String dbColName = "C" + i++;
			String k = buildAggKey(e.getKey(), input);
			RuleDataType type = e.getType();
			DataElement de = row.get(colName);
			if (de==null)
				de = row.get(dbColName);			
			if (de==null) {
				String msg = "column:" + colName + " was not returned from query:" + sSQL;
				throw new RuntimeException(msg);
			}
			//System.out.println(k + "," + type + ":" +de.toString());
			cache.putValue(input.getAccountNumber(), input.getBatchCycleDate(), k, type, de);
		}		
		
		
			
	}
	
	private void getAllAggValues(String key, String merchantAccountNumber, Date BCD, int days) {
	    AggDataElement aggElement = config.getAggMap().get(key);
		RuleDataQuery query = aggElement.getQuery();
		
		// Getting con-type, sSQL, db-connetion, and cache
	    String connectionType = aggElement.getQuery().getDbConnection();
		String sSQL = query.getQuery().trim();
		DataStorageDAO dsDao = thrRes.getDao(connectionType);
		MerchantCache cache = thrRes.getMerchantCache();
		
		List<Map<String, DataElement>> dataElements = null;
		if (connectionType.equals(ThrResImpl.DAO_NAME_MSSQL)) {
			Calendar cal = new GregorianCalendar(2013, 8, 1);		
			MSSQLProcBREAggCreditInputParameters input = 
					new MSSQLProcBREAggCreditInputParameters("4266961000000339", new java.sql.Date(cal.getTime().getTime()), "AMEX", "TRAN", "SUM", 35, "MONTH");
			dataElements = dsDao.executeProc(sSQL, input);
		} else { 
			// The same for VOLTDB as well as ORACLE
			sSQL = replaceAggQueryTemplates(sSQL, merchantAccountNumber, BCD, aggElement, days);
			dataElements = dsDao.executeQuery(sSQL);
		}
		
		if (dataElements.size()>1)
			throw new RuntimeException("Returned resultset more than 1 rows is not supported!");
		
		// TODO:BP log the fact that no rows came backs
		if (dataElements.size()==0) {
			System.out.println("No rows was returned for the query:" + sSQL);
			return;
		}
		
		Map<String,DataElement> row = dataElements.get(0);
		printRow(row);
		int i=1;
		for (RuleDataElement e: aggElement.getRdElements()) {
			String colName = e.getColumn().toUpperCase();
			String dbColName = "C" + i++;
			String k = buildAggKey(e.getKey(), days);
			RuleDataType type = e.getType();
			DataElement de = row.get(colName);
			if (de==null)
				de = row.get(dbColName);			
			if (de==null) {
				String msg = "column:" + colName + " was not returned from query:" + sSQL;
				throw new RuntimeException(msg);
			}
			//System.out.println(k + "," + type + ":" +de.toString());
			cache.putValue(merchantAccountNumber, BCD, k, type, de);
		}		
	}
	*/
	
	/*
	private static String buildAggKey(String key, int days) {
		return key + "_" + days;
	}

	private static String buildAggKey(String key, Object merchantInfo) {
		if (merchantInfo instanceof MSSQLProcBREAggCreditInputParameters==false)  
			throw new RuntimeException("Object passed is not 'MSSQLProcBREAggCreditInputParameters'!");
		
		MSSQLProcBREAggCreditInputParameters input = (MSSQLProcBREAggCreditInputParameters)merchantInfo;
		return input.getAggType() + "_" + input.getTimeUnit() + "_" + input.getCardType() + 
				"_" + input.getAggFunction() + "_" + key + "_" + input.getDays();
	}
	*/
	
	
	@Override
	public String getString(String key, String merchantAccountNumber, Date BCD) {
		if (merchantCache.isCachedStr(merchantAccountNumber, BCD, key))
			return merchantCache.getString(key, merchantAccountNumber, BCD);
		
		getAllLookupValues(key, merchantAccountNumber, BCD);
		return merchantCache.getString(key, merchantAccountNumber, BCD);
	}
	
	@Override
	public Integer getInteger(String key, String merchantAccountNumber, Date BCD) {
		if (merchantCache.isCachedInt(merchantAccountNumber, BCD, key))
			return merchantCache.getInteger(key, merchantAccountNumber, BCD);
		
		getAllLookupValues(key, merchantAccountNumber, BCD);
		return merchantCache.getInteger(key, merchantAccountNumber, BCD);
	}	

	@Override
	public Long getLong(String key, String merchantAccountNumber, Date BCD) {
		if (merchantCache.isCachedLong(merchantAccountNumber, BCD, key))
			return merchantCache.getLong(key, merchantAccountNumber, BCD);
		
		getAllLookupValues(key, merchantAccountNumber, BCD);
		return merchantCache.getLong(key, merchantAccountNumber, BCD);
	}

	@Override
	public Double getDouble(String key, String merchantAccountNumber, Date BCD) {
		if (merchantCache.isCachedDouble(merchantAccountNumber, BCD, key))
			return merchantCache.getDouble(key, merchantAccountNumber, BCD);
		
		getAllLookupValues(key, merchantAccountNumber, BCD);
		return merchantCache.getDouble(key, merchantAccountNumber, BCD);
	}

	@Override
	public Date getDate(String key, String merchantAccountNumber, Date BCD) {
		if (merchantCache.isCachedDate(merchantAccountNumber, BCD, key))
			return merchantCache.getDate(key, merchantAccountNumber, BCD);
		
		getAllLookupValues(key, merchantAccountNumber, BCD);
		return merchantCache.getDate(key, merchantAccountNumber, BCD);
	}

	/*
	@Override
	public Double getAggDouble(String key, String merchantAccountNumber, Date BCD, int days) {
		String newKey = buildAggKey(key, days);
		if (merchantCache.isCachedDouble(merchantAccountNumber, BCD, newKey))
			return merchantCache.getDouble(newKey, merchantAccountNumber, BCD);
		
		getAllAggValues(key, merchantAccountNumber, BCD, days);
		return merchantCache.getDouble(newKey, merchantAccountNumber, BCD);
	}

	@Override
	public Double getAggDouble(String key, String aggType, String monthOrDay,
			String aggFunction, String cardType, String merchantAccountNumber,
			Date BCD, int days) {
		MSSQLProcBREAggCreditInputParameters input = 
				new MSSQLProcBREAggCreditInputParameters(merchantAccountNumber, 
						new java.sql.Date(BCD.getTime()), cardType, aggType, aggFunction, days, monthOrDay);

		String newKey = buildAggKey(key, input);
		if (merchantCache.isCachedDouble(merchantAccountNumber, BCD, newKey)) {
			System.out.println("Cache Hit:" + newKey);
			return merchantCache.getDouble(newKey, merchantAccountNumber, BCD);
		}
		getAllAggValues(key, input);
		return merchantCache.getDouble(newKey, merchantAccountNumber, BCD);
	}

	@Override
	public Long getAggLong(String key, String aggType, String monthOrDay,
			String aggFunction, String cardType, String merchantAccountNumber,
			Date BCD, int days) {
		MSSQLProcBREAggCreditInputParameters input = 
				new MSSQLProcBREAggCreditInputParameters(merchantAccountNumber, 
						new java.sql.Date(BCD.getTime()), cardType, aggType, aggFunction, days, monthOrDay);

		String newKey = buildAggKey(key, input);
		if (merchantCache.isCachedLong(merchantAccountNumber, BCD, newKey)) {
			System.out.println("Cache Hit:" + newKey);
			return merchantCache.getLong(newKey, merchantAccountNumber, BCD);
		}
		
		getAllAggValues(key, input);
		return merchantCache.getLong(newKey, merchantAccountNumber, BCD);
	}
	*/
	
}
