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
 * @author apc
 */
public class LeanSampleResultTest {

    public LeanSampleResultTest() {
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
     * Test of getThreads method, of class LeanSampleResult.
     */
    @Test
    public void testGetThreads()
    {
        System.out.println("getThreads");
        LeanSampleResult instance = new LeanSampleResult();
        int expResult = 0;
        int result = instance.getThreads();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTime method, of class LeanSampleResult.
     */
    @Test
    public void testGetTime()
    {
        System.out.println("getTime");
        LeanSampleResult instance = new LeanSampleResult();
        long expResult = 0L;
        long result = instance.getTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLabel method, of class LeanSampleResult.
     */
    @Test
    public void testGetLabel()
    {
        System.out.println("getLabel");
        LeanSampleResult instance = new LeanSampleResult();
        String expResult = null;
        String result = instance.getLabel();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRepeatCount method, of class LeanSampleResult.
     */
    @Test
    public void testGetRepeatCount()
    {
        System.out.println("getRepeatCount");
        LeanSampleResult instance = new LeanSampleResult();
        int expResult = 1;
        int result = instance.getRepeatCount();
        assertEquals(expResult, result);
    }

}