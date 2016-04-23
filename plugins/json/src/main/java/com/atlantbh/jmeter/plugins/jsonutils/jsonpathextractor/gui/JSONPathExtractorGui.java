/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */
package com.atlantbh.jmeter.plugins.jsonutils.jsonpathextractor.gui;

import com.atlantbh.jmeter.plugins.jsonutils.jsonpathextractor.JSONPathExtractor;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.processor.gui.AbstractPostProcessorGui;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import java.awt.*;

/**
 * This is JSONPath extractor GUI class which contains necessary methods for
 * making GUI Post processor component suitable for execution in JMeter
 */
public class JSONPathExtractorGui extends AbstractPostProcessorGui {

    private static final long serialVersionUID = 1L;
    private JTextField variableNameTextField = null;
    private JTextField jsonPathTextField = null;
    private JTextField defaultValTextField = null;
    private static final String WIKIPAGE = "JSONPathExtractor";
    private JRadioButton useBody;
    private JRadioButton useVariable;
    private ButtonGroup group;
    private JTextField srcVariableName;

    public JSONPathExtractorGui() {
        super();
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());
        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(makeSourcePanel(), c);

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;

        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Destination Variable Name: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, variableNameTextField = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("JSONPath Expression: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, jsonPathTextField = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 3, new JLabel("Default Value: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 3, defaultValTextField = new JTextField(20));

        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
    }


    private JPanel makeSourcePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Apply to:"));

        useBody = new JRadioButton("Response Text");
        useVariable = new JRadioButton("JMeter Variable:");
        srcVariableName = new JTextField(20);

        group = new ButtonGroup();
        group.add(useBody);
        group.add(useVariable);

        panel.add(useBody);
        panel.add(useVariable);
        panel.add(srcVariableName);

        useBody.setSelected(true);

        useBody.setActionCommand(JSONPathExtractor.SUBJECT_BODY);
        useVariable.setActionCommand(JSONPathExtractor.SUBJECT_VARIABLE);

        return panel;
    }

    private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        variableNameTextField.setText("");
        jsonPathTextField.setText("");
        defaultValTextField.setText("");
        srcVariableName.setText("");
        useBody.setSelected(true);
    }

    @Override
    public TestElement createTestElement() {
        JSONPathExtractor extractor = new JSONPathExtractor();
        modifyTestElement(extractor);
        extractor.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return extractor;
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("JSON Path Extractor");
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        if (element instanceof JSONPathExtractor) {
            JSONPathExtractor extractor = (JSONPathExtractor) element;
            extractor.setVar(variableNameTextField.getText());
            extractor.setJsonPath(jsonPathTextField.getText());
            extractor.setDefaultValue(defaultValTextField.getText());
            extractor.setSrcVariableName(srcVariableName.getText());
            extractor.setSubject(group.getSelection().getActionCommand());
        }
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (element instanceof JSONPathExtractor) {
            JSONPathExtractor extractor = (JSONPathExtractor) element;
            variableNameTextField.setText(extractor.getVar());
            jsonPathTextField.setText(extractor.getJsonPath());
            defaultValTextField.setText(extractor.getDefaultValue());
            srcVariableName.setText(extractor.getSrcVariableName());
            if (extractor.getSubject().equals(JSONPathExtractor.SUBJECT_VARIABLE)) {
                useVariable.setSelected(true);
            } else {
                useBody.setSelected(true);
            }
        }
    }
}