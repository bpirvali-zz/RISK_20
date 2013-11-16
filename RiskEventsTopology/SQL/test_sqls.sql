----------------------------
-- Tests
----------------------------
delete from bre.merchant_alerts;
update bre.incoming_risk_events set processing_status=0, retries=-1, error_msg='', TIME_PROCESSED=null;
commit;
select WORKER_ID, MERCHANT_ACCOUNT_NUMBER, RETRIES, PROCESSING_STATUS, event_timestamp, time_processed, error_msg from bre.incoming_risk_events;
select * from bre.merchant_alerts;

update bre.incoming_risk_events set processing_status=0, retries=-1, error_msg='', TIME_PROCESSED=null 
WHERE MERCHANT_ACCOUNT_NUMBER IN ('100');


select MERCHANT_ACCOUNT_NUMBER, WORKER_ID, RETRIES, PROCESSING_STATUS, TIME_PROCESSED from bre.incoming_risk_events;
select WORKER_ID, MERCHANT_ACCOUNT_NUMBER, RETRIES, PROCESSING_STATUS, event_timestamp, time_processed, error_msg from bre.incoming_risk_events;
