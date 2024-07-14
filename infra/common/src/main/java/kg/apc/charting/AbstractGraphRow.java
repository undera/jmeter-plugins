package kg.apc.charting;

import kg.apc.charting.rows.*;

import java.awt.*;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class AbstractGraphRow {

    public static final int MARKER_SIZE_NONE = 0;
    public static final int MARKER_SIZE_SMALL = 2;
    public static final int MARKER_SIZE_BIG = 4;
    public static final float LINE_THICKNESS_BIG = 3.0f;
    public static final int ROW_AVERAGES = 0;
    public static final int ROW_EXACT_VALUES = 1;
    public static final int ROW_OVERALL_AVERAGES = 2;
    public static final int ROW_PERCENTILES = 3;
    public static final int ROW_SUM_VALUES = 4;
    public static final int ROW_ROLLING_SUM_VALUES = 5;
    public static final int ROW_SIMPLE = 6;
    protected Color color = Color.BLACK;
    private kg.apc.charting.GraphRowProperties properties;
    private kg.apc.charting.GraphRowDrawing drawing;
    private kg.apc.charting.GraphRowStatistics statistics;

    public AbstractGraphRow() {
        this.properties = new kg.apc.charting.GraphRowProperties();
        this.drawing = new kg.apc.charting.GraphRowDrawing();
        this.statistics = new kg.apc.charting.GraphRowStatistics();
    }

    public void setDrawThickLines(boolean isThickLine) {
        drawing.setDrawThickLines(isThickLine);
    }

    public boolean isDrawThickLines() {
        return drawing.isDrawThickLines();
    }

    public void setDrawLine(boolean b) {
        drawing.setDrawLine(b);
    }

    public boolean isDrawLine() {
        return drawing.isDrawLine();
    }

    public void setMarkerSize(int aMarkerSize) {
        drawing.setMarkerSize(aMarkerSize);
    }

    public int getMarkerSize() {
        return drawing.getMarkerSize();
    }

    public Color getColor() {
        return properties.getColor();
    }

    public void setColor(Color nextColor) {
        properties.setColor(nextColor);
    }

    public String getLabel() {
        return properties.getLabel();
    }

    public void setLabel(String label) {
        properties.setLabel(label);
    }

    public long getMaxX() {
        return statistics.getMaxX();
    }

    public double[] getMinMaxY(int maxPoints) {
        return statistics.getMinMaxY(this, maxPoints);
    }

    public long getMinX() {
        return statistics.getMinX();
    }

    public void add(long xVal, double yVal) {
        statistics.add(xVal, yVal);
    }

    public abstract Iterator<Entry<Long, AbstractGraphPanelChartElement>> iterator();

    public boolean isDrawValueLabel() {
        return properties.isDrawValueLabel();
    }

    public void setDrawValueLabel(boolean drawValueLabel) {
        properties.setDrawValueLabel(drawValueLabel);
    }

    public boolean isShowInLegend() {
        return properties.isShowInLegend();
    }

    public void setShowInLegend(boolean showInLegend) {
        properties.setShowInLegend(showInLegend);
    }

    public boolean isDrawOnChart() {
        return properties.isDrawOnChart();
    }

    public void setDrawOnChart(boolean drawOnChart) {
        properties.setDrawOnChart(drawOnChart);
    }

    public void setDrawBar(boolean b) {
        drawing.setDrawBar(b);
    }

    public boolean isDrawBar() {
        return drawing.isDrawBar();
    }

    public boolean isDrawSpline() {
        return drawing.isDrawSpline();
    }

    public void setDrawSpline(boolean drawSpline) {
        drawing.setDrawSpline(drawSpline);
    }

    public int getGranulationValue() {
        return properties.getGranulation();
    }

    public void setGranulationValue(int value) {
        properties.setGranulation(value);
    }

    public void setExcludeOutOfRangeValues(boolean excludeOutOfRangeValues) {
        // Implement if necessary
    }

    public abstract AbstractGraphPanelChartElement getElement(long value);

    public AbstractGraphPanelChartElement getLowerElement(long value) {
        throw new UnsupportedOperationException();
    }

    public Long getHigherKey(long value) {
        throw new UnsupportedOperationException();
    }

    public abstract int size();

    public static AbstractGraphRow instantiateNewRow(int rowType) {
        switch (rowType) {
            case AbstractGraphRow.ROW_AVERAGES:
                return new GraphRowAverages();
            case AbstractGraphRow.ROW_EXACT_VALUES:
                return new GraphRowExactValues();
            case AbstractGraphRow.ROW_OVERALL_AVERAGES:
                return new GraphRowOverallAverages();
            case AbstractGraphRow.ROW_PERCENTILES:
                return new GraphRowPercentiles();
            case AbstractGraphRow.ROW_SUM_VALUES:
                return new GraphRowSumValues(false);
            case AbstractGraphRow.ROW_ROLLING_SUM_VALUES:
                return new GraphRowSumValues(true);
            case AbstractGraphRow.ROW_SIMPLE:
                return new GraphRowSimple();
            default:
                return null;
        }
    }

    public long getFirstTime() {
        return statistics.getFirstTime();
    }

    public void setLegendColorBox(Rectangle val) {
        properties.setLegendColorBox(val);
    }

    public Rectangle getLegendColorBox() {
        return properties.getLegendColorBox();
    }
}
