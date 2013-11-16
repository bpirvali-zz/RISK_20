package com.intuit.bre.ruledata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RuleDataParser {

    private static Logger logger = LoggerFactory.getLogger(RuleDataParser.class);

    public RuleDataConfig parseRuleDataConfig(InputStream inStream)
			throws XPathExpressionException, IOException, SAXException {
		RuleDataConfig config = null;
		try {
			InputStream resetableInputStream = getResetableInputStream(inStream);
			validateRuleData(resetableInputStream);
			resetableInputStream.reset();
			XPath xPath = XPathFactory.newInstance().newXPath();
			InputSource xml = new InputSource(resetableInputStream);
			Node rootNode = (Node) xPath.evaluate("/RuleDataQueries", xml,
					XPathConstants.NODE);
			config = new RuleDataConfig(rootNode);
		} catch (XPathExpressionException e) {
			logger.error("Error while parsing " + Configuration.getQueryFile(), e);
			throw e;
		}
		return config;
	}
	
	private InputStream getResetableInputStream(InputStream inStream) throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		IOUtils.copy(inStream, byteOut);
		byte[] bytes = byteOut.toByteArray();		
		ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
		return byteIn;
	}

	public void validateRuleData(InputStream inStream) throws SAXException, IOException {
		try {
			SchemaFactory factory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Resource resource = new ClassPathResource("/RuleDataQueries.xsd");
			Schema schema = factory.newSchema(new StreamSource(resource.getInputStream()));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(inStream));
			logger.debug("valid xml document");
		} catch (IOException ie) {
			logger.error("Error while validating " + Configuration.getQueryFile(), ie);
			throw ie;
		} catch (SAXException se) {
			logger.error("Error while validating " + Configuration.getQueryFile(), se);
			throw se;
		}
	}
}
