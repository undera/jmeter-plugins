package kg.apc.jmeter.threads;

import org.apache.jmeter.control.LoopController;
import org.apache.jorphan.collections.HashTree;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jorphan.collections.ListedHashTree;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimpleThreadGroupTest {

    public AbstractSimpleThreadGroupTest() {
    }

   @BeforeClass
   public static void setUpClass() throws Exception {
   }

   @AfterClass
   public static void tearDownClass() throws Exception {
   }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

   /**
    * Test of scheduleThread method, of class AbstractSimpleThreadGroup.
    */
   @Test
   public void testScheduleThread_JMeterThread_long() {
      System.out.println("scheduleThread");
      JMeterThread thread = null;
      long now = 0L;
      AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
      instance.scheduleThread(thread, now);
   }

   /**
    * Test of scheduleThread method, of class AbstractSimpleThreadGroup.
    */
   @Test
   public void testScheduleThread_JMeterThread() {
      System.out.println("scheduleThread");
      JMeterThread thread = null;
      AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
      instance.scheduleThread(thread);
   }

   /**
    * Test of start method, of class AbstractSimpleThreadGroup.
    */
   @Test
   public void testStart() {
      System.out.println("start");
      int groupCount = 0;
      ListenerNotifier notifier = null;
      ListedHashTree threadGroupTree = null;
      StandardJMeterEngine engine = null;
      AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
      instance.start(groupCount, notifier, threadGroupTree, engine);
   }

   /**
    * Test of stopThread method, of class AbstractSimpleThreadGroup.
    */
   @Test
   public void testStopThread() {
      System.out.println("stopThread");
      String threadName = "";
      boolean now = false;
      AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
      boolean expResult = false;
      boolean result = instance.stopThread(threadName, now);
      assertEquals(expResult, result);
   }

   /**
    * Test of threadFinished method, of class AbstractSimpleThreadGroup.
    */
   @Test
   public void testThreadFinished() {
      System.out.println("threadFinished");
      HashTree hashtree = new HashTree();
      hashtree.add(new LoopController());
      JMeterThread thread = new JMeterThread(hashtree, null, null);
      AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
      instance.threadFinished(thread);
   }

   /**
    * Test of tellThreadsToStop method, of class AbstractSimpleThreadGroup.
    */
   @Test
   public void testTellThreadsToStop() {
      System.out.println("tellThreadsToStop");
      AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
      instance.tellThreadsToStop();
   }

   /**
    * Test of stop method, of class AbstractSimpleThreadGroup.
    */
   @Test
   public void testStop() {
      System.out.println("stop");
      AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
      instance.stop();
   }

   /**
    * Test of numberOfActiveThreads method, of class AbstractSimpleThreadGroup.
    */
   @Test
   public void testNumberOfActiveThreads() {
      System.out.println("numberOfActiveThreads");
      AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
      int expResult = 0;
      int result = instance.numberOfActiveThreads();
      assertEquals(expResult, result);
   }

   /**
    * Test of verifyThreadsStopped method, of class AbstractSimpleThreadGroup.
    */
   @Test
   public void testVerifyThreadsStopped() {
      System.out.println("verifyThreadsStopped");
      AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
      boolean expResult = true;
      boolean result = instance.verifyThreadsStopped();
      assertEquals(expResult, result);
   }

   /**
    * Test of waitThreadsStopped method, of class AbstractSimpleThreadGroup.
    */
   @Test
   public void testWaitThreadsStopped() {
      System.out.println("waitThreadsStopped");
      AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
      instance.waitThreadsStopped();
   }

   public class AbstractSimpleThreadGroupImpl extends AbstractSimpleThreadGroup {

      public void scheduleThread(JMeterThread thread, long now) {
      }
   }

}