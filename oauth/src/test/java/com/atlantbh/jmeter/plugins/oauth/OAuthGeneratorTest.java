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

public class OAuthGeneratorTest {

    public OAuthGeneratorTest() {
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
     * Test of getInstance method, of class OAuthGenerator.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        String consumerKey = "";
        String consumerSecret = "";
        OAuthGenerator expResult = null;
        OAuthGenerator result = OAuthGenerator.getInstance(consumerKey, consumerSecret);
    }

    /**
     * Test of getAuthorization method, of class OAuthGenerator.
     */
    @Test
    public void testGetAuthorization() {
        System.out.println("getAuthorization");
        String url = "http://localhost/";
        String method = "";
        OAuthGenerator instance = OAuthGenerator.getInstance("http://localhost/", "");
        String expResult = "";
        String result = instance.getAuthorization(url, method);
    }
}
