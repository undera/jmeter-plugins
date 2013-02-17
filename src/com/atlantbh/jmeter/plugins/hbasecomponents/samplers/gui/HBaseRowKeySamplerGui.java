/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.hbasecomponents.samplers.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;

import com.atlantbh.jmeter.plugins.hbasecomponents.samplers.HBaseRowkeySampler;

/**
 * GUI for {@link HBaseRowkeySampler}
 *
 */
public class HBaseRowKeySamplerGui  extends AbstractSamplerGui{

	private static final long serialVersionUID = -7273634183896280753L;

	private JLabeledTextField connTF;
	private JLabeledTextField tableTF;
	private JLabeledTextField rowKeyTF;
	private JCheckBox emitVarsCB;
	private JCheckBox latestTimestampRows;
	
	public HBaseRowKeySamplerGui() {
		super();
		init();
	}
	
	private void init() {
		setBorder(makeBorder());
        setLayout(new BorderLayout(0, 10));
	
        JPanel vertPanel = new VerticalPanel();
        vertPanel.add(makeTitlePanel());		
        add(vertPanel, BorderLayout.NORTH);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		mainPanel.add(connTF = createTF("Connection name"));
		mainPanel.add(tableTF = createTF("Table"));
		mainPanel.add(rowKeyTF = createTF("RowKey"));
		
		emitVarsCB = new JCheckBox("Create variable for each row");
		latestTimestampRows = new JCheckBox("Retrieve most recent record");
		mainPanel.add(emitVarsCB);
		mainPanel.add(latestTimestampRows);
		
		add(mainPanel, BorderLayout.CENTER);
	}
	
	public String getStaticLabel() {
		return "HBase Rowkey Sampler";	
	}	

	@Override
	public String getLabelResource() {
		return "hbase_rowkey_sampler";
	}
	
	@Override
	public TestElement createTestElement() {
        HBaseRowkeySampler sampler = new HBaseRowkeySampler();
        configureTestElement(sampler);
        modifyTestElement(sampler);
        return sampler;	
    }

	@Override
	public void modifyTestElement(TestElement te) {
		HBaseRowkeySampler sampler = (HBaseRowkeySampler)te;

		sampler.setConnectionName(connTF.getText());
		sampler.sethBaseTable(tableTF.getText());
		sampler.setRowKey(rowKeyTF.getText());
		sampler.setOmitVars(emitVarsCB.isSelected());
		sampler.setLatestTimestampRows(latestTimestampRows.isSelected());
		
		super.configureTestElement(sampler);		
	}
	
    //TODO typeCB
	public void configure(TestElement el) {
        super.configure(el);
        HBaseRowkeySampler sampler = (HBaseRowkeySampler) el;

        connTF.setText(sampler.getConnectionName());
        tableTF.setText(sampler.gethBaseTable());
        rowKeyTF.setText(sampler.getRowKey());
        emitVarsCB.setSelected(sampler.getOmitVars());
        latestTimestampRows.setSelected(sampler.getLatestTimestampRows());
    }			
	
    //TODO typeCB
    public void clearGui() {
        super.clearGui();
        tableTF.setText("");
        rowKeyTF.setText("");
        emitVarsCB.setSelected(false);
        latestTimestampRows.setSelected(false);
    }  	
		
	
	private JLabeledTextField createTF(String name) {
		JLabeledTextField tf = new JLabeledTextField(name);
		tf.setMaximumSize(new Dimension(10000, 26));
		tf.setBorder(new EmptyBorder(3, 0, 3, 0));
		tf.getComponents()[0].setPreferredSize(new Dimension(120, tf.getComponents()[0].getPreferredSize().height));
		return tf;
	}		

}
