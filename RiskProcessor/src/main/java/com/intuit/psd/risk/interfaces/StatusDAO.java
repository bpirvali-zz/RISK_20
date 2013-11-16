package com.intuit.psd.risk.interfaces;

import java.util.Date;

import com.intuit.psd.risk.processor.exceptions.RiskException;

/**
 * @author asookazian
 *
 */
public interface StatusDAO {
	
	public void markStatusSuccessfullyCompleted(BusinessObjectModel bom, int workerID) throws Exception;
	public boolean markStatusFailed(BusinessObjectModel bom, Throwable error, int workerID) throws RiskException;
	public void insertIntoIncomingRiskEvents(String merchantAccountNumber, Date date) throws RiskException;
	public void cleanUpIncomingRiskEvents() throws RiskException ;
	public void cleanUpMerchantAlerts() throws RiskException ;

}
