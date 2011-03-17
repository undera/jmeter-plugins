package kg.apc.jmeter.vizualizers;

import java.util.concurrent.ConcurrentSkipListMap;
import kg.apc.emulators.TestJMeterUtils;
import javax.swing.JComponent;
import java.awt.Image;
import javax.swing.event.ChangeEvent;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.GraphPanelChart;
import kg.apc.jmeter.charting.GraphRowOverallAverages;
import org.apache.jmeter.samplers.SampleResult;
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
public class GraphPanelTest
{
   private GraphPanel instance;

   /**
    *
    */
   public GraphPanelTest()
   {
   }

   /**
    *
    * @throws Exception
    */
   @BeforeClass
   public static void setUpClass()
        throws Exception
   {
       TestJMeterUtils.createJmeterEnv();
   }

   /**
    *
    * @throws Exception
    */
   @AfterClass
   public static void tearDownClass()
        throws Exception
   {
   }

   /**
    *
    */
   @Before
   public void setUp()
   {
      instance = new GraphPanel();
      instance.getSettingsTab().add(new JSettingsPanel(new AbstractGraphPanelVisualizerImpl(), JSettingsPanel.GRADIENT_OPTION));
      instance.getGraphObject().setRows(new ConcurrentSkipListMap<String, AbstractGraphRow>());
   }

   /**
    *
    */
   @After
   public void tearDown()
   {
   }

   /**
    * Test of updateGui method, of class GraphPanel.
    */
   @Test
   public void testUpdateGui_Graphs()
   {
      System.out.println("updateGui");
      instance.setSelectedIndex(0);
      instance.updateGui();
      instance.setSelectedIndex(1);
      instance.updateGui();
      instance.setSelectedIndex(2);
      instance.updateGui();
   }

   /**
    * Test of getGraphImage method, of class GraphPanel.
    */
   @Test
   public void testGetGraphImage()
   {
      System.out.println("getGraphImage");
      Image expResult = null;
      Image result = instance.getGraphImage();
      assertEquals(expResult, result);
   }

   /**
    * Test of getGraphObject method, of class GraphPanel.
    */
   @Test
   public void testGetGraphObject()
   {
      System.out.println("getGraphObject");
      GraphPanelChart result = instance.getGraphObject();
      assertEquals(GraphPanelChart.class.getName(), result.getClass().getName());
   }

   /**
    * Test of addRow method, of class GraphPanel.
    */
   @Test
   public void testAddRow()
   {
      System.out.println("addRow");
      AbstractGraphRow row = new GraphRowOverallAverages();
      instance.addRow(row);
   }

   /**
    * Test of stateChanged method, of class GraphPanel.
    */
   @Test
   public void testStateChanged()
   {
      System.out.println("stateChanged");
      ChangeEvent e = null;
      //instance.stateChanged(e);
   }

   /**
    * Test of clearRowsTab method, of class GraphPanel.
    */
   @Test
   public void testClearRowsTab()
   {
      System.out.println("clearRowsTab");
      instance.clearRowsTab();
   }

   @Test
   public void testUpdateGui()
   {
      System.out.println("updateGui");
      instance.updateGui();
   }

   @Test
   public void testGetSettingsTab()
   {
      System.out.println("getSettingsTab");
      JComponent result = instance.getSettingsTab();
      assertNotNull(result);
   }

        public class AbstractGraphPanelVisualizerImpl
         extends AbstractGraphPanelVisualizer
   {
      public String getLabelResource()
      {
         return "test";
      }

              @Override
        protected JSettingsPanel getSettingsPanel()
        {
            return new JSettingsPanel(this, JSettingsPanel.GRADIENT_OPTION);
        }

        @Override
        public void add(SampleResult sr)
        {
        }

        @Override
        public String getWikiPage() {
            return "";
        }
   }
}
