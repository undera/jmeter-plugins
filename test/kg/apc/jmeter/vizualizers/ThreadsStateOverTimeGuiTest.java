package kg.apc.jmeter.vizualizers;

import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleResult;
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
public class ThreadsStateOverTimeGuiTest
{
   private ThreadsStateOverTimeGui instance;
   /**
    *
    */
   public ThreadsStateOverTimeGuiTest()
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
      instance = new ThreadsStateOverTimeGui();
   }

   /**
    *
    */
   @After
   public void tearDown()
   {
   }

   /**
    * Test of add method, of class ThreadsStateOverTimeGui.
    */
   @Test
   public void testAdd()
   {
      System.out.println("add");
      SampleResult res = new SampleResult();
      res.sampleStart();
      res.setAllThreads(1);
      res.setThreadName("test 1-2");
      res.sampleEnd();
      instance.add(res);
      
      try
      {
         Thread.sleep(2000);
      }
      catch (InterruptedException ex)
      {
         Logger.getLogger(ThreadsStateOverTimeGuiTest.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      SampleResult res2 = new SampleResult(res);
      res2.setAllThreads(1);
      res2.setThreadName("test 1-2");
      instance.add(res);
   }

   /**
    * Test of clearData method, of class ThreadsStateOverTimeGui.
    */
   @Test
   public void testClearData()
   {
      System.out.println("clearData");
      instance.clearData();
   }

   /**
    * Test of getImage method, of class ThreadsStateOverTimeGui.
    */
   @Test
   public void testGetImage()
   {
      System.out.println("getImage");
      Image expResult = null;
      Image result = instance.getImage();
      assertEquals(expResult, result);
   }

   /**
    * Test of updateGui method, of class ThreadsStateOverTimeGui.
    */
   @Test
   public void testUpdateGui_Sample()
   {
      System.out.println("updateGui");
      Sample sample = null;
      instance.updateGui(sample);
   }

   /**
    * Test of updateGui method, of class ThreadsStateOverTimeGui.
    */
   @Test
   public void testUpdateGui_0args()
   {
      System.out.println("updateGui");
      instance.updateGui();
   }

   /**
    * Test of getLabelResource method, of class ThreadsStateOverTimeGui.
    */
   @Test
   public void testGetLabelResource()
   {
      System.out.println("getLabelResource");
      String expResult = "ThreadsStateOverTimeGui";
      String result = instance.getLabelResource();
      assertEquals(expResult, result);
   }

   /**
    * Test of getStaticLabel method, of class ThreadsStateOverTimeGui.
    */
   @Test
   public void testGetStaticLabel()
   {
      System.out.println("getStaticLabel");
      String result = instance.getStaticLabel();
      assertTrue(result.length()>0);
   }

   @Test
   public void testGetSettingsPanel()
   {
      System.out.println("getSettingsPanel");
      JSettingsPanel result = instance.createSettingsPanel();
      assertNotNull(result);
   }

    /**
     * Test of getWikiPage method, of class ThreadsStateOverTimeGui.
     */
    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        String result = instance.getWikiPage();
        assertTrue(result.length()>0);
    }

    /**
     * Test of createSettingsPanel method, of class ThreadsStateOverTimeGui.
     */
    @Test
    public void testCreateSettingsPanel() {
        System.out.println("createSettingsPanel");
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

   /**
    * Test of setExtraChartSettings method, of class ThreadsStateOverTimeGui.
    */
   @Test
   public void testSetExtraChartSettings() {
      System.out.println("setExtraChartSettings");
      ThreadsStateOverTimeGui instance = new ThreadsStateOverTimeGui();
      instance.setExtraChartSettings();
      assertTrue(instance.getGraphPanelChart().getChartSettings().isDrawFinalZeroingLines());
   }
}
