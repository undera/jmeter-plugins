package kg.apc.jmeter.dotchart;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
      SampleResult res = new SampleResult();
      res.setSampleLabel("Test1");
      res.setBytes(200);
      res.setGroupThreads(1);

      DotChartModel instance = new DotChartModel();

      assertEquals(0, instance.size());
      instance.addSample(res);
      assertEquals(1, instance.size());
      instance.addSample(res);
      assertEquals(1, instance.size());

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
      DotChartModel instance = new DotChartModel();

      SampleResult res = new SampleResult();
      res.setSampleLabel("Test1");
      res.setGroupThreads(1);
      instance.addSample(res);

      assertEquals(1, instance.size());
      assertTrue(instance.get("Test1") instanceof DotChartColoredRow);

      res = new SampleResult();
      res.setSampleLabel("Test2");
      res.setGroupThreads(1);
      instance.addSample(res);

      assertEquals(2, instance.size());
      assertTrue(instance.get("Test2") instanceof DotChartColoredRow);
      assertTrue(instance.get("Test1") instanceof DotChartColoredRow);
   }

   /**
    * Test of get method, of class DotChartModel.
    */
   @Test
   public void testGet()
   {
      System.out.println("get");
      DotChartModel instance = new DotChartModel();

      SampleResult sampleResult = new SampleResult();
      sampleResult.setSampleLabel("TEST");
      sampleResult.setGroupThreads(1);
      instance.addSample(sampleResult);

      DotChartColoredRow result = instance.get("TEST");
      assertTrue(result instanceof DotChartColoredRow);
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
      res1.setGroupThreads(3);
      instance.addSample(res1);
      SampleResult res2 = new SampleResult();
      res2.setAllThreads(2);
      res2.setGroupThreads(2);
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

      SampleResult res1 = new SampleResult(1, 1);
      res1.setGroupThreads(1);
      instance.addSample(res1);
      SampleResult res2 = new SampleResult(2, 2);
      res2.setGroupThreads(2);
      instance.addSample(res2);

      long expResult = 2L;
      long result = instance.getMaxTime();
      assertEquals(expResult, result);
   }

   /**
    * Test of clear method, of class DotChartModel.
    */
   @Test
   public void testClear()
   {
      System.out.println("clear");
      DotChartModel instance = new DotChartModel();
      SampleResult res1 = new SampleResult();
      res1.setAllThreads(3);
      instance.addSample(res1);
      SampleResult res2 = new SampleResult();
      res2.setAllThreads(2);
      instance.addSample(res2);

      instance.clear();
      assertEquals(0, instance.getMaxThreads());
      assertEquals(0, instance.getMaxTime());
   }

   /**
    * Test of getMaxThroughput method, of class DotChartModel.
    */
   @Test
   public void testGetMaxThroughput()
   {
      System.out.println("getMaxThroughput");
      DotChartModel instance = new DotChartModel();

      SampleResult res1 = new SampleResult(1, 100);
      res1.setAllThreads(5);
      res1.setGroupThreads(5);
      instance.addSample(res1);
      SampleResult res2 = new SampleResult(2, 50);
      res2.setAllThreads(4);
      res2.setGroupThreads(4);
      instance.addSample(res2);

      double expResult = 20;
      double result = instance.getMaxThroughput();
      assertEquals(expResult, result, 0.0);
   }
}
