package kg.apc.jmeter.vizualizers;

import kg.apc.charting.AbstractGraphPanelChartElement;
import kg.apc.charting.AbstractGraphRow;

import java.awt.*;
import java.util.Iterator;
import java.util.Map;

public class CompositeGraphRow extends AbstractGraphRow {
    private final AbstractGraphRow graphRow;

    public CompositeGraphRow(AbstractGraphRow graphRow) {
        this.graphRow = graphRow;
    }

    // override only color methods
    @Override
    public Color getColor() {
        return super.getColor();
    }

    @Override
    public void setColor(Color nextColor) {
        super.setColor(nextColor);
    }

    // other methods call from original graphRow
    @Override
    public void setDrawThickLines(boolean isThickLine) {
        graphRow.setDrawThickLines(isThickLine);
    }

    @Override
    public boolean isDrawThickLines() {
        return graphRow.isDrawThickLines();
    }

    @Override
    public void setDrawLine(boolean b) {
        graphRow.setDrawLine(b);
    }

    @Override
    public void setMarkerSize(int aMarkerSize) {
        graphRow.setMarkerSize(aMarkerSize);
    }

    @Override
    public boolean isDrawLine() {
        return graphRow.isDrawLine();
    }

    @Override
    public int getMarkerSize() {
        return graphRow.getMarkerSize();
    }

    @Override
    public String getLabel() {
        return graphRow.getLabel();
    }

    @Override
    public void setLabel(String label) {
        graphRow.setLabel(label);
    }

    @Override
    public long getMaxX() {
        return graphRow.getMaxX();
    }

    @Override
    public double[] getMinMaxY(int maxPoints) {
        return graphRow.getMinMaxY(maxPoints);
    }

    @Override
    public long getMinX() {
        return graphRow.getMinX();
    }

    @Override
    public void add(long xVal, double yVal) {
        graphRow.add(xVal, yVal);
    }

    @Override
    public boolean isDrawValueLabel() {
        return graphRow.isDrawValueLabel();
    }

    @Override
    public void setDrawValueLabel(boolean drawValueLabel) {
        graphRow.setDrawValueLabel(drawValueLabel);
    }

    @Override
    public boolean isShowInLegend() {
        return graphRow.isShowInLegend();
    }

    @Override
    public void setShowInLegend(boolean showInLegend) {
        graphRow.setShowInLegend(showInLegend);
    }

    @Override
    public boolean isDrawOnChart() {
        return graphRow.isDrawOnChart();
    }

    @Override
    public Iterator<Map.Entry<Long, AbstractGraphPanelChartElement>> iterator() {
        return graphRow.iterator();
    }

    @Override
    public AbstractGraphPanelChartElement getElement(long value) {
        return graphRow.getElement(value);
    }

    @Override
    public int size() {
        return graphRow.size();
    }
}
