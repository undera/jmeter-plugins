package kg.apc.jmeter.dotchart;

import java.awt.Color;
import java.util.Vector;
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
public class SamplingStatCalculatorColoredTest
{
   public SamplingStatCalculatorColoredTest()
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
    * Test of getColor method, of class DotChartColoredRow.
    */
   @Test
   public void testGetColor()
   {
      System.out.println("getColor");
      DotChartColoredRow instance = new DotChartColoredRow("TEST");
      Color result = instance.getColor();
      assertNotNull(result);
   }

   /**
    * Test of addSample method, of class DotChartColoredRow.
    */
   @Test
   public void testAddSample()
   {
      System.out.println("addSample");
      SampleResult res = new SampleResult();
      DotChartColoredRow instance = new DotChartColoredRow("TEST");
      instance.addSample(res);
      assertEquals(1, instance.getCount());
   }

   /**
    * Test of getCount method, of class DotChartColoredRow.
    */
   @Test
   public void testGetCount()
   {
      System.out.println("getCount");
      DotChartColoredRow instance = new DotChartColoredRow("TEST");
      int expResult = 0;
      int result = instance.getCount();
      assertEquals(expResult, result);
   }

   /**
    * Test of getLabel method, of class DotChartColoredRow.
    */
   @Test
   public void testGetLabel()
   {
      System.out.println("getLabel");
      DotChartColoredRow instance = new DotChartColoredRow("TEST");
      String expResult = "TEST";
      String result = instance.getLabel();
      assertEquals(expResult, result);
   }

   /**
    * Test of getSample method, of class DotChartColoredRow.
    */
   @Test
   public void testGetSample()
   {
      System.out.println("getSample");
      DotChartColoredRow instance = new DotChartColoredRow("TEST");
      SampleResult expResult = new SampleResult();
      instance.addSample(expResult);

      int count = 0;
      DotChartSampleResult result = instance.getSample(count);
      assertNotNull(result);
   }

   /**
    * Test of getAvgThreads method, of class DotChartColoredRow.
    */
   @Test
   public void testGetAvgThreads()
   {
      System.out.println("getAvgThreads");
      DotChartColoredRow instance = new DotChartColoredRow("TEST");

      SampleResult res1 = new SampleResult();
      res1.setAllThreads(1);
      instance.addSample(res1);
      SampleResult res2 = new SampleResult();
      res2.setAllThreads(2);
      instance.addSample(res2);

      double expResult = 1.5;
      double result = instance.getAvgThreads();
      assertEquals(expResult, result, 0.01);
   }

   /**
    * Test of getAvgTime method, of class DotChartColoredRow.
    */
   @Test
   public void testGetAvgTime()
   {
      System.out.println("getAvgTime");
      DotChartColoredRow instance = new DotChartColoredRow("TEST");

      SampleResult res1 = new SampleResult();
      res1.setTime(1);
      instance.addSample(res1);
      SampleResult res2 = new SampleResult();
      res2.setTime(2);
      instance.addSample(res2);

      double expResult = 1.5;
      double result = instance.getAvgTime();
      assertEquals(expResult, result, 0.01);
   }

   /**
    * Test of getAvgTimeByThreads method, of class DotChartColoredRow.
    */
   @Test
   public void testGetAvgTimeByThreads()
   {
      System.out.println("getAvgTimeByThreads");
      DotChartColoredRow instance = new DotChartColoredRow("TEST");
      
      add6Samples(instance);

      Vector result = instance.getAvgTimeByThreads();
      assertEquals(6, result.size());

      assertEquals(2, ((DotChartAverageValues)result.elementAt(2)).getCount());
      assertEquals(2, ((DotChartAverageValues)result.elementAt(5)).getCount());
      assertEquals(2, ((DotChartAverageValues)result.elementAt(1)).getCount());

      assertEquals(1.5, ((DotChartAverageValues)result.elementAt(2)).getAvgTime(), 0.01);
      assertEquals(3.5, ((DotChartAverageValues)result.elementAt(5)).getAvgTime(), 0.01);
      assertEquals(4.5, ((DotChartAverageValues)result.elementAt(1)).getAvgTime(), 0.01);
   }

   private void add6Samples(DotChartColoredRow i1)
   {
      SampleResult res1 = new SampleResult();
      res1.setTime(1);
      res1.setAllThreads(2);
      i1.addSample(res1);

      SampleResult res2 = new SampleResult();
      res2.setAllThreads(2);
      res2.setTime(2);
      i1.addSample(res2);

      SampleResult res3 = new SampleResult();
      res3.setAllThreads(5);
      res3.setTime(3);
      i1.addSample(res3);

      SampleResult res4 = new SampleResult();
      res4.setAllThreads(5);
      res4.setTime(4);
      i1.addSample(res4);

      SampleResult res5 = new SampleResult();
      res5.setAllThreads(1);
      res5.setTime(5);
      i1.addSample(res5);
      
      SampleResult res6 = new SampleResult();
      res6.setAllThreads(1);
      res6.setTime(4);
      i1.addSample(res6);
   }
   /**
    * Test of getMaxThreads method, of class DotChartColoredRow.
    */
   /*
   @Test
   public void testGetMaxThreads()
   {
   System.out.println("getMaxThreads");
   DotChartColoredRow instance = new DotChartColoredRow("TEST");

   SampleResult res1=new SampleResult();
   res1.setAllThreads(3);
   instance.addSample(res1);
   SampleResult res2=new SampleResult();
   res2.setAllThreads(2);
   instance.addSample(res2);


   int expResult = 3;
   int result = instance.getMaxThreads();
   assertEquals(expResult, result);
   }

   /**
    * Test of getMaxTime method, of class DotChartColoredRow.
    */
   /*
   @Test
   public void testGetMaxTime()
   {
   System.out.println("getMaxTime");
   DotChartColoredRow instance = new DotChartColoredRow("TEST");

   SampleResult res1=new SampleResult();
   res1.setTime(1);
   instance.addSample(res1);
   SampleResult res2=new SampleResult();
   res2.setTime(2);
   instance.addSample(res2);

   double expResult = 2;
   long result = instance.getMaxTime();
   assertEquals(expResult, result, 0.01);
   }
    */
}
