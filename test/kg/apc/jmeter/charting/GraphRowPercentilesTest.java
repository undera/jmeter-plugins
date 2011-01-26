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
        int expResult = 101;
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
        expResult=101;
        result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of getElement method, of class GraphRowPercentiles.
     */
    @Test
    public void testGetElement()
    {
        /*
        System.out.println("getElement");
        long value = 0L;
        GraphRowPercentiles instance = new GraphRowPercentiles();
        AbstractGraphPanelChartElement expResult = null;
        AbstractGraphPanelChartElement result = instance.getElement(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");

         */

        System.out.println("getElement");

        long value = 50L;
        GraphRowPercentiles instance = new GraphRowPercentiles();
        for(int i=0; i<100; i++)
        {
            instance.addResponseTime(20);
            instance.addResponseTime(30);
            instance.addResponseTime(40);
        }

        //force percentile calculation
        instance.iterator();

        AbstractGraphPanelChartElement result = instance.getElement(value);
 
        assertTrue(instance.getElement(value).getValue() == 30);
    }

}