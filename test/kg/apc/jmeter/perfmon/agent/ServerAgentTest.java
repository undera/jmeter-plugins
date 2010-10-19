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
import static org.junit.Assert.*;

/**
 *
 * @author APC
 */
public class ServerAgentTest {

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
      String message = "";
      ServerAgent.logMessage(message);
      fail("The test case is a prototype.");
   }

   @Test
   public void testStopService()
   {
      System.out.println("stopService");
      ServerAgent instance = null;
      instance.stopService();
      fail("The test case is a prototype.");
   }

   @Test
   public void testStartServiceAsThread()
   {
      System.out.println("startServiceAsThread");
      ServerAgent instance = null;
      instance.startServiceAsThread();
      fail("The test case is a prototype.");
   }

   @Test
   public void testStartServie()
   {
      System.out.println("startServie");
      ServerAgent instance = null;
      instance.startServie();
      fail("The test case is a prototype.");
   }

   @Test
   public void testMain()
   {
      System.out.println("main");
      String[] args = null;
      ServerAgent.main(args);
      fail("The test case is a prototype.");
   }

   @Test
   public void testRun()
   {
      System.out.println("run");
      ServerAgent instance = null;
      instance.run();
      fail("The test case is a prototype.");
   }

}