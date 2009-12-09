/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.dotchart;

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
public class DotChartAverageValuesTest
{

   public DotChartAverageValuesTest()
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

   @Test
   public void testGetCount()
   {
      System.out.println("getCount");
      DotChartAverageValues instance = new DotChartAverageValues();
      int expResult = 0;
      int result = instance.getCount();
      assertEquals(expResult, result);

      instance.addCount();
      expResult++;
      result = instance.getCount();
      assertEquals(expResult, result);
   }

   @Test
   public void testSetAvgTime()
   {
      System.out.println("setAvgTime");
      double l = 1.25;
      DotChartAverageValues instance = new DotChartAverageValues();
      instance.setAvgTime(l);
      assertEquals(l, instance.getAvgTime(), 0.01);
   }

   @Test
   public void testGetAvgTime()
   {
      System.out.println("getAvgTime");
      DotChartAverageValues instance = new DotChartAverageValues();
      double expResult = 1.25;
      instance.setAvgTime(expResult);
      double result = instance.getAvgTime();
      assertEquals(expResult, result, 0.01);
   }

   @Test
   public void testAddCount()
   {
      System.out.println("addCount");
      DotChartAverageValues instance = new DotChartAverageValues();
      instance.addCount();
      assertEquals(1, instance.getCount());
   }

   /**
    * Test of getAvgThroughput method, of class DotChartAverageValues.
    */
   @Test
   public void testGetAvgThroughput()
   {
      System.out.println("getAvgThroughput");
      DotChartAverageValues instance = new DotChartAverageValues();
      double expResult = 0.0;
      double result = instance.getAvgThroughput();
      assertEquals(expResult, result, 0.0);

      double expResult2 = 1;
      instance.setAvgThroughput(1);
      double result2 = instance.getAvgThroughput();
      assertEquals(expResult2, result2, 0.0);
   }

   /**
    * Test of setAvgThroughput method, of class DotChartAverageValues.
    */
   @Test
   public void testSetAvgThroughput()
   {
      System.out.println("setAvgThroughput");
      DotChartAverageValues instance = new DotChartAverageValues();
      double expResult2 = 1;
      instance.setAvgThroughput(1);
      double result2 = instance.getAvgThroughput();
      assertEquals(expResult2, result2, 0.0);
   }
}
