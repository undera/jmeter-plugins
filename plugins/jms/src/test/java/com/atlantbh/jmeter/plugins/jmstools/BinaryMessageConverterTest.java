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

import java.util.Map;
import javax.jms.Message;
import javax.jms.Session;

import com.atlantbh.jmeter.plugins.jmstools.BinaryMessageConverter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryMessageConverterTest {

    public BinaryMessageConverterTest() {
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
     * Test of getMessageProperties method, of class BinaryMessageConverter.
     */
    @Test
    public void testGetMessageProperties() {
        System.out.println("getMessageProperties");
        BinaryMessageConverter instance = new BinaryMessageConverter();
        Map expResult = null;
        Map result = instance.getMessageProperties();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setMessageProperties method, of class BinaryMessageConverter.
     */
    @Test
    public void testSetMessageProperties() {
        System.out.println("setMessageProperties");
        Map<String, String> messageProperties = null;
        BinaryMessageConverter instance = new BinaryMessageConverter();
        instance.setMessageProperties(messageProperties);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of fromMessage method, of class BinaryMessageConverter.
     */
    @Test
    public void testFromMessage() throws Exception {
        System.out.println("fromMessage");
        Message message = new BytesMessageEmul();
        BinaryMessageConverter instance = new BinaryMessageConverter();
        Object expResult = null;
        Object result = instance.fromMessage(message);

    }

    /**
     * Test of toMessage method, of class BinaryMessageConverter.
     */
    @Test
    public void testToMessage() throws Exception {
        System.out.println("toMessage");
        Object arg0 = "test";
        Session session = new SessionEmul();
        BinaryMessageConverter instance = new BinaryMessageConverter();
        Message expResult = null;
        Message result = instance.toMessage(arg0, session);
    }
}
