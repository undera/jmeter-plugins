/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.perfmon.agent;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author APC
 */
public class ServerAgentTest {

    private static int testPort = 4567;
    
    public ServerAgentTest() {
    }

   @BeforeClass
   public static void setUpClass() throws Exception
   {
   }

   @AfterClass
   public static void tearDownClass() throws Exception
   {
   }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

   @Test
   public void testLogMessage()
   {
      System.out.println("logMessage");
      String message = "test message";
      ServerAgent.logMessage(message);
   }

   @Test
   public void testStopService()
   {
      System.out.println("stopService");
      ServerAgent instance = new ServerAgent(testPort);
      instance.startServiceAsThread();
      instance.stopService();
   }

   @Test
   public void testStartServiceAsThread()
   {
      System.out.println("startServiceAsThread");
      ServerAgent instance = new ServerAgent(testPort);
      instance.startServiceAsThread();
      instance.stopService();
   }

   @Test
   public void testStartServie()
   {
      System.out.println("startServie");
      ServerAgent instance = new ServerAgent(testPort);
      instance.startServiceAsThread();
      instance.stopService();
   }

   @Test
   public void testMain()
   {
      System.out.println("main");
      //no test as not multithreaded...
   }

   @Test
   public void testRun()
   {
      System.out.println("startServie");
      ServerAgent instance = new ServerAgent(testPort);
      instance.startServiceAsThread();
      instance.stopService();
   }

}