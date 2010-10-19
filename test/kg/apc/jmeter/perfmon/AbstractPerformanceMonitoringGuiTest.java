/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.perfmon;

import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import kg.apc.jmeter.vizualizers.GraphPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.Sample;
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
public class AbstractPerformanceMonitoringGuiTest {

    public AbstractPerformanceMonitoringGuiTest() {
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
   public void testIsConnectorsValid()
   {
      System.out.println("isConnectorsValid");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      boolean expResult = false;
      boolean result = instance.isConnectorsValid();
      assertEquals(expResult, result);
      fail("The test case is a prototype.");
   }

   @Test
   public void testUpdateAgentConnectors()
   {
      System.out.println("updateAgentConnectors");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.updateAgentConnectors();
      fail("The test case is a prototype.");
   }

   @Test
   public void testCreateGraphPanel()
   {
      System.out.println("createGraphPanel");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      GraphPanel expResult = null;
      GraphPanel result = instance.createGraphPanel();
      assertEquals(expResult, result);
      fail("The test case is a prototype.");
   }

   @Test
   public void testGetLabelResource()
   {
      System.out.println("getLabelResource");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      String expResult = "";
      String result = instance.getLabelResource();
      assertEquals(expResult, result);
      fail("The test case is a prototype.");
   }

   @Test
   public void testGetStaticLabel()
   {
      System.out.println("getStaticLabel");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      String expResult = "";
      String result = instance.getStaticLabel();
      assertEquals(expResult, result);
      fail("The test case is a prototype.");
   }

   @Test
   public void testCreateTestElement()
   {
      System.out.println("createTestElement");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      TestElement expResult = null;
      TestElement result = instance.createTestElement();
      assertEquals(expResult, result);
      fail("The test case is a prototype.");
   }

   @Test
   public void testModifyTestElement()
   {
      System.out.println("modifyTestElement");
      TestElement te = null;
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.modifyTestElement(te);
      fail("The test case is a prototype.");
   }

   @Test
   public void testConfigure()
   {
      System.out.println("configure");
      TestElement te = null;
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.configure(te);
      fail("The test case is a prototype.");
   }

   @Test
   public void testUpdateGui_0args()
   {
      System.out.println("updateGui");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.updateGui();
      fail("The test case is a prototype.");
   }

   @Test
   public void testUpdateGui_Sample()
   {
      System.out.println("updateGui");
      Sample sample = null;
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.updateGui(sample);
      fail("The test case is a prototype.");
   }

   @Test
   public void testClearData()
   {
      System.out.println("clearData");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.clearData();
      fail("The test case is a prototype.");
   }

   @Test
   public void testTableChanged()
   {
      System.out.println("tableChanged");
      TableModelEvent e = null;
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.tableChanged(e);
      fail("The test case is a prototype.");
   }

   @Test
   public void testEditingStopped()
   {
      System.out.println("editingStopped");
      ChangeEvent e = null;
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.editingStopped(e);
      fail("The test case is a prototype.");
   }

   @Test
   public void testEditingCanceled()
   {
      System.out.println("editingCanceled");
      ChangeEvent e = null;
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.editingCanceled(e);
      fail("The test case is a prototype.");
   }

   @Test
   public void testTestStarted()
   {
      System.out.println("testStarted");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.testStarted();
      fail("The test case is a prototype.");
   }

   @Test
   public void testTestEnded()
   {
      System.out.println("testEnded");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.testEnded();
      fail("The test case is a prototype.");
   }

   public class AbstractPerformanceMonitoringGuiImpl
         extends AbstractPerformanceMonitoringGui
   {
      public String getStaticLabel()
      {
         return "";
      }

      public void testStarted()
      {
      }

      public void testEnded()
      {
      }
   }

}