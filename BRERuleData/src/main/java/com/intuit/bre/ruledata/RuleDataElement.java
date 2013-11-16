package com.intuit.bre.ruledata;

import java.util.Map;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Node;

public class RuleDataElement {
	private String key;
	private RuleDataQuery query;
	private String column;
	private RuleDataType type;

	public RuleDataElement(Node node, Map<String, RuleDataQuery> queryMap) throws XPathExpressionException {
		key = XMLUtil.getAttribute(node, "key");
		column = XMLUtil.getAttribute(node, "column");
		
		if (key == null) {
			throw new XPathExpressionException("ruledata does not specify a key");
		}
		
		if (column == null) {
			throw new XPathExpressionException("ruledata " + key + " does not specify a column");
		}
		
		String queryStr = XMLUtil.getAttribute(node, "query");
		String spcallStr = XMLUtil.getAttribute(node, "spcall");
		
		if (queryStr != null && spcallStr != null) {
			throw new XPathExpressionException("ruledata " + key + " should not have both a query and spcall attribute");
		}
		
		if (queryStr == null && spcallStr == null) {
			throw new XPathExpressionException("ruledata " + key + " requires either a query or spcall attribute");
		}
		
		if (queryStr != null) {
			query = queryMap.get(queryStr);
			if (query == null) {
				throw new XPathExpressionException("query " + queryStr + " does not exist");
			}
		}
		
		if (spcallStr != null) {
			query = queryMap.get(spcallStr);
			if (query == null) {
				throw new XPathExpressionException("spcall " + spcallStr + " does not exist");
			}
		}
		
		String typeStr = XMLUtil.getAttribute(node, "type");
		type = getType(typeStr);
		// check the parent and save into agg-hash-map if it is an aggregate!
		//	Node n = node.getParentNode();
		//System.out.println("ruledata:" + column + " parent node name:" + n.getNodeName());
		//if (n.getNodeName().equals("aggdata"))
		query.getRuleDataElements().add(this);
	}

    private static RuleDataType getType(String typeStr) {
        RuleDataType type;
        if ("string".equals(typeStr)) {
                type = RuleDataType.STRING;
        } else if ("int".equals(typeStr) || "integer".equals(typeStr)) {
                type = RuleDataType.INTEGER;
        } else if ("long".equals(typeStr)) {
                type = RuleDataType.LONG;
        } else if ("double".equals(typeStr)) {
                type = RuleDataType.DOUBLE;
        } else if ("date".equals(typeStr)) {
                type = RuleDataType.DATE;
        } else if (typeStr!=null && typeStr.contains(",")) {
                type = RuleDataType.AGG_TYPE;
        } else {
                type = RuleDataType.UNKOWN;
        }
        return type;
    }
	
	public String getKey() {
		return key;
	}

	/*
	public void setKey(String key) {
		this.key = key;
	}
	*/

	public RuleDataQuery getQuery() {
		return query;
	}

	/*
	public void setQuery(RuleDataQuery query) {
		this.query = query;
	}
	*/

	public String getColumn() {
		return column;
	}

	/*
	public void setColumn(String column) {
		this.column = column;
	}
	*/

	public RuleDataType getType() {
		return type;
	}

	/*
	public void setType(RuleDataType type) {
		this.type = type;
	}
	*/
}
