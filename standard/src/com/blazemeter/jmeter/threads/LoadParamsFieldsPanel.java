package com.blazemeter.jmeter.threads;

import com.blazemeter.jmeter.gui.ArrangedLabelFieldPanel;
import kg.apc.jmeter.JMeterVariableEvaluator;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import javax.swing.event.DocumentListener;

public class LoadParamsFieldsPanel extends ArrangedLabelFieldPanel implements ParamsPanel {
    private static final Logger log = LoggingManager.getLoggerForClass();

    protected JTextField targetRate = new JTextField();
    protected JTextField rampUpTime = new JTextField();
    protected JTextField steps = new JTextField();
    protected JTextField holdFor = new JTextField();

    protected JLabel targetRateLabel = new JLabel();
    protected JLabel rampUpLabel = new JLabel();
    protected JLabel holdLabel = new JLabel();

    public LoadParamsFieldsPanel(String targetLbl, String rampUpLbl, String holdLbl) {
        targetRateLabel.setText(targetLbl);
        rampUpLabel.setText(rampUpLbl);
        holdLabel.setText(holdLbl);

        add(targetRateLabel, targetRate);
        add(rampUpLabel, rampUpTime);
        add("Ramp-Up Steps Count: ", steps);
        add(holdLabel, holdFor);
    }

    @Override
    public void modelToUI(AbstractDynamicThreadGroup tg) {
        targetRate.setText(tg.getTargetLevel());
        rampUpTime.setText(tg.getRampUp());
        steps.setText(tg.getSteps());
        holdFor.setText(tg.getHold());
    }

    @Override
    public void UItoModel(AbstractDynamicThreadGroup tg, JMeterVariableEvaluator evaluator) {
        tg.setTargetLevel(evaluator.evaluate(targetRate.getText()));
        tg.setRampUp(evaluator.evaluate(rampUpTime.getText()));
        tg.setSteps(evaluator.evaluate(steps.getText()));
        tg.setHold(evaluator.evaluate(holdFor.getText()));
    }

    @Override
    public void clearUI() {
        targetRate.setText("12");
        rampUpTime.setText("60");
        steps.setText("3");
        holdFor.setText("180");
    }

    public void addUpdateListener(DocumentListener listener) {
        targetRate.getDocument().addDocumentListener(listener);
        rampUpTime.getDocument().addDocumentListener(listener);
        steps.getDocument().addDocumentListener(listener);
        holdFor.getDocument().addDocumentListener(listener);
    }

    public void changeUnitInLabels(String unit) {
        String oldUnit = unit.equals(AbstractDynamicThreadGroup.UNIT_MINUTES) ? AbstractDynamicThreadGroup.UNIT_SECONDS : AbstractDynamicThreadGroup.UNIT_MINUTES;
        String oldUnitStr = AbstractDynamicThreadGroup.getUnitStr(oldUnit);
        String unitStr = AbstractDynamicThreadGroup.getUnitStr(unit);
        log.debug(oldUnit + " " + oldUnitStr + "=>" + unitStr);
        targetRateLabel.setText(targetRateLabel.getText().replace("/" + oldUnitStr + ")", "/" + unitStr + ")"));
        rampUpLabel.setText(rampUpLabel.getText().replace("(" + oldUnitStr + ")", "(" + unitStr + ")"));
        holdLabel.setText(holdLabel.getText().replace("(" + oldUnitStr + ")", "(" + unitStr + ")"));
    }
}
