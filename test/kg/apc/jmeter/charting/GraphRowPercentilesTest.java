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
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
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
    public void testAddResponseTime() {
        System.out.println("addResponseTime");
        long respTime = 1234L;
        GraphRowPercentiles instance = new GraphRowPercentiles();
        instance.add(respTime, 1);
        int expResult = 1001;
        int result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of iterator method, of class GraphRowPercentiles.
     */
    @Test
    public void testIterator() {
        System.out.println("iterator");
        GraphRowPercentiles instance = new GraphRowPercentiles();
        Iterator result = instance.iterator();
        assertNotNull(result);
        assertTrue(result.hasNext());
        int cnt = 0;
        while (result.hasNext())
        {
            assertNotNull(result.next());
            cnt++;
        }
        assertEquals(1001, cnt);
    }

    /**
     * Test of size method, of class GraphRowPercentiles.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        GraphRowPercentiles instance = new GraphRowPercentiles();
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
        instance.add(1234L, 1);
        expResult = 1001;
        result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of getElement method, of class GraphRowPercentiles.
     */
    @Test
    public void testGetElement() {
        System.out.println("getElement");

        long value = 500L;
        GraphRowPercentiles instance = new GraphRowPercentiles();
        for (int i = 0; i < 1000; i++) {
            instance.add(i, i);
        }

        assertEquals(1001, instance.size());

        //force percentile calculation
        instance.iterator();

        AbstractGraphPanelChartElement result = instance.getElement(value);

        assertEquals(14, result.getValue(), 0.001);
    }
}
