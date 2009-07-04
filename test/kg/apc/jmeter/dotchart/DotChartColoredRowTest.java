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
public class DotChartColoredRowTest {

    public DotChartColoredRowTest() {
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
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

   @Test
   public void testGetColor()
   {
      System.out.println("getColor");
      DotChartColoredRow instance = null;
      Color expResult = null;
      Color result = instance.getColor();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testAddSample()
   {
      System.out.println("addSample");
      SampleResult res = null;
      DotChartColoredRow instance = null;
      instance.addSample(res);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testGetCount()
   {
      System.out.println("getCount");
      DotChartColoredRow instance = null;
      int expResult = 0;
      int result = instance.getCount();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testGetLabel()
   {
      System.out.println("getLabel");
      DotChartColoredRow instance = null;
      String expResult = "";
      String result = instance.getLabel();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testGetSample()
   {
      System.out.println("getSample");
      int count = 0;
      DotChartColoredRow instance = null;
      DotChartSampleResult expResult = null;
      DotChartSampleResult result = instance.getSample(count);
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testGetAvgThreads()
   {
      System.out.println("getAvgThreads");
      DotChartColoredRow instance = null;
      double expResult = 0.0;
      double result = instance.getAvgThreads();
      assertEquals(expResult, result, 0.0);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testGetAvgTime()
   {
      System.out.println("getAvgTime");
      DotChartColoredRow instance = null;
      double expResult = 0.0;
      double result = instance.getAvgTime();
      assertEquals(expResult, result, 0.0);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testGetAvgTimeByThreads()
   {
      System.out.println("getAvgTimeByThreads");
      DotChartColoredRow instance = null;
      Vector expResult = null;
      Vector result = instance.getAvgTimeByThreads();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

}