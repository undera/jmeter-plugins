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
package com.atlantbh.jmeter.plugins.jsonutils.jsonpathextractor;

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
public class JSONPathExtractorTest {
    
    public JSONPathExtractorTest() {
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
     * Test of getJsonPath method, of class JSONPathExtractor.
     */
    @Test
    public void testGetJsonPath() {
        System.out.println("getJsonPath");
        JSONPathExtractor instance = new JSONPathExtractor();
        String expResult = "";
        String result = instance.getJsonPath();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of setJsonPath method, of class JSONPathExtractor.
     */
    @Test
    public void testSetJsonPath() {
        System.out.println("setJsonPath");
        String jsonPath = "";
        JSONPathExtractor instance = new JSONPathExtractor();
        instance.setJsonPath(jsonPath);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getVar method, of class JSONPathExtractor.
     */
    @Test
    public void testGetVar() {
        System.out.println("getVar");
        JSONPathExtractor instance = new JSONPathExtractor();
        String expResult = "";
        String result = instance.getVar();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of setVar method, of class JSONPathExtractor.
     */
    @Test
    public void testSetVar() {
        System.out.println("setVar");
        String var = "";
        JSONPathExtractor instance = new JSONPathExtractor();
        instance.setVar(var);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of extractJSONPath method, of class JSONPathExtractor.
     */
    @Test
    public void testExtractJSONPath() throws Exception {
        System.out.println("extractJSONPath");
        String jsonString = "[]";
        String jsonPath = ".0";
        JSONPathExtractor instance = new JSONPathExtractor();
        String expResult = "NULL";
        String result = instance.extractJSONPath(jsonString, jsonPath);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of process method, of class JSONPathExtractor.
     */
    @Test
    public void testProcess() {
        System.out.println("process");
        JSONPathExtractor instance = new JSONPathExtractor();
        instance.process();
        // TODO review the generated test code and remove the default call to fail.
        
    }
}
