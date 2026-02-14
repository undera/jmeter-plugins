package kg.apc.charting.elements;

import kg.apc.charting.AbstractGraphPanelChartElement;
import org.apache.commons.math3.stat.descriptive.rank.PSquarePercentile;

public class GraphPanelChartPercentileElement extends AbstractGraphPanelChartElement {
    private final PSquarePercentile percentile;

    public GraphPanelChartPercentileElement(double percentile) {
        this.percentile = new PSquarePercentile(percentile);
    }

    @Override
    public double getValue() {
        return percentile.getResult();
    }

    @Override
    public void add(double val) {
        percentile.increment(val);
    }
}
