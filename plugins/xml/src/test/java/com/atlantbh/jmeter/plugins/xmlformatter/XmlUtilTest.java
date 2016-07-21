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
package com.atlantbh.jmeter.plugins.xmlformatter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;

public class XmlUtilTest {

    public XmlUtilTest() {
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
     * Test of stringToXml method, of class XmlUtil.
     */
    @Test
    public void testStringToXml() throws Exception {
        System.out.println("stringToXml");
        String string = "<root/>";
        Document expResult = null;
        Document result = XmlUtil.stringToXml(string);
    }

    /**
     * Test of xmlToString method, of class XmlUtil.
     */
    @Test
    public void testXmlToString() throws Exception {
        System.out.println("xmlToString");
        Document document = null;
        String expResult = "";
        String result = XmlUtil.xmlToString(document);
    }
}
