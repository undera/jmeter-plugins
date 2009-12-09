/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author APC
 */
public class DotChartColoredRowTest
{

   public DotChartColoredRowTest()
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
      DotChartColoredRow instance = new DotChartColoredRow("TEST", Color.BLACK);
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
      DotChartColoredRow instance = new DotChartColoredRow("TEST", Color.BLACK);
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
      DotChartColoredRow instance = new DotChartColoredRow("TEST", Color.BLACK);
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
      DotChartColoredRow instance = new DotChartColoredRow("TEST", Color.BLACK);
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
      DotChartColoredRow instance = new DotChartColoredRow("TEST", Color.BLACK);
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
      DotChartColoredRow instance = new DotChartColoredRow("TEST", Color.BLACK);

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
      DotChartColoredRow instance = new DotChartColoredRow("TEST", Color.BLACK);

      SampleResult res1 = new SampleResult(1, 1);
      instance.addSample(res1);
      SampleResult res2 = new SampleResult(2, 2);
      instance.addSample(res2);

      double expResult = 1.5;
      double result = instance.getAvgTime();
      assertEquals(expResult, result, 0.01);
   }

   /**
    * Test of getAveragesByThreads method, of class DotChartColoredRow.
    */
   @Test
   public void testGetAvgTimeByThreads()
   {
      System.out.println("getAvgTimeByThreads");
      DotChartColoredRow instance = new DotChartColoredRow("TEST", Color.BLACK);

      add6Samples(instance);

      Vector result = instance.getAveragesByThreads();
      assertEquals(6, result.size());

      assertEquals(2, ((DotChartAverageValues) result.elementAt(2)).getCount());
      assertEquals(2, ((DotChartAverageValues) result.elementAt(5)).getCount());
      assertEquals(2, ((DotChartAverageValues) result.elementAt(1)).getCount());

      assertEquals(1.5, ((DotChartAverageValues) result.elementAt(2)).getAvgTime(), 0.01);
      assertEquals(3.5, ((DotChartAverageValues) result.elementAt(5)).getAvgTime(), 0.01);
      assertEquals(4.5, ((DotChartAverageValues) result.elementAt(1)).getAvgTime(), 0.01);
   }

   private void add6Samples(DotChartColoredRow row)
   {
      SampleResult res1 = new SampleResult(1, 1);
      res1.setAllThreads(2);
      row.addSample(res1);

      SampleResult res2 = new SampleResult(2, 2);
      res2.setAllThreads(2);
      row.addSample(res2);

      SampleResult res3 = new SampleResult(3, 3);
      res3.setAllThreads(5);
      row.addSample(res3);

      SampleResult res4 = new SampleResult(4, 4);
      res4.setAllThreads(5);
      row.addSample(res4);

      SampleResult res5 = new SampleResult(5, 5);
      res5.setAllThreads(1);
      row.addSample(res5);

      SampleResult res6 = new SampleResult(6, 4);
      res6.setAllThreads(1);
      row.addSample(res6);
   }

   /**
    * Test of getAveragesByThreads method, of class DotChartColoredRow.
    */
   @Test
   public void testGetAveragesByThreads()
   {
      System.out.println("getAveragesByThreads");
      DotChartColoredRow instance = new DotChartColoredRow("TEST", Color.BLACK);
      Vector result = instance.getAveragesByThreads();
      assertNotNull(result);

      add6Samples(instance);
      result = instance.getAveragesByThreads();
      assertNotNull(result);
      DotChartAverageValues val = (DotChartAverageValues) result.elementAt(5);
      assertNotNull(val);
      assertEquals(1458.333, val.getAvgThroughput(), 0.01);
      assertEquals(3.5, val.getAvgTime(), 0.01);
   }

   /**
    * Test of getAvgThoughput method, of class DotChartColoredRow.
    */
   @Test
   public void testGetAvgThoughput()
   {
      System.out.println("getAvgThoughput");
      DotChartColoredRow instance = new DotChartColoredRow("TEST", Color.BLACK);

      double expResult = 0.0;
      double result = instance.getAvgThoughput();
      assertEquals(expResult, result, 0.0);

      double expResult2 = 1061.1111;
      add6Samples(instance);
      double result2 = instance.getAvgThoughput();
      assertEquals(expResult2, result2, 0.01);

   }
}
