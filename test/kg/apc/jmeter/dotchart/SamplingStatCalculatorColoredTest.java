/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.dotchart;

import java.awt.Color;
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
public class SamplingStatCalculatorColoredTest {

    public SamplingStatCalculatorColoredTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getColor method, of class SamplingStatCalculatorColored.
     */
    @Test
    public void testGetColor()
    {
        System.out.println("getColor");
        SamplingStatCalculatorColored instance = new SamplingStatCalculatorColored("TEST");
        Color result = instance.getColor();
        assertNotNull(result);
    }

    /**
     * Test of addSample method, of class SamplingStatCalculatorColored.
     */
    @Test
    public void testAddSample()
    {
        System.out.println("addSample");
        SampleResult res = new SampleResult();
        SamplingStatCalculatorColored instance = new SamplingStatCalculatorColored("TEST");
        instance.addSample(res);
        assertEquals(1, instance.getCount());
    }

    /**
     * Test of getCount method, of class SamplingStatCalculatorColored.
     */
    @Test
    public void testGetCount()
    {
        System.out.println("getCount");
        SamplingStatCalculatorColored instance = new SamplingStatCalculatorColored("TEST");
        int expResult = 0;
        int result = instance.getCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLabel method, of class SamplingStatCalculatorColored.
     */
    @Test
    public void testGetLabel()
    {
        System.out.println("getLabel");
        SamplingStatCalculatorColored instance = new SamplingStatCalculatorColored("TEST");
        String expResult = "TEST";
        String result = instance.getLabel();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSample method, of class SamplingStatCalculatorColored.
     */
    @Test
    public void testGetSample()
    {
        System.out.println("getSample");
        SamplingStatCalculatorColored instance = new SamplingStatCalculatorColored("TEST");
        SampleResult expResult = new SampleResult();
        instance.addSample(expResult);
        
        int count = 0;
        LeanSampleResult result = instance.getSample(count);
        assertNotNull(result);
    }

    /**
     * Test of getAvgThreads method, of class SamplingStatCalculatorColored.
     */
    @Test
    public void testGetAvgThreads()
    {
        System.out.println("getAvgThreads");
        SamplingStatCalculatorColored instance = new SamplingStatCalculatorColored("TEST");

        SampleResult res1=new SampleResult();
        res1.setAllThreads(1);
        instance.addSample(res1);
        SampleResult res2=new SampleResult();
        res2.setAllThreads(2);
        instance.addSample(res2);

        double expResult = 1.5;
        double result = instance.getAvgThreads();
        assertEquals(expResult, result, 0.01);
    }

    /**
     * Test of getAvgTime method, of class SamplingStatCalculatorColored.
     */
    @Test
    public void testGetAvgTime()
    {
        System.out.println("getAvgTime");
        SamplingStatCalculatorColored instance = new SamplingStatCalculatorColored("TEST");

        SampleResult res1=new SampleResult();
        res1.setTime(1);
        instance.addSample(res1);
        SampleResult res2=new SampleResult();
        res2.setTime(2);
        instance.addSample(res2);
        
        double expResult = 1.5;
        double result = instance.getAvgTime();
        assertEquals(expResult, result, 0.01);
    }

    /**
     * Test of getMaxThreads method, of class SamplingStatCalculatorColored.
     */
    /*
    @Test
    public void testGetMaxThreads()
    {
        System.out.println("getMaxThreads");
        SamplingStatCalculatorColored instance = new SamplingStatCalculatorColored("TEST");

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
     * Test of getMaxTime method, of class SamplingStatCalculatorColored.
     */
    /*
    @Test
    public void testGetMaxTime()
    {
        System.out.println("getMaxTime");
        SamplingStatCalculatorColored instance = new SamplingStatCalculatorColored("TEST");

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