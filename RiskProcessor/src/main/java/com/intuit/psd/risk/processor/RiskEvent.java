package com.intuit.psd.risk.processor;

/**
 * @author asookazian
 *
 */
public abstract  class RiskEvent {

    
    private String  eventType ;
    //TODO : convert to enum RISK_CARD, RISK_CHECK

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    
    
}
