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

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {
    
    public NodeTest() {
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
     * Test of setChilds method, of class Node.
     */
    @Test
    public void testSetChilds() {
        System.out.println("setChilds");
        List<Node> childs = null;
        Node instance = new NodeImpl();
        instance.setChilds(childs);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getChilds method, of class Node.
     */
    @Test
    public void testGetChilds() {
        System.out.println("getChilds");
        Node instance = new NodeImpl();
        List expResult = null;
        List result = instance.getChilds();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of addChild method, of class Node.
     */
    @Test
    public void testAddChild() {
        System.out.println("addChild");
        Node child = null;
        Node instance = new NodeImpl();
        instance.addChild(child);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    public class NodeImpl extends Node {
    }
}
