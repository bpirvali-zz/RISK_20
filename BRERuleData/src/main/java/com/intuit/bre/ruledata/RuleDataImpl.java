package com.intuit.bre.ruledata;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intuit.bre.ruledata.interfaces.RuleData;
import com.intuit.bre.ruledata.interfaces.RuleDataDAO;
import com.intuit.bre.ruledata.interfaces.RuleDataInternal;
import com.intuit.bre.ruledata.interfaces.ThrRes;
import com.intuit.bre.ruledata.interfaces.ThrResMgr;

public class RuleDataImpl implements RuleData, RuleDataInternal {
    private static Logger logger = LoggerFactory.getLogger(RuleDataImpl.class);

    private RuleDataDAO ruleDataDao;
	private ThrRes thrRes;
	
	public static RuleData getInstance() throws ClassNotFoundException, SQLException {
		logger.debug("Getting RuleData instance");
		ThrResMgr thrResMrg = ThrResMgrImpl.getInstance();		
		RuleData ruleData = thrResMrg.getRuleData();
		if (ruleData == null) {
			RuleDataInternal rdi = (RuleDataInternal)(new RuleDataImpl());
			thrResMrg.putRuleData(rdi);
			ruleData = rdi;
		}
		return ruleData;
	}
		
	private RuleDataImpl() throws ClassNotFoundException, SQLException {
			thrRes = new ThrResImpl();
			ruleDataDao = new RuleDataDAOImpl(RuleDataConfig.getInstance(), thrRes);
	}
	
	protected void finalize() throws Throwable {
	     try {
	         releaseResources();       
	     } finally {
	         super.finalize();
	     }
	}		
		
	void validateInput(String key, String merchantAccountNumber, Date batchCycleDate, RuleDataType expectedType) throws Exception {
		try {
			if (key == null) {
				throw new RuntimeException("invalid parameters: key is null");
			}
			if (merchantAccountNumber == null) {
				throw new RuntimeException("invalid parameters: merchant account number is null");
			}
			if (merchantAccountNumber.trim().length() == 0) {
				throw new RuntimeException("invalid parameters: merchant account number is empty");
			}
			if (batchCycleDate == null) {
				throw new RuntimeException("invalid parameters: batch cycle date is empty");
			}
			RuleDataConfig config = RuleDataConfig.getInstance();
			Map<String, RuleDataElement> ruleDataElementMap = config.getElementMap();
			RuleDataElement ruleDataElement = ruleDataElementMap.get(key);
			if (ruleDataElement == null) {
				throw new RuntimeException("Key " + key + " not found in " + Configuration.getQueryFile());
			}
			RuleDataType configuredType = ruleDataElement.getType();
			if (configuredType != expectedType) {
				throw new RuntimeException("Key " + key + " is configured in " + Configuration.getQueryFile() 
						+ " as a " + configuredType + " but is being called as if it is a " + expectedType);
			}
		} catch (Exception e) {
			logger.error("Invalid parameters passed", e);
			throw e;
		}
	}
	
	@Override
	public String getString(String key, String merchantAccountNumber, Date batchCycleDate) throws Exception {
		logger.debug("Getting string for key: " + key 
					+ ", account: " + merchantAccountNumber 
					+ ", batchCycleDate: " + batchCycleDate);
		validateInput(key, merchantAccountNumber, batchCycleDate, RuleDataType.STRING);
		return ruleDataDao.getString(key, merchantAccountNumber, batchCycleDate);
	}

	@Override
	public Integer getInteger(String key, String merchantAccountNumber, Date batchCycleDate) throws Exception {
		logger.debug("Getting integer for key: " + key 
				+ ", account: " + merchantAccountNumber 
				+ ", batchCycleDate: " + batchCycleDate);
		validateInput(key, merchantAccountNumber, batchCycleDate, RuleDataType.INTEGER);
		return ruleDataDao.getInteger(key, merchantAccountNumber, batchCycleDate);
	}

	@Override
	public Long getLong(String key, String merchantAccountNumber, Date batchCycleDate) throws Exception {
		logger.debug("Getting long for key: " + key 
				+ ", account: " + merchantAccountNumber 
				+ ", batchCycleDate: " + batchCycleDate);
		validateInput(key, merchantAccountNumber, batchCycleDate, RuleDataType.LONG);
		return ruleDataDao.getLong(key, merchantAccountNumber, batchCycleDate);
	}

	@Override
	public Double getDouble(String key, String merchantAccountNumber, Date batchCycleDate) throws Exception {
		logger.debug("Getting double for key: " + key 
				+ ", account: " + merchantAccountNumber 
				+ ", batchCycleDate: " + batchCycleDate);
		validateInput(key, merchantAccountNumber, batchCycleDate, RuleDataType.DOUBLE);
		return ruleDataDao.getDouble(key, merchantAccountNumber, batchCycleDate);
	}

	@Override
	public Date getDate(String key, String merchantAccountNumber, Date batchCycleDate) throws Exception {
		logger.debug("Getting date for key: " + key 
				+ ", account: " + merchantAccountNumber 
				+ ", batchCycleDate: " + batchCycleDate);
		validateInput(key, merchantAccountNumber, batchCycleDate, RuleDataType.DATE);
		return ruleDataDao.getDate(key, merchantAccountNumber, batchCycleDate);
	}

	@Override
	public void releaseResources() {
		if (thrRes!=null) {
			ruleDataDao = null;
			thrRes.releaseResources();
		}
		thrRes=null;
	}

	/*
	@Override
	public Double getAggDouble(String key, String merchantAccountNumber, Date batchCycleDate, int days) {
		logger.debug("Getting aggregate double for key: " + key 
				+ ", account: " + merchantAccountNumber 
				+ ", batchCycleDate: " + batchCycleDate
				+ ", days: " + days);
		return ruleDataDao.getAggDouble(key, merchantAccountNumber, batchCycleDate, days);
	}

	@Override
	public Double getAggDouble(String key, String aggType, String monthOrDay,
			String aggFunction, String cardType, String merchantAccountNumber,
			Date batchCycleDate, int days) {
		logger.debug("Getting aggregate double for key: " + key 
				+ ", aggType: " + aggType
				+ ", monthOrDay: " + monthOrDay
				+ ", aggFunction: " + aggFunction
				+ ", cardType: " + cardType
				+ ", account: " + merchantAccountNumber 
				+ ", batchCycleDate: " + batchCycleDate
				+ ", days: " + days);
		return ruleDataDao.getAggDouble(key.toUpperCase(), aggType.toUpperCase(), monthOrDay.toUpperCase(), 
				aggFunction.toUpperCase(), cardType.toUpperCase(), merchantAccountNumber, batchCycleDate, days);
	}

	@Override
	public Long getAggLong(String key, String aggType, String monthOrDay,
			String aggFunction, String cardType, String merchantAccountNumber,
			Date batchCycleDate, int days) {
		logger.debug("Getting aggregate long for key: " + key 
				+ ", aggType: " + aggType
				+ ", monthOrDay: " + monthOrDay
				+ ", aggFunction: " + aggFunction
				+ ", cardType: " + cardType
				+ ", account: " + merchantAccountNumber 
				+ ", batchCycleDate: " + batchCycleDate
				+ ", days: " + days);
		return ruleDataDao.getAggLong(key.toUpperCase(), aggType.toUpperCase(), monthOrDay.toUpperCase(), 
				aggFunction.toUpperCase(), cardType.toUpperCase(), merchantAccountNumber, batchCycleDate, days);
	}
	*/

	/* (non-Javadoc)
	 * @see com.intuit.bre.ruledata.IRuleData#rollingReal(java.lang.String, int, long, java.util.Date)
	 */
	/*
	@Override
	public double getRollingReal(String key, int days, String merchantAccountNumber, java.util.Date batchCycleDate) {
		double returnValue = ruleVoltDAO.getRealValue(key, days, merchantAccountNumber, batchCycleDate);
		return returnValue;
	}
	*/
	
	/* (non-Javadoc)
	 * @see com.intuit.bre.ruledata.IRuleData#rollingRealDateRange(java.lang.String, int, int, long, java.util.Date)
	 */
	/*
	@Override
	public double getRollingRealDateRange(String key, int start,int end, String merchantAccountNumber, java.util.Date batchCycleDate) {
		double returnValue = ruleVoltDAO.getDateRangeRealValue(key, start,end, merchantAccountNumber, batchCycleDate);
		return returnValue;
	}
	*/
}
