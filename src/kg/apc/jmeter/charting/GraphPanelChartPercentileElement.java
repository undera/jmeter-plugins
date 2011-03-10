package kg.apc.jmeter.charting;

/**
 *
 * @author Stephane Hoblingre
 */
public class GraphPanelChartPercentileElement extends AbstractGraphPanelChartElement {

    private double value = 0;

    public GraphPanelChartPercentileElement(double value) {
        this.value = value;
    }

    @Override
    public double getValue()
    {
        return value;
    }

    public void add(double value)
    {
        this.value = value;
    }

    public void incValue()
    {
        value++;
    }

}
