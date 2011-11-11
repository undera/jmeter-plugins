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
        ServerAgent instance = new ServerAgent(testPort);
        instance.startServiceAsThread();
        instance.stopService();
    }

    public void testStartServiceAsThread() {
        System.out.println("startServiceAsThread");
        ServerAgent instance = new ServerAgent(testPort);
        instance.startServiceAsThread();
        instance.stopService();
    }

    public void testStartServie() {
        System.out.println("startServie");
        ServerAgent instance = new ServerAgent(testPort);
        instance.startServiceAsThread();
        instance.stopService();
    }

    public void testMain() {
        System.out.println("main");
        //no test as not multithreaded...
    }

    public void testRun() {
        System.out.println("startServie");
        ServerAgent instance = new ServerAgent(testPort);
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
}