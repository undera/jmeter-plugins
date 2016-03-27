package com.blazemeter.jmeter.threads.arrivals;

import com.blazemeter.jmeter.threads.AbstractDynamicThreadGroup;
import com.blazemeter.jmeter.threads.AbstractDynamicThreadGroupGui;
import com.blazemeter.jmeter.threads.AdditionalFieldsPanel;
import com.blazemeter.jmeter.threads.ParamsPanel;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.DateTimeRenderer;
import kg.apc.charting.rows.GraphRowExactValues;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.JMeterVariableEvaluator;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionListener;

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
        return "bzm - Free-Form Arrivals Thread Group";
    }

    protected AbstractDynamicThreadGroup createThreadGroupObject() {
        return new FreeFormArrivalsThreadGroup();
    }

    @Override
    protected ParamsPanel createLoadPanel() {
        FreeFormLoadPanel freeFormLoadPanel = new FreeFormLoadPanel();
        freeFormLoadPanel.addTableModelListener(this);
        return freeFormLoadPanel;
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
        log.debug("Table changed");
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

        JMeterVariableEvaluator evaluator = new JMeterVariableEvaluator();
        int offset = 0;
        double totalArrivals = 0;
        PropertyIterator it = data.iterator();
        while (it.hasNext()) {
            CollectionProperty record = (CollectionProperty) it.next();
            double from = evaluator.getDouble(record.get(0));
            double to = evaluator.getDouble(record.get(1));
            double during = evaluator.getDouble(record.get(2));
            row.add(offset * 1000, from);
            offset += during * tg.getUnitFactor();
            row.add(offset * 1000, to);
            totalArrivals += during * from + during * (to - from) / 2;
        }

        previewChart.setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS, 0));
        chartModel.put(getRowLabel(totalArrivals), row);
    }

}
