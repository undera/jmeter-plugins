package kg.apc.jmeter;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import kg.apc.emulators.SocketChannelEmul;
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
public class PerfMonWorkerTest {

    public PerfMonWorkerTest() {
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
     * Test of setTCPPort method, of class PerfMonWorker.
     */
    @Test
    public void testSetTCPPort() throws IOException {
        System.out.println("setTCPPort");
        int parseInt = 0;
        PerfMonWorker instance = new PerfMonWorker();
        instance.setTCPPort(parseInt);
    }

    /**
     * Test of setUDPPort method, of class PerfMonWorker.
     */
    @Test
    public void testSetUDPPort() throws IOException {
        System.out.println("setUDPPort");
        int parseInt = 0;
        PerfMonWorker instance = new PerfMonWorker();
        instance.setUDPPort(parseInt);
    }

    /**
     * Test of isFinished method, of class PerfMonWorker.
     */
    @Test
    public void testIsFinished() throws IOException {
        System.out.println("isFinished");
        PerfMonWorker instance = new PerfMonWorker();
        boolean expResult = true;
        boolean result = instance.isFinished();
        assertEquals(expResult, result);
    }

    /**
     * Test of processCommands method, of class PerfMonWorker.
     */
    @Test
    public void testProcessCommands() throws Exception {
        System.out.println("processCommands");
        PerfMonWorker instance = new PerfMonWorker();
        instance.setUDPPort(-1);
        instance.setTCPPort(-1);
        instance.startAcceptingCommands();
        try {
            instance.processCommands();
            fail();
        } catch (IOException e) {
            instance.shutdownConnections();
        }
    }

    /**
     * Test of getExitCode method, of class PerfMonWorker.
     */
    @Test
    public void testGetExitCode() throws IOException {
        System.out.println("getExitCode");
        PerfMonWorker instance = new PerfMonWorker();
        int expResult = -1;
        int result = instance.getExitCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of startAcceptingCommands method, of class PerfMonWorker.
     */
    @Test
    public void testStartAcceptingCommands() throws Exception {
        System.out.println("startAcceptingCommands");
        PerfMonWorker instance = new PerfMonWorker();
        //Thread.sleep(5000);
        instance.startAcceptingCommands();
        instance.shutdownConnections();
    }

    /**
     * Test of shutdownConnections method, of class PerfMonWorker.
     */
    @Test
    public void testShutdownConnections() throws Exception {
        System.out.println("shutdownConnections");
        PerfMonWorker instance = new PerfMonWorker();
        instance.shutdownConnections();
    }

    @Test
    public void testProcessCommands_real() throws Exception {
        System.out.println("processCommands real");
        PerfMonWorker instance = new PerfMonWorker();
        //instance.setTCPPort(-1);
        instance.startAcceptingCommands();
        int cnt=0;
        while (!instance.isFinished() && cnt++<5) {
            //instance.processCommands();
        }
        instance.shutdownConnections();
    }

    /**
     * Test of run method, of class PerfMonWorker.
     */
    @Test
    public void testRun() throws IOException {
        System.out.println("run");
        PerfMonWorker instance = new PerfMonWorker();
        instance.run();
    }

    /**
     * Test of registerWritingChannel method, of class PerfMonWorker.
     */
    @Test
    public void testRegisterWritingChannel() throws Exception {
        System.out.println("registerWritingChannel");
        SelectableChannel channel = new SocketChannelEmul();
        PerfMonWorker instance = new PerfMonWorker();
        PerfMonMetricGetter worker = new PerfMonMetricGetter(instance, channel);
        instance.registerWritingChannel(channel, worker);
    }
}
