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

import kg.apc.jmeter.gui.LoadosophiaUploadingNotifier;
import kg.apc.jmeter.reporters.LoadosophiaUploadingNotifier;
import org.junit.*;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class LoadosophiaUploadingNotifierTest {

    public LoadosophiaUploadingNotifierTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * We will need to call {@link LoadosophiaUploadingNotifier#endCollecting()} to ensure that the files are cleared.
     * Since the {@link LoadosophiaUploadingNotifier} is a singleton, other tests might add files to the instance,
     * hence causing some of the unit tests here to fail.
     */
    @Before
    public void setUp() {
        LoadosophiaUploadingNotifier.getInstance().endCollecting();
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
        LoadosophiaUploadingNotifier result = LoadosophiaUploadingNotifier.getInstance();
    }

    /**
     * Test of startCollecting method, of class LoadosophiaUploadingNotifier.
     */
    @Test
    public void testStartCollecting() {
        System.out.println("startCollecting");
        LoadosophiaUploadingNotifier instance = LoadosophiaUploadingNotifier.getInstance();
        instance.startCollecting();
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of endCollecting method, of class LoadosophiaUploadingNotifier.
     */
    @Test
    public void testEndCollecting() {
        System.out.println("endCollecting");
        LoadosophiaUploadingNotifier instance = LoadosophiaUploadingNotifier.getInstance();
        instance.endCollecting();
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getFiles method, of class LoadosophiaUploadingNotifier.
     */
    @Test
    public void testGetFiles() {
        System.out.println("getFiles");
        LoadosophiaUploadingNotifier instance = LoadosophiaUploadingNotifier.getInstance();
        LinkedList expResult = new LinkedList();
        LinkedList result = instance.getFiles();
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of addFile method, of class LoadosophiaUploadingNotifier.
     */
    @Test
    public void testAddFile() {
        System.out.println("addFile");
        String file = "";
        LoadosophiaUploadingNotifier instance = LoadosophiaUploadingNotifier.getInstance();
        instance.addFile(file);
        // TODO review the generated test code and remove the default call to fail.

    }
}
