package kg.apc.charting.rows;

import kg.apc.charting.AbstractGraphPanelChartElement;
import kg.apc.charting.elements.GraphPanelChartPercentileElement;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GraphRowOverTimePercentileTest {

    private GraphRowOverTimePercentile instance;

    /**
     *
     */
    @Before
    public void setUp() {
        instance = new GraphRowOverTimePercentile(50);
    }

    /**
     *
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        instance.add(1, 1);
        assertEquals(1,  instance.size());
    }

    /**
     *
     */
    @Test
    public void testIterator() {
        System.out.println("iterator");
        assertNotNull(instance.iterator());
        assertFalse(instance.iterator().hasNext());
        instance.add(0, 0);
        assertTrue(instance.iterator().hasNext());
    }

    /**
     *
     */
    @Test
    public void testSize() {
        System.out.println("size");
        instance.add(1, 1);
        instance.add(1, 1);
        instance.add(2, 1);

        assertEquals(2,  instance.size());
    }

    /**
     *
     */
    @Test
    public void testGetElement() {
        System.out.println("getElement");
        long xValue = 100L; double yValue = 10d;
        AbstractGraphPanelChartElement expResult = new GraphPanelChartPercentileElement(50);
        expResult.add(yValue);
        instance.add(xValue, yValue);
        AbstractGraphPanelChartElement result = instance.getElement(xValue);
        assertEquals(expResult.getValue(), result.getValue(), 0.0);
    }

}
