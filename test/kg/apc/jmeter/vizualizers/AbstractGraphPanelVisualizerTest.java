package kg.apc.jmeter.vizualizers;

import java.awt.Image;
import kg.apc.jmeter.charting.GraphPanelChart;
import kg.apc.jmeter.util.TestJMeterUtils;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.LongProperty;
import org.apache.jmeter.visualizers.Sample;
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
public class AbstractGraphPanelVisualizerTest
{
   /**
    *
    */
   public AbstractGraphPanelVisualizerTest()
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
   }

   /**
    * 
    */
   @After
   public void tearDown()
   {
   }

   /**
    * Test of createGraphPanel method, of class AbstractGraphPanelVisualizer.
    */
   @Test
   public void testCreateGraphPanel()
   {
      System.out.println("createGraphPanel");
      AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
      GraphPanel result = instance.createGraphPanel();
      assertNotNull(result);
   }

   /**
    * Test of updateGui method, of class AbstractGraphPanelVisualizer.
    */
   @Test
   public void testUpdateGui_Sample()
   {
      System.out.println("updateGui");
      Sample sample = null;
      AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
      instance.updateGui(sample);
   }

   /**
    * Test of updateGui method, of class AbstractGraphPanelVisualizer.
    */
   @Test
   public void testUpdateGui_0args()
   {
      System.out.println("updateGui");
      AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
      instance.updateGui();
   }

   /**
    * Test of clearData method, of class AbstractGraphPanelVisualizer.
    */
   @Test
   public void testClearData()
   {
      System.out.println("clearData");
      AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
      instance.clearData();
   }

   /**
    * Test of getImage method, of class AbstractGraphPanelVisualizer.
    */
   @Test
   public void testGetImage()
   {
      System.out.println("getImage");
      AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
      Image result = instance.getImage();
      assertNull(result); // in unit test we'll have null here
   }

   /**
    *
    */
   public class AbstractGraphPanelVisualizerImpl
         extends AbstractGraphPanelVisualizer
   {
      public String getLabelResource()
      {
         return "test";
      }

      public void add(SampleResult sample)
      {
         return;
      }

        @Override
        protected JSettingsPanel getSettingsPanel()
        {
            return new JSettingsPanel(this, true, true, true, true, true);
        }
   }

   @Test
   public void testGetGranulation()
   {
      System.out.println("getGranulation");
      AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
      long result = instance.getGranulation();
      assertTrue(result > 0);
   }

   @Test
   public void testGetGranulation_2()
   {
      System.out.println("getGranulation");
      AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
      try
      {
         instance.setGranulation(0);
         fail("Exception expected");
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   @Test
   public void testSetGranulation()
   {
      System.out.println("setGranulation");
      int i = 100;
      AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
      instance.setGranulation(i);
   }

   @Test
   public void testCreateTestElement()
   {
      System.out.println("createTestElement");
      AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
      TestElement result = instance.createTestElement();
      assertNotNull(result);
   }

   @Test
   public void testModifyTestElement()
   {
      System.out.println("modifyTestElement");
      TestElement c = new ResultCollector();
      AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
      instance.modifyTestElement(c);
   }

   @Test
   public void testConfigure()
   {
      System.out.println("configure");
      TestElement el = new ResultCollector();
      el.setProperty(new LongProperty(AbstractGraphPanelVisualizer.INTERVAL_PROPERTY, 10000));
      AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
      instance.configure(el);
   }

   @Test
   public void testGetSettingsPanel()
   {
      System.out.println("getSettingsPanel");
      AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
      JSettingsPanel result = instance.getSettingsPanel();
      assertNotNull(result);
   }

   @Test
   public void testGetGraphPanelChart()
   {
      System.out.println("getGraphPanelChart");
      AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
      GraphPanelChart result = instance.getGraphPanelChart();
      assertNotNull(result);
   }

    /**
     * Test of switchModel method, of class AbstractGraphPanelVisualizer.
     */
    @Test
    public void testSwitchModel()
    {
        System.out.println("switchModel");
        boolean aggregate = false;
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        instance.switchModel(aggregate);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
