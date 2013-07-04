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
package com.atlantbh.jmeter.plugins.jsonutils.jsonpathassertion;

import org.apache.jmeter.assertions.AssertionResult;
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
public class JSONPathAssertionTest {

    public JSONPathAssertionTest() {
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
     * Test of getJsonPath method, of class JSONPathAssertion.
     */
    @Test
    public void testGetJsonPath() {
        System.out.println("getJsonPath");
        JSONPathAssertion instance = new JSONPathAssertion();
        String expResult = "";
        String result = instance.getJsonPath();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setJsonPath method, of class JSONPathAssertion.
     */
    @Test
    public void testSetJsonPath() {
        System.out.println("setJsonPath");
        String jsonPath = "";
        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath(jsonPath);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getExpectedValue method, of class JSONPathAssertion.
     */
    @Test
    public void testGetExpectedValue() {
        System.out.println("getExpectedValue");
        JSONPathAssertion instance = new JSONPathAssertion();
        String expResult = "";
        String result = instance.getExpectedValue();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setExpectedValue method, of class JSONPathAssertion.
     */
    @Test
    public void testSetExpectedValue() {
        System.out.println("setExpectedValue");
        String expectedValue = "";
        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setExpectedValue(expectedValue);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setJsonValidationBool method, of class JSONPathAssertion.
     */
    @Test
    public void testSetJsonValidationBool() {
        System.out.println("setJsonValidationBool");
        boolean jsonValidation = false;
        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonValidationBool(jsonValidation);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of isJsonValidationBool method, of class JSONPathAssertion.
     */
    @Test
    public void testIsJsonValidationBool() {
        System.out.println("isJsonValidationBool");
        JSONPathAssertion instance = new JSONPathAssertion();
        boolean expResult = false;
        boolean result = instance.isJsonValidationBool();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getResult method, of class JSONPathAssertion.
     */
    @Test
    public void testGetResult() {
        System.out.println("getResult");
        SampleResult samplerResult = new SampleResult();
        JSONPathAssertion instance = new JSONPathAssertion();
        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        // TODO review the generated test code and remove the default call to fail.

    }
}
