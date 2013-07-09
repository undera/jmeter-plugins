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

import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobID;
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
public class JobLayerTest {

    public JobLayerTest() {
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
     * Test of getJobId method, of class JobLayer.
     */
    @Test
    public void testGetJobId() {
        System.out.println("getJobId");
        JobLayer instance = new JobLayer();
        String expResult = "";
        String result = instance.getJobId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setJobId method, of class JobLayer.
     */
    @Test
    public void testSetJobId() {
        System.out.println("setJobId");
        String jobId = "";
        JobLayer instance = new JobLayer();
        instance.setJobId(jobId);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getJobTracker method, of class JobLayer.
     */
    @Test
    public void testGetJobTracker() {
        System.out.println("getJobTracker");
        JobLayer instance = new JobLayer();
        String expResult = "";
        String result = instance.getJobTracker();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setJobTracker method, of class JobLayer.
     */
    @Test
    public void testSetJobTracker() {
        System.out.println("setJobTracker");
        String jobTracker = "";
        JobLayer instance = new JobLayer();
        instance.setJobTracker(jobTracker);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getJobState method, of class JobLayer.
     */
    @Test
    public void testGetJobState() {
        System.out.println("getJobState");
        JobLayer instance = new JobLayer();
        String expResult = "";
        String result = instance.getJobState();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setJobState method, of class JobLayer.
     */
    @Test
    public void testSetJobState() {
        System.out.println("setJobState");
        String jobState = "";
        JobLayer instance = new JobLayer();
        instance.setJobState(jobState);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getGroupName method, of class JobLayer.
     */
    @Test
    public void testGetGroupName() {
        System.out.println("getGroupName");
        JobLayer instance = new JobLayer();
        String expResult = "";
        String result = instance.getGroupName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setGroupName method, of class JobLayer.
     */
    @Test
    public void testSetGroupName() {
        System.out.println("setGroupName");
        String groupName = "";
        JobLayer instance = new JobLayer();
        instance.setGroupName(groupName);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of prepareJobClient method, of class JobLayer.
     */
    @Test
    public void testPrepareJobClient() throws Exception {
        System.out.println("prepareJobClient");
        String jobTracker = "localhost:0";
        JobLayer instance = new JobLayerEmul();
        JobClient expResult = null;
        JobClient result = instance.prepareJobClient(jobTracker);
    }

    /**
     * Test of convertToJobId method, of class JobLayer.
     */
    @Test
    public void testConvertToJobId() {
        System.out.println("convertToJobId");
        String jobId = "0_0";
        JobLayer instance = new JobLayer();
        JobID result = instance.convertToJobId(jobId);
    }

    /**
     * Test of getCountersAsXml method, of class JobLayer.
     */
    @Test
    public void testGetCountersAsXml() throws Exception {
        System.out.println("getCountersAsXml");
        Map<String, String> jobCounters = new HashMap<String, String>();
        JobLayer instance = new JobLayer();
        String expResult = "";
        String result = instance.getCountersAsXml(jobCounters);
    }

    /**
     * Test of getJobCountersByJobId method, of class JobLayer.
     */
    @Test
    public void testGetJobCountersByJobId() throws Exception {
        System.out.println("getJobCountersByJobId");
        String jobTracker = "";
        String jobId = "0_0";
        JobLayer instance = new JobLayerEmul();
        Map expResult = new HashMap();
        Map result = instance.getJobCountersByJobId(jobTracker, jobId);
        assertEquals(expResult, result);
    }

    /**
     * Test of getJobCountersByJobIdAndGroupName method, of class JobLayer.
     */
    @Test
    public void testGetJobCountersByJobIdAndGroupName() throws Exception {
        System.out.println("getJobCountersByJobIdAndGroupName");
        String jobTracker = "";
        String jobId = "0_0";
        String groupName = "";
        JobLayer instance = new JobLayerEmul();
        Map expResult = new HashMap();
        Map result = instance.getJobCountersByJobIdAndGroupName(jobTracker, jobId, groupName);
        assertEquals(expResult, result);
    }

    /**
     * Test of getJobStatisticsByJobId method, of class JobLayer.
     */
    @Test
    public void testGetJobStatisticsByJobId() throws Exception {
        System.out.println("getJobStatisticsByJobId");
        String jobTracker = "";
        String jobId = "0_0";
        JobLayer instance = new JobLayerEmul();
        String result = instance.getJobStatisticsByJobId(jobTracker, jobId);
    }
}
