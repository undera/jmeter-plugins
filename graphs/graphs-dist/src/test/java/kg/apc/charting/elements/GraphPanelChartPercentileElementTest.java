package kg.apc.charting.elements;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GraphPanelChartPercentileElementTest {

    /**
     *
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        GraphPanelChartPercentileElement instance = new GraphPanelChartPercentileElement(50);
        instance.add(0);
    }

    /**
     *
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        GraphPanelChartPercentileElement instance = new GraphPanelChartPercentileElement(50);
        instance.add(1);
        instance.add(2);
        instance.add(3);
        double result = instance.getValue();
        assertEquals(2, result, 0.0);
    }
}
