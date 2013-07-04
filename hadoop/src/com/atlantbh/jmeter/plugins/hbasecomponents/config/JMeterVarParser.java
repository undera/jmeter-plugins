/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.hbasecomponents.config;

import java.util.regex.*;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Parsing JMeter variables by prefix.
 *
 */
public class JMeterVarParser {
	
	private static Pattern pattern = Pattern.compile("\\$\\{\\w+\\}");
	
	private static final Logger log = LoggingManager.getLoggerForClass();
	
	public static String parse(String format, JMeterVariables vars){
		Matcher matcher = pattern.matcher(format);
		StringBuffer result = new StringBuffer();
		String s;
		
		while (matcher.find())    		
			if ((s = vars.get(format.substring(matcher.start()+2, matcher.end()-1))) != null)
				matcher.appendReplacement(result, s);
			else
				log.error("Undefined variable: " + matcher.group());

		matcher.appendTail(result);
		return result.toString();
	}
}