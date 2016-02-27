package com.blazemeter.jmeter.threads;

import com.blazemeter.jmeter.gui.ArrangedLabelFieldPanel;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.DateTimeRenderer;
import kg.apc.charting.rows.GraphRowExactValues;
import kg.apc.jmeter.gui.GuiBuilderHelper;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractDynamicThreadGroupGui extends AbstractBaseDynamicThreadGroupGui {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private JTextField logFile = new JTextField();
    private JTextField targetRate = new JTextField();
    private JTextField rampUpTime = new JTextField();
    private JTextField steps = new JTextField();
    private JTextField holdFor = new JTextField();
    private JTextField iterations = new JTextField();
    protected JLabel targetRateLabel = new JLabel();
    protected JLabel rampUpLabel = new JLabel();
    protected JLabel holdLabel = new JLabel();
    private ArrangedLabelFieldPanel fieldsPanel = null;

    public AbstractDynamicThreadGroupGui() {
        super();
        init();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (element instanceof AbstractDynamicThreadGroup) {
            AbstractDynamicThreadGroup tg = (AbstractDynamicThreadGroup) element;
            logFile.setText(tg.getLogFilename());
            targetRate.setText(tg.getTargetLevel());
            rampUpTime.setText(tg.getRampUp());
            steps.setText(tg.getSteps());
            holdFor.setText(tg.getHold());
            iterations.setText(tg.getIterationsLimit());

            updateUI();
        }
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        if (element instanceof AbstractDynamicThreadGroup) {
            AbstractDynamicThreadGroup tg = (AbstractDynamicThreadGroup) element;
            tg.setLogFilename(logFile.getText());
            tg.setTargetLevel(targetRate.getText());
            tg.setRampUp(rampUpTime.getText());
            tg.setSteps(steps.getText());
            tg.setHold(holdFor.getText());
            tg.setIterationsLimit(iterations.getText());
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        logFile.setText("");
        targetRate.setText("12");
        rampUpTime.setText("60");
        steps.setText("3");
        holdFor.setText("180");
        iterations.setText("");
    }

    private void init() {
        JPanel container = new VerticalPanel();
        container.add(getFieldsPanel(), BorderLayout.NORTH);
        container.add(GuiBuilderHelper.getComponentWithMargin(getPreviewChart(), 2, 2, 0, 2), BorderLayout.CENTER);
        add(container, BorderLayout.CENTER);
        uiCreated = true;
    }

    protected JPanel getFieldsPanel() {
        if (fieldsPanel == null) {
            fieldsPanel = new ArrangedLabelFieldPanel();
            fieldsPanel.add(targetRateLabel, targetRate);
            fieldsPanel.add(rampUpLabel, rampUpTime);
            fieldsPanel.add("Ramp-Up Steps Count: ", steps);
            fieldsPanel.add(holdLabel, holdFor);
            fieldsPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            fieldsPanel.add("Thread Iterations Limit: ", iterations);
            fieldsPanel.add("Log Arrivals/Completions into File: ", logFile);

            targetRate.getDocument().addDocumentListener(this);
            rampUpTime.getDocument().addDocumentListener(this);
            steps.getDocument().addDocumentListener(this);
            holdFor.getDocument().addDocumentListener(this);
        }
        return fieldsPanel;
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

    public void updateUI() {
        super.updateUI();
        if (!uiCreated) {
            log.debug("Won't update UI");
            return;
        }
        log.debug("Updating UI");

        AbstractDynamicThreadGroup atg = createThreadGroupObject();
        modifyTestElement(atg);
        try {
            updateChart(atg.getTargetLevelAsDouble(), atg.getRampUpSeconds(), atg.getHoldSeconds(), atg.getStepsAsLong(), atg.getUnitFactor());
        } catch (NumberFormatException e) {
            previewChart.setErrorMessage("The values entered cannot be rendered in preview...");
        } finally {
            setChartPropertiesFromTG(atg);
            previewChart.invalidateCache();
            previewChart.repaint();
        }
    }


}
