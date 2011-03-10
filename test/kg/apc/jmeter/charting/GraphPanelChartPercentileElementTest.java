package kg.apc.jmeter.charting;

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
public class GraphPanelChartPercentileElementTest {

    public GraphPanelChartPercentileElementTest() {
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
     * Test of getValue method, of class GraphPanelChartPercentileElement.
     */
    @Test
    public void testGetValue()
    {
        System.out.println("getValue");
        GraphPanelChartPercentileElement instance = new GraphPanelChartPercentileElement(2);
        double expResult = 2.0;
        double result = instance.getValue();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of setValue method, of class GraphPanelChartPercentileElement.
     */
    @Test
    public void testSetValue()
    {
        System.out.println("setValue");
        double value = 0.0;
        GraphPanelChartPercentileElement instance = new GraphPanelChartPercentileElement(1);
        instance.add(10);
        double expResult = 10.0;
        double result = instance.getValue();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of incValue method, of class GraphPanelChartPercentileElement.
     */
    @Test
    public void testIncValue()
    {
        System.out.println("incValue");
        GraphPanelChartPercentileElement instance = new GraphPanelChartPercentileElement(2);
        instance.incValue();
        instance.incValue();
        double expResult = 4.0;
        double result = instance.getValue();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of add method, of class GraphPanelChartPercentileElement.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        double value = 0.0;
        GraphPanelChartPercentileElement instance = new GraphPanelChartPercentileElement(2);
        instance.add(value);
    }

}