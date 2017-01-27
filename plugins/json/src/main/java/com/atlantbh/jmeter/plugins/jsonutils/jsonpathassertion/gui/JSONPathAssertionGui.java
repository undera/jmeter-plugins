/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */
package com.atlantbh.jmeter.plugins.jsonutils.jsonpathassertion.gui;

import com.atlantbh.jmeter.plugins.jsonutils.jsonpathassertion.JSONPathAssertion;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.assertions.gui.AbstractAssertionGui;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextArea;
import org.apache.jorphan.gui.JLabeledTextField;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Java class representing GUI for the JSON Path Assertion component in JMeter
 */
public class JSONPathAssertionGui extends AbstractAssertionGui implements ChangeListener {

    //private static final Logger log = LoggingManager.getLoggerForClass();
    private static final long serialVersionUID = 1L;
    private JLabeledTextField jsonPath = null;
    private JLabeledTextArea jsonValue = null;
    private JCheckBox jsonValidation = null;
    private JCheckBox expectNull = null;
    private JCheckBox invert = null;
    private static final String WIKIPAGE = "JSONPathAssertion";
    private JCheckBox isRegex;

    public JSONPathAssertionGui() {
        init();
    }

    public void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());
        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);

        VerticalPanel panel = new VerticalPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        jsonPath = new JLabeledTextField("Assert JSON Path exists: ");
        jsonValidation = new JCheckBox("Additionally assert value");
        isRegex = new JCheckBox("Match as regular expression");
        jsonValue = new JLabeledTextArea("Expected Value: ");
        expectNull = new JCheckBox("Expect null");
        invert = new JCheckBox("Invert assertion (will fail if above conditions met)");

        jsonValidation.addChangeListener(this);
        expectNull.addChangeListener(this);

        panel.add(jsonPath);
        panel.add(jsonValidation);
        panel.add(isRegex);
        panel.add(jsonValue);
        panel.add(expectNull);
        panel.add(invert);

        add(panel, BorderLayout.CENTER);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        jsonPath.setText("$.");
        jsonValue.setText("");
        jsonValidation.setSelected(false);
        expectNull.setSelected(false);
        invert.setSelected(false);
        isRegex.setSelected(true);
    }

    @Override
    public TestElement createTestElement() {
        JSONPathAssertion jpAssertion = new JSONPathAssertion();
        modifyTestElement(jpAssertion);
        jpAssertion.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return jpAssertion;
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("JSON Path Assertion");
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        if (element instanceof JSONPathAssertion) {
            JSONPathAssertion jpAssertion = (JSONPathAssertion) element;
            jpAssertion.setJsonPath(jsonPath.getText());
            jpAssertion.setExpectedValue(jsonValue.getText());
            jpAssertion.setJsonValidationBool(jsonValidation.isSelected());
            jpAssertion.setExpectNull(expectNull.isSelected());
            jpAssertion.setInvert(invert.isSelected());
            jpAssertion.setIsRegex(isRegex.isSelected());
        }
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        JSONPathAssertion jpAssertion = (JSONPathAssertion) element;
        jsonPath.setText(jpAssertion.getJsonPath());
        jsonValue.setText(jpAssertion.getExpectedValue());
        jsonValidation.setSelected(jpAssertion.isJsonValidationBool());
        expectNull.setSelected(jpAssertion.isExpectNull());
        invert.setSelected(jpAssertion.isInvert());
        isRegex.setSelected(jpAssertion.isUseRegex());
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        jsonValue.setEnabled(jsonValidation.isSelected() && !expectNull.isSelected());
        isRegex.setEnabled(jsonValidation.isSelected() && !expectNull.isSelected());
    }
}