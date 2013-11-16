package com.intuit.bre.ruledata;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtil {

	/**
	 * Gets a single child node.
	 * @param parentNode the parent node
	 * @param tagName the name of the child node
	 * @return the child node
	 */
	/*
	static public Node getChildNode(final Node parentNode, final String tagName) {
		NodeList childNodes = parentNode.getChildNodes();
		if (childNodes != null) {
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				if (tagName.equals(childNode.getLocalName())) {
					return childNode;
				}
			}
		}
		return null;
	}
	*/
	
	/**
	 * Get a list of child nodes.
	 * @param parentNode parent node
	 * @param tagName the name of the child nodes
	 * @return list of child nodes
	 */
	static public List<Node> getChildNodes(final Node parentNode, final String tagName) {
		List<Node> nodeList = new ArrayList<Node>();
		NodeList childNodes = parentNode.getChildNodes();
		if (childNodes != null) {
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				if (tagName.equals(childNode.getLocalName())) {
					nodeList.add(childNode);
				}
			}
		}
		return nodeList;
	}

	/**
	 * Get the text of a child tag
	 * @param parentNode parent tag
	 * @param tagName name of child node
	 * @return test of child node
	 */
	/*
	static public String getTagText(final Node parentNode, final String tagName) {
		Node childNode = getChildNode(parentNode, tagName);
		
		if (childNode == null) {
			return null;
		} else {
			return childNode.getTextContent();
		}
	}
	*/

	/**
	 * Gets text of a node
	 * @param parentNode the node
	 * @return the text
	 */
	static public String getText(final Node parentNode) {
		return parentNode.getTextContent();
	}
	
	/**
	 * Gets the attribute value of a node
	 * @param node the node
	 * @param attribute the name of the attribute
	 * @return the attribute value
	 */
	static public String getAttribute(final Node node, final String attribute) {
		NamedNodeMap attributeMap = node.getAttributes();
		Node attributeNode = attributeMap.getNamedItem(attribute);
		if (attributeNode == null) {
			return null;
		} else {
			return attributeNode.getTextContent();
		}
	}
	
}
