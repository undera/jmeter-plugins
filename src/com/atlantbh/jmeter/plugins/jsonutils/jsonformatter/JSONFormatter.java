/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.jsonutils.jsonformatter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.jmeter.processor.PostProcessor;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.threads.JMeterContext;

/**
 * This is main class for JSON formatter which contains formatJSON method that takes sample result and do pretty
 * print in JSON
 * 
 * @author Bakir Jusufbegovic / AtlantBH
 */

public class JSONFormatter extends AbstractTestElement implements PostProcessor {

	private static final long serialVersionUID = 1L;

	public JSONFormatter() {
		super();
	}

	public String formatJSON(String json) {

		if (json.startsWith("[") && json.endsWith("]")) {
			return JSONArray.fromObject(json).toString(4);
		} else {
			return JSONObject.fromObject(json).toString(4);
		}
	}

	@Override
	public void process() {
		JMeterContext context = getThreadContext();
		String responseData = context.getPreviousResult().getResponseDataAsString();
		context.getPreviousResult().setResponseData((this.formatJSON(responseData)).getBytes());
	}
}
