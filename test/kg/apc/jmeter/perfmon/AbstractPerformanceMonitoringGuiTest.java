package kg.apc.jmeter.perfmon;

import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import kg.apc.jmeter.util.TestJMeterUtils;
import kg.apc.jmeter.vizualizers.GraphPanel;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
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
public class AbstractPerformanceMonitoringGuiTest
{
   private PowerTableModel dataModel;

   public AbstractPerformanceMonitoringGuiTest()
   {
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
   public void setUp()
   {
      dataModel = new PowerTableModel(AbstractPerformanceMonitoringGui.columnIdentifiers, AbstractPerformanceMonitoringGui.columnClasses);
      dataModel.addRow(new Object[]
            {
               "localhost", 4444
            });
   }

   @After
   public void tearDown()
   {
   }

   @Test
   public void testIsConnectorsValid()
   {
      System.out.println("isConnectorsValid");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      boolean expResult = false;
      boolean result = instance.isConnectorsValid();
      assertEquals(expResult, result);
   }

   @Test
   public void testUpdateAgentConnectors()
   {
      System.out.println("updateAgentConnectors");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      assertEquals(false, instance.isConnectorsValid());
      instance.addRowButton.doClick();
      instance.updateAgentConnectors();
      assertEquals(true, instance.isConnectorsValid());
   }

   @Test
   public void testCreateGraphPanel()
   {
      System.out.println("createGraphPanel");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      GraphPanel result = instance.createGraphPanel();
      assertNotNull(result);
   }

   @Test
   public void testGetLabelResource()
   {
      System.out.println("getLabelResource");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      String expResult = "performanceMonitoring";
      String result = instance.getLabelResource();
      assertEquals(expResult, result);
   }

   @Test
   public void testGetStaticLabel()
   {
      System.out.println("getStaticLabel");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      String expResult = "test";
      String result = instance.getStaticLabel();
      assertEquals(expResult, result);
   }

   @Test
   public void testCreateTestElement()
   {
      System.out.println("createTestElement");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      TestElement result = instance.createTestElement();
      assertTrue(result instanceof PerformanceMonitoringTestElement);
   }

   @Test
   public void testModifyTestElement()
   {
      System.out.println("modifyTestElement");
      PerformanceMonitoringTestElement te = new PerformanceMonitoringTestElement();
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.addRowButton.doClick();
      instance.modifyTestElement(te);

      CollectionProperty data = (CollectionProperty) PerformanceMonitoringTestElement.tableModelToCollectionProperty((PowerTableModel) instance.grid.getModel());
      assertEquals(instance.grid.getModel().getColumnCount(), data.size());
      assertEquals(instance.grid.getModel().getRowCount(), ((List<?>) data.get(0).getObjectValue()).size());
   }

   @Test
   public void testConfigure()
   {
      System.out.println("configure");
      PerformanceMonitoringTestElement pte = new PerformanceMonitoringTestElement();
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.configure(pte);
      assertNotNull(pte.gui);
   }

   @Test
   public void testConfigure_NonEmpty()
   {
      System.out.println("configure");
      PerformanceMonitoringTestElement pte = new PerformanceMonitoringTestElement();
      pte.setData(PerformanceMonitoringTestElement.tableModelToCollectionProperty(dataModel));
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.configure(pte);
      assertNotNull(pte.gui);
   }

   @Test
   public void testUpdateGui_0args()
   {
      System.out.println("updateGui");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.updateGui();
   }

   @Test
   public void testUpdateGui_Sample()
   {
      System.out.println("updateGui");
      Sample sample = null;
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.updateGui(sample);
   }

   @Test
   public void testClearData()
   {
      System.out.println("clearData");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.clearData();
   }

   @Test
   public void testTableChanged()
   {
      System.out.println("tableChanged");
      TableModelEvent e = null;
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.tableChanged(e);
   }

   @Test
   public void testEditingStopped()
   {
      System.out.println("editingStopped");
      ChangeEvent e = null;
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.editingStopped(e);
   }

   @Test
   public void testEditingCanceled()
   {
      System.out.println("editingCanceled");
      ChangeEvent e = null;
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.editingCanceled(e);
   }

   @Test
   public void testTestStarted()
   {
      System.out.println("testStarted");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.testStarted();
   }

   @Test
   public void testTestEnded()
   {
      System.out.println("testEnded");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.testEnded();
   }

   @Test
   public void testAddRow()
   {
      // Just do like UltimateThreadGroup test does
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.addRowButton.doClick();
      instance.grid.editCellAt(0, 0);
      instance.addRowButton.doClick();
   }

   @Test
   public void testDeleteRow()
   {
      // Just do like UltimateThreadGroup test does
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.addRowButton.doClick();
      instance.addRowButton.doClick();
      instance.grid.editCellAt(0, 0);
      instance.deleteRowButton.doClick();
      instance.deleteRowButton.doClick();
   }

   public class AbstractPerformanceMonitoringGuiImpl
         extends AbstractPerformanceMonitoringGui
   {
      public String getStaticLabel()
      {
         return "test";
      }

      public void testStarted()
      {
      }

      public void testEnded()
      {
      }
   }
}
