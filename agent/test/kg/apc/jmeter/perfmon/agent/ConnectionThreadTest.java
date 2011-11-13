package kg.apc.jmeter.perfmon.agent;

import java.io.PrintWriter;
import java.net.Socket;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import kg.apc.emulators.SocketEmulator;

/**
 *
 * @author APC
 */
public class ConnectionThreadTest extends TestCase {

    private class ConnectionThreadEmul extends ConnectionThread {

        public ConnectionThreadEmul(Socket socket) {
            super(socket);
        }

        protected void exit(int rc) {
            System.err.println("Simulated exit with " + rc);
        }
    }
    private static ServerAgent agent;
    private static int testPort = 4567;

    public ConnectionThreadTest() {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ConnectionThreadTest.class);
        return suite;
    }

    public static void setUpClass() throws Exception {
        //agent = new ServerAgent(testPort);
        //agent.startServiceAsThread();
        //wait the Server Agent starts
        Thread.sleep(2000);
    }

    public static void tearDownClass() throws Exception {
        //agent.stopService();
    }

    public void testRun() throws Exception {
        System.out.println("run");

        Socket socket = new SocketEmulator();
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        //close the automaticly created ConnectionThread
        out.println("bye");

        //create here the test
        ConnectionThread instance = new ConnectionThread(socket);
        instance.start();
        out.println("bye");
    }

    /**
     * Test of exit method, of class ConnectionThread.
     */
    public void testExit() {
        System.out.println("exit");
        int i = 0;
        ConnectionThread instance = new ConnectionThreadEmul(null);
        instance.exit(i);
    }
}
