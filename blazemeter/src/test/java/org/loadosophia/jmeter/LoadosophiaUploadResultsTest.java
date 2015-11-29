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
package org.loadosophia.jmeter;

import org.loadosophia.jmeter.LoadosophiaUploadResults;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LoadosophiaUploadResultsTest {
    
    public LoadosophiaUploadResultsTest() {
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
     * Test of getRedirectLink method, of class LoadosophiaUploadResults.
     */
    @Test
    public void testGetRedirectLink() {
        System.out.println("getRedirectLink");
        LoadosophiaUploadResults instance = new LoadosophiaUploadResults();
        String expResult = "";
        String result = instance.getRedirectLink();
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of setRedirectLink method, of class LoadosophiaUploadResults.
     */
    @Test
    public void testSetRedirectLink() {
        System.out.println("setRedirectLink");
        String string = "";
        LoadosophiaUploadResults instance = new LoadosophiaUploadResults();
        instance.setRedirectLink(string);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of setQueueID method, of class LoadosophiaUploadResults.
     */
    @Test
    public void testSetQueueID() {
        System.out.println("setQueueID");
        int aQueueID = 0;
        LoadosophiaUploadResults instance = new LoadosophiaUploadResults();
        instance.setQueueID(aQueueID);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of setTestID method, of class LoadosophiaUploadResults.
     */
    @Test
    public void testSetTestID() {
        System.out.println("setTestID");
        int aTestID = 0;
        LoadosophiaUploadResults instance = new LoadosophiaUploadResults();
        instance.setTestID(aTestID);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getTestID method, of class LoadosophiaUploadResults.
     */
    @Test
    public void testGetTestID() {
        System.out.println("getTestID");
        LoadosophiaUploadResults instance = new LoadosophiaUploadResults();
        int expResult = 0;
        int result = instance.getTestID();
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getQueueID method, of class LoadosophiaUploadResults.
     */
    @Test
    public void testGetQueueID() {
        System.out.println("getQueueID");
        LoadosophiaUploadResults instance = new LoadosophiaUploadResults();
        int expResult = 0;
        int result = instance.getQueueID();
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }
}
