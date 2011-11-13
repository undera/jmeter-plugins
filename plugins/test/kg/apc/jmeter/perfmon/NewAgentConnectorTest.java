package kg.apc.jmeter.perfmon;

import java.io.IOException;
import java.net.InetSocketAddress;
import kg.apc.perfmon.client.AbstractTransport;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

    private static class EmulatorTransport extends AbstractTransport {

        public EmulatorTransport() throws IOException {
            super(new InetSocketAddress("localhost", 4444));
        }

        @Override
        public void disconnect() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean test() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void startWithMetrics(String[] metricsArray) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String[] readMetrics() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        protected void writeln(String line) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        protected String readln() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private class NewConnEmul extends NewAgentConnector {

        public NewConnEmul(int protocol, String host, int port) throws IOException {
            super(host, port);
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
    public void setUp() throws IOException {
        instance = new NewConnEmul(0, "localhost", 0);
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
