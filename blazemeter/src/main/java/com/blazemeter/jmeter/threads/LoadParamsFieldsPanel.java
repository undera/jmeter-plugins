package com.blazemeter.jmeter.threads;

import com.blazemeter.jmeter.gui.ArrangedLabelFieldPanel;

import javax.swing.*;
import javax.swing.event.DocumentListener;

public class LoadParamsFieldsPanel extends ArrangedLabelFieldPanel implements ParamsPanel {
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
    public void UItoModel(AbstractDynamicThreadGroup tg) {
        tg.setTargetLevel(targetRate.getText());
        tg.setRampUp(rampUpTime.getText());
        tg.setSteps(steps.getText());
        tg.setHold(holdFor.getText());
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

    /*
    private void changeUnitInLabels() {
        if (unitGroup.getSelection() != null) {
            String unitStr = ArrivalsThreadGroup.getUnitStr(unitGroup.getSelection().getActionCommand());
            targetRateLabel.setText("Target Rate (arrivals/" + unitStr + "): ");
            rampUpLabel.setText("Ramp Up Time (" + unitStr + "): ");
            holdLabel.setText("Hold Target Rate Time (" + unitStr + "): ");
        }
    }
    */
}
