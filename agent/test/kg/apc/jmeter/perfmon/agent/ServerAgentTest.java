package kg.apc.jmeter.perfmon.agent;

import kg.apc.emulators.ServerSocketEmulator;
import java.io.IOException;
import java.net.ServerSocket;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author APC
 */
public class ServerAgentTest extends TestCase {

    private class ServerAgentEmul extends ServerAgent {

        public ServerAgentEmul() {
            super(0);
        }

        private ServerAgentEmul(int testPort) {
            super(testPort);
        }

        protected void exit(int rc) {
            System.err.println("Simulated exit with " + rc);
        }

        protected ServerSocket getServerSocket(int port) throws IOException {
            return new ServerSocketEmulator();
        }
    }
    private static int testPort = 4567;

    public ServerAgentTest() {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ServerAgentTest.class);
        return suite;
    }

    public void testLogMessage() {
        System.out.println("logMessage");
        String message = "test message";
        ServerAgent.logMessage(message);
    }

    public void testStopService() {
        System.out.println("stopService");
        ServerAgent instance = new ServerAgentEmul(testPort);
        instance.startServiceAsThread();
        instance.stopService();
    }

    public void testStartServiceAsThread() {
        System.out.println("startServiceAsThread");
        ServerAgent instance = new ServerAgentEmul(testPort);
        instance.startServiceAsThread();
        instance.stopService();
    }

    public void testStartServie() {
        System.out.println("startServie");
        ServerAgent instance = new ServerAgentEmul(testPort);
        instance.startServiceAsThread();
        instance.stopService();
    }

    public void testMain() {
        System.out.println("main");
        //no test as not multithreaded...
    }

    public void testRun() {
        System.out.println("startServie");
        ServerAgent instance = new ServerAgentEmul(testPort);
        instance.startServiceAsThread();
        instance.stopService();
    }

    /**
     * Test of isAutoStop method, of class ServerAgent.
     */
    public void testIsAutoStop() {
        System.out.println("isAutoStop");
        boolean expResult = false;
        boolean result = ServerAgent.isAutoStop();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerSocket method, of class ServerAgent.
     */
    public void testGetServerSocket() throws Exception {
        System.out.println("getServerSocket");
        int port = 0;
        ServerAgent instance = new ServerAgentEmul();
        ServerSocket result = instance.getServerSocket(port);
        assertNotNull(result);
    }

    /**
     * Test of exit method, of class ServerAgent.
     */
    public void testExit() {
        System.out.println("exit");
        int rc = 0;
        ServerAgent instance = new ServerAgentEmul();
        instance.exit(rc);
    }
}