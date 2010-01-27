package kg.apc.jmeter.vizualizers;

import java.awt.Image;
import kg.apc.jmeter.util.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.Sample;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractGraphPanelVisualizerTest
{
   public AbstractGraphPanelVisualizerTest()
   {
   }

   @BeforeClass
   public static void setUpClass()
        throws Exception
   {
      TestJMeterUtils.createJmeterEnv();
   }

   @AfterClass
   public static void tearDownClass()
        throws Exception
   {
   }

   @Before
   public void setUp()
   {
   }

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
   }
}
