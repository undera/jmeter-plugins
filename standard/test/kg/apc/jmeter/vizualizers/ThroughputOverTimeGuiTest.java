/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.vizualizers;

import java.util.Collection;
import kg.apc.emulators.TestJMeterUtils;
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
public class ThroughputOverTimeGuiTest
{
   /**
    * 
    */
   public ThroughputOverTimeGuiTest()
   {
   }

   /**
    *
    * @throws Exception
    */
   @BeforeClass
   public static void setUpClass() throws Exception
   {
      TestJMeterUtils.createJmeterEnv();
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
    *
    */
   @Test
   public void testGetLabelResource()
   {
      System.out.println("getLabelResource");
      ThroughputOverTimeGui instance = new ThroughputOverTimeGui();
      String expResult = "ThroughputOverTimeGui";
      String result = instance.getLabelResource();
      assertEquals(expResult, result);
   }

   /**
    *
    */
   @Test
   public void testGetStaticLabel()
   {
      System.out.println("getStaticLabel");
      ThroughputOverTimeGui instance = new ThroughputOverTimeGui();
      String result = instance.getStaticLabel();
      assertTrue(result.length()>0);
   }

   /**
    *
    */
   @Test
   public void testAdd()
   {
      System.out.println("add");
      SampleResult res = new SampleResult();
      res.setAllThreads(1);
      res.setThreadName("test 1-2");
      ThroughputOverTimeGui instance = new ThroughputOverTimeGui();
      instance.add(res);
      res.sampleStart();
      try
      {
         Thread.sleep(10);
      }
      catch (InterruptedException ex)
      {
      }
      res.sampleEnd();
      instance.add(res);
   }

   @Test
   public void testGetSettingsPanel()
   {
      System.out.println("getSettingsPanel");
      ThroughputOverTimeGui instance = new ThroughputOverTimeGui();
      JSettingsPanel result = instance.createSettingsPanel();
      assertNotNull(result);
   }

    /**
     * Test of getWikiPage method, of class ThroughputOverTimeGui.
     */
    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        ThroughputOverTimeGui instance = new ThroughputOverTimeGui();
        String expResult = "";
        String result = instance.getWikiPage();
        assertTrue(result.length()>0);
    }

    /**
     * Test of createSettingsPanel method, of class ThroughputOverTimeGui.
     */
    @Test
    public void testCreateSettingsPanel() {
        System.out.println("createSettingsPanel");
        ThroughputOverTimeGui instance = new ThroughputOverTimeGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

    /**
     * Test of getMenuCategories method, of class ThroughputOverTimeGui.
     */
    @Test
    public void testGetMenuCategories() {
        System.out.println("getMenuCategories");
        ThroughputOverTimeGui instance = new ThroughputOverTimeGui();
        Collection result = instance.getMenuCategories();
        assertEquals(0, result.size());
    }
}
