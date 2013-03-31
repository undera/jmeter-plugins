/*
 * Copyright 2013 undera.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.atlantbh.jmeter.plugins.hadooputilities.jobstatistics;

import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author undera
 */
public class JobStatisticsTest {

    public JobStatisticsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setJobTracker method, of class JobStatistics.
     */
    @Test
    public void testSetJobTracker() {
        System.out.println("setJobTracker");
        String jobTracker = "";
        JobStatistics instance = new JobStatistics();
        instance.setJobTracker(jobTracker);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getJobTracker method, of class JobStatistics.
     */
    @Test
    public void testGetJobTracker() {
        System.out.println("getJobTracker");
        JobStatistics instance = new JobStatistics();
        String expResult = "";
        String result = instance.getJobTracker();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setJobId method, of class JobStatistics.
     */
    @Test
    public void testSetJobId() {
        System.out.println("setJobId");
        String jobId = "";
        JobStatistics instance = new JobStatistics();
        instance.setJobId(jobId);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getJobId method, of class JobStatistics.
     */
    @Test
    public void testGetJobId() {
        System.out.println("getJobId");
        JobStatistics instance = new JobStatistics();
        String expResult = "";
        String result = instance.getJobId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setJobIdAndGroup method, of class JobStatistics.
     */
    @Test
    public void testSetJobIdAndGroup() {
        System.out.println("setJobIdAndGroup");
        String jobIdAndGroup = "";
        JobStatistics instance = new JobStatistics();
        instance.setJobIdAndGroup(jobIdAndGroup);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getJobIdAndGroup method, of class JobStatistics.
     */
    @Test
    public void testGetJobIdAndGroup() {
        System.out.println("getJobIdAndGroup");
        JobStatistics instance = new JobStatistics();
        String expResult = "";
        String result = instance.getJobIdAndGroup();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setJobCountersByIdBool method, of class JobStatistics.
     */
    @Test
    public void testSetJobCountersByIdBool() {
        System.out.println("setJobCountersByIdBool");
        boolean jobCountersById = false;
        JobStatistics instance = new JobStatistics();
        instance.setJobCountersByIdBool(jobCountersById);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of isJobCountersByIdBool method, of class JobStatistics.
     */
    @Test
    public void testIsJobCountersByIdBool() {
        System.out.println("isJobCountersByIdBool");
        JobStatistics instance = new JobStatistics();
        boolean expResult = false;
        boolean result = instance.isJobCountersByIdBool();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setJobCountersByIdAndGroupBool method, of class JobStatistics.
     */
    @Test
    public void testSetJobCountersByIdAndGroupBool() {
        System.out.println("setJobCountersByIdAndGroupBool");
        boolean jobCountersByIdAndGroup = false;
        JobStatistics instance = new JobStatistics();
        instance.setJobCountersByIdAndGroupBool(jobCountersByIdAndGroup);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of isJobCountersByIdAndGroupBool method, of class JobStatistics.
     */
    @Test
    public void testIsJobCountersByIdAndGroupBool() {
        System.out.println("isJobCountersByIdAndGroupBool");
        JobStatistics instance = new JobStatistics();
        boolean expResult = false;
        boolean result = instance.isJobCountersByIdAndGroupBool();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setJobStatisticsByIdBool method, of class JobStatistics.
     */
    @Test
    public void testSetJobStatisticsByIdBool() {
        System.out.println("setJobStatisticsByIdBool");
        boolean jobStatistics = false;
        JobStatistics instance = new JobStatistics();
        instance.setJobStatisticsByIdBool(jobStatistics);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of isJobStatisticsByIdBool method, of class JobStatistics.
     */
    @Test
    public void testIsJobStatisticsByIdBool() {
        System.out.println("isJobStatisticsByIdBool");
        JobStatistics instance = new JobStatistics();
        boolean expResult = false;
        boolean result = instance.isJobStatisticsByIdBool();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setTaskCountersByIdBool method, of class JobStatistics.
     */
    @Test
    public void testSetTaskCountersByIdBool() {
        System.out.println("setTaskCountersByIdBool");
        boolean taskCountersById = false;
        JobStatistics instance = new JobStatistics();
        instance.setTaskCountersByIdBool(taskCountersById);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of isTaskCountersByIdBool method, of class JobStatistics.
     */
    @Test
    public void testIsTaskCountersByIdBool() {
        System.out.println("isTaskCountersByIdBool");
        JobStatistics instance = new JobStatistics();
        boolean expResult = false;
        boolean result = instance.isTaskCountersByIdBool();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setTaskStatisticsByIdBool method, of class JobStatistics.
     */
    @Test
    public void testSetTaskStatisticsByIdBool() {
        System.out.println("setTaskStatisticsByIdBool");
        boolean taskStatisticsById = false;
        JobStatistics instance = new JobStatistics();
        instance.setTaskStatisticsByIdBool(taskStatisticsById);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of isTaskStatisticsByIdBool method, of class JobStatistics.
     */
    @Test
    public void testIsTaskStatisticsByIdBool() {
        System.out.println("isTaskStatisticsByIdBool");
        JobStatistics instance = new JobStatistics();
        boolean expResult = false;
        boolean result = instance.isTaskStatisticsByIdBool();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of sample method, of class JobStatistics.
     */
    @Test
    public void testSample() {
        System.out.println("sample");
        Entry arg0 = null;
        JobStatistics instance = new JobStatistics();
        SampleResult expResult = null;
        SampleResult result = instance.sample(arg0);
    }
}
