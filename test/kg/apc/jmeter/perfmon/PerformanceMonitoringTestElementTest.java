/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.perfmon;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
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
public class PerformanceMonitoringTestElementTest {

    public PerformanceMonitoringTestElementTest() {
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
   public void testTableModelToCollectionProperty()
   {
      System.out.println("tableModelToCollectionProperty");
      PowerTableModel model = null;
      CollectionProperty expResult = null;
      CollectionProperty result = PerformanceMonitoringTestElement.tableModelToCollectionProperty(model);
      assertEquals(expResult, result);
      fail("The test case is a prototype.");
   }

   @Test
   public void testGetData()
   {
      System.out.println("getData");
      PerformanceMonitoringTestElement instance = new PerformanceMonitoringTestElement();
      JMeterProperty expResult = null;
      JMeterProperty result = instance.getData();
      assertEquals(expResult, result);
      fail("The test case is a prototype.");
   }

   @Test
   public void testSetData()
   {
      System.out.println("setData");
      CollectionProperty rows = null;
      PerformanceMonitoringTestElement instance = new PerformanceMonitoringTestElement();
      instance.setData(rows);
      fail("The test case is a prototype.");
   }

   @Test
   public void testGetType()
   {
      System.out.println("getType");
      PerformanceMonitoringTestElement instance = new PerformanceMonitoringTestElement();
      int expResult = 0;
      int result = instance.getType();
      assertEquals(expResult, result);
      fail("The test case is a prototype.");
   }

   @Test
   public void testSetType()
   {
      System.out.println("setType");
      int type = 0;
      PerformanceMonitoringTestElement instance = new PerformanceMonitoringTestElement();
      instance.setType(type);
      fail("The test case is a prototype.");
   }

   @Test
   public void testClearData()
   {
      System.out.println("clearData");
      PerformanceMonitoringTestElement instance = new PerformanceMonitoringTestElement();
      instance.clearData();
      fail("The test case is a prototype.");
   }

   @Test
   public void testRegister()
   {
      System.out.println("register");
      AbstractPerformanceMonitoringGui gui = null;
      PerformanceMonitoringTestElement instance = new PerformanceMonitoringTestElement();
      instance.register(gui);
      fail("The test case is a prototype.");
   }

   @Test
   public void testClone()
   {
      System.out.println("clone");
      PerformanceMonitoringTestElement instance = new PerformanceMonitoringTestElement();
      Object expResult = null;
      Object result = instance.clone();
      assertEquals(expResult, result);
      fail("The test case is a prototype.");
   }

   @Test
   public void testTestStarted_0args()
   {
      System.out.println("testStarted");
      PerformanceMonitoringTestElement instance = new PerformanceMonitoringTestElement();
      instance.testStarted();
      fail("The test case is a prototype.");
   }

   @Test
   public void testTestStarted_String()
   {
      System.out.println("testStarted");
      String string = "";
      PerformanceMonitoringTestElement instance = new PerformanceMonitoringTestElement();
      instance.testStarted(string);
      fail("The test case is a prototype.");
   }

   @Test
   public void testTestEnded_0args()
   {
      System.out.println("testEnded");
      PerformanceMonitoringTestElement instance = new PerformanceMonitoringTestElement();
      instance.testEnded();
      fail("The test case is a prototype.");
   }

   @Test
   public void testTestEnded_String()
   {
      System.out.println("testEnded");
      String string = "";
      PerformanceMonitoringTestElement instance = new PerformanceMonitoringTestElement();
      instance.testEnded(string);
      fail("The test case is a prototype.");
   }

   @Test
   public void testTestIterationStart()
   {
      System.out.println("testIterationStart");
      LoopIterationEvent lie = null;
      PerformanceMonitoringTestElement instance = new PerformanceMonitoringTestElement();
      instance.testIterationStart(lie);
      fail("The test case is a prototype.");
   }

}