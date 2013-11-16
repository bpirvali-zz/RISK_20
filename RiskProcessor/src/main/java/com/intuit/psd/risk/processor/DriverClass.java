/**
 * 
 */
package com.intuit.psd.risk.processor;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;

import com.intuit.payments.common.logging.DSLogger;
import com.intuit.psd.risk.interfaces.RiskFactory;
import com.intuit.psd.risk.interfaces.StatusDAO;
import com.intuit.psd.risk.processor.card.CardBatchEventImpl;
import com.intuit.psd.risk.processor.utilities.DateConverter;

/**
 * @author asural
 *
 */
public class DriverClass {
	
	 private static DSLogger logger = DSLogger.getLogger(RiskProcessorImpl.class);

    public static void main(String[] args) {
    	String merchantFileLocation = "";
    	String startBatchCycleDateString = "";
    	String endBatchCycleDateString = "";
    	String eventType = "";    	    	
    	
    	RiskProcessorImpl riskProcessor = RiskProcessorImpl.getInstance();        	
    	riskProcessor.prepare(1);
    	
		logger.info("Rules engine is now starting up...");
		//riskProcessor.rulesEngine.startup();		
		
		DateTime startBatchCycleDate = null;
		DateTime endBatchCycleDate = null;    		
		List<LocalDate> dates = null;
		
		//get cmd line arguments
    	try {
	    	if (args != null && args.length > 0) {
	    		merchantFileLocation = args[0];
	    		startBatchCycleDateString = args[1];
	    		endBatchCycleDateString = args[2];
	    		eventType = args[3];
	    	}
	    	if (startBatchCycleDateString != null && startBatchCycleDateString.length() > 0) {
	    		DateConverter dateConverter = new DateConverter();
	    		startBatchCycleDate = new DateTime(dateConverter.convert(startBatchCycleDateString));
	    	}
	    	if (endBatchCycleDateString != null && endBatchCycleDateString.length() > 0) {
	    		DateConverter dateConverter = new DateConverter();
	    		endBatchCycleDate = new DateTime(dateConverter.convert(endBatchCycleDateString));
	    	}
	    	int rtn = startBatchCycleDate.compareTo(endBatchCycleDate);
	    	if (rtn > 0) {
	    		logger.error("You have provided a endBatchCycleDate that is before startBatchCycleDate.  Please fix args and try again.");
	    		System.exit(0);
	    	}
	    	dates = new ArrayList<LocalDate>();
	    	int days = Days.daysBetween(startBatchCycleDate, endBatchCycleDate).getDays();
	    	for (int i=0; i <= days; i++) {
	    	    LocalDate d = startBatchCycleDate.withFieldAdded(DurationFieldType.days(), i).toLocalDate();
	    	    dates.add(d);
	    	}
    	}
    	catch(ArrayIndexOutOfBoundsException e) {
    		logger.error("You did not provide 4 arguments for the main method: merchant path/file location, start batchCycleDate, end batchCycleDate and eventType.  Please fix args and try again.");
    		System.exit(0);
    	}
    	
    	//read and loop thru merchants and call execute() to process rules for each merchant for each BCD required
    	
    	DriverClass.processMerchantsInFile(riskProcessor.getRiskAbstractFactory(), merchantFileLocation, dates, eventType, riskProcessor);
 
    }
	
private static void processMerchantsInFile(RiskAbstractFactory riskAbsFactory, String merchantFileLocation, 
	List<LocalDate> dates, String eventType, RiskProcessor riskProcessor) {
Scanner sc = null;
try {
	//TODO: This block of code is temporary for inserting into Incoming Risk Events
	RiskFactory riskFactory = null;
	if (eventType != null) {
		riskFactory = riskAbsFactory.getRiskFactory(eventType);
	}
	StatusDAO statusDAO = riskFactory.getStatusDAOInstance();					
	logger.info("Cleaning the merchants info from the Incoming Risk Events table ");
	statusDAO.cleanUpIncomingRiskEvents();
	logger.info("Cleaning the merchant alerts table  ");
	statusDAO.cleanUpMerchantAlerts();
	sc = new Scanner(new File(merchantFileLocation));	   	
	while(sc.hasNext()){	 		
		String merchantAccountNumber = sc.nextLine();
		for(LocalDate localDate : dates) {
			//RiskEvent cardBatchEvent = new CardBatchEventImpl(merchantAccountNumber, localDate.toDate(), eventType);				
			RiskEvent cardBatchEvent = new CardBatchEventImpl(merchantAccountNumber, new Timestamp(localDate.toDate().getTime()), 0, 0 ,0 ,0 , eventType);				
	    	logger.info("Inserting the merchant into the Incoming Risk Events table : " + merchantAccountNumber);
			statusDAO.insertIntoIncomingRiskEvents(merchantAccountNumber, localDate.toDate());					
			RiskProcessorResponse response = riskProcessor.execute(cardBatchEvent);
			if (response != null) {
			    logger.info("RiskProcessor.main(): errorCode = " + response.getErrorCode());
			    logger.info("RiskProcessor.main(): errorMessage = " + response.getErrorMessage());
			    if (response.getError() != null)
			    	logger.info("RiskProcessor.main(): message = " + response.getError().getMessage());
			} else {
				logger.info("Merchant processed successfully");
			}
		}
	}
	logger.info("Rules engine is now shutting down...");
	riskProcessor.cleanUp();
}
catch(Exception e) {
	logger.error("Exception occurred during scanning of merchant account numbers: ", e);
}
finally {
	if (sc != null)
		sc.close();
}
}

}
