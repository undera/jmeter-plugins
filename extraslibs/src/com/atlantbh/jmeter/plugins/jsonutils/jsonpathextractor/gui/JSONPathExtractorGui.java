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
 *
 * @author Bakir Jusufbegovic / AtlantBH
 */
public class JSONPathExtractorGui extends AbstractPostProcessorGui {

    private static final long serialVersionUID = 1L;
    private JTextField variableNameTextField = null;
    private JTextField jsonPathTextField = null;
    private JTextField defaultValTextField = null;
    private static final String WIKIPAGE = "JSONPathExtractor";

    public JSONPathExtractorGui() {
        super();
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());
        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;

        addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel("Variable Name: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 0, variableNameTextField = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("JSON Path: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1,1, jsonPathTextField = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("Default Value: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, defaultValTextField = new JTextField(20));

        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
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
    }

    @Override
    public TestElement createTestElement() {
        // TODO Auto-generated method stub
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
        }
    }
}