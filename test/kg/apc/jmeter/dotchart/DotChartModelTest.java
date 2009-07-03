package kg.apc.jmeter.dotchart;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
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
public class DotChartModelTest
{
   public DotChartModelTest()
   {
   }

   @BeforeClass
   public static void setUpClass()
        throws Exception
   {
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
    * Test of addSample method, of class DotChartModel.
    */
   @Test
   public void testAddSampleSuccess()
   {
      System.out.println("addSample");
      SampleResult res = new HTTPSampleResult();
      res.setSampleLabel("Test1");
      res.setBytes(200);

      DotChartModel instance = new DotChartModel();

      //assertNull(instance.getCurrentSample());
      assertEquals(0, instance.size());
      instance.addSample(res);
      assertEquals(1, instance.size());
      instance.addSample(res);
      assertEquals(1, instance.size());

      //assertNotNull(instance.getCurrentSample());
      //assertEquals(200, instance.getCurrentSample().getBytes());

      res.setSampleLabel("Test2");
      instance.addSample(res);
      assertEquals(2, instance.size());
   }

   /**
    * Test of addSample method, of class DotChartModel.
    */
   @Test
   public void testAddSample()
   {
      System.out.println("addSample");
      SampleResult res = new SampleResult();
      res.setSampleLabel("Test1");
      DotChartModel instance = new DotChartModel();
      instance.addSample(res);
      assertEquals(1, instance.size());
   }

   /**
    * Test of get method, of class DotChartModel.
    */
   @Test
   public void testGet()
   {
      System.out.println("get");
      String key = "";
      DotChartModel instance = new DotChartModel();
      SamplingStatCalculatorColored expResult = null;
      SamplingStatCalculatorColored result = instance.get(key);
      assertEquals(expResult, result);

      instance.addSample(new SampleResult());
      result = instance.get(key);
      assertTrue(result instanceof SamplingStatCalculatorColored);
   }

   /**
    * Test of getMaxThreads method, of class DotChartModel.
    */
   @Test
   public void testGetMaxThreads()
   {
      System.out.println("getMaxThreads");
      DotChartModel instance = new DotChartModel();
      SampleResult res1 = new SampleResult();
      res1.setAllThreads(3);
      instance.addSample(res1);
      SampleResult res2 = new SampleResult();
      res2.setAllThreads(2);
      instance.addSample(res2);

      int expResult = 3;
      int result = instance.getMaxThreads();
      assertEquals(expResult, result);
   }

   /**
    * Test of getMaxTime method, of class DotChartModel.
    */
   @Test
   public void testGetMaxTime()
   {
      System.out.println("getMaxTime");
      DotChartModel instance = new DotChartModel();

      SampleResult res1 = new SampleResult();
      res1.setTime(1);
      instance.addSample(res1);
      SampleResult res2 = new SampleResult();
      res2.setTime(2);
      instance.addSample(res2);

      long expResult = 2L;
      long result = instance.getMaxTime();
      assertEquals(expResult, result);
   }
}
