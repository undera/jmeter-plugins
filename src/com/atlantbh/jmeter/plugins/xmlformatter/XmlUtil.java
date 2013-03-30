/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */
package com.atlantbh.jmeter.plugins.xmlformatter;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * This class provides transformation string to XML {@link Document} and vice
 * versa.
 *
 */
public class XmlUtil {

    private static Transformer transformer;
    private static DocumentBuilder builder;

    static {
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            transformer = null;
            builder = null;
        }
    }

    /**
     * Convert string to XML {@link Document}
     *
     * @param string - string to be converted
     * @return - {@link Document}
     * @throws Exception - if {@link DocumentBuilder} is not initialized
     */
    public static Document stringToXml(String string) throws Exception {
        if (builder == null) {
            throw new Exception("DocumentBuilder is null.");
        }
        return builder.parse(new InputSource(new ByteArrayInputStream(string.getBytes("UTF-8"))));
    }

    /**
     * Convert XML {@link Document} to its string representation.
     *
     * @param document for conversion
     * @return - string representation of XML {@link Document}
     * @throws Exception - if {@link DocumentBuilder} is not initialized
     */
    public static String xmlToString(Document document) throws Exception {
        if (transformer == null) {
            throw new Exception("Transformer is null");
        }
        Source xmlSource = new DOMSource(document);
        StringWriter stringWriter = new StringWriter();
        Result result = new StreamResult(stringWriter);
        transformer.transform(xmlSource, result);
        return stringWriter.toString();
    }
}