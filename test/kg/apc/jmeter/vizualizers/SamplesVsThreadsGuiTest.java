package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.util.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class SamplesVsThreadsGuiTest
{
   public SamplesVsThreadsGuiTest()
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
    * Test of getLabelResource method, of class SamplesVsThreadsGui.
    */
   @Test
   public void testGetLabelResource()
   {
      System.out.println("getLabelResource");
      SamplesVsThreadsGui instance = new SamplesVsThreadsGui();
      String expResult = "SamplesVsThreadsGui";
      String result = instance.getLabelResource();
      assertEquals(expResult, result);
   }

   /**
    * Test of getStaticLabel method, of class SamplesVsThreadsGui.
    */
   @Test
   public void testGetStaticLabel()
   {
      System.out.println("getStaticLabel");
      SamplesVsThreadsGui instance = new SamplesVsThreadsGui();
      String expResult = "Samples vs Threads";
      String result = instance.getStaticLabel();
      assertEquals(expResult, result);
   }

   /**
    * Test of add method, of class SamplesVsThreadsGui.
    */
   @Test
   public void testAdd()
   {
      System.out.println("add");
      SampleResult res = new SampleResult();
      SamplesVsThreadsGui instance = new SamplesVsThreadsGui();
      instance.add(res);
   }
}
