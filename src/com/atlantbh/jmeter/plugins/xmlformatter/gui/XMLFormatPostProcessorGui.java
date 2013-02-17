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

/**
 * Class that exposes {@link XMLFormatPostProcessor} as PostProcessor.
 *
 */
public class XMLFormatPostProcessorGui extends AbstractPostProcessorGui 
{

	private static final long serialVersionUID = 2058383675974452993L;

	public XMLFormatPostProcessorGui() {
		init();
	}

	private void init() {
        setBorder(makeBorder());
        setLayout(new BorderLayout());
        JPanel vertPanel = new VerticalPanel();
        vertPanel.add(makeTitlePanel());
        
        add(vertPanel, BorderLayout.NORTH);
	}

	public TestElement createTestElement() {
		XMLFormatPostProcessor te = new XMLFormatPostProcessor();
		modifyTestElement(te);
		return te;
	}

	public String getLabelResource() {
		return "xml_format_post_processor_label";
	}
	
	public String getStaticLabel()
	{
		return "XML Format Post Processor";	
	}
	
	public void modifyTestElement(TestElement te) {
		configureTestElement(te);
	}


}
