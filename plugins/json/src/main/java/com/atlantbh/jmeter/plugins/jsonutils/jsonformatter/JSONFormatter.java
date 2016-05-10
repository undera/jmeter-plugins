/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */
package com.atlantbh.jmeter.plugins.jsonutils.jsonformatter;

import net.sf.json.JSON;
import net.sf.json.JSONException;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import org.apache.jmeter.processor.PostProcessor;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * This is main class for JSON formatter which contains formatJSON method that
 * takes sample result and do pretty print in JSON
 */
public class JSONFormatter extends AbstractTestElement implements PostProcessor {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final long serialVersionUID = 1L;
    private static final JsonConfig config = new JsonConfig();

    public JSONFormatter() {
        super();
    }

    private String formatJSON(String json) {
        JSON object = JSONSerializer.toJSON(json, config);
        return object.toString(4); // TODO: make a property to manage the indent
    }

    @Override
    public void process() {
        JMeterContext context = getThreadContext();
        String responseData = context.getPreviousResult().getResponseDataAsString();
        try {
            String str = this.formatJSON(responseData);
            context.getPreviousResult().setResponseData(str.getBytes());
        } catch (JSONException e) {
            log.warn("Failed to format JSON: " + e.getMessage());
            log.debug("Failed to format JSON", e);
        }
    }
}
