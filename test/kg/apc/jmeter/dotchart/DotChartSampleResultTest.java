/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.dotchart;

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

    /**
     * Test of getThreads method, of class DotChartSampleResult.
     */
    @Test
    public void testGetThreads()
    {
        System.out.println("getThreads");
        DotChartSampleResult instance = new DotChartSampleResult();
        int expResult = 0;
        int result = instance.getThreads();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTime method, of class DotChartSampleResult.
     */
    @Test
    public void testGetTime()
    {
        System.out.println("getTime");
        DotChartSampleResult instance = new DotChartSampleResult();
        long expResult = 0L;
        long result = instance.getTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLabel method, of class DotChartSampleResult.
     */
    @Test
    public void testGetLabel()
    {
        System.out.println("getLabel");
        DotChartSampleResult instance = new DotChartSampleResult();
        String expResult = null;
        String result = instance.getLabel();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRepeatCount method, of class DotChartSampleResult.
     */
    @Test
    public void testGetRepeatCount()
    {
        System.out.println("getRepeatCount");
        DotChartSampleResult instance = new DotChartSampleResult();
        int expResult = 1;
        int result = instance.getRepeatCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of addRepeat method, of class DotChartSampleResult.
     */
    @Test
    public void testAddRepeat()
    {
        System.out.println("addRepeat");
        DotChartSampleResult instance = new DotChartSampleResult();
        assertEquals(1, instance.getRepeatCount());
        instance.addRepeat();
        assertEquals(2, instance.getRepeatCount());
        instance.addRepeat();
        assertEquals(3, instance.getRepeatCount());
    }

   /**
    * Test of getThroughput method, of class DotChartSampleResult.
    */
   @Test
   public void testGetThroughputSingle()
   {
      System.out.println("getThroughput");

      SampleResult res=new SampleResult(1, 25);
      res.setAllThreads(1);
      System.out.println(res.getAllThreads());
      DotChartSampleResult instance = new DotChartSampleResult(res);
      double expResult = 40;
      double result = instance.getThroughput();
      assertEquals(expResult, result, 0.0);
   }

   /**
    * Test of getThroughput method, of class DotChartSampleResult.
    */
   @Test
   public void testGetThroughputMany()
   {
      System.out.println("getThroughput");

      SampleResult res=new SampleResult(1, 25);
      res.setAllThreads(5);
      DotChartSampleResult instance = new DotChartSampleResult(res);
      double expResult = 200;
      double result = instance.getThroughput();
      assertEquals(expResult, result, 0.0);
   }
}