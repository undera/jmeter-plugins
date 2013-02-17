/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.hadooputilities.jobstatistics.gui;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;

import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;

import com.atlantbh.jmeter.plugins.hadooputilities.jobstatistics.JobStatistics;

/**
 * Java class representing GUI for the Hadoop Job Tracker Sampler component in JMeter
 * 
 * @author Bakir Jusufbegovic / AtlantBH
 */
public class JobStatisticsGui extends AbstractSamplerGui {

private static final long serialVersionUID = 1L;
	
	private JLabeledTextField jobTrackerTextField = null;
	private JLabeledTextField jobTextField = null;
	private JLabeledTextField groupOfCountersNameTextField = null;
	
	private JCheckBox getJobCountersByIdCheckBox;
	private JCheckBox getJobCountersByIdAndGroupNameCheckBox;
	private JCheckBox getJobStatisticsByJobIdCheckBox;
	private JCheckBox getTaskLevelCountersByJobIdCheckBox;
	private JCheckBox getTaskStatisticsByJobIdCheckBox;

	public JobStatisticsGui()
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
          
		//menu panel
		VerticalPanel menuPanel = new VerticalPanel();
		menuPanel.setBorder(BorderFactory.createTitledBorder("Input parameters"));
		
		jobTrackerTextField = new JLabeledTextField("mapred.job.tracker");
		jobTextField = new JLabeledTextField("Job Id");
		groupOfCountersNameTextField = new JLabeledTextField("Job Counters Group");
		
	    menuPanel.add(jobTrackerTextField);
		menuPanel.add(jobTextField);
		menuPanel.add(groupOfCountersNameTextField);
		
		//job stats panel
		VerticalPanel jobStatsPanel = new VerticalPanel();
		jobStatsPanel.setBorder(BorderFactory.createTitledBorder("Job stats"));
		
		ButtonGroup jobStatsGroup = new ButtonGroup();		
		getJobCountersByIdCheckBox = new JCheckBox("Get Job Counters By Job Id");
		getJobCountersByIdAndGroupNameCheckBox = new JCheckBox("Get Job Counters By Job Id And Group Name");
		getJobStatisticsByJobIdCheckBox = new JCheckBox("Get Job Stats By Job Id");
		
		jobStatsGroup.add(getJobCountersByIdCheckBox);
		jobStatsGroup.add(getJobCountersByIdAndGroupNameCheckBox);
		jobStatsGroup.add(getJobStatisticsByJobIdCheckBox);
		
		registerCheckBoxForItemListener(getJobCountersByIdAndGroupNameCheckBox);
		
		jobStatsPanel.add(getJobCountersByIdCheckBox);
		jobStatsPanel.add(getJobCountersByIdAndGroupNameCheckBox);
		jobStatsPanel.add(getJobStatisticsByJobIdCheckBox);
		
		//task stats panel
		VerticalPanel taskStatsPanel = new VerticalPanel();
		taskStatsPanel.setBorder(BorderFactory.createTitledBorder("Task stats"));
		
		getTaskLevelCountersByJobIdCheckBox = new JCheckBox("Get Task Level Counters By Job Id");
		getTaskStatisticsByJobIdCheckBox = new JCheckBox("Get Task Stats By Job Id");
		
		jobStatsGroup.add(getTaskLevelCountersByJobIdCheckBox);
		jobStatsGroup.add(getTaskStatisticsByJobIdCheckBox);
		
		taskStatsPanel.add(getTaskLevelCountersByJobIdCheckBox);
		taskStatsPanel.add(getTaskStatisticsByJobIdCheckBox);
		
        panel.add(menuPanel);
        panel.add(jobStatsPanel);
        panel.add(taskStatsPanel);
        add(panel,BorderLayout.CENTER);
        
		if (getJobCountersByIdAndGroupNameCheckBox.isSelected())
		{
			groupOfCountersNameTextField.setEnabled(true);
		}
	}	
	
	public void registerCheckBoxForItemListener(JCheckBox checkBox)
	{
		checkBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					groupOfCountersNameTextField.setEnabled(true);
				}
				else if (e.getStateChange() == ItemEvent.DESELECTED)
				{
					groupOfCountersNameTextField.setEnabled(false);
				}
			}	
		}
		);
	}
	
	@Override
	public void clearGui()
	{
	    super.clearGui();
	    jobTrackerTextField.setText("");
	    jobTextField.setText("");
	    groupOfCountersNameTextField.setText("");
	    groupOfCountersNameTextField.setEnabled(false);
	    getJobCountersByIdCheckBox.setSelected(true);
	    getJobCountersByIdAndGroupNameCheckBox.setSelected(false);
	    getJobStatisticsByJobIdCheckBox.setSelected(false);
	    getTaskLevelCountersByJobIdCheckBox.setSelected(false);
	    getTaskStatisticsByJobIdCheckBox.setSelected(false);
	}
	
	@Override
	public TestElement createTestElement() {
		JobStatistics jobStats = new JobStatistics();
		modifyTestElement(jobStats);
		return jobStats;
	}
	
	@Override
	public void modifyTestElement(TestElement element) {
		super.configureTestElement(element);
		if (element instanceof JobStatistics)
		{
			JobStatistics jobStats = (JobStatistics) element;
			jobStats.setJobTracker(jobTrackerTextField.getText());
			jobStats.setJobId(jobTextField.getText());
			jobStats.setJobIdAndGroup(groupOfCountersNameTextField.getText());
			jobStats.setJobCountersByIdBool(getJobCountersByIdCheckBox.isSelected());
			jobStats.setJobCountersByIdAndGroupBool(getJobCountersByIdAndGroupNameCheckBox.isSelected());
			jobStats.setJobStatisticsByIdBool(getJobStatisticsByJobIdCheckBox.isSelected());
			jobStats.setTaskCountersByIdBool(getTaskLevelCountersByJobIdCheckBox.isSelected());
			jobStats.setTaskStatisticsByIdBool(getTaskStatisticsByJobIdCheckBox.isSelected());
		}
	}
	
	@Override
	public void configure(TestElement element)
	{
		super.configure(element);
		if (element instanceof JobStatistics)
		{
			JobStatistics jobStats = (JobStatistics) element;
			jobTrackerTextField.setText(jobStats.getJobTracker());
			jobTextField.setText(jobStats.getJobId());
			groupOfCountersNameTextField.setText(jobStats.getJobIdAndGroup());
			getJobCountersByIdCheckBox.setSelected(jobStats.isJobCountersByIdBool());
			getJobCountersByIdAndGroupNameCheckBox.setSelected(jobStats.isJobCountersByIdAndGroupBool());
			getJobStatisticsByJobIdCheckBox.setSelected(jobStats.isJobStatisticsByIdBool());
			getTaskLevelCountersByJobIdCheckBox.setSelected(jobStats.isTaskCountersByIdBool());
			getTaskStatisticsByJobIdCheckBox.setSelected(jobStats.isTaskStatisticsByIdBool());
		}
	}
	
	@Override
	public String getLabelResource() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String getStaticLabel() {
	    return "Hadoop Job Tracker Sampler";
	}	
}
