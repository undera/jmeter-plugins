package kg.apc.jmeter.vizualizers;

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
public class SamplesVsThreadsGuiTest
{
   /**
    *
    */
   public SamplesVsThreadsGuiTest()
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
   }
}
