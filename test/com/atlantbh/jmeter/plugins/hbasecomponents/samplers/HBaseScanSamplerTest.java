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
package com.atlantbh.jmeter.plugins.hbasecomponents.samplers;

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
public class HBaseScanSamplerTest {

    public HBaseScanSamplerTest() {
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
     * Test of main method, of class HBaseScanSampler.
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        HBaseScanSampler.main(args);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of sample method, of class HBaseScanSampler.
     */
    @Test
    public void testSample() {
        System.out.println("sample");
        Entry arg0 = null;
        HBaseScanSampler instance = new HBaseScanSampler();
        SampleResult expResult = null;
        SampleResult result = instance.sample(arg0);
    }

    /**
     * Test of getConnectionName method, of class HBaseScanSampler.
     */
    @Test
    public void testGetConnectionName() {
        System.out.println("getConnectionName");
        HBaseScanSampler instance = new HBaseScanSampler();
        String expResult = "";
        String result = instance.getConnectionName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setConnectionName method, of class HBaseScanSampler.
     */
    @Test
    public void testSetConnectionName() {
        System.out.println("setConnectionName");
        String value = "";
        HBaseScanSampler instance = new HBaseScanSampler();
        instance.setConnectionName(value);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getTableName method, of class HBaseScanSampler.
     */
    @Test
    public void testGetTableName() {
        System.out.println("getTableName");
        HBaseScanSampler instance = new HBaseScanSampler();
        String expResult = "";
        String result = instance.getTableName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setTableName method, of class HBaseScanSampler.
     */
    @Test
    public void testSetTableName() {
        System.out.println("setTableName");
        String value = "";
        HBaseScanSampler instance = new HBaseScanSampler();
        instance.setTableName(value);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getStartKey method, of class HBaseScanSampler.
     */
    @Test
    public void testGetStartKey() {
        System.out.println("getStartKey");
        HBaseScanSampler instance = new HBaseScanSampler();
        String expResult = "";
        String result = instance.getStartKey();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setStartKey method, of class HBaseScanSampler.
     */
    @Test
    public void testSetStartKey() {
        System.out.println("setStartKey");
        String value = "";
        HBaseScanSampler instance = new HBaseScanSampler();
        instance.setStartKey(value);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getEndKey method, of class HBaseScanSampler.
     */
    @Test
    public void testGetEndKey() {
        System.out.println("getEndKey");
        HBaseScanSampler instance = new HBaseScanSampler();
        String expResult = "";
        String result = instance.getEndKey();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setEndKey method, of class HBaseScanSampler.
     */
    @Test
    public void testSetEndKey() {
        System.out.println("setEndKey");
        String value = "";
        HBaseScanSampler instance = new HBaseScanSampler();
        instance.setEndKey(value);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getLimit method, of class HBaseScanSampler.
     */
    @Test
    public void testGetLimit() {
        System.out.println("getLimit");
        HBaseScanSampler instance = new HBaseScanSampler();
        String expResult = "";
        String result = instance.getLimit();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setLimit method, of class HBaseScanSampler.
     */
    @Test
    public void testSetLimit() {
        System.out.println("setLimit");
        String value = "";
        HBaseScanSampler instance = new HBaseScanSampler();
        instance.setLimit(value);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getFilter method, of class HBaseScanSampler.
     */
    @Test
    public void testGetFilter() {
        System.out.println("getFilter");
        HBaseScanSampler instance = new HBaseScanSampler();
        String expResult = "";
        String result = instance.getFilter();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setFilter method, of class HBaseScanSampler.
     */
    @Test
    public void testSetFilter() {
        System.out.println("setFilter");
        String value = "";
        HBaseScanSampler instance = new HBaseScanSampler();
        instance.setFilter(value);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getPassAll method, of class HBaseScanSampler.
     */
    @Test
    public void testGetPassAll() {
        System.out.println("getPassAll");
        HBaseScanSampler instance = new HBaseScanSampler();
        Boolean expResult = false;
        Boolean result = instance.getPassAll();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setPassAll method, of class HBaseScanSampler.
     */
    @Test
    public void testSetPassAll() {
        System.out.println("setPassAll");
        Boolean value = false;
        HBaseScanSampler instance = new HBaseScanSampler();
        instance.setPassAll(value);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getOmitVars method, of class HBaseScanSampler.
     */
    @Test
    public void testGetOmitVars() {
        System.out.println("getOmitVars");
        HBaseScanSampler instance = new HBaseScanSampler();
        Boolean expResult = false;
        Boolean result = instance.getOmitVars();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setOmitVars method, of class HBaseScanSampler.
     */
    @Test
    public void testSetOmitVars() {
        System.out.println("setOmitVars");
        Boolean value = false;
        HBaseScanSampler instance = new HBaseScanSampler();
        instance.setOmitVars(value);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getLatestTimestampRows method, of class HBaseScanSampler.
     */
    @Test
    public void testGetLatestTimestampRows() {
        System.out.println("getLatestTimestampRows");
        HBaseScanSampler instance = new HBaseScanSampler();
        Boolean expResult = false;
        Boolean result = instance.getLatestTimestampRows();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setLatestTimestampRows method, of class HBaseScanSampler.
     */
    @Test
    public void testSetLatestTimestampRows() {
        System.out.println("setLatestTimestampRows");
        Boolean value = false;
        HBaseScanSampler instance = new HBaseScanSampler();
        instance.setLatestTimestampRows(value);
        // TODO review the generated test code and remove the default call to fail.

    }
}
