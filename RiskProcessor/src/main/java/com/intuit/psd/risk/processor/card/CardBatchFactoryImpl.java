package com.intuit.psd.risk.processor.card;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intuit.psd.risk.interfaces.BusinessObjectModel;
import com.intuit.psd.risk.interfaces.RiskFactory;
import com.intuit.psd.risk.interfaces.StatusDAO;
import com.intuit.psd.risk.processor.RiskEvent;
import com.intuit.psd.risk.processor.RuleProcessor;

/**
 * @author asookazian
 *
 */
public class CardBatchFactoryImpl implements RiskFactory {
	
	private static Logger logger = LoggerFactory.getLogger(CardBatchFactoryImpl.class);
	
	private StatusDAO statusDAO;
	private RuleProcessor ruleProcessor;

	/**
	 * This method creates StatusDAO implementation instance.
	 * @param 
	 * @return StatusDAO
	 */
	public StatusDAO getStatusDAOInstance() {
		return statusDAO;
	}

	/**
	 * This method creates RuleProcessor instance.
	 * @param 
	 * @return RuleProcessor
	 */
	public RuleProcessor getRuleFlowInstance() {
		return ruleProcessor;
	}

	/**
	 * This method creates a BusinessObjectModel based on RiskEvent.
	 * @param riskEvent
	 * @return BusinessObjectModel
	 */
	public BusinessObjectModel createBOM(RiskEvent riskEvent){
		logger.info("Inside createBOM()");
		CardBatchEventImpl cardBatchEvent = (CardBatchEventImpl)riskEvent;
		Date batchCycleDate = cardBatchEvent.getBatchCycleDate();
		String merchantAccountNumber = cardBatchEvent.getMerchantAccountNumber();
		CardBatchBOMImpl bom = new CardBatchBOMImpl();
		bom.setAccountNumber(merchantAccountNumber);
		bom.setBatchCycleDate(batchCycleDate);
		return bom;
	}

	public StatusDAO getStatusDAO() {
		return statusDAO;
	}

	public void setStatusDAO(StatusDAO statusDAO) {
		this.statusDAO = statusDAO;
	}

	public RuleProcessor getRuleFlow() {
		return ruleProcessor;
	}

	public void setRuleFlow(RuleProcessor ruleFlow) {
		this.ruleProcessor = ruleFlow;
	}

}
