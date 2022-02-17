package com.blazemeter.jmeter.threads;

import com.blazemeter.jmeter.gui.ArrangedLabelFieldPanel;
import com.blazemeter.jmeter.threads.arrivals.ArrivalsThreadGroup;
import kg.apc.jmeter.JMeterVariableEvaluator;
import org.apache.jmeter.gui.util.HorizontalPanel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Enumeration;

public class AdditionalFieldsPanel extends ArrangedLabelFieldPanel implements ParamsPanel {
    protected JTextField logFile = new JTextField();
    protected JTextField iterations = new JTextField();
    protected JTextField concurrLimit = new JTextField();
    protected JTextField propertiesIdentifier = new JTextField();
    protected ButtonGroup unitGroup = new ButtonGroup();
    protected JRadioButton unitSeconds = new JRadioButton("seconds");
    protected JRadioButton unitMinutes = new JRadioButton("minutes");
    protected JCheckBox useIdentifier = new JCheckBox("Use identification property for profile");
    protected boolean showIdentifier;

    public AdditionalFieldsPanel(boolean showConcurrencyLimit, boolean showIdentifier) {
        JPanel groupPanel = new HorizontalPanel();
        unitMinutes.setActionCommand(ArrivalsThreadGroup.UNIT_MINUTES);
        unitSeconds.setActionCommand(ArrivalsThreadGroup.UNIT_SECONDS);
        unitGroup.add(unitMinutes);
        unitGroup.add(unitSeconds);
        groupPanel.add(unitMinutes);
        groupPanel.add(unitSeconds);
        add("Time Unit: ", groupPanel);


        add("Thread Iterations Limit: ", iterations);

        add("Log Threads Status into File: ", logFile);
        this.showIdentifier = showIdentifier;
        if (this.showIdentifier) {
            add("Load from Properties", useIdentifier);
            add("Properties identifier:", propertiesIdentifier);
        }

        if (showConcurrencyLimit) {
            add("Concurrency Limit: ", concurrLimit);
        }
    }

    public void modelToUI(AbstractDynamicThreadGroup tg) {
        logFile.setText(tg.getLogFilename());
        iterations.setText(tg.getIterationsLimit());
        concurrLimit.setText("1000");
        if (showIdentifier) {
            useIdentifier.setSelected(tg.getUseIdentifier());
            propertiesIdentifier.setText(tg.getIdentifier());
        }
        unitMinutes.setSelected(true);
        if (tg instanceof ArrivalsThreadGroup) {
            ArrivalsThreadGroup atg = (ArrivalsThreadGroup) tg;
            concurrLimit.setText(atg.getConcurrencyLimit());
        }

        Enumeration<AbstractButton> it = unitGroup.getElements();
        while (it.hasMoreElements()) {
            AbstractButton btn = it.nextElement();
            if (btn.getActionCommand().equals(tg.getUnit())) {
                btn.setSelected(true);
            }
        }
    }

    public void UItoModel(AbstractDynamicThreadGroup tg, JMeterVariableEvaluator evaluator) {
        tg.setLogFilename(evaluator.evaluate(logFile.getText()));
        tg.setIterationsLimit(evaluator.evaluate(iterations.getText()));
        if (showIdentifier) {
            tg.setUseIdentifier(useIdentifier.isSelected());
            tg.setIdentifier(evaluator.evaluate(propertiesIdentifier.getText()));
        }
        if (unitGroup.getSelection() != null) {
            tg.setUnit(unitGroup.getSelection().getActionCommand());
        }

        if (tg instanceof ArrivalsThreadGroup) {
            ArrivalsThreadGroup atg = (ArrivalsThreadGroup) tg;
            atg.setConcurrencyLimit(evaluator.evaluate(concurrLimit.getText()));
        }
    }

    public void clearUI() {
        logFile.setText("");
        iterations.setText("");
        concurrLimit.setText("1000");
        propertiesIdentifier.setText("");
        useIdentifier.setSelected(false);
        unitMinutes.setSelected(true);
    }

    public void addActionListener(ActionListener listener) {
        unitMinutes.addActionListener(listener);
        unitSeconds.addActionListener(listener);
    }
}
