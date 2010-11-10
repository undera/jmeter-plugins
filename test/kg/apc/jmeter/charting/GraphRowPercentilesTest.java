/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.charting;

import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stéphane Hoblingre
 */
public class GraphRowPercentilesTest {

    public GraphRowPercentilesTest() {
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
     * Test of addResponseTime method, of class GraphRowPercentiles.
     */
    @Test
    public void testAddResponseTime()
    {
        System.out.println("addResponseTime");
        long respTime = 1234L;
        GraphRowPercentiles instance = new GraphRowPercentiles();
        instance.addResponseTime(respTime);
        int expResult = 100;
        int result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of iterator method, of class GraphRowPercentiles.
     */
    @Test
    public void testIterator()
    {
        System.out.println("iterator");
        GraphRowPercentiles instance = new GraphRowPercentiles();
        Iterator result = instance.iterator();
        assertNotNull(result);
    }

    /**
     * Test of size method, of class GraphRowPercentiles.
     */
    @Test
    public void testSize()
    {
        System.out.println("size");
        GraphRowPercentiles instance = new GraphRowPercentiles();
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
        instance.addResponseTime(1234L);
        expResult=100;
        result = instance.size();
        assertEquals(expResult, result);
    }

}