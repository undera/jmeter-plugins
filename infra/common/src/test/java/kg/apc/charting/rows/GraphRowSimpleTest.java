/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.charting.rows;

import kg.apc.charting.AbstractGraphPanelChartElement;
import kg.apc.charting.elements.GraphPanelChartSimpleElement;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.rows.GraphRowSimple;
import java.util.Iterator;

import org.junit.*;

import static org.junit.Assert.*;

public class GraphRowSimpleTest {

    private GraphRowSimple instance;

    public GraphRowSimpleTest() {
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
        instance = new GraphRowSimple();
        instance.setDrawLine(true);
        instance.setMarkerSize(AbstractGraphRow.MARKER_SIZE_BIG);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class GraphRowSimple.
     */
    @Test
    public void testAdd()
    {
        System.out.println("add");
        long xVal = 10;
        double yVal = 5.0;
        instance.add(xVal, yVal);

        double[] minMax = instance.getMinMaxY(-1);

        assertEquals(xVal, instance.getMinX());
        assertEquals(xVal, instance.getMaxX());
        Assert.assertEquals(yVal, minMax[0], 0.001);
        Assert.assertEquals(yVal, minMax[1], 0.001);
    }

    /**
     * Test of iterator method, of class GraphRowSimple.
     */
    @Test
    public void testIterator()
    {
        System.out.println("iterator");
        Iterator result = instance.iterator();
        Assert.assertNotNull(result);
    }

    /**
     * Test of size method, of class GraphRowSimple.
     */
    @Test
    public void testSize()
    {
        System.out.println("size");
        int expResult = 2;
        instance.add(1000, 3);
        instance.add(1000, 2);
        instance.add(2000, 1);

        int result = instance.size();

        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of getElement method, of class GraphRowSimple.
     */
    @Test
    public void testGetElement()
    {
        System.out.println("getElement");
        long value = 100L;
        AbstractGraphPanelChartElement expResult = new GraphPanelChartSimpleElement(10);
        instance.add(value, 10);
        AbstractGraphPanelChartElement result = instance.getElement(value);
        Assert.assertTrue(result.getValue() == expResult.getValue());
    }

    /**
     * Test of getHigherKey method, of class GraphRowSimple.
     */
    @Test
    public void testGetHigherKey() {
        System.out.println("getHigherKey");
        long value = 0L;
        Long expResult = null;
        Long result = instance.getHigherKey(value);
        Assert.assertEquals(expResult, result);
    }

}