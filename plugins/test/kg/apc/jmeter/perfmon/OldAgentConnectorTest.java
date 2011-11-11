/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.perfmon;

import kg.apc.emulators.SocketEmulator;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
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

    private static class Gen implements PerfMonSampleGenerator {

        public Gen() {
        }

        public void generate2Samples(long[] netIO, String string, String string0, double d) {
        }

        public void generate2Samples(long[] disksIO, String string, String string0) {
        }

        public void generateSample(double d, String string) {
        }

        public void generateErrorSample(String label, String errorMsg) {
        }
    }

    private class OldConnEmul extends OldAgentConnector {

        public OldConnEmul(String host, int port) {
            super(host, port);
        }
        SocketEmulator sock = new SocketEmulator();

        @Override
        protected Socket createSocket(String host, int port) throws UnknownHostException, IOException {
            return sock;
        }
    }
    private OldAgentConnector instance;

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
        instance = new OldConnEmul("localhost", 4444);
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
        instance.setMetricType(metric);
    }

    /**
     * Test of setParams method, of class OldAgentConnector.
     */
    @Test
    public void testSetParams() {
        System.out.println("setParams");
        String params = "";
        instance.setParams(params);
    }

    /**
     * Test of connect method, of class OldAgentConnector.
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");
        instance.connect();
    }

    /**
     * Test of disconnect method, of class OldAgentConnector.
     */
    @Test
    public void testDisconnect() {
        System.out.println("disconnect");
        instance.disconnect();
    }

    /**
     * Test of createSocket method, of class OldAgentConnector.
     */
    @Test
    public void testCreateSocket() throws Exception {
        System.out.println("createSocket");
        String host = "";
        int port = 0;
        Socket result = instance.createSocket(host, port);
        assertNotNull(result);
    }

    /**
     * Test of generateSamples method, of class OldAgentConnector.
     */
    @Test
    public void testGenerateSamples() throws Exception {
        System.out.println("generateSamples");
        PerfMonSampleGenerator collector = new Gen();
        instance.connect();
        instance.generateSamples(collector);
    }
}
