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

import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class HbaseCrudTest {

    public HbaseCrudTest() {
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
     * Test of setHbaseZookeeperQuorum method, of class HbaseCrud.
     */
    @Test
    public void testSetHbaseZookeeperQuorum() {
        System.out.println("setHbaseZookeeperQuorum");
        String hbaseZookeeperQuorum = "";
        HbaseCrud instance = new HbaseCrud();
        instance.setHbaseZookeeperQuorum(hbaseZookeeperQuorum);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getHbaseZookeeperQuorum method, of class HbaseCrud.
     */
    @Test
    public void testGetHbaseZookeeperQuorum() {
        System.out.println("getHbaseZookeeperQuorum");
        HbaseCrud instance = new HbaseCrud();
        String expResult = "";
        String result = instance.getHbaseZookeeperQuorum();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setHbaseSourceTable method, of class HbaseCrud.
     */
    @Test
    public void testSetHbaseSourceTable() {
        System.out.println("setHbaseSourceTable");
        String hbaseSourceTable = "";
        HbaseCrud instance = new HbaseCrud();
        instance.setHbaseSourceTable(hbaseSourceTable);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getHbaseSourceTable method, of class HbaseCrud.
     */
    @Test
    public void testGetHbaseSourceTable() {
        System.out.println("getHbaseSourceTable");
        HbaseCrud instance = new HbaseCrud();
        String expResult = "";
        String result = instance.getHbaseSourceTable();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setRowKey method, of class HbaseCrud.
     */
    @Test
    public void testSetRowKey() {
        System.out.println("setRowKey");
        String rowKey = "";
        HbaseCrud instance = new HbaseCrud();
        instance.setRowKey(rowKey);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getRowKey method, of class HbaseCrud.
     */
    @Test
    public void testGetRowKey() {
        System.out.println("getRowKey");
        HbaseCrud instance = new HbaseCrud();
        String expResult = "";
        String result = instance.getRowKey();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setFullColumnNamesList method, of class HbaseCrud.
     */
    @Test
    public void testSetFullColumnNamesList() {
        System.out.println("setFullColumnNamesList");
        String fullColumnNamesList = "";
        HbaseCrud instance = new HbaseCrud();
        instance.setFullColumnNamesList(fullColumnNamesList);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getFullColumnNamesList method, of class HbaseCrud.
     */
    @Test
    public void testGetFullColumnNamesList() {
        System.out.println("getFullColumnNamesList");
        HbaseCrud instance = new HbaseCrud();
        String expResult = "";
        String result = instance.getFullColumnNamesList();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setColumnFamilyColumnNameList method, of class HbaseCrud.
     */
    @Test
    public void testSetColumnFamilyColumnNameList() {
        System.out.println("setColumnFamilyColumnNameList");
        String columnFamilyColumnNameList = "";
        HbaseCrud instance = new HbaseCrud();
        instance.setColumnFamilyColumnNameList(columnFamilyColumnNameList);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getColumnFamilyColumnNameList method, of class HbaseCrud.
     */
    @Test
    public void testGetColumnFamilyColumnNameList() {
        System.out.println("getColumnFamilyColumnNameList");
        HbaseCrud instance = new HbaseCrud();
        String expResult = "";
        String result = instance.getColumnFamilyColumnNameList();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setFilterColumnFamiliesForTimestamp method, of class HbaseCrud.
     */
    @Test
    public void testSetFilterColumnFamiliesForTimestamp() {
        System.out.println("setFilterColumnFamiliesForTimestamp");
        String filterColumnFamiliesForTimestamp = "";
        HbaseCrud instance = new HbaseCrud();
        instance.setFilterColumnFamiliesForTimestamp(filterColumnFamiliesForTimestamp);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getFilterColumnFamiliesForTimestamp method, of class HbaseCrud.
     */
    @Test
    public void testGetFilterColumnFamiliesForTimestamp() {
        System.out.println("getFilterColumnFamiliesForTimestamp");
        HbaseCrud instance = new HbaseCrud();
        String expResult = "";
        String result = instance.getFilterColumnFamiliesForTimestamp();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setAddOrUpdateDataOnRecordBool method, of class HbaseCrud.
     */
    @Test
    public void testSetAddOrUpdateDataOnRecordBool() {
        System.out.println("setAddOrUpdateDataOnRecordBool");
        boolean addOrUpdateDataOnRecord = false;
        HbaseCrud instance = new HbaseCrud();
        instance.setAddOrUpdateDataOnRecordBool(addOrUpdateDataOnRecord);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of isAddOrUpdateDataOnRecordBool method, of class HbaseCrud.
     */
    @Test
    public void testIsAddOrUpdateDataOnRecordBool() {
        System.out.println("isAddOrUpdateDataOnRecordBool");
        HbaseCrud instance = new HbaseCrud();
        boolean expResult = false;
        boolean result = instance.isAddOrUpdateDataOnRecordBool();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setDeleteDataFromRecordBool method, of class HbaseCrud.
     */
    @Test
    public void testSetDeleteDataFromRecordBool() {
        System.out.println("setDeleteDataFromRecordBool");
        boolean deleteDataFromRecord = false;
        HbaseCrud instance = new HbaseCrud();
        instance.setDeleteDataFromRecordBool(deleteDataFromRecord);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of isDeleteDataFromRecordBool method, of class HbaseCrud.
     */
    @Test
    public void testIsDeleteDataFromRecordBool() {
        System.out.println("isDeleteDataFromRecordBool");
        HbaseCrud instance = new HbaseCrud();
        boolean expResult = false;
        boolean result = instance.isDeleteDataFromRecordBool();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setLatestTimestampOperationBool method, of class HbaseCrud.
     */
    @Test
    public void testSetLatestTimestampOperationBool() {
        System.out.println("setLatestTimestampOperationBool");
        boolean latestTimestampOperation = false;
        HbaseCrud instance = new HbaseCrud();
        instance.setLatestTimestampOperationBool(latestTimestampOperation);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of isLatestTimestampOperationBool method, of class HbaseCrud.
     */
    @Test
    public void testIsLatestTimestampOperationBool() {
        System.out.println("isLatestTimestampOperationBool");
        HbaseCrud instance = new HbaseCrud();
        boolean expResult = false;
        boolean result = instance.isLatestTimestampOperationBool();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setLatestTimestampOperationWithFilterBool method, of class
     * HbaseCrud.
     */
    @Test
    public void testSetLatestTimestampOperationWithFilterBool() {
        System.out.println("setLatestTimestampOperationWithFilterBool");
        boolean latestTimestampOperationWithFilter = false;
        HbaseCrud instance = new HbaseCrud();
        instance.setLatestTimestampOperationWithFilterBool(latestTimestampOperationWithFilter);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of isLatestTimestampOperationWithFilterBool method, of class
     * HbaseCrud.
     */
    @Test
    public void testIsLatestTimestampOperationWithFilterBool() {
        System.out.println("isLatestTimestampOperationWithFilterBool");
        HbaseCrud instance = new HbaseCrud();
        boolean expResult = false;
        boolean result = instance.isLatestTimestampOperationWithFilterBool();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of sample method, of class HbaseCrud.
     */
    @Test
    public void testSample() {
        System.out.println("sample");
        Entry arg0 = null;
        HbaseCrud instance = new HbaseCrud();
        SampleResult expResult = null;
        SampleResult result = instance.sample(arg0);
    }
}
