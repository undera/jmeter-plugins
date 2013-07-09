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
public class HBaseRowkeySamplerTest {

    public HBaseRowkeySamplerTest() {
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
     * Test of sample method, of class HBaseRowkeySampler.
     */
    @Test
    public void testSample() {
        System.out.println("sample");
        Entry entry = null;
        HBaseRowkeySampler instance = new HBaseRowkeySampler();
        SampleResult expResult = null;
        SampleResult result = instance.sample(entry);
    }

    /**
     * Test of getConnectionName method, of class HBaseRowkeySampler.
     */
    @Test
    public void testGetConnectionName() {
        System.out.println("getConnectionName");
        HBaseRowkeySampler instance = new HBaseRowkeySampler();
        String expResult = "";
        String result = instance.getConnectionName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setConnectionName method, of class HBaseRowkeySampler.
     */
    @Test
    public void testSetConnectionName() {
        System.out.println("setConnectionName");
        String connectionName = "";
        HBaseRowkeySampler instance = new HBaseRowkeySampler();
        instance.setConnectionName(connectionName);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of gethBaseTable method, of class HBaseRowkeySampler.
     */
    @Test
    public void testGethBaseTable() {
        System.out.println("gethBaseTable");
        HBaseRowkeySampler instance = new HBaseRowkeySampler();
        String expResult = "";
        String result = instance.gethBaseTable();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of sethBaseTable method, of class HBaseRowkeySampler.
     */
    @Test
    public void testSethBaseTable() {
        System.out.println("sethBaseTable");
        String hBaseTable = "";
        HBaseRowkeySampler instance = new HBaseRowkeySampler();
        instance.sethBaseTable(hBaseTable);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getRowKey method, of class HBaseRowkeySampler.
     */
    @Test
    public void testGetRowKey() {
        System.out.println("getRowKey");
        HBaseRowkeySampler instance = new HBaseRowkeySampler();
        String expResult = "";
        String result = instance.getRowKey();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setRowKey method, of class HBaseRowkeySampler.
     */
    @Test
    public void testSetRowKey() {
        System.out.println("setRowKey");
        String rowKey = "";
        HBaseRowkeySampler instance = new HBaseRowkeySampler();
        instance.setRowKey(rowKey);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getOmitVars method, of class HBaseRowkeySampler.
     */
    @Test
    public void testGetOmitVars() {
        System.out.println("getOmitVars");
        HBaseRowkeySampler instance = new HBaseRowkeySampler();
        Boolean expResult = false;
        Boolean result = instance.getOmitVars();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setOmitVars method, of class HBaseRowkeySampler.
     */
    @Test
    public void testSetOmitVars() {
        System.out.println("setOmitVars");
        Boolean value = false;
        HBaseRowkeySampler instance = new HBaseRowkeySampler();
        instance.setOmitVars(value);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getLatestTimestampRows method, of class HBaseRowkeySampler.
     */
    @Test
    public void testGetLatestTimestampRows() {
        System.out.println("getLatestTimestampRows");
        HBaseRowkeySampler instance = new HBaseRowkeySampler();
        Boolean expResult = false;
        Boolean result = instance.getLatestTimestampRows();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setLatestTimestampRows method, of class HBaseRowkeySampler.
     */
    @Test
    public void testSetLatestTimestampRows() {
        System.out.println("setLatestTimestampRows");
        Boolean value = false;
        HBaseRowkeySampler instance = new HBaseRowkeySampler();
        instance.setLatestTimestampRows(value);
        // TODO review the generated test code and remove the default call to fail.

    }
}
