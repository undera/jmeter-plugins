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
public class TaskLayerTest {
    
    public TaskLayerTest() {
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
     * Test of getTaskLevelCountersByJobId method, of class TaskLayer.
     */
    @Test
    public void testGetTaskLevelCountersByJobId() throws Exception {
        System.out.println("getTaskLevelCountersByJobId");
        String jobTracker = "";
        String jobId = "";
        TaskLayer instance = new TaskLayer();
        String expResult = "";
        String result = instance.getTaskLevelCountersByJobId(jobTracker, jobId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getTaskStatisticsByJobId method, of class TaskLayer.
     */
    @Test
    public void testGetTaskStatisticsByJobId() throws Exception {
        System.out.println("getTaskStatisticsByJobId");
        String jobTracker = "";
        String jobId = "";
        TaskLayer instance = new TaskLayer();
        String expResult = "";
        String result = instance.getTaskStatisticsByJobId(jobTracker, jobId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }
}
