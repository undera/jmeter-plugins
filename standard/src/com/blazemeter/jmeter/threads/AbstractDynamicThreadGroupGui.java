package com.blazemeter.jmeter.threads;

import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.DateTimeRenderer;
import kg.apc.charting.GraphPanelChart;
import kg.apc.charting.rows.GraphRowExactValues;
import kg.apc.jmeter.DummyEvaluator;
import kg.apc.jmeter.JMeterVariableEvaluator;
import kg.apc.jmeter.gui.GuiBuilderHelper;
import org.apache.jmeter.gui.util.VerticalPanel;
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

public abstract class AbstractDynamicThreadGroupGui extends AbstractThreadGroupGui
        implements DocumentListener, Runnable, ActionListener {
    private static final Logger log = LoggingManager.getLoggerForClass();
    protected GraphPanelChart previewChart;
    protected ConcurrentHashMap<String, AbstractGraphRow> chartModel;
    protected boolean uiCreated = false;
    private ParamsPanel loadFields = null;
    private AdditionalFieldsPanel additionalFields = null;
    private JMeterVariableEvaluator evaluator = new DummyEvaluator();

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (!uiCreated) {
            initUI();
        }
        if (element instanceof AbstractDynamicThreadGroup) {
            AbstractDynamicThreadGroup tg = (AbstractDynamicThreadGroup) element;
            loadFields.modelToUI(tg);
            additionalFields.modelToUI(tg);
            updateUI();
        }
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        if (!uiCreated) {
            initUI();
        }

        if (element instanceof AbstractDynamicThreadGroup) {
            AbstractDynamicThreadGroup tg = (AbstractDynamicThreadGroup) element;
            loadFields.UItoModel(tg, evaluator);
            additionalFields.UItoModel(tg, evaluator);
        }
    }

    protected void initUI() {
        JPanel container = new VerticalPanel();
        loadFields = createLoadPanel();
        container.add((Component) loadFields, BorderLayout.NORTH);
        container.add(GuiBuilderHelper.getComponentWithMargin(getPreviewChart(), 2, 2, 0, 2), BorderLayout.CENTER);
        additionalFields = getAdditionalFieldsPanel();
        additionalFields.addActionListener(this);
        container.add(additionalFields, BorderLayout.SOUTH);
        add(container, BorderLayout.CENTER);
        uiCreated = true;
    }

    protected abstract AdditionalFieldsPanel getAdditionalFieldsPanel();

    @Override
    public void clearGui() {
        super.clearGui();
        if (uiCreated) {
            loadFields.clearUI();
            additionalFields.clearUI();
        }
    }

    protected abstract ParamsPanel createLoadPanel();


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

    public void updateUI() {
        super.updateUI();
        if (!uiCreated) {
            log.debug("Won't update UI");
            return;
        }
        log.debug("Updating UI");

        AbstractDynamicThreadGroup atg = createThreadGroupObject();

        JMeterVariableEvaluator evaluator = new JMeterVariableEvaluator();
        loadFields.UItoModel(atg, evaluator);
        additionalFields.UItoModel(atg, evaluator);

        try {
            updateChart(atg);
        } catch (NumberFormatException e) {
            previewChart.setErrorMessage("The values entered cannot be rendered in preview...");
        } finally {
            setChartPropertiesFromTG(atg);
            previewChart.invalidateCache();
            previewChart.repaint();
        }

        if (loadFields instanceof LoadParamsFieldsPanel) {
            LoadParamsFieldsPanel panel = (LoadParamsFieldsPanel) loadFields;
            panel.changeUnitInLabels(atg.getUnit());
        }
    }

    protected void updateChart(AbstractDynamicThreadGroup atg) {
        double targetRate = atg.getTargetLevelAsDouble();
        long rampUp = atg.getRampUpSeconds();
        long holdFor = atg.getHoldSeconds();
        long stepsCount = atg.getStepsAsLong();
        double unitFactor = atg.getUnitFactor();

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
