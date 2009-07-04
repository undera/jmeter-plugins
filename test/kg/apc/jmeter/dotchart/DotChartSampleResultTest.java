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
public class DotChartSampleResultTest {

    public DotChartSampleResultTest() {
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
   public void testGetThreads()
   {
      System.out.println("getThreads");
      DotChartSampleResult instance = new DotChartSampleResult();
      int expResult = 0;
      int result = instance.getThreads();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testGetTime()
   {
      System.out.println("getTime");
      DotChartSampleResult instance = new DotChartSampleResult();
      long expResult = 0L;
      long result = instance.getTime();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testGetLabel()
   {
      System.out.println("getLabel");
      DotChartSampleResult instance = new DotChartSampleResult();
      String expResult = "";
      String result = instance.getLabel();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testGetRepeatCount()
   {
      System.out.println("getRepeatCount");
      DotChartSampleResult instance = new DotChartSampleResult();
      int expResult = 0;
      int result = instance.getRepeatCount();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testAddRepeat()
   {
      System.out.println("addRepeat");
      DotChartSampleResult instance = new DotChartSampleResult();
      instance.addRepeat();
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

}