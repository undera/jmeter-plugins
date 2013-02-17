/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.hbasecrud;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * This class exposes functionalities for hbase record crud operations. Those functionalities are:
 * 1. addOrUpdateDataToHBase (by latest timestamp or with filter for excluded column families for timestamp evaluation)
 * 2. deleteDataFromHbase (by latest timestamp or with filter for excluded column families for timestamp evaluation) 
 * 
 * @author Bakir Jusufbegovic / AtlantBH
 */
public class HbaseCrudHelper {

	private Configuration conf = null;
	private HTable hTable = null;
	private String responseMessage = "";
	
	private ArrayList<String> filterColumnFamilyList = new ArrayList<String>();
	private ArrayList<String> fullColumnNamesList = new ArrayList<String>();
	
	public HbaseCrudHelper() {};
	
	public String getResponseMessage()
	{
		return responseMessage;
	}
	
	public void setResponseMessage(String responseMessage)
	{
		this.responseMessage = responseMessage;
	}
	
	public void setConfiguration(String hbaseZK, String hbaseTable) throws IOException
	{
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", hbaseZK);
		hTable = new HTable(conf, hbaseTable);
	}
	
	public void parseColumnFamilies(String columnFamilies)
	{
		String[] strTokens = columnFamilies.split(",");
		
		for (int i = 0; i<strTokens.length; i++) {
			filterColumnFamilyList.add(strTokens[i]);
		}
	}
	
	private void parseFullColumnNamesList(String columnNames)
	{
		String[] strTokens = columnNames.split(",");		
	
		for (int i = 0; i<strTokens.length; i++) {
			fullColumnNamesList.add(strTokens[i]);
		}
	}
	
	private long getMostRecentTimestamp(String rowKey) throws IOException
	{
		long maxTimestamp = Long.MIN_VALUE;
		Get get = new Get(Bytes.toBytes(rowKey));
			
		Result result = hTable.get(get);
		
		if (filterColumnFamilyList.isEmpty()) {
			for (KeyValue kv : result.list()) {
				if (maxTimestamp < kv.getTimestamp()) {
					maxTimestamp = kv.getTimestamp();
				}
			}
		} else {
			for (KeyValue kv : result.list()) {
				byte[] bytes = kv.getFamily();
				String str = new String(bytes);
				if (!filterColumnFamilyList.contains(str)) {
					if (maxTimestamp < kv.getTimestamp()) {
						maxTimestamp = kv.getTimestamp();
					}
				}
			}
		}				
		return maxTimestamp;
	}
	
	public void addOrUpdateDataToHBase(String rowKey, String columnFamilyColumnQualifiers) throws IOException
	{		
		List<Put> puts = new ArrayList<Put>();
		Put put = new Put(Bytes.toBytes(rowKey));
		
		parseFullColumnNamesList(columnFamilyColumnQualifiers);
		long timestamp;
		String response = "";
		
		timestamp = this.getMostRecentTimestamp(rowKey);
			
		String[] columnFamilyColumnValueToken = null;
		String[] columnValueToken = null;
		
		try {
			for (int i=0; i<fullColumnNamesList.size(); i++) {
				columnFamilyColumnValueToken = fullColumnNamesList.get(i).split(":");
				columnValueToken = columnFamilyColumnValueToken[1].split("=");
				put.add(Bytes.toBytes(columnFamilyColumnValueToken[0]), Bytes.toBytes(columnValueToken[0]), Long.valueOf(timestamp), Bytes.toBytes(columnValueToken[1]));
				puts.add(put);	
				response += fullColumnNamesList.get(i) + " ";
			}
			hTable.put(puts);
			this.setResponseMessage("Added to POI:" + response);
		}
		catch (IOException ex) {
			throw ex;
		}
		finally {
			hTable.close();
		}
	}
	
	public void deleteDataFromHbase(String rowKey, String columnFamilyColumnQualifiers) throws IOException
	{
		List<Delete> deletes = new ArrayList<Delete>();
		Delete delete = new Delete(Bytes.toBytes(rowKey));
		
		parseFullColumnNamesList(columnFamilyColumnQualifiers);		
		long timestamp = this.getMostRecentTimestamp(rowKey);		
		String[] columnFamilyColumnValueToken = null;
		
		String response = "";
		
		try
		{
			for (int i=0; i<fullColumnNamesList.size(); i++)
			{
				if(fullColumnNamesList.get(i).contains(":")) {					
					columnFamilyColumnValueToken = fullColumnNamesList.get(i).split(":");
					delete.deleteColumn(Bytes.toBytes(columnFamilyColumnValueToken[0]), Bytes.toBytes(columnFamilyColumnValueToken[1]), Long.valueOf(timestamp));
					response += fullColumnNamesList.get(i) + " ";
				} else {					
					delete.deleteFamily(Bytes.toBytes(fullColumnNamesList.get(i)), Long.valueOf(timestamp));
					response += fullColumnNamesList.get(i) + " ";
				}
				deletes.add(delete);
			}		
			hTable.delete(deletes);
			this.setResponseMessage("Deleted from POI: " + response);
		}
		catch (IOException ex) {
			throw ex;
		}
		finally {
			hTable.close();
		}		
	}	
}
