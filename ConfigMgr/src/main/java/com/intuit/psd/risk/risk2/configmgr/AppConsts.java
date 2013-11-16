package com.intuit.psd.risk.risk2.configmgr;

public class AppConsts { 
	public static final String 	BCD_START									= "BCD_START";
	public static final String 	BCD_END										= "BCD_END";
	public static final String 	MERCHANTS_FILE								= "MERCHANTS_FILE";
	
	// SRE=STORM RISK EVENTS
	public static final String	SRE_NO_WORKERS 								= "SRE_NO_WORKERS"; 
	public static final String	SRE_NO_SPOUTS 								= "SRE_NO_SPOUTS";
	public static final String	SRE_NO_BOLTS 								= "SRE_NO_BOLTS";
	public static final String	SRE_CURR_WORKER_ID 							= "SRE_CURR_WORKER_ID";
	public static final String	SRE_MAX_RETRIES								= "SRE_RETRIES";
	public static final String	SRE_SPOUT_SLEEP_SECS						= "SRE_SPOUT_SLEEP_SECS";
	public static final String	SRE_SPOUT_HEART_BEAT_SLEEPS					= "SRE_SPOUT_HEART_BEAT_SLEEPS";
	public static final String	SRE_BATCH_SIZE								= "SRE_BATCH_SIZE";
	public static final String	SRE_DEBUG 								    = "SRE_DEBUG";
	public static final String	SRE_FAKE_HANG_SECS						    = "SRE_FAKE_HANG_SECS";
	
	public static final String SRE_TOPOLOGY_ENABLE_MESSAGE_TIMEOUTS 		= "SRE_TOPOLOGY_ENABLE_MESSAGE_TIMEOUTS";
	public static final String SRE_TOPOLOGY_MESSAGE_TIMEOUT_SECS 			= "SRE_TOPOLOGY_MESSAGE_TIMEOUT_SECS";
	public static final String SRE_TOPOLOGY_MAX_SPOUT_PENDING 				= "SRE_TOPOLOGY_MAX_SPOUT_PENDING";
	                                           
	
	public static final String CARD_BATCH_EVENT_SPOUT_NAME					= "CardBatchEventSpout";
	public static final String CARD_BATCH_EVENT_BOLT_NAME					= "CardBatchEventBolt";
	
}
