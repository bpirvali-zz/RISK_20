package com.intuit.psd.risk.risk2.topology;

import java.lang.management.ManagementFactory;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.TopologyContext;


public class Util {
	private static final Logger logger = LoggerFactory.getLogger(Util.class);
	
    public static void logStartupInfo(String title, TopologyContext context) {
    	String s = ManagementFactory.getRuntimeMXBean().getName();
    	String[] sa = s.split("@");
    	if (sa==null) 
    		sa = new String[] {"UNKOWN", "UN_KNOWN"};
    	
    	logger.info("-------------------------------------------");
    	logger.info("{} started with:     ", title);
    	logger.info("");
		logger.trace("getThisComponentId: " + context.getThisComponentId());
		logger.trace("getThisTaskId:      " + context.getThisTaskId());
    	logger.info("Thread-ID:          " + Thread.currentThread().getId());
    	logger.info("{}-Index:         {}", title, context.getThisTaskIndex());
    	logger.info("Process-ID:         " + sa[0] );
    	logger.info("Host:               " + sa[1]);
    	logger.info("-------------------------------------------");
    }
    
	public static void logClassPath() {
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		URL[] urls = ((URLClassLoader) cl).getURLs();
    	logger.info("-------------------------------------------");
    	logger.info("-- Current Class Path:");
		for (URL url : urls) {
			logger.info("-- {}", url.getFile());
			//System.out.println(url.getFile());
		}
    	logger.info("-------------------------------------------");
	}
	
	public static void printList(List<Map<String, Object>> l, String title) {
		StringBuilder sb = new StringBuilder();
		logger.info("----------------------------------------------");
		logger.info("-- {}", title);
		logger.info("----------------------------------------------");
		for (Map<String, Object> e: l) {
			sb.setLength(0);
			sb.append("(");
			for (Map.Entry<String, Object> en: e.entrySet()) {
				sb.append(en.getKey());
				sb.append("=");
				sb.append(en.getValue());
				sb.append(", ");
				
				//logger.info("key:{}, val:{}", en.getKey(), en.getValue() );
			}
			sb.setLength(sb.length()-2);
			sb.append(")");
			logger.info(sb.toString());
		}
	}	
}
