package com.blazemeter.jmeter.gui;

import javax.swing.*;
import java.awt.*;

public class ArrangedLabelFieldPanel extends JPanel {

    private final GridBagConstraints labelC;
    private final GridBagConstraints fieldC;
    private final GridBagConstraints oneLineC;
    private int rowN = 0;

    public ArrangedLabelFieldPanel() {
        super(new GridBagLayout());
        labelC = new GridBagConstraints();
        labelC.anchor = GridBagConstraints.FIRST_LINE_END;
        labelC.gridx = 0;
        labelC.insets = new Insets(5, 2, 5, 2);

        fieldC = new GridBagConstraints();
        fieldC.anchor = GridBagConstraints.FIRST_LINE_START;
        fieldC.weightx = 1.0;
        fieldC.fill = GridBagConstraints.HORIZONTAL;
        fieldC.gridx = 1;
        fieldC.insets = new Insets(5, 2, 5, 2);

        oneLineC = new GridBagConstraints();
        oneLineC.gridx = 0;
        oneLineC.gridwidth = 2;
        oneLineC.fill = GridBagConstraints.HORIZONTAL;
        oneLineC.weighty = 1;
        oneLineC.insets = new Insets(5, 0, 5, 0);
    }

    public void add(Component label, Component field) {
        assert label != null;
        assert field != null;
        labelC.gridy = rowN;
        fieldC.gridy = rowN;
        add(label, labelC);
        add(field, fieldC);
        rowN++;
    }

    public Component add(String label, Component field) {
        add(new JLabel(label), field);
        return this;
    }

    public Component add(Component comp) {
        add(comp, oneLineC);
        rowN++;
        return this;
    }
}
