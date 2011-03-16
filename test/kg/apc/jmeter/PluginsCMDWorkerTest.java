/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter;

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
public class PluginsCMDWorkerTest {

    public PluginsCMDWorkerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addExportMode method, of class PluginsCMDWorker.
     */
    @Test
    public void testAddExportMode() {
        System.out.println("addExportMode");
        int mode = 0;
        PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.addExportMode(mode);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setInputFile method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetInputFile() {
        System.out.println("setInputFile");
        String string = "";
        PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setInputFile(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setOutputCSVFile method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetOutputCSVFile() {
        System.out.println("setOutputCSVFile");
        String string = "";
        PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setOutputCSVFile(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setOutputPNGFile method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetOutputPNGFile() {
        System.out.println("setOutputPNGFile");
        String string = "";
        PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setOutputPNGFile(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPluginType method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetPluginType() {
        System.out.println("setPluginType");
        String string = "";
        PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setPluginType(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setGraphWidth method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetGraphWidth() {
        System.out.println("setGraphWidth");
        int i = 0;
        PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setGraphWidth(i);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setGraphHeight method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetGraphHeight() {
        System.out.println("setGraphHeight");
        int i = 0;
        PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setGraphHeight(i);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of doJob method, of class PluginsCMDWorker.
     */
    @Test
    public void testDoJob() {
        System.out.println("doJob");
        PluginsCMDWorker instance = new PluginsCMDWorker();
        int expResult = 0;
        int result = instance.doJob();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}