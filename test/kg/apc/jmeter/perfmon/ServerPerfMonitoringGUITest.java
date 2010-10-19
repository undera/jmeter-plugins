/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.perfmon;

import kg.apc.jmeter.util.TestJMeterUtils;
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
       TestJMeterUtils.createJmeterEnv();
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
      String expResult = "Servers Performance Monitoring";
      String result = instance.getStaticLabel();
      assertEquals(expResult, result);
   }

   @Test
   public void testTestStarted()
   {
      System.out.println("testStarted");
      ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
      instance.testStarted();
   }

   @Test
   public void testTestEnded()
   {
      System.out.println("testEnded");
      ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
      instance.testEnded();
   }

   @Test
   public void testRun()
   {
      System.out.println("run");
      ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
      instance.run();
   }

}