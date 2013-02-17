package kg.apc.jmeter.perfmon;

import kg.apc.emulators.SocketEmulator;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import kg.apc.emulators.SocketEmulatorInputStream;
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
        instance = new OldAgentConnector("localhost", 4444);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of connect method, of class OldAgentConnector.
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");
        instance = new OldConnEmul("", 0);
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
        instance = new OldConnEmul("", 0);
        Socket result = instance.createSocket(host, port);
        assertNotNull(result);
    }

    /**
     * Test of generateSamples method, of class OldAgentConnector.
     */
    @Test
    public void testGenerateSamples() throws Exception {
        System.out.println("generateSamples");
        instance = new OldConnEmul("", 0);
        PerfMonSampleGenerator collector = new Gen();
        SocketEmulator s = ((OldConnEmul) instance).sock;
        SocketEmulatorInputStream is = (SocketEmulatorInputStream) s.getInputStream();
        is.setBytesToRead("test\n1\n");
        instance.connect();
        instance.generateSamples(collector);
    }

    /**
     * Test of addMetric method, of class OldAgentConnector.
     */
    @Test
    public void testAddMetric() {
        System.out.println("addMetric");
        String metric = "";
        String params = "";
        instance.addMetric(metric, params, null);
    }
}
