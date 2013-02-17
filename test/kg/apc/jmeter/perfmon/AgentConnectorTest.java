package kg.apc.jmeter.perfmon;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kg.apc.emulators.SocketEmulator;
import kg.apc.emulators.SocketEmulatorInputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * FIXME:   APC: order of tests should never be important, it is mistake...
 * @author Stephane Hoblingre
 */
public class AgentConnectorTest {

    private static AgentConnector instance;
    private static ServerAgent agent;
    private static int testPort = 4567;

    private static class ServerAgent {

        private ServerAgent(int testPort) {
        }

        private void startServiceAsThread() {
        }

        private void stopService() {
        }
    }
    private SocketEmulator socketEmulator;

    public AgentConnectorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        agent = new ServerAgent(testPort);
        agent.startServiceAsThread();
        //wait the Server Agent starts
        Thread.sleep(2000);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        agent.stopService();
    }

    @Before
    public void setUp() {
        socketEmulator = new SocketEmulator();
        try {
            prepareTestData(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException ex) {
            Logger.getLogger(AgentConnectorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        prepareInstance();
    }

    private void prepareInstance() {
        instance = new AgentConnector("localhost", testPort);
        try {
            instance.connect(socketEmulator);
        } catch (IOException ex) {
            Logger.getLogger(AgentConnectorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of connect method, of class AgentConnector.
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");
    }

    /**
     * Test of getMem method, of class AgentConnector.
     */
    @Test
    public void testGetMem() throws PerfMonException {
        System.out.println("getMem");
        prepareTestData(123);
        long result = instance.getMem();
        System.out.println(result);
        assertTrue(result >= 0);
    }

    /**
     * Test of getCpu method, of class AgentConnector.
     */
    @Test
    public void testGetCpu() throws PerfMonException {
        System.out.println("getCpu");
        prepareTestData(123);
        double result = instance.getCpu();
        System.out.println(result);
        assertTrue(result >= 0);
    }

    /**
     * Test of getRemoteServerName method, of class AgentConnector.
     */
    @Test
    public void testGetRemoteServerName() throws PerfMonException {
        System.out.println("getRemoteServerName");
        String result = instance.getRemoteServerName();
        System.out.println(result);

        String hostname = null;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            //should never happen, localhost is always known...
            hostname = "unknown";
        }

        assertEquals(result, hostname);
    }

    @Test
    public void testGetSwap() throws PerfMonException {
        System.out.println("getSwap");
        prepareTestData("123:456");
        long[] result = instance.getSwap();
        assertTrue(result[0] >= 0);
        assertTrue(result[1] >= 0);
    }

    @Test
    public void testGetDisksIO() throws PerfMonException {
        System.out.println("getDisksIO");
        prepareTestData("123:456");
        long[] result = instance.getDisksIO();
        assertTrue(result[0] >= 0);
        assertTrue(result[1] >= 0);
    }

    @Test
    public void testGetNetIO() throws PerfMonException {
        System.out.println("getNetIO");
        prepareTestData("123:456");
        long[] result = instance.getNetIO();
        assertTrue(result[0] >= 0);
        assertTrue(result[1] >= 0);
    }

    @Test
    public void testGetHost() {
        System.out.println("getHost");
        String expResult = "localhost";
        String result = instance.getHost();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetPort() {
        System.out.println("getPort");
        int expResult = testPort;
        int result = instance.getPort();
        assertEquals(expResult, result);
    }

    /**
     * Test of disconnect method, of class AgentConnector.
     */
    @Test
    public void testDisconnect() throws Exception {
        System.out.println("disconnect");
        instance.disconnect();
    }

    private void prepareTestData(Object aData) {
        String data = aData.toString() + "\n";
        SocketEmulatorInputStream is = null;
        try {
            is = (SocketEmulatorInputStream) socketEmulator.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(AgentConnectorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        is.setBytesToRead(data.getBytes());
    }

    @Test
    public void testSetMetricType() {
        System.out.println("setMetricType");
        String metric = "";
        instance.setMetricType(metric);
    }

    @Test
    public void testGetMetricType() {
        System.out.println("getMetricType");
        int expResult = 0;
        int result = instance.getMetricType();
        assertEquals(expResult, result);
    }
}
