package com.intuit.psd.risk.risk2.topology.bolts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intuit.psd.risk.processor.RiskEvent;
import com.intuit.psd.risk.processor.RiskProcessor;
import com.intuit.psd.risk.processor.RiskProcessorResponse;
import com.intuit.psd.risk.processor.exceptions.RiskException;

public class TestRiskProcessorImpl implements RiskProcessor {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(CardBatchEventBolt.class);
	@Override
	public void prepare(int workerID) throws RiskException {
		//logger.info(arg0)

	}

	@Override
	public RiskProcessorResponse execute(RiskEvent riskEvent) {
		return null;
	}

	@Override
	public void cleanUp() throws RiskException {
	}

}
