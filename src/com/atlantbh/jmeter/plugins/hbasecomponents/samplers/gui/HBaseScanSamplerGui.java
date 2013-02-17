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
import org.apache.jorphan.gui.JLabeledTextArea;
import org.apache.jorphan.gui.JLabeledTextField;

import com.atlantbh.jmeter.plugins.hbasecomponents.samplers.HBaseScanSampler;

/**
 * GUI for {@link HBaseScanSampler}
 *
 */
public class HBaseScanSamplerGui  extends AbstractSamplerGui{

	private static final long serialVersionUID = -4115128550923988380L;

	private JLabeledTextField connTF;
	private JLabeledTextField tableTF;
	private JLabeledTextField startRowKeyTF;
	private JLabeledTextField endRowKeyTF;
	private JLabeledTextField recordCountTF;
	private JLabeledTextArea filtersTA;
	private JCheckBox emitVarsCB;
	private JCheckBox latestTimestampRows;
	
	
	public HBaseScanSamplerGui() {
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
		mainPanel.add(startRowKeyTF = createTF("Start rowKey"));
		mainPanel.add(endRowKeyTF = createTF("End rowKey"));
		mainPanel.add(recordCountTF = createTF("Limit"));

		filtersTA = new JLabeledTextArea("Filters");
		mainPanel.add(filtersTA);
	
		emitVarsCB = new JCheckBox("Create variable for each row");
		latestTimestampRows = new JCheckBox("Retrieve most recent record");
		
		mainPanel.add(emitVarsCB);
		mainPanel.add(latestTimestampRows);
	
		add(mainPanel, BorderLayout.CENTER);		
	}
	
	public String getStaticLabel() {
		return "HBase Scan Sampler";	
	}	

	@Override
	public String getLabelResource() {
		return "hbase_scan_sampler";
	}	
	
	@Override
	public TestElement createTestElement() {
		HBaseScanSampler sampler = new HBaseScanSampler();
        configureTestElement(sampler);
        modifyTestElement(sampler);
        return sampler;	
	}



	@Override
	public void modifyTestElement(TestElement te) {
		HBaseScanSampler sampler = (HBaseScanSampler)te;

		sampler.setConnectionName(connTF.getText());
		sampler.setTableName(tableTF.getText());
		sampler.setStartKey(startRowKeyTF.getText());
		sampler.setEndKey(endRowKeyTF.getText());
		sampler.setLimit(recordCountTF.getText());
		sampler.setFilter(filtersTA.getText());
		sampler.setOmitVars(emitVarsCB.isSelected());
		sampler.setLatestTimestampRows(latestTimestampRows.isSelected());
		
		super.configureTestElement(sampler);	
	}
	
    //TODO typeCB
	public void configure(TestElement el) {
        super.configure(el);
        HBaseScanSampler sampler = (HBaseScanSampler) el;

		connTF.setText(sampler.getConnectionName());
		tableTF.setText(sampler.getTableName());
		startRowKeyTF.setText(sampler.getStartKey());
		endRowKeyTF.setText(sampler.getEndKey());
		recordCountTF.setText(sampler.getLimit());
		filtersTA.setText(sampler.getFilter());
		emitVarsCB.setSelected(sampler.getOmitVars());
		latestTimestampRows.setSelected(sampler.getLatestTimestampRows());
    }			
	
    //TODO typeCB
    public void clearGui() {
        super.clearGui();
        connTF.setText("");
        tableTF.setText("");
        startRowKeyTF.setText("");
        endRowKeyTF.setText("");
        recordCountTF.setText("5");
        filtersTA.setText("");
        emitVarsCB.setSelected(false);
        latestTimestampRows.setSelected(false);
    }  	
    
	private JLabeledTextField createTF(String name) {
		JLabeledTextField tf = new JLabeledTextField(name);
		tf.setMaximumSize(new Dimension(10000, 26));
		tf.setBorder(new EmptyBorder(3, 0, 3, 0));
		tf.getComponents()[0].setPreferredSize(new Dimension(180, tf.getComponents()[0].getPreferredSize().height));
		return tf;
	}    

 
}
