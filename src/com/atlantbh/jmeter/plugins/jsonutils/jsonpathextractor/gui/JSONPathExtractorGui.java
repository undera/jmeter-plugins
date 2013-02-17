/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.jsonutils.jsonpathextractor.gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.processor.gui.AbstractPostProcessorGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;

import com.atlantbh.jmeter.plugins.jsonutils.jsonpathextractor.JSONPathExtractor;

/**
 * This is JSONPath extractor GUI class which contains necessary methods for making GUI Post processor component suitable for execution in JMeter
 * 
 * @author Bakir Jusufbegovic / AtlantBH
 */
public class JSONPathExtractorGui extends AbstractPostProcessorGui{

	private static final long serialVersionUID = 1L;
	
	private JLabeledTextField jsonExtractValueTextField = null;
	private JLabeledTextField jsonPathTextField = null;

	public JSONPathExtractorGui()
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
        
        jsonExtractValueTextField = new JLabeledTextField("Name: ");
        jsonPathTextField = new JLabeledTextField("JSON path: ");
        
        panel.add(jsonExtractValueTextField);
        panel.add(jsonPathTextField);
        add(panel,BorderLayout.CENTER);
	}
	
	@Override
	public void clearGui(){
		super.clearGui();
		jsonExtractValueTextField.setText("");
		jsonPathTextField.setText("");
	}
	
	@Override
	public TestElement createTestElement() {
		// TODO Auto-generated method stub
		JSONPathExtractor extractor = new JSONPathExtractor();
		modifyTestElement(extractor);
		return extractor;
	}

	@Override
	public String getLabelResource() {
		return "JSON Path Extractor";
	}
	
	@Override
	public String getStaticLabel() {
		return "JSON Path Extractor";
	}

	@Override
	public void modifyTestElement(TestElement element) {
		super.configureTestElement(element);
		if (element instanceof JSONPathExtractor)
		{
			JSONPathExtractor extractor = (JSONPathExtractor) element;
			extractor.setVar(jsonExtractValueTextField.getText());
			extractor.setJsonPath(jsonPathTextField.getText());
		}		
	}
	
	@Override
	public void configure(TestElement element){
		super.configure(element);
		if (element instanceof JSONPathExtractor)
		{
			JSONPathExtractor extractor = (JSONPathExtractor) element;
			jsonExtractValueTextField.setText(extractor.getVar());
			jsonPathTextField.setText(extractor.getJsonPath());
		}
	}
}