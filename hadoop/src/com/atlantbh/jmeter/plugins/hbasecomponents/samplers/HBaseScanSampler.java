/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.hbasecomponents.samplers;

import java.util.Iterator;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import com.atlantbh.jmeter.plugins.hbasecomponents.config.HBaseConnection;
import com.atlantbh.jmeter.plugins.hbasecomponents.config.HBaseConnectionVariable;
import com.atlantbh.jmeter.plugins.hbasecomponents.utils.Row2XML;
import com.atlantbh.jmeter.plugins.hbasecomponents.utils.filter.FilterParser;
import com.atlantbh.jmeter.plugins.hbasecomponents.utils.filter.HBaseFilterParser;
import com.atlantbh.jmeter.plugins.hbasecomponents.utils.filter.Node;

import com.atlantbh.jmeter.plugins.hbasecomponents.config.JMeterVarParser;

/**
 * Sampler that enables scanning HBase table by specifying filter.
 *
 */
public class HBaseScanSampler extends AbstractSampler implements Sampler {

	private static final long serialVersionUID = -4685749611597443212L;

	private static final String CONN_NAME = "HBS_CONN";
	private static final String HBS_TABLE = "HBS_TABLE";
	private static final String HBS_START = "HBS_START";
	private static final String HBS_END = "HBS_END";
	private static final String HBS_LIMIT = "HBS_LIMIT";
	private static final String HBS_FILTER = "HBS_FILTER";
	private static final String HBS_PASSALL = "HBS_PASSALL";
	private static final String HBS_OMITVARS = "HBS_OMITVARS";
	private static final String HBS_LATEST_TIMESTAMP_ROWS = "HBS_LATEST_TIMESTAMP_ROWS";

	public static void main(String[] args) throws Exception {
		String filter = "OPERATIONAL:JOB_ID=job_201104141652_2429";
		Node node = FilterParser.parse(filter);
		Filter fil = HBaseFilterParser.parse(node);
	}
	
	public SampleResult sample(Entry arg0) {

		StringBuilder response = new StringBuilder();
		StringBuilder request = new StringBuilder();
		SampleResult result = new SampleResult();
		
		JMeterVariables vars = getThreadContext().getVariables();
		String connection= JMeterVarParser.parse(getConnectionName(), vars);
		String table = JMeterVarParser.parse(getTableName(), vars);
		String startKey = JMeterVarParser.parse(getStartKey(), vars);
		String endKey = JMeterVarParser.parse(getEndKey(), vars);
		String limit = JMeterVarParser.parse(getLimit(), vars);
		String filter = JMeterVarParser.parse(getFilter(), vars);

		result.setSampleLabel(getName());
		result.sampleStart();
		request.append("Executing scan:\n\thost : ").append(connection).append("\n\ttable : ").append(table).append("\n\tstart rowkey : ").append(startKey).append(
				"\n\tend rowkey : ").append(endKey);

		response.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		response.append("<response>\n");

		try {
			HBaseConnectionVariable hbVar = HBaseConnection.getConnection(connection);

			// Check connection variable
			if (hbVar == null) {
				makeError(result, "There is no connection with name '" + connection + "'", false);
			} else {
				// Get HTable from pool
				HTableInterface hTable = hbVar.getTable(table);

				Scan scan = new Scan();

				FilterList mainFilterList = new FilterList(Operator.MUST_PASS_ALL);

				try {
					if (filter != null && !"".equals(filter.trim())) {
						request.append("\nfilters:[").append(filter).append("]\n\n");
						Node node = FilterParser.parse(filter);
						Filter filters = HBaseFilterParser.parse(node);
						if (filters != null) {
							mainFilterList.addFilter(filters);
						}
					}
				} catch (Exception e) {
					makeError(result, "Invalid filter syntax ".concat(e.getMessage()), true);
				}

				if (limit != null && !"".equals(limit.trim())) {
					Long l = Long.valueOf(limit);
					PageFilter pageFilter = new PageFilter(l);
					mainFilterList.addFilter(pageFilter);
				}

				scan.setFilter(mainFilterList);

				if (startKey != null && !"".equals(startKey.trim())) {
					scan.setStartRow(Bytes.toBytes(startKey));
				}
				if (endKey != null && !"".equals(endKey.trim())) {
					scan.setStopRow(Bytes.toBytes(endKey));
				}

				// Get data and convert to return SampleResult
				// Data is in format :
				// family:column_#=value - where # is column index
				// 
				// Additional variable named by Hbase Scan name (getName()) is
				// created with number of columns returned, e.g. 'HBase Scan
				// Sampler_#'
				ResultScanner resultScanner = hTable.getScanner(scan);
				Iterator<Result> iter = resultScanner.iterator();
				Integer i = 0;

				while (iter.hasNext()) {
					Result res = iter.next();
					
					if (getLatestTimestampRows() == Boolean.TRUE)
					{
						response.append(Row2XML.row2xmlStringLatest(res, getOmitVars() ? vars : null, i + 1));
						i++;
					}
					else
					{
						response.append(Row2XML.row2xmlstring(res, getOmitVars() ? vars : null, i + 1));
						i++;						
					}
				}
				response.append("</response>\n");

				// Set sampler data, type, success, ...
				result.setResponseData(response.toString(), "UTF-8");
				result.setDataType(SampleResult.TEXT);
				result.setSuccessful(true);
				result.setSamplerData(request.toString());

				result.sampleEnd();

				// Back to pool
				hbVar.putTable(hTable);
			}

		} catch (Exception e) {
			makeError(result, "Failed to access database : host:[" + connection + "] table:[" + table + "]\n" + e.getMessage(), false);
		}

		return result;
	}

	private void makeError(SampleResult result, String msg, Boolean throwError) {
		result.setResponseMessage(msg);
		result.setSuccessful(false);
		//result.sampleEnd();
		if (throwError) {
			throw new RuntimeException(msg);
		}
	}

	private FilterList parseFilterString(String filters, StringBuilder request, SampleResult result, Boolean passAll) {
		FilterList booleanFilter = new FilterList(passAll == true ? Operator.MUST_PASS_ALL : Operator.MUST_PASS_ONE);

		if (getFilter().trim().equals("") == false) {
			String[] subFilters = getFilter().trim().split("\\s");
			request.append("\nfilters:[" + (passAll == true ? "MUST_PASS_ALL" : "MUST_PASS_ONE") + "]\n\n");
			for (String subFilter : subFilters) {
				String[] qualiValue = subFilter.split("=");
				if (qualiValue.length != 2) {
					makeError(result, "Invalid filter syntax '" + subFilter + "' Use - family:column=value ", true);
				}

				String[] fq = qualiValue[0].split(":");
				String value = qualiValue[1];

				if (fq.length != 2) {
					makeError(result, "Invalid filter syntax '" + subFilter + "' Use - family:column=value ", true);
				}

				request.append("Filter : " + fq[0] + ":" + fq[1] + "=" + value + "\n");
				Filter f = new SingleColumnValueFilter(fq[0].getBytes(), fq[1].getBytes(), CompareOp.EQUAL, value.getBytes());
				booleanFilter.addFilter(f);
			}
		}

		return booleanFilter;
	}

	public String getConnectionName() {
		return getPropertyAsString(CONN_NAME);
	}

	public void setConnectionName(String value) {
		setProperty(CONN_NAME, value);
	}

	public String getTableName() {
		return getPropertyAsString(HBS_TABLE);
	}

	public void setTableName(String value) {
		setProperty(HBS_TABLE, value);
	}

	public String getStartKey() {
		return getPropertyAsString(HBS_START);
	}

	public void setStartKey(String value) {
		setProperty(HBS_START, value);
	}

	public String getEndKey() {
		return getPropertyAsString(HBS_END);
	}

	public void setEndKey(String value) {
		setProperty(HBS_END, value);
	}

	public String getLimit() {
		return getPropertyAsString(HBS_LIMIT);
	}

	public void setLimit(String value) {
		setProperty(HBS_LIMIT, value);
	}

	public String getFilter() {
		return getPropertyAsString(HBS_FILTER);
	}

	public void setFilter(String value) {
		setProperty(HBS_FILTER, value);
	}

	public Boolean getPassAll() {
		return getPropertyAsBoolean(HBS_PASSALL);
	}

	public void setPassAll(Boolean value) {
		setProperty(HBS_PASSALL, value);
	}

	public Boolean getOmitVars() {
		return getPropertyAsBoolean(HBS_OMITVARS);
	}

	public void setOmitVars(Boolean value) {
		setProperty(HBS_OMITVARS, value);
	}
	
	public Boolean getLatestTimestampRows() {
		return getPropertyAsBoolean("HBS_LATEST_TIMESTAMP_ROWS");
	}

	public void setLatestTimestampRows(Boolean value) {
		setProperty("HBS_LATEST_TIMESTAMP_ROWS", value);
	}
}