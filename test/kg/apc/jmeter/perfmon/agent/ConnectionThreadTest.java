package kg.apc.jmeter.perfmon.agent;

import java.io.PrintWriter;
import java.net.Socket;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author APC
 */
public class ConnectionThreadTest
{

    private static ServerAgent agent;
    private static int testPort = 4567;

    public ConnectionThreadTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        agent = new ServerAgent(testPort);
        agent.startServiceAsThread();
        //wait the Server Agent starts
        Thread.sleep(2000);
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
        agent.stopService();
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testRun() throws Exception
    {
        System.out.println("run");

        Socket socket = new Socket("localhost", testPort);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        //close the automaticly created ConnectionThread
        out.println("bye");

        //create here the test
        ConnectionThread instance = new ConnectionThread(socket);
        instance.start();
        out.println("bye");
    }
}
