/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.hbasecomponents.config;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.threads.JMeterVariables;

import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import com.atlantbh.jmeter.plugins.hbasecomponents.samplers.HBaseRowkeySampler;
import com.atlantbh.jmeter.plugins.hbasecomponents.samplers.HBaseScanSampler;

/**
 * Class for creating HBase connection (in fact pool of connections) that is used (by its name) 
 * in {@link HBaseScanSampler} and {@link HBaseRowkeySampler}
 *
 */
public class HBaseConnection  extends ConfigTestElement implements ConfigElement, TestListener {

	private static final long serialVersionUID = -2642777372269255604L;
	
	private static final Logger log = LoggingManager.getLoggerForClass();
	
	private static final String ZK_HOST = "ZK_HOST";
	private static final String ZK_NAME = "ZK_NAME";
	
	private static ConcurrentHashMap<String, HBaseConnectionVariable> pool = new ConcurrentHashMap<String, HBaseConnectionVariable>();
	

	public static HBaseConnectionVariable getConnection(String name) { 
		return pool.get(name);
	}
	
	public String getZkHost() {
		return getPropertyAsString(ZK_HOST);
	}
	
	public void setZkHost(String zkHost) {
		setProperty(ZK_HOST, zkHost);
	}
	
	public String getZkName() {
		return getPropertyAsString(ZK_NAME);
	}
	
	public void setZkName(String zkName) {
		setProperty(ZK_NAME, zkName);
	}
	
	public void testStarted(String s){testStarted();}
	
	public void testStarted(){
		JMeterVariables vars = getThreadContext().getVariables();
		String name = JMeterVarParser.parse(getZkName(), vars);
		
		if (pool.containsKey(name)){
			log.error("Test error: Multiple HBase connections called " + name);
			return;
		}
		
		pool.put(name, new HBaseConnectionVariable(JMeterVarParser.parse(getZkHost(), vars),name));
	}
	
	public void testEnded(String s){testEnded();}
	
	public void testEnded(){
		JMeterVariables vars = getThreadContext().getVariables();
		String name = JMeterVarParser.parse(getZkName(), vars);
		
		if (!pool.containsKey(name)){
			log.error("Test warning: HBase connection already cleared: " + name);
			return;
		}		
		pool.remove(name);
	}
	
	public void testIterationStart(LoopIterationEvent event) {
	}
	
}
