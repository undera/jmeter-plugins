/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.jsonutils.jsonpathassertion.gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import org.apache.jmeter.assertions.gui.AbstractAssertionGui;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;

import com.atlantbh.jmeter.plugins.jsonutils.jsonpathassertion.JSONPathAssertion;

/**
 * Java class representing GUI for the JSON Path Assertion component in JMeter
 * 
 * @author Semir Sabic / AtlantBH
 */
public class JSONPathAssertionGui extends AbstractAssertionGui{

	private static final long serialVersionUID = 1L;
	private JLabeledTextField jsonPath = null;
	private JLabeledTextField jsonValue = null;
	private JCheckBox jsonValidation = null;

	public JSONPathAssertionGui(){
		init();
	}
	
	public void init(){
		setLayout(new BorderLayout());
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        
        VerticalPanel panel = new VerticalPanel();
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "JSON path assertion"));
       
        jsonPath = new JLabeledTextField("JSON path: ");
        jsonValue = new JLabeledTextField("Expected value: ");
        jsonValidation = new JCheckBox("Validate against expected value");
        
        panel.add(jsonPath);
        panel.add(jsonValue);
        panel.add(jsonValidation);
        
        add(panel,BorderLayout.CENTER);
	}
	
	@Override
	public void clearGui(){
		super.clearGui();
		jsonPath.setText("");
		jsonValue.setText("");
		jsonValidation.setSelected(false);
	}
	
	@Override
	public TestElement createTestElement() {
		JSONPathAssertion jpAssertion = new JSONPathAssertion();
		modifyTestElement(jpAssertion);
		return jpAssertion;
	}

	@Override
	public String getLabelResource() {
		return "JSON Path Assertion";
	}
	
	@Override
	public String getStaticLabel() {
		return "JSON Path Assertion";
	}

	@Override
	public void modifyTestElement(TestElement element) {
		super.configureTestElement(element);
		if(element instanceof JSONPathAssertion){
			JSONPathAssertion jpAssertion = (JSONPathAssertion) element;
			jpAssertion.setJsonPath(jsonPath.getText());
			jpAssertion.setExpectedValue(jsonValue.getText());
			jpAssertion.setJsonValidationBool(jsonValidation.isSelected());
		}		
	}
	
	@Override
    public void configure(TestElement element) {
        super.configure(element);
        JSONPathAssertion jpAssertion = (JSONPathAssertion) element;
        jsonPath.setText(jpAssertion.getJsonPath());
        jsonValue.setText(jpAssertion.getExpectedValue());
        jsonValidation.setSelected(jpAssertion.isJsonValidationBool());
    }
}