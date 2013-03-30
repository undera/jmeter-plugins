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
package com.atlantbh.jmeter.plugins.hbasecomponents.utils;

import org.apache.hadoop.hbase.client.Result;
import org.apache.jmeter.threads.JMeterVariables;
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
public class Row2XMLTest {
    
    public Row2XMLTest() {
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
     * Test of row2xmlstring method, of class Row2XML.
     */
    @Test
    public void testRow2xmlstring() throws Exception {
        System.out.println("row2xmlstring");
        Result result_2 = null;
        JMeterVariables vars = null;
        int index = 0;
        String expResult = "";
        String result = Row2XML.row2xmlstring(result_2, vars, index);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of row2xmlStringLatest method, of class Row2XML.
     */
    @Test
    public void testRow2xmlStringLatest() {
        System.out.println("row2xmlStringLatest");
        Result result_2 = null;
        JMeterVariables vars = null;
        int index = 0;
        String expResult = "";
        String result = Row2XML.row2xmlStringLatest(result_2, vars, index);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }
}
