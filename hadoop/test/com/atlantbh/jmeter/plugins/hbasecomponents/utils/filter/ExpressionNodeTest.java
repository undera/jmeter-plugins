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
package com.atlantbh.jmeter.plugins.hbasecomponents.utils.filter;

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
public class ExpressionNodeTest {

    public ExpressionNodeTest() {
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
     * Test of getColumnFamily method, of class ExpressionNode.
     */
    @Test
    public void testGetColumnFamily() {
        System.out.println("getColumnFamily");
        ExpressionNode instance = new ExpressionNode();
        String expResult = "";
        String result = instance.getColumnFamily();
    }

    /**
     * Test of setColumnFamily method, of class ExpressionNode.
     */
    @Test
    public void testSetColumnFamily() {
        System.out.println("setColumnFamily");
        String columnFamily = "";
        ExpressionNode instance = new ExpressionNode();
        instance.setColumnFamily(columnFamily);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getQualifier method, of class ExpressionNode.
     */
    @Test
    public void testGetQualifier() {
        System.out.println("getQualifier");
        ExpressionNode instance = new ExpressionNode();
        String expResult = "";
        String result = instance.getQualifier();
    }

    /**
     * Test of setQualifier method, of class ExpressionNode.
     */
    @Test
    public void testSetQualifier() {
        System.out.println("setQualifier");
        String qualifier = "";
        ExpressionNode instance = new ExpressionNode();
        instance.setQualifier(qualifier);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getValue method, of class ExpressionNode.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        ExpressionNode instance = new ExpressionNode();
        String expResult = "";
        String result = instance.getValue();
    }

    /**
     * Test of setValue method, of class ExpressionNode.
     */
    @Test
    public void testSetValue() {
        System.out.println("setValue");
        String value = "";
        ExpressionNode instance = new ExpressionNode();
        instance.setValue(value);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setOper method, of class ExpressionNode.
     */
    @Test
    public void testSetOper() {
        System.out.println("setOper");
        String oper = "";
        ExpressionNode instance = new ExpressionNode();
        instance.setOper(oper);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getOper method, of class ExpressionNode.
     */
    @Test
    public void testGetOper() {
        System.out.println("getOper");
        ExpressionNode instance = new ExpressionNode();
        String expResult = "";
        String result = instance.getOper();
    }
}
