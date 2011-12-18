package kg.apc.perfmon;

import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import junit.framework.TestCase;

/**
 *
 * @author undera
 */
public class PerfMonWorkerTest extends TestCase {

    public PerfMonWorkerTest() {
    }

    /**
     * Test of setTCPPort method, of class PerfMonWorker.
     */
    public void testSetTCPPort() throws IOException {
        System.out.println("setTCPPort");
        int parseInt = 0;
        PerfMonWorker instance = new PerfMonWorker();
        instance.setTCPPort(parseInt);
    }

    /**
     * Test of setUDPPort method, of class PerfMonWorker.
     */
    public void testSetUDPPort() throws IOException {
        System.out.println("setUDPPort");
        int parseInt = 0;
        PerfMonWorker instance = new PerfMonWorker();
        instance.setUDPPort(parseInt);
    }

    /**
     * Test of isFinished method, of class PerfMonWorker.
     */
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
    public void testShutdownConnections() throws Exception {
        System.out.println("shutdownConnections");
        PerfMonWorker instance = new PerfMonWorker();
        instance.shutdownConnections();
    }

    public void testProcessCommands_real() throws Exception {
        System.out.println("processCommands real");
        PerfMonWorker instance = new PerfMonWorker();
        //instance.setTCPPort(-1);
        instance.startAcceptingCommands();
        int cnt = 0;
        while (!instance.isFinished() && cnt++ < 5) {
            //instance.processCommands();
        }
        instance.shutdownConnections();
    }

    /**
     * Test of run method, of class PerfMonWorker.
     */
    public void testRun() throws IOException {
        System.out.println("run");
        PerfMonWorker instance = new PerfMonWorker();
        instance.run();
    }

    /**
     * Test of run method, of class PerfMonWorker.
     */
    public void testLogVersion() throws IOException {
        System.out.println("logVersion");
        PerfMonWorker instance = new PerfMonWorker();
        instance.logVersion();
    }
}
