/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.hadooputilities.jobstatistics;

import java.io.IOException;
import java.util.Map;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;

/**
 * Test Element class, which connects GUI part and functionality
 * that this component exposes
 * 
 * @author Bakir Jusufbegovic / AtlantBH
 */
public class JobStatistics extends AbstractSampler {

	private static final long serialVersionUID = 1L;
	
	private static final String JOBTRACKER = "JOBTRACKER";
	private static final String JOBID = "JOBID";
	private static final String JOBIDANDGROUP = "JOBIDANDGROUP";
	private static final String JOBCOUNTERSBYIDBOOL = "JOBCOUNTERSBYIDBOOL";
	private static final String JOBCOUNTERSBYIDANDGROUPBOOL = "JOBCOUNTERSBYIDANDGROUPBOOL";
	private static final String JOBSTATISTICSBYID = "JOBSTATISTICSBYID";
	private static final String TASKCOUNTERSBYIDBOOL = "TASKCOUNTERSBYIDBOOL";
	private static final String TASKSTATISTICSBYIDBOOL = "TASKSTATISTICSBYIDBOOL";
	
	
	public void setJobTracker(String jobTracker)
	{
		setProperty(JOBTRACKER, jobTracker);
	}
	
	public String getJobTracker()
	{
		return getPropertyAsString(JOBTRACKER);
	}
	
	public void setJobId(String jobId)
	{
		setProperty(JOBID, jobId);
	}
	
	public String getJobId()
	{
		return getPropertyAsString(JOBID);
	}
	
	public void setJobIdAndGroup(String jobIdAndGroup)
	{
		setProperty(JOBIDANDGROUP, jobIdAndGroup);
	}
	
	public String getJobIdAndGroup()
	{
		return getPropertyAsString(JOBIDANDGROUP);
	}
	
	public void setJobCountersByIdBool(boolean jobCountersById)
	{
		setProperty(JOBCOUNTERSBYIDBOOL, jobCountersById);
	}
	
	public boolean isJobCountersByIdBool()
	{
		return getPropertyAsBoolean(JOBCOUNTERSBYIDBOOL);
	}
	
	public void setJobCountersByIdAndGroupBool(boolean jobCountersByIdAndGroup)
	{
		setProperty(JOBCOUNTERSBYIDANDGROUPBOOL, jobCountersByIdAndGroup);
	}
	
	public boolean isJobCountersByIdAndGroupBool()
	{
		return getPropertyAsBoolean(JOBCOUNTERSBYIDANDGROUPBOOL);
	}
	
	public void setJobStatisticsByIdBool(boolean jobStatistics)
	{
		setProperty(JOBSTATISTICSBYID, jobStatistics);
	}
	
	public boolean isJobStatisticsByIdBool()
	{
		return getPropertyAsBoolean(JOBSTATISTICSBYID);
	}
	
	public void setTaskCountersByIdBool(boolean taskCountersById)
	{
		setProperty(TASKCOUNTERSBYIDBOOL, taskCountersById);
	}
	
	public boolean isTaskCountersByIdBool()
	{
		return getPropertyAsBoolean(TASKCOUNTERSBYIDBOOL);
	}
	
	public void setTaskStatisticsByIdBool(boolean taskStatisticsById)
	{
		setProperty(TASKSTATISTICSBYIDBOOL, taskStatisticsById);
	}
	
	public boolean isTaskStatisticsByIdBool()
	{
		return getPropertyAsBoolean(TASKSTATISTICSBYIDBOOL);
	}
	
	@Override
	public SampleResult sample(Entry arg0) {
		
		JobLayer jobLayer = new JobLayer();
		TaskLayer taskLayer = new TaskLayer();
		String response = "";
		
		SampleResult result = new SampleResult();
		
		String requestData = "mapred.job.tracker: " + this.getJobTracker() + "\n";
		requestData += "Job id: " + this.getJobId();
		
		result.setSampleLabel(getName());
		result.setSamplerData(requestData);
		result.setDataType(SampleResult.TEXT);
		
		result.sampleStart();
		
		if (isJobCountersByIdBool())
		{
			try 
			{
				Map<String,String> counters = jobLayer.getJobCountersByJobId(this.getJobTracker(), this.getJobId());
				response = jobLayer.getCountersAsXml(counters);
				result.setResponseData(response.getBytes());
				result.setSuccessful(true);
			} 
			catch (IOException e1) 
			{
				result.setResponseData(e1.getMessage().getBytes());
				result.setSuccessful(false);
			} 
			catch (Exception e2)
			{
				result.setResponseData(e2.getMessage().getBytes());
				result.setSuccessful(false);
			}
		}
		
		if (isJobCountersByIdAndGroupBool())
		{
			try 
			{
				Map<String,String> counters = jobLayer.getJobCountersByJobIdAndGroupName(this.getJobTracker(), this.getJobId(), this.getJobIdAndGroup());
				response = jobLayer.getCountersAsXml(counters);
				result.setResponseData(response.getBytes());
				result.setSuccessful(true);
			} 
			catch (IOException e1) 
			{
				result.setResponseData(e1.getMessage().getBytes());
				result.setSuccessful(false);
			} 
			catch (Exception e2)
			{
				result.setResponseData(e2.getMessage().getBytes());
				result.setSuccessful(false);
			}
		}
		
		if (isJobStatisticsByIdBool())
		{
			try 
			{
				response = jobLayer.getJobStatisticsByJobId(this.getJobTracker(), this.getJobId());
				result.setResponseData(response.getBytes());
				result.setSuccessful(true);
			} 
			catch (IOException e1) 
			{
				result.setResponseData(e1.getMessage().getBytes());
				result.setSuccessful(false);
			}
			catch (Exception e2)
			{
				result.setResponseData(e2.getMessage().getBytes());
				result.setSuccessful(false);
			}
		}
		
		if (isTaskCountersByIdBool())
		{
			try 
			{
				response = taskLayer.getTaskLevelCountersByJobId(this.getJobTracker(), this.getJobId());
				result.setResponseData(response.getBytes());
				result.setSuccessful(true);
			} 
			catch (IOException e1)
			{
				result.setResponseData(e1.getMessage().getBytes());
				result.setSuccessful(false);
			}
			catch (Exception e2)
			{
				result.setResponseData(e2.getMessage().getBytes());
				result.setSuccessful(false);
			}
		}
				
		if (isTaskStatisticsByIdBool())
		{
			try 
			{
				response = taskLayer.getTaskStatisticsByJobId(this.getJobTracker(), this.getJobId());
				result.setResponseData(response.getBytes());
				result.setSuccessful(true);
			} 
			catch (IOException e1) 
			{
				result.setResponseData(e1.getMessage().getBytes());
				result.setSuccessful(false);
			}
			catch (Exception e2)
			{
				result.setResponseData(e2.getMessage().getBytes());
				result.setSuccessful(false);
			}

		}
		
		result.sampleEnd();
		return result;
	}
}
