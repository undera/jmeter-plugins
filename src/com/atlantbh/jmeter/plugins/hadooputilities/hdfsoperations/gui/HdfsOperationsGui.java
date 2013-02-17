/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.hadooputilities.hdfsoperations.gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;

import com.atlantbh.jmeter.plugins.hadooputilities.hdfsoperations.HdfsOperations;

/**
 * Java class representing GUI for the HDFS Operations component in JMeter
 * 
 * @author Bakir Jusufbegovic / AtlantBH
 */
public class HdfsOperationsGui extends AbstractSamplerGui {

	private static final long serialVersionUID = 1L;
	
	private JLabeledTextField inputFilePathTextField = null;
	private JLabeledTextField outputFilePathTextField = null;
	private JLabeledTextField nameNodeTextField = null;

	public HdfsOperationsGui()
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
        nameNodeTextField = new JLabeledTextField("Namenode");
        inputFilePathTextField = new JLabeledTextField("Input file destination");
        outputFilePathTextField = new JLabeledTextField("Output destination on HDFS");
        
        panel.add(nameNodeTextField);
        panel.add(inputFilePathTextField);
        panel.add(outputFilePathTextField);
        add(panel, BorderLayout.CENTER);
	}
	
	@Override
	public void clearGui()
	{
	    super.clearGui();
	    nameNodeTextField.setText("");
	    inputFilePathTextField.setText("");
	    outputFilePathTextField.setText("");
	}
	
	@Override
	public TestElement createTestElement() {
		HdfsOperations operations = new HdfsOperations();
		modifyTestElement(operations);
		return operations;
	}

	@Override
	public String getLabelResource() {
		return "HDFS operations";
	}
	
	@Override
	public String getStaticLabel() {
	    return "HDFS operations";
	}

	@Override
	public void modifyTestElement(TestElement element) {
		super.configureTestElement(element);
		if (element instanceof HdfsOperations)
		{
			HdfsOperations operations = (HdfsOperations) element;
			operations.setNameNode(nameNodeTextField.getText());
			operations.setInputFilePath(inputFilePathTextField.getText());
			operations.setOutputFilePath(outputFilePathTextField.getText());
		}
	}
	
	@Override
	public void configure(TestElement element)
	{
		super.configure(element);
		if (element instanceof HdfsOperations)
		{
			HdfsOperations operations = (HdfsOperations) element;
			nameNodeTextField.setText(operations.getNameNode());
			inputFilePathTextField.setText(operations.getInputFilePath());
			outputFilePathTextField.setText(operations.getOutputFilePath());
		}
	}
	
	
}
