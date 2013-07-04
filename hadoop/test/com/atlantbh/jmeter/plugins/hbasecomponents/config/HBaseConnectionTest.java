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
package com.atlantbh.jmeter.plugins.hbasecomponents.config;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.junit.*;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author undera
 */
@FixMethodOrder(MethodSorters.JVM)
public class HBaseConnectionTest {
    
    public HBaseConnectionTest() {
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
     * Test of getConnection method, of class HBaseConnection.
     */
    @Test
    public void testGetConnection() {
        System.out.println("getConnection");
        String name = "";
        HBaseConnectionVariable expResult = null;
        HBaseConnectionVariable result = HBaseConnection.getConnection(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getZkHost method, of class HBaseConnection.
     */
    @Test
    public void testGetZkHost() {
        System.out.println("getZkHost");
        HBaseConnection instance = new HBaseConnection();
        String expResult = "";
        String result = instance.getZkHost();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of setZkHost method, of class HBaseConnection.
     */
    @Test
    public void testSetZkHost() {
        System.out.println("setZkHost");
        String zkHost = "";
        HBaseConnection instance = new HBaseConnection();
        instance.setZkHost(zkHost);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getZkName method, of class HBaseConnection.
     */
    @Test
    public void testGetZkName() {
        System.out.println("getZkName");
        HBaseConnection instance = new HBaseConnection();
        String expResult = "";
        String result = instance.getZkName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of setZkName method, of class HBaseConnection.
     */
    @Test
    public void testSetZkName() {
        System.out.println("setZkName");
        String zkName = "";
        HBaseConnection instance = new HBaseConnection();
        instance.setZkName(zkName);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of testStarted method, of class HBaseConnection.
     */
    @Test
    public void testTestStarted_String() {
        System.out.println("testStarted");
        String s = "";
        HBaseConnection instance = new HBaseConnection();
        instance.testStarted(s);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of testStarted method, of class HBaseConnection.
     */
    @Test
    public void testTestStarted_0args() {
        System.out.println("testStarted");
        HBaseConnection instance = new HBaseConnection();
        instance.testStarted();
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of testEnded method, of class HBaseConnection.
     */
    @Test
    public void testTestEnded_String() {
        System.out.println("testEnded");
        String s = "";
        HBaseConnection instance = new HBaseConnection();
        instance.testEnded(s);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of testEnded method, of class HBaseConnection.
     */
    @Test
    public void testTestEnded_0args() {
        System.out.println("testEnded");
        HBaseConnection instance = new HBaseConnection();
        instance.testEnded();
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of testIterationStart method, of class HBaseConnection.
     */
    @Test
    public void testTestIterationStart() {
        System.out.println("testIterationStart");
        LoopIterationEvent event = null;
        HBaseConnection instance = new HBaseConnection();
        instance.testIterationStart(event);
        // TODO review the generated test code and remove the default call to fail.
        
    }
}
