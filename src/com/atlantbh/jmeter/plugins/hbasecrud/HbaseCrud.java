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

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;

/**
 * Test Element class, which connects GUI part and functionality
 * that this component exposes
 * 
 * @author Bakir Jusufbegovic / AtlantBH
 */
public class HbaseCrud extends AbstractSampler{

	private static final long serialVersionUID = 1L;
	
	private static final String HBASEZOOKEEPERQUORUM = "HBASEZOOKEEPERQUORUM";
	private static final String HBASESOURCETABLE = "HBASESOURCETABLE";
	private static final String ROWKEY = "ROWKEY";
	private static final String COLUMNFAMILYCOLUMNNAMELIST = "COLUMNFAMILYCOLUMNNAMELIST";
	private static final String FULLCOLUMNNAMESLIST = "FULLCOLUMNNAMESLIST";
	private static final String FILTERCOLUMNFAMILIESFORTIMESTAMP = "FILTERCOLUMNSFORTIMESTAMP";

	private static final String ADDORUPDATEDATAONRECORDBOOL = "ADDORUPDATEDATAONRECORDBOOL";
	private static final String DELETEDATAFROMRECORDBOOL = "DELETEDATAFROMRECORDBOOL";
	private static final String LATESTTIMESTAMPOPERATIONBOOL = "LATESTTIMESTAMPOPERATIONBOOL";
	private static final String LATESTTIMESTAMPOPERATIONWITHFILTERBOOL = "LATESTTIMESTAMPOPERATIONWITHFILTERBOOL";
	
	public void setHbaseZookeeperQuorum(String hbaseZookeeperQuorum)
	{
		setProperty(HBASEZOOKEEPERQUORUM, hbaseZookeeperQuorum);
	}
	
	public String getHbaseZookeeperQuorum()
	{
		return getPropertyAsString(HBASEZOOKEEPERQUORUM);
	}
	
	public void setHbaseSourceTable(String hbaseSourceTable)
	{
		setProperty(HBASESOURCETABLE, hbaseSourceTable);
	}
	
	public String getHbaseSourceTable()
	{
		return getPropertyAsString(HBASESOURCETABLE);
	}
	
	public void setRowKey(String rowKey)
	{
		setProperty(ROWKEY, rowKey);
	}
	
	public String getRowKey()
	{
		return getPropertyAsString(ROWKEY);
	}
	
	public void setFullColumnNamesList(String fullColumnNamesList)
	{
		setProperty(FULLCOLUMNNAMESLIST, fullColumnNamesList);
	}
	
	public String getFullColumnNamesList()
	{
		return getPropertyAsString(FULLCOLUMNNAMESLIST);
	}
	
	public void setColumnFamilyColumnNameList(String columnFamilyColumnNameList)
	{
		setProperty(COLUMNFAMILYCOLUMNNAMELIST, columnFamilyColumnNameList);
	}
	
	public String getColumnFamilyColumnNameList()
	{
		return getPropertyAsString(COLUMNFAMILYCOLUMNNAMELIST);
	}
	
	public void setFilterColumnFamiliesForTimestamp(String filterColumnFamiliesForTimestamp)
	{
		setProperty(FILTERCOLUMNFAMILIESFORTIMESTAMP, filterColumnFamiliesForTimestamp);
	}
	
	public String getFilterColumnFamiliesForTimestamp()
	{
		return getPropertyAsString(FILTERCOLUMNFAMILIESFORTIMESTAMP);
	}
	
	public void setAddOrUpdateDataOnRecordBool(boolean addOrUpdateDataOnRecord)
	{
		setProperty(ADDORUPDATEDATAONRECORDBOOL, addOrUpdateDataOnRecord);
	}
	
	public boolean isAddOrUpdateDataOnRecordBool()
	{
		return getPropertyAsBoolean(ADDORUPDATEDATAONRECORDBOOL);
	}
	
	public void setDeleteDataFromRecordBool(boolean deleteDataFromRecord)
	{
		setProperty(DELETEDATAFROMRECORDBOOL, deleteDataFromRecord);
	}
	
	public boolean isDeleteDataFromRecordBool()
	{
		return getPropertyAsBoolean(DELETEDATAFROMRECORDBOOL);
	}
	
	public void setLatestTimestampOperationBool(boolean latestTimestampOperation)
	{
		setProperty(LATESTTIMESTAMPOPERATIONBOOL, latestTimestampOperation);
	}
	
	public boolean isLatestTimestampOperationBool()
	{
		return getPropertyAsBoolean(LATESTTIMESTAMPOPERATIONBOOL);
	}
	
	public void setLatestTimestampOperationWithFilterBool(boolean latestTimestampOperationWithFilter)
	{
		setProperty(LATESTTIMESTAMPOPERATIONWITHFILTERBOOL, latestTimestampOperationWithFilter);
	}
	
	public boolean isLatestTimestampOperationWithFilterBool()
	{
		return getPropertyAsBoolean(LATESTTIMESTAMPOPERATIONWITHFILTERBOOL);
	}
	
	
	@Override
	public SampleResult sample(Entry arg0) {
		
		SampleResult result = new SampleResult();
		
		String requestData = "hbase.zookeeper.quorum: " + getHbaseZookeeperQuorum() + "\n";
		requestData += "HBase source table: " + getHbaseSourceTable() + "\n";
		requestData += "Rowkey: " + getRowKey() + "\n";
		requestData += "Attributes specified: \n";
		
		if (!"".equals(getFullColumnNamesList()))
		{
			String[] fullColumnNamesList = getFullColumnNamesList().split(",");
			for (String fullColumnName : fullColumnNamesList)
			{
				requestData += "Full column name: " + fullColumnName + "\n";
			}
		}
		
		if (!"".equals(getColumnFamilyColumnNameList()))
		{
			String[] columnFamilyColumnNameList = getColumnFamilyColumnNameList().split(",");
			for (String columnFamilyColumnName : columnFamilyColumnNameList)
			{
				requestData += "Column family column name: " + columnFamilyColumnName + "\n";
			}
		}		
		
		String response = "";
		
		result.setSampleLabel(getName());
		result.setSamplerData(requestData);
		result.setDataType(SampleResult.TEXT);
		
		result.sampleStart();
		
		HbaseCrudHelper hbaseCrudHelper = new HbaseCrudHelper();
		
		try 
		{
			hbaseCrudHelper.setConfiguration(getHbaseZookeeperQuorum(),getHbaseSourceTable());
		} 
		catch (IOException ex)
		{
			result.setResponseData(ex.getMessage().getBytes());
			result.setSuccessful(false);
			return result;
		}
		catch (Exception ex)
		{
			result.setResponseData(ex.getMessage().getBytes());
			result.setSuccessful(false);
			return result;
		}
		
		if (isAddOrUpdateDataOnRecordBool())
		{
			if (isLatestTimestampOperationBool())
			{
				try
				{
					hbaseCrudHelper.addOrUpdateDataToHBase(getRowKey(),getFullColumnNamesList());
					response = hbaseCrudHelper.getResponseMessage();
					result.setResponseData(response.getBytes());
					result.setSuccessful(true);
				}
				catch (IOException ex)
				{
					result.setResponseData(ex.getMessage().getBytes());
					result.setSuccessful(false);
				}
				catch (Exception ex)
				{
					result.setResponseData(ex.getMessage().getBytes());
					result.setSuccessful(false);
				}
			}
			
			if (isLatestTimestampOperationWithFilterBool())
			{
				try
				{
					hbaseCrudHelper.parseColumnFamilies(getFilterColumnFamiliesForTimestamp());
					hbaseCrudHelper.addOrUpdateDataToHBase(getRowKey(),getFullColumnNamesList());
					response = hbaseCrudHelper.getResponseMessage();
					result.setResponseData(response.getBytes());
					result.setSuccessful(true);
				}
				catch (IOException ex)
				{
					result.setResponseData(ex.getMessage().getBytes());
					result.setSuccessful(false);
				}
				catch (Exception ex)
				{
					result.setResponseData(ex.getMessage().getBytes());
					result.setSuccessful(false);
				}
			}
		}
		
		if (isDeleteDataFromRecordBool())
		{
			if (isLatestTimestampOperationBool())
			{
				try
				{
					hbaseCrudHelper.deleteDataFromHbase(getRowKey(),getColumnFamilyColumnNameList());
					response = hbaseCrudHelper.getResponseMessage();
					result.setResponseData(response.getBytes());
					result.setSuccessful(true);
				}
				catch (IOException ex)
				{
					result.setResponseData(ex.getMessage().getBytes());
					result.setSuccessful(false);
				}
				catch (Exception ex)
				{
					result.setResponseData(ex.getMessage().getBytes());
					result.setSuccessful(false);
				}
			}
			
			if (isLatestTimestampOperationWithFilterBool())
			{
				try
				{
					hbaseCrudHelper.parseColumnFamilies(getFilterColumnFamiliesForTimestamp());
					hbaseCrudHelper.deleteDataFromHbase(getRowKey(),getColumnFamilyColumnNameList());
					response = hbaseCrudHelper.getResponseMessage();
					result.setResponseData(response.getBytes());
					result.setSuccessful(true);
				}
				catch (IOException ex)
				{
					result.setResponseData(ex.getMessage().getBytes());
					result.setSuccessful(false);
				}
				catch (Exception ex)
				{
					result.setResponseData(ex.getMessage().getBytes());
					result.setSuccessful(false);
				}
			}
		}
		
		return result;
	}
}
