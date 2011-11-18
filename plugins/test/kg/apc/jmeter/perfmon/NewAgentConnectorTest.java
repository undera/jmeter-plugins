package kg.apc.jmeter.perfmon;

import java.io.IOException;
import java.nio.ByteBuffer;
import kg.apc.emulators.DatagramChannelEmul;
import kg.apc.perfmon.client.NIOTransport;
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
    private DatagramChannelEmul channel;
    private NIOTransport transport;

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
        channel=(DatagramChannelEmul) DatagramChannelEmul.open();
        transport=new NIOTransport();
        transport.setChannels(channel, channel);
        instance.setTransport(transport);
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
        channel.setBytesToRead(ByteBuffer.wrap("0.123\n".getBytes()));
        instance.setMetricType("test");
        instance.generateSamples(collector);
    }

    @Test
    public void testGenerateSamples_none() throws Exception {
        System.out.println("generateSamples");
        PerfMonSampleGenerator collector = new Gen();
        channel.setBytesToRead(ByteBuffer.wrap("".getBytes()));
        instance.setMetricType("test");
        instance.generateSamples(collector);
    }

    public void testGenerateSamples_many() throws Exception {
        System.out.println("generateSamples");
        PerfMonSampleGenerator collector = new Gen();
        channel.setBytesToRead(ByteBuffer.wrap("0.123  3424\n".getBytes()));
        instance.generateSamples(collector);
    }
}
