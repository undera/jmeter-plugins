package kg.apc.jmeter.perfmon;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import kg.apc.perfmon.client.AbstractTransport;
import kg.apc.perfmon.client.TransportFactory;
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

    private static class EmulFactory extends TransportFactory {

        public EmulFactory() {
        }

        @Override
        public AbstractTransport getTransport(SocketAddress addr) throws IOException {
            return new EmulatorTransport();
        }
    }

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
    private NewConnEmul instance;

    private static class EmulatorTransport extends AbstractTransport {

        private String[] metricsToReturn;

        public EmulatorTransport() throws IOException {
            super(new InetSocketAddress("localhost", 4444));
        }

        @Override
        public void disconnect() {
        }

        @Override
        public boolean test() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void startWithMetrics(String[] metricsArray) {
        }

        @Override
        public String[] readMetrics() {
            return metricsToReturn;
        }

        @Override
        protected void writeln(String line) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        protected String readln() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        private void esetMetricsToReturn(String[] split) {
            metricsToReturn = split;
        }
    }

    private class NewConnEmul extends NewAgentConnector {

        final EmulatorTransport etrans;

        public NewConnEmul() throws IOException {
            super("", 0, new EmulFactory());
            etrans = new EmulatorTransport();
            transport = etrans;
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
        instance = new NewConnEmul();
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
        instance.etrans.esetMetricsToReturn("0.123".split("\t"));
        instance.setMetricType("test");
        instance.generateSamples(collector);
    }

    @Test
    public void testGenerateSamples_none() throws Exception {
        System.out.println("generateSamples");
        PerfMonSampleGenerator collector = new Gen();
        instance.etrans.esetMetricsToReturn("".split("\t"));
        instance.setMetricType("test");
        instance.generateSamples(collector);
    }

    public void testGenerateSamples_many() throws Exception {
        System.out.println("generateSamples");
        PerfMonSampleGenerator collector = new Gen();
        instance.etrans.esetMetricsToReturn("0.123  3424".split("\t"));
        instance.generateSamples(collector);
    }
}
