package kg.apc.jmeter.perfmon;

import kg.apc.perfmon.client.AbstractTransport;
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

    private NewAgentConnector instance;

    private static class EmulatorTransport implements AbstractTransport {

        public EmulatorTransport() {
        }

        public void disconnect() {
        }

        public void writeln(String string) {
        }

        public String readln() {
            return "";
        }
    }

    private class NewConnEmul extends NewAgentConnector {

        public NewConnEmul(int protocol, String host, int port) {
            super(protocol, host, port);
            transport = new EmulatorTransport();
        }
    }

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
        instance = new NewConnEmul(0, null, 0);
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
        instance.setMetricType(metric);
    }

    /**
     * Test of test method, of class NewAgentConnector.
     */
    @Test
    public void testTest() {
        System.out.println("test");
        boolean expResult = false;
        boolean result = instance.test();
        assertEquals(expResult, result);
    }

    /**
     * Test of setParams method, of class NewAgentConnector.
     */
    @Test
    public void testSetParams() {
        System.out.println("setParams");
        String params = "";
        instance.setParams(params);
    }

    /**
     * Test of connect method, of class NewAgentConnector.
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");
        instance.connect();
    }

    /**
     * Test of disconnect method, of class NewAgentConnector.
     */
    @Test
    public void testDisconnect() {
        System.out.println("disconnect");
        instance.disconnect();
    }

    /**
     * Test of generateSamples method, of class NewAgentConnector.
     */
    @Test
    public void testGenerateSamples() throws Exception {
        System.out.println("generateSamples");
        PerfMonSampleGenerator collector = new Gen();
        instance.generateSamples(collector);
    }
}
