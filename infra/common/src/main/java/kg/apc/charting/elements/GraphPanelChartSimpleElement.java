package kg.apc.charting.elements;

import kg.apc.charting.AbstractGraphPanelChartElement;

/**
 * {@inheritDoc}
 */
public class GraphPanelChartSimpleElement
        extends AbstractGraphPanelChartElement {
    double value = 0;

    public GraphPanelChartSimpleElement(double yVal) {
        add(yVal);
    }

    GraphPanelChartSimpleElement() {
    }

    public void add(double yVal) {
        value = yVal;
    }

    /**
     * {@inheritDoc}
     */
    public double getValue() {
        return value;
    }
}
