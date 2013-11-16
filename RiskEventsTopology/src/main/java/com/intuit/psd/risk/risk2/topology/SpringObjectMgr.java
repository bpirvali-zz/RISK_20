package com.intuit.psd.risk.risk2.topology;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringObjectMgr {
	private static final Logger logger = LoggerFactory.getLogger(SpringObjectMgr.class);
	private static String[] springCtxFiles = new String[] { "AppCtx-JdbcTemplate.xml", "AppCtx-RiskEvents.xml", "AppCtx-RiskProcessor.xml" };
	private static ApplicationContext springCtx = null; 
	private static Map<String, Object> map = new ConcurrentHashMap<String, Object>();
	
	public static void setSpringAppCtxFiles(String[] files) {
		springCtxFiles = files;
	}
	
//	public static String[] getSpringAppCtxFiles() {
//		return springCtxFiles;
//	}
	
	public static SpringObjectMgr getInstance() {
		return TransientObjectMgrHolder.INSTANCE;
	}
	
//	public static void setBean(String objectID, Object obj) {
//		map.put(objectID, obj);
//	}
	
	public Object getBean(String objectID) {
//		if (objectID.equalsIgnoreCase("riskProcessorImpl"))
//			return createMockedProcessor();
		
		Object o = map.get(objectID);
		if (o==null) 
			o = springCtx.getBean(objectID);
		if (o==null)
			throw new RuntimeException("Bean: " + objectID  + " does not exist!" );
		//map.put(objectID,o);
		return o;
	}
	
	private static void init() {
		logger.trace("entered init:{}", springCtxFiles[0]);
		
		if (springCtxFiles==null)
			throw new RuntimeException("springCtxsFiles is null!");
		springCtx = new ClassPathXmlApplicationContext(springCtxFiles);
		logger.trace("After Spring boot");
		if (springCtx==null)
			throw new RuntimeException("Failed to create Spring Context");
	}
	
	private static class TransientObjectMgrHolder {
		private static final SpringObjectMgr INSTANCE = createInstance();
		private static SpringObjectMgr createInstance() {
			if (springCtx==null)
				init();
			return new SpringObjectMgr();
		}
	}

//	private RiskProcessor createMockedProcessor() {
//		RiskProcessor riskProc = EasyMock.createNiceMock(RiskProcessor.class);
//		riskProc.prepare(EasyMock.anyInt());
//		EasyMock.expectLastCall().once();
//		// EasyMock.expect(riskProc.execute(EasyMock.anyObject(CardBatchEventImpl.class))).andReturn(null).once();
//		EasyMock.expect(
//				riskProc.execute(EasyMock.anyObject(CardBatchEventImpl.class)))
//				.andReturn(null).once();
//		EasyMock.replay(riskProc);
//		return riskProc;
//	}
	
//	public static void main(String[] args) {
//		SpringObjectMgr.setSpringAppCtxFiles(new String[] {"ApplicationContext.xml"});
//		SpringObjectMgr tom=SpringObjectMgr.getInstance();
//		JdbcTemplate jdbc = (JdbcTemplate)tom.getBean("jdbcTemplate");
//		logger.info("jdbc:" + jdbc.toString());
//
//	}
}
