/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.hbasecomponents.config;

import java.io.IOException;
import java.io.Serializable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;

public class HBaseConnectionVariable implements Serializable {
	private static final long serialVersionUID = -4476571761212844047L;

	private String zkHost;
	private String name;
	private HTablePool tablePool = null;
	
	public HBaseConnectionVariable() {
	}
	
	public Configuration getConfig() {
		Configuration conf = HBaseConfiguration.create();
		System.out.println("Connecting to  " + zkHost);
		conf.set("hbase.zookeeper.quorum", zkHost);
        conf.set("hbase.client.retries.number", "10");        
        return conf;
	}
	
	public HTableInterface getTable(String tableName) throws IOException {
		if (tablePool == null) {
			System.out.println("Creating table pool for connection " + name + "...");
			tablePool = new HTablePool(getConfig(), 4096);		
		}		
		return tablePool.getTable(tableName);
	}
	
	public void putTable(HTableInterface table) {
		tablePool.putTable(table);
	}
	
	public HBaseConnectionVariable(String zkHost, String name) {
		super();
		this.zkHost = zkHost;
		this.name = name;
	}
	
	public String getZkHost() {
		return zkHost;
	}
	
	public void setZkHost(String zkHost) {
		this.zkHost = zkHost;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
}
