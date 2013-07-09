/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.hbasecomponents.samplers;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import com.atlantbh.jmeter.plugins.hbasecomponents.config.HBaseConnection;
import com.atlantbh.jmeter.plugins.hbasecomponents.config.HBaseConnectionVariable;
import com.atlantbh.jmeter.plugins.hbasecomponents.utils.Row2XML;

import com.atlantbh.jmeter.plugins.hbasecomponents.config.JMeterVarParser;

/**
 * Sampler that enable getting HBase record by its rowkey.
 *
 */
public class HBaseRowkeySampler extends AbstractSampler implements Sampler {

	private static final long serialVersionUID = 6701023631583398986L;

	@Override
	public SampleResult sample(Entry entry) {
		StringBuilder response = new StringBuilder();
		JMeterVariables vars = getThreadContext().getVariables();
		String rowKey = JMeterVarParser.parse(getRowKey(),vars);
		String tableName = JMeterVarParser.parse(gethBaseTable(),vars);
		String conString = JMeterVarParser.parse(getConnectionName(),vars);

		// Prepare Result of sampler
		SampleResult result = new SampleResult();
		result.setSampleLabel(getName());
		result.setSamplerData("Executing:\n\thost : " + conString + "\n\ttable : " + tableName + "\n\trowkey : " + rowKey);
		result.sampleStart();
		result.setDataEncoding("UTF-8");
        result.setDataType("text/xml");
		
		
		response.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

		response.append("<response>\n");
		
		try {
			HBaseConnectionVariable hbVar = HBaseConnection.getConnection(conString);
			// If there is no connection with that name
			if (hbVar == null) {
				makeError(result, "There is no connection with name '" + getConnectionName() + "'", false);
			}
			else {
				// Get table from pool
				HTable hTable =  (HTable)hbVar.getTable(tableName);
	
				// Get row from HBase
				Result res = hTable.get(new Get(rowKey.getBytes()));
	
				// Iterate trough all columns and create variables
				// in form  family:column=value
				// also add new line for each column in result response in same form
				
				if (getLatestTimestampRows() == Boolean.TRUE)
				{			
					response.append(Row2XML.row2xmlStringLatest(res, getOmitVars() ? vars : null, 0));
				}
				else
				{
					response.append(Row2XML.row2xmlstring(res, getOmitVars() ? vars : null, 0));	
				}
				
				// Back to pool
				hbVar.putTable(hTable);
				
				response.append("</response>\n");

				
				// Finish response...
				result.setResponseData(response.toString(), "UTF-8");
				result.setDataType("text");
				result.setSuccessful(true);
				result.sampleEnd();
			}
		} catch (Exception e) {
			makeError(result, "Failed to access database : host:[" + conString + "] table:[" + tableName + "] rowkey:[" + rowKey + "]", false);
		}

		return result;
	}

	public String getConnectionName() {
		return getPropertyAsString("HB_CONNAME");
	}

	public void setConnectionName(String connectionName) {
		setProperty("HB_CONNAME", connectionName);
	}

	public String gethBaseTable() {
		return getPropertyAsString("HB_TABLE");
	}

	public void sethBaseTable(String hBaseTable) {
		setProperty("HB_TABLE", hBaseTable);
	}

	public String getRowKey() {
		return getPropertyAsString("HB_ROWKEY");
	}

	public void setRowKey(String rowKey) {
		setProperty("HB_ROWKEY", rowKey);
	}
	
	public Boolean getOmitVars() {
		return getPropertyAsBoolean("HB_OMITVARS");
	}

	public void setOmitVars(Boolean value) {
		setProperty("HB_OMITVARS", value);
	}
	
	public Boolean getLatestTimestampRows() {
		return getPropertyAsBoolean("HB_LATEST_TIMESTAMP_ROWS");
	}

	public void setLatestTimestampRows(Boolean value) {
		setProperty("HB_LATEST_TIMESTAMP_ROWS", value);
	}
	
	private void makeError(SampleResult result, String msg, Boolean throwError) {
		result.setResponseMessage(msg);
		result.setSuccessful(false);
		result.sampleEnd();
		if (throwError) {
			throw new RuntimeException(msg);
		}
	}

}
