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
public class AverageValuesTest {

    public AverageValuesTest() {
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
   public void testGetCount()
   {
      System.out.println("getCount");
      DotChartAverageValues instance = new DotChartAverageValues();
      int expResult = 0;
      int result = instance.getCount();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testSetAvgTime()
   {
      System.out.println("setAvgTime");
      double l = 0.0;
      DotChartAverageValues instance = new DotChartAverageValues();
      instance.setAvgTime(l);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testGetAvgTime()
   {
      System.out.println("getAvgTime");
      DotChartAverageValues instance = new DotChartAverageValues();
      double expResult = 0.0;
      double result = instance.getAvgTime();
      assertEquals(expResult, result, 0.0);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testAddCount()
   {
      System.out.println("addCount");
      DotChartAverageValues instance = new DotChartAverageValues();
      instance.addCount();
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

}