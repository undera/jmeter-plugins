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
package com.atlantbh.jmeter.plugins.jsontoxmlconverter;

import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSONToXMLConverterTest {

    public JSONToXMLConverterTest() {
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
     * Test of setJsonInput method, of class JSONToXMLConverter.
     */
    @Test
    public void testSetJsonInput() {
        System.out.println("setJsonInput");
        String jsonInput = "";
        JSONToXMLConverter instance = new JSONToXMLConverter();
        instance.setJsonInput(jsonInput);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getJsonInput method, of class JSONToXMLConverter.
     */
    @Test
    public void testGetJsonInput() {
        System.out.println("getJsonInput");
        JSONToXMLConverter instance = new JSONToXMLConverter();
        String expResult = "";
        String result = instance.getJsonInput();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setXmlOutput method, of class JSONToXMLConverter.
     */
    @Test
    public void testSetXmlOutput() {
        System.out.println("setXmlOutput");
        String xmlOutput = "";
        JSONToXMLConverter instance = new JSONToXMLConverter();
        instance.setXmlOutput(xmlOutput);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getXmlOutput method, of class JSONToXMLConverter.
     */
    @Test
    public void testGetXmlOutput() {
        System.out.println("getXmlOutput");
        JSONToXMLConverter instance = new JSONToXMLConverter();
        String expResult = "";
        String result = instance.getXmlOutput();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of sample method, of class JSONToXMLConverter.
     */
    @Test
    public void testSample() {
        System.out.println("sample");
        Entry e = null;
        JSONToXMLConverter instance = new JSONToXMLConverter();
        SampleResult expResult = null;
        SampleResult result = instance.sample(e);
    }
}
