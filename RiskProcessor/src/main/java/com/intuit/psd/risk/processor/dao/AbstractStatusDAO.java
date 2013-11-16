package com.intuit.psd.risk.processor.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import com.intuit.psd.risk.interfaces.BusinessObjectModel;

public abstract class AbstractStatusDAO {
    
    protected JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
    	return jdbcTemplate;
    }
    
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    	this.jdbcTemplate = jdbcTemplate;
    }
    
    public static String getInsertIncomingRiskEventsQuery() {
    	StringBuilder sb = new StringBuilder();
    	
		sb.append("insert into bre.incoming_risk_events ");
		sb.append(" (MERCHANT_ACCOUNT_NUMBER, ");
		sb.append(" SYSTEM_ID, ");
		sb.append(" EVENT_CATEGORY_ID, ");
		sb.append(" WORKER_ID, ");
		sb.append(" EVENT_TYPE_ID, ");
		sb.append(" EVENT_ID, ");
		sb.append(" EVENT_TIMESTAMP, ");
		sb.append(" RETRIES, ");
		sb.append(" TIME_DE_QUEUED, ");
		sb.append(" PROCESSING_STATUS) ");
		sb.append(" values (?, 1, 1, 1,1,1,?, 0, ?, 0 );");
  		return sb.toString();
      }
    
    public static String getInsertMerchantAlertsQuery() {

		StringBuilder sb = new StringBuilder();
	
		sb.append("insert into BRE.merchant_alerts ");
		sb.append(" (EID, ");
		sb.append(" MERCHANT_ACCOUNT_NUMBER, ");
		sb.append(" ALERT_TIMESTAMP, ");
		sb.append(" BATCH_CYCLE_DATE, ");
		sb.append(" CASE_TYPE, ");
		sb.append(" ALERT_TYPE, ");
		sb.append(" WORK_QUEUE, ");
		sb.append(" RULE_NAME, ");
		sb.append(" RULE_VERSIONID, ");
		sb.append(" RULE_DESCRIPTION, ");
		sb.append(" RULE_ACTUALVALUE, ");
		sb.append(" RULE_SET, ");
		sb.append(" WORK_QUEUE_CATEGORY, ");
		sb.append(" ALERT_STATUS, ");
		sb.append(" MERCHANT_EID, ");
		sb.append(" SYSTEM_IDENTIFIER, ");
		sb.append(" ALERT_IDENTIFIER) ");
		sb.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, null, null, null );");
	
		return sb.toString();

    }
    
    //TODO: Behzad found a bug here.  The following update may update multiple rows as it does not include
    //BCD or other additional criteria in the where clause.
    public static String getUpdateIncomingRiskEventsSuccessQuery() {
		String query = "update bre.incoming_risk_events set processing_status = ?, TIME_PROCESSED=?, retries=retries+1 where merchant_account_number = ? ";
		return query;
    }

    public static String getUpdateIncomingRiskEventsFailureQuery(BusinessObjectModel bom) {
    	StringBuilder sb = new StringBuilder();
    	    	
    	sb.append("UPDATE bre.incoming_risk_events set PROCESSING_STATUS=?, TIME_PROCESSED=?, ERROR_MSG = ERROR_MSG || '[ ]' || ?");
    	sb.append(", RETRIES=RETRIES+1 ");
    	sb.append(" WHERE merchant_account_number = ? ");

		return sb.toString();
    }

    public static String getCheckForSuccessQuery() {
		String query = "SELECT PROCESSING_STATUS FROM  bre.incoming_risk_events WHERE merchant_account_number = ? ";
		return query;
    }
    
    public static String getIncomingRiskEventsCleanupQuery() {
		String query = "delete from bre.incoming_risk_events ;";
		return query;
    }
    
    public static String getMerchantAlertsCleanupQuery() {
 		String query = "delete from bre.merchant_alerts ;";
 		return query;
     }

}
