package kg.apc.charting.rows;

import kg.apc.charting.AbstractGraphPanelChartElement;
import kg.apc.charting.rows.GraphRowPercentiles;
import java.util.Map.Entry;
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

    private int expectedCount = 999;

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
        int expResult = expectedCount;
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
        while (result.hasNext()) {
            assertNotNull(result.next());
            cnt++;
        }
        assertEquals(expectedCount, cnt);
    }

    @Test
    public void testIterator_req1() {
        System.out.println("iterator 1req");
        GraphRowPercentiles instance = new GraphRowPercentiles();
        instance.add(10, 1);
        instance.add(10, 1);
        Iterator<Entry<Long, AbstractGraphPanelChartElement>> result = instance.iterator();
        assertNotNull(result);
        assertTrue(result.hasNext());
        int cnt = 0;
        while (result.hasNext()) {
            Entry<Long, AbstractGraphPanelChartElement> obj = result.next();
            assertNotNull(obj);
            //System.err.println(cnt + " " + obj.getValue().getValue());
            assertEquals(10 , obj.getValue().getValue(), 0.01);
            cnt++;
        }
        assertEquals(expectedCount, cnt);
    }

    @Test
    public void testIterator_req2() {
        System.out.println("iterator 2req");
        GraphRowPercentiles instance = new GraphRowPercentiles();
        instance.add(10, 1);
        instance.add(20, 1);
        Iterator<Entry<Long, AbstractGraphPanelChartElement>> result = instance.iterator();
        assertNotNull(result);
        assertTrue(result.hasNext());
        int cnt = 0;
        while (result.hasNext()) {
            Entry<Long, AbstractGraphPanelChartElement> obj = result.next();
            assertNotNull(obj);
            //System.err.println(cnt + " " + obj.getValue().getValue());
            assertEquals(cnt < 500 ? 10 : 20, obj.getValue().getValue(), 0.01);
            cnt++;
        }
        assertEquals(expectedCount, cnt);
    }

    @Test
    public void testIterator_req3() {
        System.out.println("iterator 3req");
        GraphRowPercentiles instance = new GraphRowPercentiles();

        for (int n = 0; n < expectedCount; n++) {
            instance.add(n, 1);
        }
        
        Iterator<Entry<Long, AbstractGraphPanelChartElement>> result = instance.iterator();
        assertNotNull(result);
        assertTrue(result.hasNext());
        int cnt = 0;
        while (result.hasNext()) {
            Entry<Long, AbstractGraphPanelChartElement> obj = result.next();
            assertNotNull(obj);
            //System.err.println(cnt + " " + obj.getValue().getValue());
            assertEquals(cnt, obj.getValue().getValue(), 0.01);
            cnt++;
        }
        assertEquals(expectedCount, cnt);
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
        expResult = expectedCount;
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
        for (int i = 0; i < expectedCount; i++) {
            instance.add(i, i);
        }

        assertEquals(expectedCount, instance.size());

        //force percentile calculation
        instance.iterator();

        AbstractGraphPanelChartElement result = instance.getElement(value);

        assertEquals(32, result.getValue(), 0.001);
    }

    /**
     * Test of add method, of class GraphRowPercentiles.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        long xVal = 0L;
        double yVal = 0.0;
        GraphRowPercentiles instance = new GraphRowPercentiles();
        instance.add(xVal, yVal);
    }

    /**
     * Test of getMinX method, of class GraphRowPercentiles.
     */
    @Test
    public void testGetMinX() {
        System.out.println("getMinX");
        GraphRowPercentiles instance = new GraphRowPercentiles();
        long expResult = 0L;
        long result = instance.getMinX();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMaxX method, of class GraphRowPercentiles.
     */
    @Test
    public void testGetMaxX() {
        System.out.println("getMaxX");
        GraphRowPercentiles instance = new GraphRowPercentiles();
        long expResult = 1000;
        long result = instance.getMaxX();
        assertEquals(expResult, result);
    }
}
