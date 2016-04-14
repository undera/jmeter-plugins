/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */
package com.atlantbh.jmeter.plugins.jsontoxmlconverter.gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;

import org.apache.jmeter.gui.util.*;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;

import com.atlantbh.jmeter.plugins.jsontoxmlconverter.JSONToXMLConverter;
import kg.apc.jmeter.JMeterPluginsUtils;

/**
 * This is JSON to XML converter GUI class which contains necessary methods for
 * making GUI component suitable for execution in JMeter.
 */
public class JSONToXMLConverterGui extends AbstractSamplerGui {

    private static final long serialVersionUID = 1L;
    private JLabeledTextField jsonInputTextField = null;
    private static final String WIKIPAGE = "JSONToXMLConverter";

    public JSONToXMLConverterGui() {
        super();
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());
        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);

        VerticalPanel panel = new VerticalPanel();
        panel.setBorder(BorderFactory.createEtchedBorder());
        jsonInputTextField = new JLabeledTextField("JSON input");
        panel.add(jsonInputTextField);
        add(panel, BorderLayout.CENTER);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        jsonInputTextField.setText("");
    }

    @Override
    public TestElement createTestElement() {
        JSONToXMLConverter converter = new JSONToXMLConverter();
        modifyTestElement(converter);
        converter.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return converter;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        if (element instanceof JSONToXMLConverter) {
            JSONToXMLConverter conv = (JSONToXMLConverter) element;
            conv.setJsonInput(jsonInputTextField.getText());
        }
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        JSONToXMLConverter conv = null;
        if (element instanceof JSONToXMLConverter) {
            conv = (JSONToXMLConverter) element;
            jsonInputTextField.setText(conv.getJsonInput());
        }
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("JSON to XML Converter");
    }
}
