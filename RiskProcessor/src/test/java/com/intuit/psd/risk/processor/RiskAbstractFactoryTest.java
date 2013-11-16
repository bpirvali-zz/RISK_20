package com.intuit.psd.risk.processor;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.intuit.psd.risk.interfaces.RiskFactory;
import com.intuit.psd.risk.processor.card.CardBatchFactoryImpl;

public class RiskAbstractFactoryTest {
	
	@Test
	public void getterAndSetterRiskAbstractFactory() {
		CardBatchFactoryImpl cardBatchFactoryImpl = new CardBatchFactoryImpl();
		RiskAbstractFactory riskAbstractFactory = new RiskAbstractFactory();
		riskAbstractFactory.setCardBatchFactoryImpl(cardBatchFactoryImpl);
		
		Assert.assertEquals(cardBatchFactoryImpl, riskAbstractFactory.getCardBatchFactoryImpl());
	}
	
	@Test
	public void getRiskFactoryHappyPath() {
		RiskAbstractFactory riskAbstractFactory = new RiskAbstractFactory();
		CardBatchFactoryImpl cardBatchFactoryImpl = new CardBatchFactoryImpl(); 
		riskAbstractFactory.setCardBatchFactoryImpl(cardBatchFactoryImpl);
		RiskFactory riskFactory = riskAbstractFactory.getRiskFactory("CARD_BATCH");
		Assert.assertEquals(cardBatchFactoryImpl, (CardBatchFactoryImpl)riskFactory);
	}
	
	@Test
	public void getRiskFactoryUnhappyPath() {
		RiskAbstractFactory riskAbstractFactory = new RiskAbstractFactory();
		CardBatchFactoryImpl cardBatchFactoryImpl = new CardBatchFactoryImpl(); 
		riskAbstractFactory.setCardBatchFactoryImpl(cardBatchFactoryImpl);
		RiskFactory riskFactory = riskAbstractFactory.getRiskFactory(null);
		Assert.assertNull(riskFactory);
	}

}
