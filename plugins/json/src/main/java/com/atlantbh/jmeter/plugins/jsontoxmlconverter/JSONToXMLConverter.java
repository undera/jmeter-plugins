/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */
package com.atlantbh.jmeter.plugins.jsontoxmlconverter;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;

/**
 * This is main class for JSON to XML converter which contains method
 * ConvertToXML that takes json input as String type and convert it to xml
 * output as String type
 */
public class JSONToXMLConverter extends AbstractSampler {

    private static final long serialVersionUID = 1L;
    private static final String JSONINPUT = "JSONINPUT";
    private static final String XMLOUTPUT = "XMLOUTPUT";

    public JSONToXMLConverter() {
        super();
    }

    @Deprecated
    private String ConvertToXML(String jsonData) {
        XMLSerializer serializer = new XMLSerializer();
        JSON json = JSONSerializer.toJSON(jsonData);
        serializer.setRootName("xmlOutput");
        serializer.setTypeHintsEnabled(false);
        return serializer.write(json);
    }

    private void convertToXML() {
        XMLSerializer serializer = new XMLSerializer();
        JSON json = JSONSerializer.toJSON(this.getJsonInput());
        serializer.setRootName("xmlOutput");
        serializer.setTypeHintsEnabled(false);
        setXmlOutput(serializer.write(json));
    }

    public void setJsonInput(String jsonInput) {
        setProperty(JSONINPUT, jsonInput);
    }

    public String getJsonInput() {
        return getPropertyAsString(JSONINPUT);
    }

    public void setXmlOutput(String xmlOutput) {
        setProperty(XMLOUTPUT, xmlOutput);
    }

    public String getXmlOutput() {
        return getPropertyAsString(XMLOUTPUT);
    }

    @Override
    public SampleResult sample(Entry e) {
        SampleResult result = new SampleResult();

        result.setSampleLabel(getName());
        result.setSamplerData(this.getJsonInput());
        result.setDataType(SampleResult.TEXT);

        result.sampleStart();

        if (!getJsonInput().equalsIgnoreCase("")) {
            try {
                this.convertToXML();
                result.setResponseData(this.getXmlOutput().getBytes());
                result.setSuccessful(true);
            } catch (Exception e1) {
                result.setResponseData(e1.getMessage().getBytes());
                result.setSuccessful(false);
            }
        }

        result.sampleEnd();
        return result;
    }
}
