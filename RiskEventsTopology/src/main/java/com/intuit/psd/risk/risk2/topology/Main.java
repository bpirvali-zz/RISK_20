package com.intuit.psd.risk.risk2.topology;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

import com.intuit.bre.ruledata.Configuration;
import com.intuit.psd.risk.risk2.configmgr.AppConsts;
import com.intuit.psd.risk.risk2.configmgr.interfaces.ConfigMgr;
import com.intuit.psd.risk.risk2.topology.bolts.CardBatchEventBolt;
import com.intuit.psd.risk.risk2.topology.spouts.CardBatchEventSpout;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		boolean runLocalCluster = false;
		String[] springFiles = null;
		
		if (args!=null && args.length>0 && args[0].equalsIgnoreCase("local_cluster"))
			runLocalCluster = true;
		
		if (args!=null && args.length>1 && args[1].length()>0) {
			springFiles =new String[args.length-1];
			for (int i=1; i<args.length; i++)
				springFiles[i-1] = args[i];
		}
		
		// -------------------------
		// 1) Spring bootstrapping
    	// 2) Getting configmgr
		//logClassPath();
		logger.info("Loading Spring App Ctx...");    	
		Configuration.setJdbcPropertiesFile("/RiskEventsJdbc.properties");
		if (springFiles!=null)
			SpringObjectMgr.setSpringAppCtxFiles(springFiles);
		
    	ConfigMgr cfg = (ConfigMgr)SpringObjectMgr.getInstance().getBean("configMgrImpl");
        CardBatchEventSpout spout = (CardBatchEventSpout)SpringObjectMgr.getInstance().getBean("cardBatchEventSpout");
		logger.info("Loading Spring App Ctx...DONE!");
       	logRunTimeParms(cfg);
    	
    	// 3) Configure storm & topology
		Config conf = new Config();
    	
		// parallelize the spout
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, cfg.getInt(AppConsts.SRE_TOPOLOGY_MAX_SPOUT_PENDING));
		
		// enable reliable messaging
		conf.put(Config.TOPOLOGY_ENABLE_MESSAGE_TIMEOUTS, true);
		conf.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS, cfg.getInt(AppConsts.SRE_TOPOLOGY_MESSAGE_TIMEOUT_SECS));
		
		// set debug to false
		conf.setDebug(cfg.getBoolean(AppConsts.SRE_DEBUG));

		// Customize config with my own data
		conf.put(AppConsts.SRE_MAX_RETRIES, cfg.get(AppConsts.SRE_MAX_RETRIES));
		conf.put(AppConsts.SRE_BATCH_SIZE, cfg.get(AppConsts.SRE_BATCH_SIZE));
		conf.put(AppConsts.SRE_SPOUT_SLEEP_SECS, cfg.get(AppConsts.SRE_SPOUT_SLEEP_SECS));
		conf.put(AppConsts.SRE_SPOUT_HEART_BEAT_SLEEPS, cfg.get(AppConsts.SRE_SPOUT_HEART_BEAT_SLEEPS));
		conf.put(AppConsts.SRE_FAKE_HANG_SECS, cfg.get(AppConsts.SRE_FAKE_HANG_SECS));
		
		// Mock Risk Processor
		//SpringObjectMgr.getInstance().getBean("riskProcessorImpl");
		//SpringObjectMgr.setBean("riskProcessorImpl", createMockedProcessor());
		
		// set no of workers
		conf.setNumWorkers(cfg.getInt(AppConsts.SRE_NO_WORKERS));

    	// 4) build the topology
		TopologyBuilder builder = new TopologyBuilder();
		
		// set the parallelism hint to two --> 1 spout / worker process
		builder.setSpout(AppConsts.CARD_BATCH_EVENT_SPOUT_NAME, spout, cfg.getInt(AppConsts.SRE_NO_SPOUTS));
		
		// set the parallelism hint to four --> 2 bolts / worker process
		builder.setBolt(
				AppConsts.CARD_BATCH_EVENT_BOLT_NAME, 
				new CardBatchEventBolt(), 
				cfg.getInt(AppConsts.SRE_NO_BOLTS)).localOrShuffleGrouping(AppConsts.CARD_BATCH_EVENT_SPOUT_NAME, CardBatchEventSpout.STREAM_RISK_EVENTS);		
    	
		if (runLocalCluster) {
			// -----------------------
			// 5) create local cluster 
			// -----------------------
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("Risk-Events-Topology", conf, builder.createTopology());
			long sleepSecs = Long.valueOf(cfg.get(AppConsts.SRE_SPOUT_SLEEP_SECS));
			//logger.info("Waiting {} secs for processing for to be finished...", 300 );
			//Thread.sleep(300000);
			while(!isTimeToShutdown(cfg)) {
				logger.info("Waiting {} secs for processing for to be finished...", sleepSecs );
				Thread.sleep(sleepSecs*1000);
			}
			logger.info("Processing DONE!");
			logger.info("Shuting down the cluster...");
			cluster.shutdown();
		} else {
			// -------------------------------
			// 6) submit to distributed cluster
			// -------------------------------
			try {
				StormSubmitter.submitTopology("Risk-Events-Topology", conf, builder.createTopology());
			} catch (AlreadyAliveException e) {
				logger.error("The topology is already running!!!", e);
			} catch (InvalidTopologyException e) {
				logger.error("The topology is invalid!!!", e);
			}
		}
	}
	
//	public static RiskProcessor createMockedProcessor() {
//		RiskProcessor riskProc = EasyMock.createNiceMock(RiskProcessor.class);
//		riskProc.prepare(EasyMock.anyInt());
//		EasyMock.expectLastCall().once();
//		//EasyMock.expect(riskProc.execute(EasyMock.anyObject(CardBatchEventImpl.class))).andReturn(null).once();		
//		EasyMock.expect(riskProc.execute(EasyMock.anyObject(CardBatchEventImpl.class))).andReturn(null).once();		
//		EasyMock.replay(riskProc);
//		return riskProc;
//	}
	
	private static boolean isTimeToShutdown(ConfigMgr cfg) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate)SpringObjectMgr.getInstance().getBean("jdbcTemplate");
		String sSQL = "SELECT COUNT(*) FROM bre.incoming_risk_events " + 
		              "WHERE processing_status=0 " +
		              "OR (processing_status=3 AND retries<" + cfg.get("sre_retries") + ")";
		//logger.debug("Shutdown-SQL:" + sSQL);
		
		int cntToProcess = jdbcTemplate.queryForInt(sSQL);
		if (cntToProcess==0)
			return true;
		return false;
	}
	private static void logRunTimeParms(ConfigMgr cfg) {
		logger.info("---------------------------------------------------------------");
		logger.info("-- Risk Event Topology Run Time Parameters:");
		logger.info("");
		logger.info("topology_max_spout_pending:          " + cfg.get("sre_topology_max_spout_pending"));
		logger.info("topology_message_timeout_secs: [sec] " + cfg.get("sre_topology_message_timeout_secs"));
		logger.info("Fake Hang Seconds: [sec]             " + cfg.get("sre_fake_hang_secs"));
		logger.info("debug:                               " + cfg.get("sre_debug"));
		logger.info("bolts:                               " + cfg.get("sre_no_bolts"));
		logger.info("spouts:                              " + cfg.get("sre_no_spouts"));
		logger.info("workers:                             " + cfg.get("sre_no_workers"));
		logger.info("Risk Event Retries:                  " + cfg.get("sre_retries"));
		logger.info("Batch Size:                          " + cfg.get("sre_batch_size"));
		logger.info("Spout Sleep between READs [sec]:     " + cfg.get("sre_spout_sleep_secs"));
		logger.info("Spout Heart Beat Sleeps:             " + cfg.get("sre_spout_heart_beat_sleeps"));
		logger.info("----------------------------------------------------------------");
	}
}
