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

import com.atlantbh.jmeter.plugins.hbasecrud.HTableEmul;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTableInterface;
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
public class HBaseConnectionVariableTest {

    public HBaseConnectionVariableTest() {
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
     * Test of getConfig method, of class HBaseConnectionVariable.
     */
    @Test
    public void testGetConfig() {
        System.out.println("getConfig");
        HBaseConnectionVariable instance = new HBaseConnectionVariable();
        Configuration expResult = null;
        Configuration result = instance.getConfig();
    }

    /**
     * Test of getTable method, of class HBaseConnectionVariable.
     */
    @Test
    public void testGetTable() throws Exception {
        System.out.println("getTable");
        String tableName = "";
        HBaseConnectionVariable instance = new HBaseConnectionVariableEmul();
        HTableInterface expResult = null;
        HTableInterface result = instance.getTable(tableName);
    }

    /**
     * Test of putTable method, of class HBaseConnectionVariable.
     */
    @Test
    public void testPutTable() {
        System.out.println("putTable");
        HTableInterface table = new HTableEmul();
        HBaseConnectionVariable instance = new HBaseConnectionVariableEmul();
        instance.putTable(table);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getZkHost method, of class HBaseConnectionVariable.
     */
    @Test
    public void testGetZkHost() {
        System.out.println("getZkHost");
        HBaseConnectionVariable instance = new HBaseConnectionVariable();
        String expResult = "";
        String result = instance.getZkHost();
    }

    /**
     * Test of setZkHost method, of class HBaseConnectionVariable.
     */
    @Test
    public void testSetZkHost() {
        System.out.println("setZkHost");
        String zkHost = "";
        HBaseConnectionVariable instance = new HBaseConnectionVariable();
        instance.setZkHost(zkHost);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getName method, of class HBaseConnectionVariable.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        HBaseConnectionVariable instance = new HBaseConnectionVariable();
        String expResult = "";
        String result = instance.getName();
    }

    /**
     * Test of setName method, of class HBaseConnectionVariable.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        String name = "";
        HBaseConnectionVariable instance = new HBaseConnectionVariable();
        instance.setName(name);
        // TODO review the generated test code and remove the default call to fail.

    }
}
