/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.threads;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jorphan.collections.HashTree;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apc
 */
public class SteppingThreadGroupTest
{
   /**
    *
    */
   public SteppingThreadGroupTest()
   {
   }

   /**
    *
    * @throws Exception
    */
   @BeforeClass
   public static void setUpClass() throws Exception
   {
   }

   /**
    *
    * @throws Exception
    */
   @AfterClass
   public static void tearDownClass() throws Exception
   {
   }

   /**
    *
    */
   @Before
   public void setUp()
   {
   }

   /**
    *
    */
   @After
   public void tearDown()
   {
   }

   /**
    * Test of scheduleThread method, of class SteppingThreadGroup.
    */
   @Test
   public void testScheduleThread()
   {
      System.out.println("scheduleThread");
      HashTree hashtree = new HashTree();
      hashtree.add(new LoopController());
      JMeterThread thread = new JMeterThread(hashtree, null, null);
      SteppingThreadGroup instance = new SteppingThreadGroup();
      instance.scheduleThread(thread);
   }

   /**
    * Test of getThreadGroupDelay method, of class SteppingThreadGroup.
    */
   @Test
   public void testGetThreadGroupDelay()
   {
      System.out.println("getThreadGroupDelay");
      SteppingThreadGroup instance = new SteppingThreadGroup();
      int expResult = 0;
      int result = instance.getThreadGroupDelay();
      assertEquals(expResult, result);
   }

   /**
    * Test of getInUserPeriod method, of class SteppingThreadGroup.
    */
   @Test
   public void testGetInUserPeriod()
   {
      System.out.println("getInUserPeriod");
      SteppingThreadGroup instance = new SteppingThreadGroup();
      int expResult = 0;
      int result = instance.getInUserPeriod();
      assertEquals(expResult, result);
   }

   /**
    * Test of getInUserCount method, of class SteppingThreadGroup.
    */
   @Test
   public void testGetInUserCount()
   {
      System.out.println("getInUserCount");
      SteppingThreadGroup instance = new SteppingThreadGroup();
      int expResult = 1;
      int result = instance.getInUserCount();
      assertEquals(expResult, result);
   }

   /**
    * Test of getFlightTime method, of class SteppingThreadGroup.
    */
   @Test
   public void testGetFlightTime()
   {
      System.out.println("getFlightTime");
      SteppingThreadGroup instance = new SteppingThreadGroup();
      int expResult = 0;
      int result = instance.getFlightTime();
      assertEquals(expResult, result);
   }

   /**
    * Test of getOutUserPeriod method, of class SteppingThreadGroup.
    */
   @Test
   public void testGetOutUserPeriod()
   {
      System.out.println("getOutUserPeriod");
      SteppingThreadGroup instance = new SteppingThreadGroup();
      int expResult = 0;
      int result = instance.getOutUserPeriod();
      assertEquals(expResult, result);
   }

   /**
    * Test of getOutUserCount method, of class SteppingThreadGroup.
    */
   @Test
   public void testGetOutUserCount()
   {
      System.out.println("getOutUserCount");
      SteppingThreadGroup instance = new SteppingThreadGroup();
      int expResult = 1;
      int result = instance.getOutUserCount();
      assertEquals(expResult, result);
   }
}
