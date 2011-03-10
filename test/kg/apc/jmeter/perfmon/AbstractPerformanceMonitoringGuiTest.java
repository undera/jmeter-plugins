package kg.apc.jmeter.perfmon;

import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import kg.apc.jmeter.charting.GraphPanelChart;
import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.vizualizers.GraphPanel;
import kg.apc.jmeter.vizualizers.JSettingsPanel;
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
      //instance.
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


   public class AbstractPerformanceMonitoringGuiImpl
            extends AbstractPerformanceMonitoringGui
    {

        public String getStaticLabel()
        {
            return "test";
        }

        public void addPerfRecord(String serverName, double value)
        {
        }

        @Override
        public void addPerfRecord(String serverName, double value, long time)
        {
        }

        @Override
        public void setErrorMessage(String msg)
        {
        }

        @Override
        public void clearErrorMessage()
        {
        }

        public void setChartType(int monitorType)
        {
        }

         @Override
        public void setLoadMenuEnabled(boolean enabled)
        {
        }

        @Override
        protected JSettingsPanel getSettingsPanel()
        {
            return new JSettingsPanel(this, true, true, true, true, true);
        }

        @Override
        public String getWikiPage() {
           return  "";
        }
    }

   @Test
   public void testGetSettingsPanel()
   {
      System.out.println("getSettingsPanel");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      JSettingsPanel result = instance.getSettingsPanel();
      assertNotNull(result);
   }

   @Test
   public void testGetGranulation()
   {
      System.out.println("getGranulation");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      int expResult = 1000;
      int result = instance.getGranulation();
      assertEquals(expResult, result);
   }

   @Test
   public void testSetGranulation()
   {
      System.out.println("setGranulation");
      int granulation = 0;
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      instance.setGranulation(granulation);
   }

   @Test
   public void testGetGraphPanelChart()
   {
      System.out.println("getGraphPanelChart");
      AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
      GraphPanelChart result = instance.getGraphPanelChart();
      assertNotNull(result);
   }

    /**
     * Test of addPerfRecord method, of class AbstractPerformanceMonitoringGui.
     */
    @Test
    public void testAddPerfRecord_String_double()
    {
        System.out.println("addPerfRecord");
        String serverName = "localhost";
        double value = 0.0;
        AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
        instance.addPerfRecord(serverName, value);
    }

    /**
     * Test of addPerfRecord method, of class AbstractPerformanceMonitoringGui.
     */
    @Test
    public void testAddPerfRecord_3args()
    {
        System.out.println("addPerfRecord");
        String serverName = "localhost";
        double value = 0.0;
        long time = 100000L;
        AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
        instance.addPerfRecord(serverName, value, time);
    }

    /**
     * Test of setErrorMessage method, of class AbstractPerformanceMonitoringGui.
     */
    @Test
    public void testSetErrorMessage()
    {
        System.out.println("setErrorMessage");
        String msg = "";
        AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
        instance.setErrorMessage(msg);
    }

    /**
     * Test of clearErrorMessage method, of class AbstractPerformanceMonitoringGui.
     */
    @Test
    public void testClearErrorMessage()
    {
        System.out.println("clearErrorMessage");
        AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
        instance.clearErrorMessage();
    }

    /**
     * Test of setChartType method, of class AbstractPerformanceMonitoringGui.
     */
    @Test
    public void testSetChartType()
    {
        System.out.println("setChartType");
        int monitorType = 0;
        AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
        instance.setChartType(monitorType);
    }

    /**
     * Test of getSelectedTypeIndex method, of class AbstractPerformanceMonitoringGui.
     */
    @Test
    public void testGetSelectedTypeIndex()
    {
        System.out.println("getSelectedTypeIndex");
        AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
        int expResult = 0;
        int result = instance.getSelectedTypeIndex();
        assertEquals(expResult, result);
    }

    /**
     * Test of switchModel method, of class AbstractPerformanceMonitoringGui.
     */
    @Test
    public void testSwitchModel()
    {
        System.out.println("switchModel");
        boolean aggregate = false;
        AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
        instance.switchModel(aggregate);
    }

    /**
     * Test of setLoadMenuEnabled method, of class AbstractPerformanceMonitoringGui.
     */
    @Test
    public void testSetLoadMenuEnabled()
    {
        System.out.println("setLoadMenuEnabled");
        boolean enabled = false;
        AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
        instance.setLoadMenuEnabled(enabled);
    }

    /**
     * Test of normalizeTime method, of class AbstractPerformanceMonitoringGui.
     */
    @Test
    public void testNormalizeTime() {
        System.out.println("normalizeTime");
        long time = 0L;
        AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
        long expResult = 0L;
        long result = instance.normalizeTime(time);
        assertEquals(expResult, result);
    }

    /**
     * Test of getWikiPage method, of class AbstractPerformanceMonitoringGui.
     */
    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        AbstractPerformanceMonitoringGui instance = new AbstractPerformanceMonitoringGuiImpl();
        String expResult = "";
        String result = instance.getWikiPage();
        assertEquals(expResult, result);
    }
}
