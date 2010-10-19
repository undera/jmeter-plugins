/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.perfmon;

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
public class ServerPerfMonitoringGUITest {

    public ServerPerfMonitoringGUITest() {
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
   public void testGetStaticLabel()
   {
      System.out.println("getStaticLabel");
      ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
      String expResult = "";
      String result = instance.getStaticLabel();
      assertEquals(expResult, result);
      fail("The test case is a prototype.");
   }

   @Test
   public void testTestStarted()
   {
      System.out.println("testStarted");
      ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
      instance.testStarted();
      fail("The test case is a prototype.");
   }

   @Test
   public void testTestEnded()
   {
      System.out.println("testEnded");
      ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
      instance.testEnded();
      fail("The test case is a prototype.");
   }

   @Test
   public void testRun()
   {
      System.out.println("run");
      ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
      instance.run();
      fail("The test case is a prototype.");
   }

}