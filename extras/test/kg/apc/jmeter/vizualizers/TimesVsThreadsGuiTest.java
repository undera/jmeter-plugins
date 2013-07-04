package kg.apc.jmeter.vizualizers;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.testelement.TestElement;
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
public class TimesVsThreadsGuiTest
{
   /**
    *
    */
   public TimesVsThreadsGuiTest()
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
    * Test of getLabelResource method, of class TimesVsThreadsGui.
    */
   @Test
   public void testGetLabelResource()
   {
      System.out.println("getLabelResource");
      TimesVsThreadsGui instance = new TimesVsThreadsGui();
      String expResult = "TimesVsThreadsGui";
      String result = instance.getLabelResource();
      assertEquals(expResult, result);
   }

   /**
    * Test of getStaticLabel method, of class TimesVsThreadsGui.
    */
   @Test
   public void testGetStaticLabel()
   {
      System.out.println("getStaticLabel");
      TimesVsThreadsGui instance = new TimesVsThreadsGui();
      String result = instance.getStaticLabel();
      assertTrue(result.length()>0);
   }

   /**
    * Test of add method, of class TimesVsThreadsGui.
    */
   @Test
   public void testAdd()
   {
      System.out.println("add");
      SampleResult res = new SampleResult();
      TimesVsThreadsGui instance = new TimesVsThreadsGui();
      instance.add(res);
      instance.add(res);
   }

   @Test
   public void testCreateTestElement()
   {
      System.out.println("createTestElement");
      TimesVsThreadsGui instance = new TimesVsThreadsGui();
      TestElement result = instance.createTestElement();
      assertNotNull(result);
   }

   @Test
   public void testModifyTestElement()
   {
      System.out.println("modifyTestElement");
      TestElement c = new ResultCollector();
      TimesVsThreadsGui instance = new TimesVsThreadsGui();
      instance.modifyTestElement(c);
   }

   @Test
   public void testConfigure()
   {
      System.out.println("configure");
      TestElement el = new ResultCollector();
      TimesVsThreadsGui instance = new TimesVsThreadsGui();
      instance.configure(el);
   }

   @Test
   public void testGetSettingsPanel()
   {
      System.out.println("getSettingsPanel");
      TimesVsThreadsGui instance = new TimesVsThreadsGui();
      JSettingsPanel result = instance.createSettingsPanel();
      assertNotNull(result);
   }

    /**
     * Test of getWikiPage method, of class TimesVsThreadsGui.
     */
    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        TimesVsThreadsGui instance = new TimesVsThreadsGui();
        String expResult = "";
        String result = instance.getWikiPage();
        assertTrue(result.length()>0);
    }

    /**
     * Test of createSettingsPanel method, of class TimesVsThreadsGui.
     */
    @Test
    public void testCreateSettingsPanel() {
        System.out.println("createSettingsPanel");
        TimesVsThreadsGui instance = new TimesVsThreadsGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }
}
