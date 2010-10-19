package kg.apc.jmeter.perfmon;

import java.net.InetAddress;
import java.net.UnknownHostException;
import kg.apc.jmeter.perfmon.agent.ServerAgent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Orders of tests is important
 * @author Stephane Hoblingre
 */
public class AgentConnectorTest {

    private static AgentConnector instance;
    private static ServerAgent agent;
    private static int testPort = 4567;

    public AgentConnectorTest() {
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
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of connect method, of class AgentConnector.
     */
    @Test
    public void testConnect() throws Exception
    {
        System.out.println("connect");
        instance = new AgentConnector("localhost", testPort);
        instance.connect();
    }

    /**
     * Test of getMem method, of class AgentConnector.
     */
    @Test
    public void testGetMem()
    {
        System.out.println("getMem");
        long result = instance.getMem();
        System.out.println(result);
        assertTrue(result >= 0);
    }

    /**
     * Test of getCpu method, of class AgentConnector.
     */
    @Test
    public void testGetCpu()
    {
        System.out.println("getCpu");
        double result = instance.getCpu();
        System.out.println(result);
        assertTrue(result >= 0);
    }

    /**
     * Test of getRemoteServerName method, of class AgentConnector.
     */
    @Test
    public void testGetRemoteServerName()
    {
        System.out.println("getRemoteServerName");
        String result = instance.getRemoteServerName();
        System.out.println(result);

        String hostname = null;
        try
        {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex)
        {
            //should never happen, localhost is always known...
            hostname = "unknown";
        }

        assertEquals(result, hostname);
    }

       @Test
   public void testGetSwap()
   {
      System.out.println("getSwap");
      long[] result = instance.getSwap();
      assertTrue(result[0] >= 0);
      assertTrue(result[1] >= 0);
   }

   @Test
   public void testGetDisksIO()
   {
      System.out.println("getDisksIO");
      long[] result = instance.getDisksIO();
      assertTrue(result[0] >= 0);
      assertTrue(result[1] >= 0);
   }

   @Test
   public void testGetNetIO()
   {
      System.out.println("getNetIO");
      long[] result = instance.getNetIO();
      assertTrue(result[0] >= 0);
      assertTrue(result[1] >= 0);
   }

   @Test
   public void testGetHost()
   {
      System.out.println("getHost");
      String expResult = "localhost";
      String result = instance.getHost();
      assertEquals(expResult, result);
   }

   @Test
   public void testGetPort()
   {
      System.out.println("getPort");
      int expResult = testPort;
      int result = instance.getPort();
      assertEquals(expResult, result);
   }

    /**
     * Test of disconnect method, of class AgentConnector.
     */
    @Test
    public void testDisconnect() throws Exception
    {
        System.out.println("disconnect");
        instance.disconnect();
    }



}