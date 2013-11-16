package com.intuit.bre.ruledata;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

/*
 * @multi-threaded
 * */
public class RuleDataConfig {
    private static Logger logger = LoggerFactory.getLogger(RuleDataConfig.class);

	Map<String, RuleDataQuery> queryMap = new HashMap<String, RuleDataQuery>();
	Map<String, RuleDataQuery> spcallMap = new HashMap<String, RuleDataQuery>();
	
	Map<String, RuleDataElement> elementMap = new HashMap<String, RuleDataElement>();
	//Map<String, AggDataElement> aggMap = new HashMap<String, AggDataElement>();

	public RuleDataConfig(Node node) throws XPathExpressionException {
		List<Node> queryNodes = XMLUtil.getChildNodes(node, "query");
		for (Node queryNode : queryNodes) {
			if (queryNode != null) {
				RuleDataQuery query = new RuleDataQuery(queryNode);
				query.setStoredProc(false);
				queryMap.put(query.getName(), query);
			}			
		}
		
		List<Node> spcallNodes = XMLUtil.getChildNodes(node, "spcall");
		for (Node spcallNode : spcallNodes) {
			if (spcallNode != null) {
				RuleDataQuery spcall = new RuleDataQuery(spcallNode);
				spcall.setStoredProc(true);
				queryMap.put(spcall.getName(), spcall);
			}			
		}
		
		List<Node> ruleDataNodes = XMLUtil.getChildNodes(node, "ruledata");
		for (Node ruleDataNode : ruleDataNodes) {
			if (ruleDataNode != null) {
				RuleDataElement ruleData = new RuleDataElement(ruleDataNode, queryMap);
				elementMap.put(ruleData.getKey(), ruleData);
			}			
		}
		
		/*
		List<Node> aggDataNodes = XMLUtil.getChildNodes(node, "aggdata");
		for (Node aggDataNode : aggDataNodes) {
			if (aggDataNode != null) {
				List<RuleDataElement> rdElements = getRuleDataSet(aggDataNode, queryMap);
				String name = ((Attr)aggDataNode.getAttributes().item(0)).getValue();
				String queryName = ((Attr)aggDataNode.getAttributes().item(1)).getValue();
				AggDataElement aggData = new AggDataElement(name, queryName, rdElements, queryMap);
				for (RuleDataElement el: rdElements) {
					aggMap.put(el.getKey(), aggData);
				}
			}			
		}
		*/
	}
	
	/*
	private static List<RuleDataElement> getRuleDataSet(Node aggNode, Map<String, RuleDataQuery> queryMap) throws XPathExpressionException {
		List<RuleDataElement> rdElements = new ArrayList<RuleDataElement>();
		List<Node> ruleDataNodes = XMLUtil.getChildNodes(aggNode, "ruledata");
		for (Node ruleDataNode : ruleDataNodes) {
			if (ruleDataNode != null) {
				RuleDataElement ruleData = new RuleDataElement(ruleDataNode, queryMap);
				//elementMap.put(ruleData.getKey(), ruleData);
				rdElements.add(ruleData);
			}			
		}
		return rdElements;
	}
	public String[] getKeys(RuleDataType type) {
		Collection<RuleDataElement> allElements = elementMap.values();
		Collection<String> typeElements = new ArrayList<String>();
		for (RuleDataElement element : allElements) {
			if (element.getType() == type) {
				typeElements.add(element.getKey());
			}
		}
		String[] keys = new String[typeElements.size()];
		int i = 0;
		for (String key : typeElements) {
			keys[i] = key;
			i++;
		}
		return keys;
	}
	*/
	
	public Map<String, RuleDataQuery> getQueryMap() {
		return queryMap;
	}

	/*
	public void setQueryMap(Map<String, RuleDataQuery> queryMap) {
		this.queryMap = queryMap;
	}
	*/

	public Map<String, RuleDataElement> getElementMap() {
		return elementMap;
	}

	/*
	public void setElementMap(Map<String, RuleDataElement> elementMap) {
		this.elementMap = elementMap;
	} 
	*/
	
	/*
	public Map<String, AggDataElement> getAggMap() {
		return aggMap;
	}
	public void setAggMap(Map<String, AggDataElement> aggMap) {
		this.aggMap = aggMap;
	}
	*/

	private static class configHolder {
		private static final RuleDataConfig INSTANCE = createInstance();
	
		private static RuleDataConfig createInstance() {
			InputStream in = RuleDataConfig.class.getResourceAsStream(Configuration.getQueryFile());
			RuleDataParser parser = new RuleDataParser();
			RuleDataConfig config =null;
			try {
				config = parser.parseRuleDataConfig(in);
			} catch (Exception e) {
				logger.error("Error while parsing " + Configuration.getQueryFile(), e);
			} 
			return config;
		}
	}
	
	public static RuleDataConfig getInstance() {
		return configHolder.INSTANCE;		
	}

}
