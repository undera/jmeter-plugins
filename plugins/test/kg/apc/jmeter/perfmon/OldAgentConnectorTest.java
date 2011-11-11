/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.perfmon;

import java.net.Socket;
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
public class OldAgentConnectorTest {
    
    public OldAgentConnectorTest() {
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
     * Test of setMetricType method, of class OldAgentConnector.
     */
    @Test
    public void testSetMetricType() {
        System.out.println("setMetricType");
        String metric = "";
        OldAgentConnector instance = null;
        instance.setMetricType(metric);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setParams method, of class OldAgentConnector.
     */
    @Test
    public void testSetParams() {
        System.out.println("setParams");
        String params = "";
        OldAgentConnector instance = null;
        instance.setParams(params);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of connect method, of class OldAgentConnector.
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");
        OldAgentConnector instance = null;
        instance.connect();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of disconnect method, of class OldAgentConnector.
     */
    @Test
    public void testDisconnect() {
        System.out.println("disconnect");
        OldAgentConnector instance = null;
        instance.disconnect();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createSocket method, of class OldAgentConnector.
     */
    @Test
    public void testCreateSocket() throws Exception {
        System.out.println("createSocket");
        String host = "";
        int port = 0;
        OldAgentConnector instance = null;
        Socket expResult = null;
        Socket result = instance.createSocket(host, port);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLabel method, of class OldAgentConnector.
     */
    @Test
    public void testGetLabel() {
        System.out.println("getLabel");
        boolean translate = false;
        OldAgentConnector instance = null;
        String expResult = "";
        String result = instance.getLabel(translate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateSamples method, of class OldAgentConnector.
     */
    @Test
    public void testGenerateSamples() throws Exception {
        System.out.println("generateSamples");
        PerfMonSampleGenerator collector = null;
        OldAgentConnector instance = null;
        instance.generateSamples(collector);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
