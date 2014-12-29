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
import java.util.Iterator;

import org.apache.hadoop.mapred.Counters;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobID;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapred.TaskReport;
import org.apache.hadoop.mapred.Counters.Counter;
import org.apache.hadoop.mapred.Counters.Group;

/**
 * This class exposes functionalities on Task layer. Those functionalities are:
 * 1. getTaskLevelCountersByJobId 2. getTaskStatisticsByJobId
 */
public class TaskLayer extends JobLayer {

    public TaskLayer() {
    }

    public String getTaskLevelCountersByJobId(String jobTracker, String jobId) throws IOException {
        StringBuilder taskCounters = new StringBuilder();

        JobID id = this.convertToJobId(jobId);
        JobClient client = this.prepareJobClient(jobTracker);
        RunningJob job = client.getJob(id);
        TaskReport[] mapTaskReports = client.getMapTaskReports(id);
        TaskReport[] reduceTaskReports = client.getReduceTaskReports(id);

        taskCounters.append("<job id='").append(jobId).append("' name='").append(job.getJobName()).append("'>\n");
        taskCounters.append(" <mapTasks>\n");

        for (TaskReport mapTaskReport : mapTaskReports) {
            taskCounters.append("  <task id='").append(mapTaskReport.getTaskID().toString()).append("'\n");
            taskCounters.append("   <counters>\n");

            Counters counter = mapTaskReport.getCounters();

            Iterator<Group> iter = counter.iterator();

            while (iter.hasNext()) {
                Group group = iter.next();

                Iterator<Counter> cIter = group.iterator();
                while (cIter.hasNext()) {
                    Counter c = cIter.next();
                    taskCounters.append("    <counter name='").append(c.getDisplayName()).append("' value='").append(c.getValue()).append("'>\n");
                }
            }

            taskCounters.append("   </counters>\n");
            taskCounters.append("  </task>\n");
        }

        taskCounters.append(" </mapTasks>\n");

        taskCounters.append(" <reduceTasks>\n");

        for (TaskReport reduceTaskReport : reduceTaskReports) {
            taskCounters.append("  <task id='").append(reduceTaskReport.getTaskID().toString()).append("'\n");
            taskCounters.append("   <counters>\n");

            Counters counter = reduceTaskReport.getCounters();

            Iterator<Group> iter = counter.iterator();

            while (iter.hasNext()) {
                Group group = iter.next();

                Iterator<Counter> cIter = group.iterator();
                while (cIter.hasNext()) {
                    Counter c = cIter.next();
                    taskCounters.append("    <counter name='").append(c.getDisplayName()).append("' value='").append(c.getValue()).append("'>\n");
                }
            }

            taskCounters.append("   </counters>\n");
            taskCounters.append("  </task>\n");
        }

        taskCounters.append(" </reduceTasks>\n");
        taskCounters.append("</job>");

        return taskCounters.toString();
    }

    public String getTaskStatisticsByJobId(String jobTracker, String jobId) throws IOException {
        StringBuilder taskStatistics = new StringBuilder();
        long taskDuration;
        String duration;

        JobID id = this.convertToJobId(jobId);
        JobClient client = this.prepareJobClient(jobTracker);
        RunningJob job = client.getJob(id);

        TaskReport[] mapTaskReports = client.getMapTaskReports(id);
        TaskReport[] reduceTaskReports = client.getReduceTaskReports(id);

        taskStatistics.append("<job id='").append(jobId).append("' name='").append(job.getJobName()).append("'>\n");
        taskStatistics.append(" <mapTasks>\n");

        for (TaskReport mapTaskReport : mapTaskReports) {
            taskDuration = mapTaskReport.getFinishTime() - mapTaskReport.getStartTime();

            if (taskDuration < 0) {
                duration = "N/A";
            } else {
                duration = String.valueOf(taskDuration);
            }

            double progress = mapTaskReport.getProgress() * 100;
            String taskProgress = Double.toString(progress) + "%";

            taskStatistics.append("  <task id='").append(mapTaskReport.getTaskID().toString()).append("'\n");
            taskStatistics.append("   <progress>").append(taskProgress).append("</progress>\n");
            taskStatistics.append("   <duration>").append(duration).append("</duration>\n");
            taskStatistics.append("   <status>").append(mapTaskReport.getCurrentStatus().toString()).append("</status>\n");
            taskStatistics.append("  </task>\n");
        }

        taskStatistics.append(" </mapTasks>\n");

        taskStatistics.append(" <reduceTasks>\n");

        for (TaskReport reduceTaskReport : reduceTaskReports) {
            taskDuration = reduceTaskReport.getFinishTime() - reduceTaskReport.getStartTime();

            if (taskDuration < 0) {
                duration = "N/A";
            } else {
                duration = String.valueOf(taskDuration);
            }

            double progress = reduceTaskReport.getProgress() * 100;
            String taskProgress = Double.toString(progress) + "%";

            taskStatistics.append("  <task id='").append(reduceTaskReport.getTaskID().toString()).append("'\n");
            taskStatistics.append("   <progress>").append(taskProgress).append("</progress>\n");
            taskStatistics.append("   <duration>").append(duration).append("</duration>\n");
            taskStatistics.append("   <status>").append(reduceTaskReport.getCurrentStatus().toString()).append("</status>\n");
            taskStatistics.append("  </task>\n");
        }

        taskStatistics.append(" </reduceTasks>\n");
        taskStatistics.append("</job>");

        return taskStatistics.toString();
    }
}