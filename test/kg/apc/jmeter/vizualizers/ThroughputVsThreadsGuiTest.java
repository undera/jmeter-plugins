package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.util.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ThroughputVsThreadsGuiTest
{
   public ThroughputVsThreadsGuiTest()
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
    * Test of getLabelResource method, of class ThroughputVsThreadsGui.
    */
   @Test
   public void testGetLabelResource()
   {
      System.out.println("getLabelResource");
      ThroughputVsThreadsGui instance = new ThroughputVsThreadsGui();
      String expResult = "ThroughputVsThreadsGui";
      String result = instance.getLabelResource();
      assertEquals(expResult, result);
   }

   /**
    * Test of getStaticLabel method, of class ThroughputVsThreadsGui.
    */
   @Test
   public void testGetStaticLabel()
   {
      System.out.println("getStaticLabel");
      ThroughputVsThreadsGui instance = new ThroughputVsThreadsGui();
      String expResult = "Transaction Throughput vs Threads";
      String result = instance.getStaticLabel();
      assertEquals(expResult, result);
   }

   /**
    * Test of add method, of class ThroughputVsThreadsGui.
    */
   @Test
   public void testAdd()
   {
      System.out.println("add");
      ThroughputVsThreadsGui instance = new ThroughputVsThreadsGui();
      instance.add(new SampleResult());
      instance.add(new SampleResult(0, 100));
      instance.add(new SampleResult(0, 100));
   }
}
