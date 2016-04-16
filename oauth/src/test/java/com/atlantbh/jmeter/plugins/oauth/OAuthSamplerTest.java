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

import java.net.URL;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthSamplerTest {

    public OAuthSamplerTest() {
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
     * Test of setConsumerKey method, of class OAuthSampler.
     */
    @Test
    public void testSetConsumerKey() {
        System.out.println("setConsumerKey");
        String consumerKey = "";
        OAuthSampler instance = new OAuthSampler();
        instance.setConsumerKey(consumerKey);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setConsumerSecret method, of class OAuthSampler.
     */
    @Test
    public void testSetConsumerSecret() {
        System.out.println("setConsumerSecret");
        String consumerSecret = "";
        OAuthSampler instance = new OAuthSampler();
        instance.setConsumerSecret(consumerSecret);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getConsumerKey method, of class OAuthSampler.
     */
    @Test
    public void testGetConsumerKey() {
        System.out.println("getConsumerKey");
        OAuthSampler instance = new OAuthSampler();
        String expResult = "";
        String result = instance.getConsumerKey();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getConsumerSecret method, of class OAuthSampler.
     */
    @Test
    public void testGetConsumerSecret() {
        System.out.println("getConsumerSecret");
        OAuthSampler instance = new OAuthSampler();
        String expResult = "";
        String result = instance.getConsumerSecret();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setRequestBody method, of class OAuthSampler.
     */
    @Test
    public void testSetRequestBody() {
        System.out.println("setRequestBody");
        String data = "";
        OAuthSampler instance = new OAuthSampler();
        instance.setRequestBody(data);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setRequestHeaders method, of class OAuthSampler.
     */
    @Test
    public void testSetRequestHeaders() {
        System.out.println("setRequestHeaders");
        String headers = "";
        OAuthSampler instance = new OAuthSampler();
        instance.setRequestHeaders(headers);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getRequestBody method, of class OAuthSampler.
     */
    @Test
    public void testGetRequestBody() {
        System.out.println("getRequestBody");
        OAuthSampler instance = new OAuthSampler();
        String expResult = "";
        String result = instance.getRequestBody();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getRequestHeaders method, of class OAuthSampler.
     */
    @Test
    public void testGetRequestHeaders() {
        System.out.println("getRequestHeaders");
        OAuthSampler instance = new OAuthSampler();
        String expResult = "";
        String result = instance.getRequestHeaders();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setResource method, of class OAuthSampler.
     */
    @Test
    public void testSetResource() {
        System.out.println("setResource");
        String data = "";
        OAuthSampler instance = new OAuthSampler();
        instance.setResource(data);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getResource method, of class OAuthSampler.
     */
    @Test
    public void testGetResource() {
        System.out.println("getResource");
        OAuthSampler instance = new OAuthSampler();
        String expResult = "";
        String result = instance.getResource();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setPortNumber method, of class OAuthSampler.
     */
    @Test
    public void testSetPortNumber() {
        System.out.println("setPortNumber");
        String data = "";
        OAuthSampler instance = new OAuthSampler();
        instance.setPortNumber(data);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getPortNumber method, of class OAuthSampler.
     */
    @Test
    public void testGetPortNumber() {
        System.out.println("getPortNumber");
        OAuthSampler instance = new OAuthSampler();
        String expResult = "";
        String result = instance.getPortNumber();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setHostBaseUrl method, of class OAuthSampler.
     */
    @Test
    public void testSetHostBaseUrl() {
        System.out.println("setHostBaseUrl");
        String data = "";
        OAuthSampler instance = new OAuthSampler();
        instance.setHostBaseUrl(data);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getHostBaseUrl method, of class OAuthSampler.
     */
    @Test
    public void testGetHostBaseUrl() {
        System.out.println("getHostBaseUrl");
        OAuthSampler instance = new OAuthSampler();
        String expResult = "";
        String result = instance.getHostBaseUrl();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getUrl method, of class OAuthSampler.
     */
    @Test
    public void testGetUrl() throws Exception {
        System.out.println("getUrl");
        OAuthSampler instance = new OAuthSampler();
        instance.setHostBaseUrl("http://localhost/");
        URL expResult = new URL("http://localhost/");
        URL result = instance.getUrl();
    }

    /**
     * Test of setupConnection method, of class OAuthSampler.
     */
    @Test
    public void testSetupConnection() throws Exception {
        System.out.println("setupConnection");
        URL u = null;
        HttpMethodBase httpMethod = null;
        OAuthSampler instance = new OAuthSamplerEmul();
        HttpClient expResult = null;
        HttpClient result = instance.setupConnection(u, httpMethod);
    }

    /**
     * Test of sample method, of class OAuthSampler.
     */
    @Test
    public void testSample_4args() {
        System.out.println("sample");
        URL url = null;
        String method = "";
        boolean areFollowingRedirect = false;
        int frameDepth = 0;
        OAuthSampler instance = new OAuthSampler();
        HTTPSampleResult expResult = null;
        try {
            HTTPSampleResult result = instance.sample(url, method, areFollowingRedirect, frameDepth);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test of sample method, of class OAuthSampler.
     */
    @Test
    public void testSample_0args() {
        System.out.println("sample");
        OAuthSampler instance = new OAuthSampler();
        SampleResult expResult = null;
        SampleResult result = instance.sample();
    }
}
