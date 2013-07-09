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
package com.atlantbh.jmeter.plugins.hbasecrud;

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
public class HbaseCrudHelperTest {
    
    public HbaseCrudHelperTest() {
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
     * Test of getResponseMessage method, of class HbaseCrudHelper.
     */
    @Test
    public void testGetResponseMessage() {
        System.out.println("getResponseMessage");
        HbaseCrudHelper instance = new HbaseCrudHelper();
        String expResult = "";
        String result = instance.getResponseMessage();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of setResponseMessage method, of class HbaseCrudHelper.
     */
    @Test
    public void testSetResponseMessage() {
        System.out.println("setResponseMessage");
        String responseMessage = "";
        HbaseCrudHelper instance = new HbaseCrudHelper();
        instance.setResponseMessage(responseMessage);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of setConfiguration method, of class HbaseCrudHelper.
     */
    @Test
    public void testSetConfiguration() throws Exception {
        System.out.println("setConfiguration");
        String hbaseZK = "";
        String hbaseTable = "";
        HbaseCrudHelper instance = new HbaseCrudHelperEmul();
        instance.setConfiguration(hbaseZK, hbaseTable);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of parseColumnFamilies method, of class HbaseCrudHelper.
     */
    @Test
    public void testParseColumnFamilies() {
        System.out.println("parseColumnFamilies");
        String columnFamilies = "";
        HbaseCrudHelper instance = new HbaseCrudHelper();
        instance.parseColumnFamilies(columnFamilies);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of addOrUpdateDataToHBase method, of class HbaseCrudHelper.
     */
    @Test
    public void testAddOrUpdateDataToHBase() throws Exception {
        System.out.println("addOrUpdateDataToHBase");
        String rowKey = "";
        String columnFamilyColumnQualifiers = "";
        HbaseCrudHelper instance = new HbaseCrudHelperEmul();
        instance.addOrUpdateDataToHBase(rowKey, columnFamilyColumnQualifiers);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of deleteDataFromHbase method, of class HbaseCrudHelper.
     */
    @Test
    public void testDeleteDataFromHbase() throws Exception {
        System.out.println("deleteDataFromHbase");
        String rowKey = "";
        String columnFamilyColumnQualifiers = "";
        HbaseCrudHelper instance = new HbaseCrudHelperEmul();
        instance.deleteDataFromHbase(rowKey, columnFamilyColumnQualifiers);
        // TODO review the generated test code and remove the default call to fail.
        
    }
}
