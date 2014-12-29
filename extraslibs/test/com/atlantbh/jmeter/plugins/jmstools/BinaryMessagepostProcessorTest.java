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
package com.atlantbh.jmeter.plugins.jmstools;

import java.util.HashMap;
import java.util.Map;
import javax.jms.Message;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryMessagepostProcessorTest {

    public BinaryMessagepostProcessorTest() {
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
     * Test of getMessageProperties method, of class BinaryMessagepostProcessor.
     */
    @Test
    public void testGetMessageProperties() {
        System.out.println("getMessageProperties");
        BinaryMessagepostProcessor instance = new BinaryMessagepostProcessor();
        Map expResult = new HashMap();
        Map result = instance.getMessageProperties();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setMessageProperties method, of class BinaryMessagepostProcessor.
     */
    @Test
    public void testSetMessageProperties() {
        System.out.println("setMessageProperties");
        Map<String, String> messageProperties = null;
        BinaryMessagepostProcessor instance = new BinaryMessagepostProcessor();
        instance.setMessageProperties(messageProperties);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of postProcessMessage method, of class BinaryMessagepostProcessor.
     */
    @Test
    public void testPostProcessMessage() throws Exception {
        System.out.println("postProcessMessage");
        Message message = null;
        BinaryMessagepostProcessor instance = new BinaryMessagepostProcessor();
        Message expResult = null;
        Message result = instance.postProcessMessage(message);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }
}
