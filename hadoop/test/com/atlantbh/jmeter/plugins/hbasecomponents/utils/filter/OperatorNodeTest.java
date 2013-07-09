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

import java.util.Set;
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
public class OperatorNodeTest {
    
    public OperatorNodeTest() {
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
     * Test of getOperators method, of class OperatorNode.
     */
    @Test
    public void testGetOperators() {
        System.out.println("getOperators");
        OperatorNode instance = new OperatorNode();
        Set expResult = null;
        Set result = instance.getOperators();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of setOperators method, of class OperatorNode.
     */
    @Test
    public void testSetOperators() {
        System.out.println("setOperators");
        Set<String> operators = null;
        OperatorNode instance = new OperatorNode();
        instance.setOperators(operators);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of addOperator method, of class OperatorNode.
     */
    @Test
    public void testAddOperator() throws Exception {
        System.out.println("addOperator");
        String operator = "AND";
        OperatorNode instance = new OperatorNode();
        instance.addOperator(operator);
        // TODO review the generated test code and remove the default call to fail.
        
    }
}
