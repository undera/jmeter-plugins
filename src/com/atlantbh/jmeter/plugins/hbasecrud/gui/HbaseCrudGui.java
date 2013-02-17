/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.hbasecrud.gui;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;

import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;

import com.atlantbh.jmeter.plugins.hbasecrud.HbaseCrud;

/**
 * Java class representing GUI for the HBase CRUD Sampler component in JMeter
 * 
 * @author Bakir Jusufbegovic / AtlantBH
 */
public class HbaseCrudGui extends AbstractSamplerGui {

	private static final long serialVersionUID = 1L;
	
	private JLabeledTextField hbaseZookeeperQuorumTextField = null;
	private JLabeledTextField hbaseSourceTableTextField = null;
	private JLabeledTextField rowKeyTextField = null;
	private JLabeledTextField fullColumnNamesListTextField = null;
	private JLabeledTextField columnFamilyColumnNameListTextField = null;
	private JLabeledTextField filterColumnFamiliesForTimestampTextField = null;
	
	private JCheckBox addOrUpdateDataOnRecordCheckBox = null;
	private JCheckBox deleteDataFromRecordCheckBox = null;
	private JCheckBox latestTimestampOperationCheckBox = null;
	private JCheckBox latestTimestampOperationWithExcludeFilterCheckBox = null;
	
	public HbaseCrudGui()
	{
		super();
		init();
	}
	
	public void init()
	{
		setLayout(new BorderLayout());
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        
        //main panel
        VerticalPanel panel = new VerticalPanel();
        panel.setBorder(BorderFactory.createEtchedBorder());
        
        //input panel
		VerticalPanel inputPanel = new VerticalPanel();
		inputPanel.setBorder(BorderFactory.createTitledBorder("Input parameters"));
		
		hbaseZookeeperQuorumTextField = new JLabeledTextField("hbase.zookeeper.quorum");
		hbaseSourceTableTextField = new JLabeledTextField("Hbase source table");
		rowKeyTextField = new JLabeledTextField("Rowkey");
		
		inputPanel.add(hbaseZookeeperQuorumTextField);
		inputPanel.add(hbaseSourceTableTextField);
		inputPanel.add(rowKeyTextField);
		
		//operations panel
		VerticalPanel operationsPanel = new VerticalPanel();
		operationsPanel.setBorder(BorderFactory.createTitledBorder("Operations"));
		
		ButtonGroup operationsGroup = new ButtonGroup();	
		
		addOrUpdateDataOnRecordCheckBox = new JCheckBox("Add Or Update Data On Record (full column names list - comma-separated)");
		fullColumnNamesListTextField = new JLabeledTextField("",40);
		HorizontalPanel hPanel1 = new HorizontalPanel();
		hPanel1.add(addOrUpdateDataOnRecordCheckBox);
		hPanel1.add(fullColumnNamesListTextField);
		
		registerCheckBoxForItemListener(addOrUpdateDataOnRecordCheckBox, fullColumnNamesListTextField);
		
		deleteDataFromRecordCheckBox = new JCheckBox("Delete Data From Record (column family:column name list or column family list - comma-separated)");
		columnFamilyColumnNameListTextField = new JLabeledTextField("",40);
		HorizontalPanel hPanel2 = new HorizontalPanel();
		hPanel2.add(deleteDataFromRecordCheckBox);
		hPanel2.add(columnFamilyColumnNameListTextField);
		
		registerCheckBoxForItemListener(deleteDataFromRecordCheckBox, columnFamilyColumnNameListTextField);
	
		operationsGroup.add(addOrUpdateDataOnRecordCheckBox);
		operationsGroup.add(deleteDataFromRecordCheckBox);
		operationsPanel.add(hPanel1);
		operationsPanel.add(hPanel2);
		
		//mode panel
		VerticalPanel modePanel = new VerticalPanel();
		modePanel.setBorder(BorderFactory.createTitledBorder("Operations mode"));
		
		ButtonGroup modeGroup = new ButtonGroup();
		
		latestTimestampOperationCheckBox = new JCheckBox("Latest Timestamp Operation");
		latestTimestampOperationWithExcludeFilterCheckBox = new JCheckBox("Latest Timestamp Operation With Exclude Column Families Filter (comma-separated)");
		filterColumnFamiliesForTimestampTextField = new JLabeledTextField("",25);
		
		HorizontalPanel hPanel3 = new HorizontalPanel();
		hPanel3.add(latestTimestampOperationWithExcludeFilterCheckBox);
		hPanel3.add(filterColumnFamiliesForTimestampTextField);
				
		registerCheckBoxForItemListener(latestTimestampOperationWithExcludeFilterCheckBox, filterColumnFamiliesForTimestampTextField);
		
		modeGroup.add(latestTimestampOperationCheckBox);
		modeGroup.add(latestTimestampOperationWithExcludeFilterCheckBox);
		modePanel.add(latestTimestampOperationCheckBox);
		modePanel.add(hPanel3);
		
		panel.add(inputPanel);
		panel.add(operationsPanel);
		panel.add(modePanel);
		add(panel, BorderLayout.CENTER);
	}
	
	public void registerCheckBoxForItemListener(JCheckBox checkBox, final JLabeledTextField textField)
	{
		checkBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					textField.setEnabled(true);
				}
				else if (e.getStateChange() == ItemEvent.DESELECTED)
				{
					textField.setEnabled(false);
				}
			}	
		}
		);
	}
	
	@Override
	public void clearGui()
	{
	    super.clearGui();
	    hbaseZookeeperQuorumTextField.setText("");
	    hbaseSourceTableTextField.setText("");
	    rowKeyTextField.setText("");
	    fullColumnNamesListTextField.setText("");
	    fullColumnNamesListTextField.setEnabled(true);
	    columnFamilyColumnNameListTextField.setText("");
	    columnFamilyColumnNameListTextField.setEnabled(false);
	    filterColumnFamiliesForTimestampTextField.setText("");
	    filterColumnFamiliesForTimestampTextField.setEnabled(false);
	    
		addOrUpdateDataOnRecordCheckBox.setSelected(true);
		deleteDataFromRecordCheckBox.setSelected(false);
		latestTimestampOperationCheckBox.setSelected(true);
		latestTimestampOperationWithExcludeFilterCheckBox.setSelected(false);
	}	
		
	@Override
	public TestElement createTestElement() {
		HbaseCrud hbaseCrud = new HbaseCrud();
		modifyTestElement(hbaseCrud);
		return hbaseCrud;
	}
	
	@Override
	public void modifyTestElement(TestElement element) {
		super.configureTestElement(element);
		if (element instanceof HbaseCrud)
		{
			HbaseCrud hbaseCrud = (HbaseCrud) element;
			hbaseCrud.setHbaseZookeeperQuorum(hbaseZookeeperQuorumTextField.getText());
			hbaseCrud.setHbaseSourceTable(hbaseSourceTableTextField.getText());
			hbaseCrud.setRowKey(rowKeyTextField.getText());
			hbaseCrud.setFullColumnNamesList(fullColumnNamesListTextField.getText());
			hbaseCrud.setColumnFamilyColumnNameList(columnFamilyColumnNameListTextField.getText());
			hbaseCrud.setFilterColumnFamiliesForTimestamp(filterColumnFamiliesForTimestampTextField.getText());
			hbaseCrud.setAddOrUpdateDataOnRecordBool(addOrUpdateDataOnRecordCheckBox.isSelected());
			hbaseCrud.setDeleteDataFromRecordBool(deleteDataFromRecordCheckBox.isSelected());
			hbaseCrud.setLatestTimestampOperationBool(latestTimestampOperationCheckBox.isSelected());
			hbaseCrud.setLatestTimestampOperationWithFilterBool(latestTimestampOperationWithExcludeFilterCheckBox.isSelected());
		}
	}
	
	@Override
	public void configure(TestElement element)
	{
		super.configure(element);
		if (element instanceof HbaseCrud)
		{
			HbaseCrud hbaseCrud = (HbaseCrud) element;
			hbaseZookeeperQuorumTextField.setText(hbaseCrud.getHbaseZookeeperQuorum());
			hbaseSourceTableTextField.setText(hbaseCrud.getHbaseSourceTable());
			rowKeyTextField.setText(hbaseCrud.getRowKey());
			fullColumnNamesListTextField.setText(hbaseCrud.getFullColumnNamesList());
			columnFamilyColumnNameListTextField.setText(hbaseCrud.getColumnFamilyColumnNameList());
			filterColumnFamiliesForTimestampTextField.setText(hbaseCrud.getFilterColumnFamiliesForTimestamp());
			addOrUpdateDataOnRecordCheckBox.setSelected(hbaseCrud.isAddOrUpdateDataOnRecordBool());
			deleteDataFromRecordCheckBox.setSelected(hbaseCrud.isDeleteDataFromRecordBool());
			latestTimestampOperationCheckBox.setSelected(hbaseCrud.isLatestTimestampOperationBool());
			latestTimestampOperationWithExcludeFilterCheckBox.setSelected(hbaseCrud.isLatestTimestampOperationWithFilterBool());
		}
	}

	@Override
	public String getLabelResource() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String getStaticLabel() {
	    return "HBase CRUD Sampler";
	}	
}
