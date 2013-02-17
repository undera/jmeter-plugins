/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.jsonutils.jsonformatter.gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.processor.gui.AbstractPostProcessorGui;
import org.apache.jmeter.testelement.TestElement;

import com.atlantbh.jmeter.plugins.jsonutils.jsonformatter.JSONFormatter;

/**
 * Java class representing GUI for the JSON Format Post Processor component in JMeter
 * 
 * @author Bakir Jusufbegovic / AtlantBH
 */
public class JSONFormatterGui extends AbstractPostProcessorGui{

	private static final long serialVersionUID = 1L;
	
	public JSONFormatterGui()
	{
		super();
		init();
	}
	
	public void init()
	{
		setLayout(new BorderLayout());
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        
        VerticalPanel panel = new VerticalPanel();
        panel.setBorder(BorderFactory.createEtchedBorder());
        
        add(panel,BorderLayout.CENTER);
	}
	
	@Override
	public void clearGui(){
		super.clearGui();
	}
	
	@Override
	public TestElement createTestElement() {
		JSONFormatter formatter = new JSONFormatter();
		modifyTestElement(formatter);
		return formatter;
	}

	@Override
	public String getLabelResource() {
		return "JSON Format Post Processor";
	}
	
	@Override
	public String getStaticLabel() {
		return "JSON Format Post Processor";
	}

	@Override
	public void modifyTestElement(TestElement element) {
		super.configureTestElement(element);		
	}
}