package kg.apc.charting.rows;

import kg.apc.charting.AbstractGraphPanelChartElement;
import kg.apc.charting.elements.GraphPanelChartSumElement;
import org.junit.*;

import java.util.Iterator;
import java.util.Map.Entry;

import static org.junit.Assert.assertEquals;

public class GraphRowSumValuesTest {
    /**
     *
     */
    public GraphRowSumValuesTest() {
    }

    /**
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     *
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        long xVal = 0L;
        GraphRowSumValues instance = new GraphRowSumValues();
        instance.add(xVal, 1);
        instance.add(xVal, 2);
        instance.add(xVal, 3);
    }

    /**
     *
     */
    @Test
    public void testIterator() {
        System.out.println("iterator");
        System.out.println("iterator");
        GraphRowSumValues instance = new GraphRowSumValues();
        instance.add(1, 1);
        instance.add(1, 2);
        instance.add(1, 3);
        Iterator<Entry<Long, AbstractGraphPanelChartElement>> result = instance.iterator();
        assertEquals(6, result.next().getValue().getValue(), 0.001);
    }

    /**
     *
     */
    @Test
    public void testHasNext() {
        System.out.println("hasNext");
        GraphRowSumValues instance = new GraphRowSumValues();
        Iterator it = instance.iterator();
        boolean result = it.hasNext();
        Assert.assertEquals(false, result);
    }

    /**
     *
     */
    @Test
    public void testNext() {
        System.out.println("next");
        GraphRowSumValues instance = new GraphRowSumValues();
        instance.add(1, 1);
        Iterator<Entry<Long, AbstractGraphPanelChartElement>> it = instance.iterator();
        Entry result = it.next();
        Assert.assertNotNull(result);
    }

    /**
     *
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        GraphRowSumValues instance = new GraphRowSumValues();
        try {
            instance.remove();
            Assert.fail("Exception expected");
        } catch (UnsupportedOperationException ignored) {
        }
    }

    @Test
    public void testSize() {
        System.out.println("size");
        GraphRowSumValues instance = new GraphRowSumValues();
        int expResult = 0;
        int result = instance.size();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of setExcludeOutOfRangeValues method, of class GraphRowSumValues.
     */
    @Test
    public void testSetExcludeOutOfRangeValues() {
        System.out.println("setExcludeOutOfRangeValues");
        GraphRowSumValues instance = new GraphRowSumValues();
        instance.setExcludeOutOfRangeValues(true);
    }

    /**
     * Test of getMaxX method, of class GraphRowSumValues.
     */
    @Test
    public void testGetMaxX() {
        System.out.println("getMaxX");
        GraphRowSumValues instance = new GraphRowSumValues();
        instance.setGranulationValue(100);
        instance.setExcludeOutOfRangeValues(true);
        instance.add(1000, 10);
        instance.add(100000, 20);

        long expResult = 1000L;
        long result = instance.getMaxX();
        Assert.assertEquals(expResult, result);

        instance.setExcludeOutOfRangeValues(false);
        expResult = 100000;
        result = instance.getMaxX();
        Assert.assertEquals(expResult, result);

    }

    /**
     * Test of getElement method, of class GraphRowSumValues.
     */
    @Test
    public void testGetElement() {
        System.out.println("getElement");
        long value = 100L;
        GraphRowSumValues instance = new GraphRowSumValues();
        AbstractGraphPanelChartElement expResult = new GraphPanelChartSumElement(10);
        instance.add(value, 10);
        instance.getElement(value);
        Assert.assertTrue(instance.getElement(value).getValue() == expResult.getValue());
    }
}
