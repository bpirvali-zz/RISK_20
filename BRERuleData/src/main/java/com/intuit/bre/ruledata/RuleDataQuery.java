package com.intuit.bre.ruledata;

import java.util.HashSet;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

public class RuleDataQuery {

	private String name;
	
	private String query;
	
	private boolean isStoredProc;
	
	private String dbConnection;
	
	private Set<RuleDataElement> ruleDataElements = new HashSet<RuleDataElement>();

	public RuleDataQuery() {
		
	}
	
	public RuleDataQuery(Node node) throws XPathExpressionException {
		name = XMLUtil.getAttribute(node, "name");
		dbConnection = XMLUtil.getAttribute(node, "dbConnection");
		query = XMLUtil.getText(node);
	}
		
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getDbConnection() {
		return dbConnection;
	}

	public void setDbConnection(String dbConnection) {
		this.dbConnection = dbConnection;
	}

	public Set<RuleDataElement> getRuleDataElements() {
		return ruleDataElements;
	}

	/*
	public void setRuleDataElements(Set<RuleDataElement> ruleDataElements) {
		this.ruleDataElements = ruleDataElements;
	}
	*/
	
	/*
	public void addToAggElementMap(String aggName, AggDataElement aggEl) {
		if (aggDataElements == null)
			aggDataElements = new HashMap<String, AggDataElement>();
		aggDataElements.put(aggName, aggEl);
	}
	
	public Map<String, AggDataElement> getAggDataElements() {
		return aggDataElements;
	}

	public void setAggDataElements(Map<String, AggDataElement> aggDataElements) {
		this.aggDataElements = aggDataElements;
	}
	*/

	public String getName() {
		return name;
	}

	/*
	public void setName(String name) {
		this.name = name;
	}
	*/
	
	public boolean isStoredProc() {
		return isStoredProc;
	}

	public void setStoredProc(boolean isStoredProc) {
		this.isStoredProc = isStoredProc;
	}	
	
}
