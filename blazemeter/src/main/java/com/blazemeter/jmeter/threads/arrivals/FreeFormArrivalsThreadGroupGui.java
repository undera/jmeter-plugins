package com.blazemeter.jmeter.threads.arrivals;

import com.blazemeter.jmeter.threads.AbstractDynamicThreadGroup;
import com.blazemeter.jmeter.threads.AbstractDynamicThreadGroupGui;
import com.blazemeter.jmeter.threads.AdditionalFieldsPanel;
import com.blazemeter.jmeter.threads.ParamsPanel;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.DateTimeRenderer;
import kg.apc.charting.GraphPanelChart;
import kg.apc.charting.rows.GraphRowExactValues;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;

public class FreeFormArrivalsThreadGroupGui extends AbstractDynamicThreadGroupGui implements TableModelListener, DocumentListener, Runnable, ActionListener {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public FreeFormArrivalsThreadGroupGui() {
        super();
        JMeterPluginsUtils.addHelpLinkToPanel(this, getClass().getSimpleName());
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Free-Form Arrivals Thread Group");
    }

    protected AbstractDynamicThreadGroup createThreadGroupObject() {
        return new FreeFormArrivalsThreadGroup();
    }

    @Override
    protected ParamsPanel createLoadPanel() {
        return new FreeFormLoadPanel();
    }

    @Override
    protected AdditionalFieldsPanel getAdditionalFieldsPanel() {
        return new AdditionalFieldsPanel(true);
    }

    protected void setChartPropertiesFromTG(AbstractDynamicThreadGroup tg) {
        if (tg instanceof ArrivalsThreadGroup) {
            ArrivalsThreadGroup atg = (ArrivalsThreadGroup) tg;
            previewChart.setYAxisLabel("Number of arrivals/" + atg.getUnitStr());
        }
    }

    protected Color getRowColor() {
        return Color.MAGENTA;
    }

    protected String getRowLabel(double totalArrivals) {
        log.debug("Total arr: " + totalArrivals);
        return "Arrival Rate (~" + Math.round(totalArrivals) + " total arrivals)";
    }


    @Override
    public void tableChanged(TableModelEvent e) {
        log.info("Table changed");
        SwingUtilities.invokeLater(this);
    }

    protected void updateChart(AbstractDynamicThreadGroup tg) {
        FreeFormArrivalsThreadGroup atg = (FreeFormArrivalsThreadGroup) tg;
        CollectionProperty data = atg.getData();
        chartModel.clear();
        previewChart.clearErrorMessage();
        AbstractGraphRow row = new GraphRowExactValues();
        row.setColor(getRowColor());
        row.setDrawLine(true);
        row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_NONE);
        row.setDrawThickLines(true);

        row.add(0, 0); // initial value to force min Y

        int offset = 0;
        double totalArrivals = 0;
        PropertyIterator it = data.iterator();
        while (it.hasNext()) {
            CollectionProperty record = (CollectionProperty) it.next();
            int from = record.get(0).getIntValue();
            int to = record.get(1).getIntValue();
            int during = record.get(2).getIntValue();
            row.add(offset * 1000, from);
            offset += during;
            row.add(offset * 1000, to);
            totalArrivals += during * from + during * (to - from) / 2;
        }

        previewChart.setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS, 0));
        chartModel.put(getRowLabel(totalArrivals), row);
    }

    @Override
    public void insertUpdate(DocumentEvent documentEvent) {
        SwingUtilities.invokeLater(this);
    }

    @Override
    public void removeUpdate(DocumentEvent documentEvent) {
        SwingUtilities.invokeLater(this);
    }

    @Override
    public void changedUpdate(DocumentEvent documentEvent) {
        SwingUtilities.invokeLater(this);
    }

    @Override
    public void run() {
        updateUI();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        SwingUtilities.invokeLater(this);
    }

    @Override
    public TestElement createTestElement() {
        AbstractDynamicThreadGroup te = createThreadGroupObject();
        modifyTestElement(te);
        return te;
    }

    private void updateChart(double targetRate, long rampUp, long holdFor, long stepsCount, double unitFactor) {
        chartModel.clear();
        previewChart.clearErrorMessage();
        AbstractGraphRow row = new GraphRowExactValues();
        row.setColor(getRowColor());
        row.setDrawLine(true);
        row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_NONE);
        row.setDrawThickLines(true);

        row.add(0, 0); // initial value to force min Y

        double totalArrivals = 0;

        if (stepsCount > 0) {
            double stepSize = targetRate / (double) stepsCount;
            double stepLen = rampUp / (double) stepsCount;

            for (int n = 1; n <= stepsCount; n++) {
                double stepRate = stepSize * n;
                row.add(Math.round((n - 1) * stepLen * 1000), stepRate);
                row.add(Math.round(n * stepLen * 1000), stepRate);
                totalArrivals += stepLen * stepRate;
            }
        } else {
            row.add(rampUp * 1000, targetRate);
            totalArrivals += rampUp * targetRate / 2.0;
        }
        row.add((rampUp + holdFor) * 1000, targetRate);
        totalArrivals += holdFor * targetRate;
        totalArrivals /= unitFactor;

        previewChart.setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS, 0));
        chartModel.put(getRowLabel(totalArrivals), row);
    }

    public Component getPreviewChart() {
        previewChart = new GraphPanelChart(false, true);
        chartModel = new ConcurrentHashMap<>();
        previewChart.setRows(chartModel);
        previewChart.setxAxisLabel("Elapsed Time");
        previewChart.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        return previewChart;
    }
}
