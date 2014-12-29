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
package com.atlantbh.jmeter.plugins.rest;

import java.net.URL;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.lang.NotImplementedException;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class RestSamplerTest {

    public RestSamplerTest() {
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
     * Test of setRequestBody method, of class RestSampler.
     */
    @Test
    public void testSetRequestBody() {
        System.out.println("setRequestBody");
        String data = "";
        RestSampler instance = new RestSampler();
        instance.setRequestBody(data);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setRequestHeaders method, of class RestSampler.
     */
    @Test
    public void testSetRequestHeaders() {
        System.out.println("setRequestHeaders");
        String headers = "";
        RestSampler instance = new RestSampler();
        instance.setRequestHeaders(headers);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getRequestBody method, of class RestSampler.
     */
    @Test
    public void testGetRequestBody() {
        System.out.println("getRequestBody");
        RestSampler instance = new RestSampler();
        String expResult = "";
        String result = instance.getRequestBody();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getRequestHeaders method, of class RestSampler.
     */
    @Test
    public void testGetRequestHeaders() {
        System.out.println("getRequestHeaders");
        RestSampler instance = new RestSampler();
        String expResult = "";
        String result = instance.getRequestHeaders();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setResource method, of class RestSampler.
     */
    @Test
    public void testSetResource() {
        System.out.println("setResource");
        String data = "";
        RestSampler instance = new RestSampler();
        instance.setResource(data);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getResource method, of class RestSampler.
     */
    @Test
    public void testGetResource() {
        System.out.println("getResource");
        RestSampler instance = new RestSampler();
        String expResult = "";
        String result = instance.getResource();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setPortNumber method, of class RestSampler.
     */
    @Test
    public void testSetPortNumber() {
        System.out.println("setPortNumber");
        String data = "";
        RestSampler instance = new RestSampler();
        instance.setPortNumber(data);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getPortNumber method, of class RestSampler.
     */
    @Test
    public void testGetPortNumber() {
        System.out.println("getPortNumber");
        RestSampler instance = new RestSampler();
        String expResult = "";
        String result = instance.getPortNumber();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setHostBaseUrl method, of class RestSampler.
     */
    @Test
    public void testSetHostBaseUrl() {
        System.out.println("setHostBaseUrl");
        String data = "";
        RestSampler instance = new RestSampler();
        instance.setHostBaseUrl(data);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getHostBaseUrl method, of class RestSampler.
     */
    @Test
    public void testGetHostBaseUrl() {
        System.out.println("getHostBaseUrl");
        RestSampler instance = new RestSampler();
        String expResult = "";
        String result = instance.getHostBaseUrl();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getUrl method, of class RestSampler.
     */
    @Test
    public void testGetUrl() throws Exception {
        System.out.println("getUrl");
        RestSampler instance = new RestSampler();
        URL expResult = null;
        instance.setHostBaseUrl("http://localhost/");
        URL result = instance.getUrl();
    }

    /**
     * Test of toString method, of class RestSampler.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        RestSampler instance = new RestSampler();
        String expResult = "";
        String result = instance.toString();
    }

    /**
     * Test of setupConnection method, of class RestSampler.
     */
    @Test
    public void testSetupConnection() throws Exception {
        System.out.println("setupConnection");
        URL u = new URL("http://localhost/");
        HttpMethodBase httpMethod = null;
        RestSampler instance = new RestSamplerEmul();
        HttpClient expResult = null;
        HttpClient result = instance.setupConnection(u, httpMethod);
    }

    /**
     * Test of sample method, of class RestSampler.
     */
    @Test
    public void testSample_4args() {
        System.out.println("sample");
        URL url = null;
        String method = "";
        boolean areFollowingRedirect = false;
        int frameDepth = 0;
        RestSampler instance = new RestSampler();
        HTTPSampleResult expResult = null;
        try {
            HTTPSampleResult result = instance.sample(url, method, areFollowingRedirect, frameDepth);
            fail();
        } catch (NotImplementedException e) {
        }
    }

    /**
     * Test of sample method, of class RestSampler.
     */
    @Test
    public void testSample_0args() {
        System.out.println("sample");
        RestSampler instance = new RestSampler();
        SampleResult expResult = null;
        SampleResult result = instance.sample();
    }
}
