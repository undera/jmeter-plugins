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
package com.atlantbh.jmeter.plugins.oauth;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParameterTest {

    public ParameterTest() {
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
     * Test of getKey method, of class Parameter.
     */
    @Test
    public void testGetKey() {
        System.out.println("getKey");
        Parameter instance = new Parameter("key", "value");
        String expResult = "key";
        String result = instance.getKey();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getValue method, of class Parameter.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        Parameter instance = new Parameter("key", "value");
        String expResult = "value";
        String result = instance.getValue();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setValue method, of class Parameter.
     */
    @Test
    public void testSetValue() {
        System.out.println("setValue");
        String value = "";
        Parameter instance = new Parameter("key", "value");
        String expResult = "value";
        String result = instance.setValue(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of toString method, of class Parameter.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Parameter instance = new Parameter("key", "value");
        String expResult = "key=value";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of compareTo method, of class Parameter.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        Parameter other = new Parameter("key", "value");
        Parameter instance = new Parameter("key", "value");
        int expResult = 0;
        int result = instance.compareTo(other);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of percentEncode method, of class Parameter.
     */
    @Test
    public void testPercentEncode() {
        System.out.println("percentEncode");
        String value = "";
        String expResult = "";
        String result = Parameter.percentEncode(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }
}
