package com.intuit.psd.risk.processor;

import com.intuit.psd.risk.interfaces.RiskFactory;

	
/**
 * @author asookazian
 *
 */
public class RiskAbstractFactory {
	
	public static final String EVENT_TYPE_RISK_CARD = "CARD_BATCH";
	private RiskFactory cardBatchFactoryImpl;
	
	public RiskFactory getCardBatchFactoryImpl() {
		return cardBatchFactoryImpl;
	}

	public void setCardBatchFactoryImpl(RiskFactory cardBatchFactoryImpl) {
		this.cardBatchFactoryImpl = cardBatchFactoryImpl;
	}

	public RiskFactory getRiskFactory(String eventType){
		if (eventType != null && eventType.equalsIgnoreCase(EVENT_TYPE_RISK_CARD)) {
			return cardBatchFactoryImpl;
		}
		return null;
	}
	
}
