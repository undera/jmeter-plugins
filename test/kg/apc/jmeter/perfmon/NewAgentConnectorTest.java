package kg.apc.jmeter.perfmon;

import java.io.IOException;
import java.nio.ByteBuffer;
import kg.apc.emulators.DatagramChannelEmul;
import kg.apc.perfmon.client.Transport;
import org.junit.*;

/**
 *
 * @author undera
 */
public class NewAgentConnectorTest {

    private static class TransportEmul implements Transport {

        public TransportEmul() {
        }

        public void disconnect() {
        }

        public String[] readMetrics() {
            return new String[0];
        }

        public String readln() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setInterval(long interval) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void shutdownAgent() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void startWithMetrics(String[] metricsArray) throws IOException {
        }

        public boolean test() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void writeln(String line) throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String getAddressLabel() {
            return "label";
        }

        public void setAddressLabel(String label) {
        }
    }
    private DatagramChannelEmul channel;
    private Transport transport;

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
        instance = new NewAgentConnector();
        channel = (DatagramChannelEmul) DatagramChannelEmul.open();
        transport = new TransportEmul();
        instance.setTransport(transport);
    }

    @After
    public void tearDown() {
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
        channel.setBytesToRead(ByteBuffer.wrap("0.123\n".getBytes()));
        instance.generateSamples(collector);
    }

    @Test
    public void testGenerateSamples_none() throws Exception {
        System.out.println("generateSamples");
        PerfMonSampleGenerator collector = new Gen();
        channel.setBytesToRead(ByteBuffer.wrap("".getBytes()));
        instance.generateSamples(collector);
    }

    public void testGenerateSamples_many() throws Exception {
        System.out.println("generateSamples");
        PerfMonSampleGenerator collector = new Gen();
        channel.setBytesToRead(ByteBuffer.wrap("0.123  3424\n".getBytes()));
        instance.generateSamples(collector);
    }

    /**
     * Test of setTransport method, of class NewAgentConnector.
     */
    @Test
    public void testSetTransport() {
        System.out.println("setTransport");
        Transport atransport = null;
        instance.setTransport(atransport);
    }

    /**
     * Test of addMetric method, of class NewAgentConnector.
     */
    @Test
    public void testAddMetric() {
        System.out.println("addMetric");
        String metric = "";
        String params = "";
        instance.addMetric(metric, params, null);
    }
}
