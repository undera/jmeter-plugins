/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */
package com.atlantbh.jmeter.plugins.xmlformatter;

import java.io.Serializable;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.jmeter.processor.PostProcessor;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.w3c.dom.Document;

/**
 * This class provides pretty print of XML result from response.
 *
 */
public class XMLFormatPostProcessor extends AbstractTestElement implements
        Cloneable, Serializable, PostProcessor, TestElement {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final long serialVersionUID = -4885245911424989596L;

    public void process() {
        JMeterContext threadContext = getThreadContext();

        String responseString = threadContext.getPreviousResult().getResponseDataAsString();
        try {
            threadContext.getPreviousResult().setResponseData(serialize2(responseString).getBytes("UTF-8"));
        } catch (Exception e) {
            log.info("Error while formating response xml - " + e.getMessage());
        }
    }

    private String serialize2(String unformattedXml) throws Exception {
        final Document document = XmlUtil.stringToXml(unformattedXml);
        TransformerFactory tfactory = TransformerFactory.newInstance();
        StringWriter buffer = new StringWriter();
        Transformer serializer = tfactory.newTransformer();
        // Setup indenting to "pretty print"
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        serializer.transform(new DOMSource(document), new StreamResult(buffer));

        return buffer.toString();
    }
}