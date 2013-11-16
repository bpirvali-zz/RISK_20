package com.intuit.psd.risk.processor;

import com.intuit.psd.risk.processor.exceptions.RiskException;

/**
 * This is the main Risk Processor class responsible for processing risk events
 * per merchant. The execute method is invoked by the Storm bolt. In case of
 * exception, return 1 to the caller (do not throw any exceptions back to the
 * caller).
 * 
 * @author asookazian
 * 
 */

public interface RiskProcessor {

    public void prepare(int workerID) throws RiskException;

    /**
     * This method is called form STORM bolt.execute() method. Purpose of this
     * method is to process the merchant. Method returns 0 on success and 1 for
     * error.
     * 
     * @param riskEvent
     * @param eventType
     * @return
     */
    //TODO: add a null check for riskEvent
    public RiskProcessorResponse execute(RiskEvent riskEvent);

    public void cleanUp() throws RiskException;

}
