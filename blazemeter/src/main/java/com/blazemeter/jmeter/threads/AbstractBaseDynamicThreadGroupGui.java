package com.blazemeter.jmeter.threads;

import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.GraphPanelChart;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.gui.AbstractThreadGroupGui;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBaseDynamicThreadGroupGui extends AbstractThreadGroupGui
        implements DocumentListener, Runnable, ActionListener {
    private static final Logger log = LoggingManager.getLoggerForClass();
    protected GraphPanelChart previewChart;
    protected ConcurrentHashMap<String, AbstractGraphRow> chartModel;
    protected boolean uiCreated = false;

    protected abstract AbstractDynamicThreadGroup createThreadGroupObject();

    protected abstract void setChartPropertiesFromTG(AbstractDynamicThreadGroup atg);

    protected abstract String getRowLabel(double totalArrivals);

    protected abstract Color getRowColor();

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

    public Component getPreviewChart() {
        previewChart = new GraphPanelChart(false, true);
        chartModel = new ConcurrentHashMap<>();
        previewChart.setRows(chartModel);
        previewChart.setxAxisLabel("Elapsed Time");
        previewChart.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        return previewChart;
    }
}
