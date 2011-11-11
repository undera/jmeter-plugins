/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.perfmon;

import java.io.IOException;
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
public class UnavailableAgentConnectorTest {

    private class Gen implements PerfMonSampleGenerator {

        public void generate2Samples(long[] netIO, String string, String string0, double d) {
        }

        public void generate2Samples(long[] disksIO, String string, String string0) {
        }

        public void generateSample(double d, String string) {
        }

        public void generateErrorSample(String label, String errorMsg) {
        }
    }

    public UnavailableAgentConnectorTest() {
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
     * Test of setMetricType method, of class UnavailableAgentConnector.
     */
    @Test
    public void testSetMetricType() {
        System.out.println("setMetricType");
        String metric = "";
        UnavailableAgentConnector instance = new UnavailableAgentConnector(new IOException());
        instance.setMetricType(metric);
    }

    /**
     * Test of setParams method, of class UnavailableAgentConnector.
     */
    @Test
    public void testSetParams() {
        System.out.println("setParams");
        String params = "";
        UnavailableAgentConnector instance = new UnavailableAgentConnector(new IOException());
        instance.setParams(params);
    }

    /**
     * Test of connect method, of class UnavailableAgentConnector.
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");
        UnavailableAgentConnector instance = new UnavailableAgentConnector(new IOException());
        instance.connect();
    }

    /**
     * Test of disconnect method, of class UnavailableAgentConnector.
     */
    @Test
    public void testDisconnect() {
        System.out.println("disconnect");
        UnavailableAgentConnector instance = new UnavailableAgentConnector(new IOException());
        instance.disconnect();
    }

    /**
     * Test of getLabel method, of class UnavailableAgentConnector.
     */
    @Test
    public void testGetLabel() {
        System.out.println("getLabel");
        boolean translateHost = false;
        UnavailableAgentConnector instance = new UnavailableAgentConnector(new IOException());
        String result = instance.getLabel(translateHost);
        assertNotNull(result);
    }

    /**
     * Test of generateSamples method, of class UnavailableAgentConnector.
     */
    @Test
    public void testGenerateSamples() throws Exception {
        System.out.println("generateSamples");
        PerfMonSampleGenerator collector = new Gen();
        UnavailableAgentConnector instance = new UnavailableAgentConnector(new IOException());
        instance.generateSamples(collector);
    }
}
