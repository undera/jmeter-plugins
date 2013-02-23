/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */
package com.atlantbh.jmeter.plugins.xmlformatter.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.processor.gui.AbstractPostProcessorGui;
import org.apache.jmeter.testelement.TestElement;

import com.atlantbh.jmeter.plugins.xmlformatter.XMLFormatPostProcessor;
import kg.apc.jmeter.JMeterPluginsUtils;

/**
 * Class that exposes {@link XMLFormatPostProcessor} as PostProcessor.
 *
 */
public class XMLFormatPostProcessorGui extends AbstractPostProcessorGui {

    private static final long serialVersionUID = 2058383675974452993L;
    private static final String WIKIPAGE = "XMLFormatPostProcessor";

    public XMLFormatPostProcessorGui() {
        init();
    }

    private void init() {
        setBorder(makeBorder());
        setLayout(new BorderLayout());
        JPanel vertPanel = new VerticalPanel();
        vertPanel.add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);
        add(vertPanel, BorderLayout.NORTH);
    }

    @Override
    public TestElement createTestElement() {
        XMLFormatPostProcessor te = new XMLFormatPostProcessor();
        modifyTestElement(te);
        te.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return te;
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("XML Format Post Processor");
    }

    @Override
    public void modifyTestElement(TestElement te) {
        configureTestElement(te);
    }
}
