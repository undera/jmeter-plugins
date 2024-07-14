package kg.apc.charting;

import java.awt.*;

public class GraphRowProperties {
    private boolean drawValueLabel = false;
    private boolean showInLegend = true;
    private boolean drawOnChart = true;
    private Color color = Color.BLACK;
    private String label = "";
    private int granulation = 0;
    private Rectangle legendColorBox = null;

    public boolean isDrawValueLabel() {
        return drawValueLabel;
    }

    public void setDrawValueLabel(boolean drawValueLabel) {
        this.drawValueLabel = drawValueLabel;
    }

    public boolean isShowInLegend() {
        return showInLegend;
    }

    public void setShowInLegend(boolean showInLegend) {
        this.showInLegend = showInLegend;
    }

    public boolean isDrawOnChart() {
        return drawOnChart;
    }

    public void setDrawOnChart(boolean drawOnChart) {
        this.drawOnChart = drawOnChart;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getGranulation() {
        return granulation;
    }

    public void setGranulation(int granulation) {
        this.granulation = granulation;
    }

    public Rectangle getLegendColorBox() {
        return legendColorBox;
    }

    public void setLegendColorBox(Rectangle legendColorBox) {
        this.legendColorBox = legendColorBox;
    }
}