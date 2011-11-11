/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.perfmon;

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
public class NewAgentConnectorTest {
    
    public NewAgentConnectorTest() {
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
     * Test of setMetricType method, of class NewAgentConnector.
     */
    @Test
    public void testSetMetricType() {
        System.out.println("setMetricType");
        String metric = "";
        NewAgentConnector instance = null;
        instance.setMetricType(metric);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of test method, of class NewAgentConnector.
     */
    @Test
    public void testTest() {
        System.out.println("test");
        NewAgentConnector instance = null;
        boolean expResult = false;
        boolean result = instance.test();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setParams method, of class NewAgentConnector.
     */
    @Test
    public void testSetParams() {
        System.out.println("setParams");
        String params = "";
        NewAgentConnector instance = null;
        instance.setParams(params);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of connect method, of class NewAgentConnector.
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");
        NewAgentConnector instance = null;
        instance.connect();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of disconnect method, of class NewAgentConnector.
     */
    @Test
    public void testDisconnect() {
        System.out.println("disconnect");
        NewAgentConnector instance = null;
        instance.disconnect();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLabel method, of class NewAgentConnector.
     */
    @Test
    public void testGetLabel() {
        System.out.println("getLabel");
        boolean translateHost = false;
        NewAgentConnector instance = null;
        String expResult = "";
        String result = instance.getLabel(translateHost);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateSamples method, of class NewAgentConnector.
     */
    @Test
    public void testGenerateSamples() throws Exception {
        System.out.println("generateSamples");
        PerfMonSampleGenerator collector = null;
        NewAgentConnector instance = null;
        instance.generateSamples(collector);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
