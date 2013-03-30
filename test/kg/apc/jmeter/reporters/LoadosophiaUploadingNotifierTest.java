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
package kg.apc.jmeter.reporters;

import java.util.LinkedList;
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
public class LoadosophiaUploadingNotifierTest {
    
    public LoadosophiaUploadingNotifierTest() {
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
     * Test of getInstance method, of class LoadosophiaUploadingNotifier.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        LoadosophiaUploadingNotifier expResult = null;
        LoadosophiaUploadingNotifier result = LoadosophiaUploadingNotifier.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of startCollecting method, of class LoadosophiaUploadingNotifier.
     */
    @Test
    public void testStartCollecting() {
        System.out.println("startCollecting");
        LoadosophiaUploadingNotifier instance = null;
        instance.startCollecting();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of endCollecting method, of class LoadosophiaUploadingNotifier.
     */
    @Test
    public void testEndCollecting() {
        System.out.println("endCollecting");
        LoadosophiaUploadingNotifier instance = null;
        instance.endCollecting();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFiles method, of class LoadosophiaUploadingNotifier.
     */
    @Test
    public void testGetFiles() {
        System.out.println("getFiles");
        LoadosophiaUploadingNotifier instance = null;
        LinkedList expResult = null;
        LinkedList result = instance.getFiles();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addFile method, of class LoadosophiaUploadingNotifier.
     */
    @Test
    public void testAddFile() {
        System.out.println("addFile");
        String file = "";
        LoadosophiaUploadingNotifier instance = null;
        instance.addFile(file);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
