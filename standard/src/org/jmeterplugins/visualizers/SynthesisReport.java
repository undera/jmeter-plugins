/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.jmeterplugins.visualizers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import kg.apc.jmeter.JMeterPluginsUtils;

import org.apache.jmeter.gui.util.FileDialoger;
import org.apache.jmeter.gui.util.HeaderAsPropertyRenderer;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.save.CSVSaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.SamplingStatCalculator;
import org.apache.jorphan.gui.NumberRenderer;
import org.apache.jorphan.gui.ObjectTableModel;
import org.apache.jorphan.gui.RateRenderer;
import org.apache.jorphan.gui.RendererUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.reflect.Functor;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;
import org.jmeterplugins.visualizers.gui.AbstractFilterableVisualizer;

/**
 * 
 * @author Félix Henry
 * @author Vincent Daburon
 */

/**
 * Synthesis Table-Based Reporting Visualizer for JMeter.
 * 
 */
public class SynthesisReport extends AbstractFilterableVisualizer implements
		Clearable, ActionListener {

	private static final long serialVersionUID = 240L;

	private static final Logger log = LoggingManager.getLoggerForClass();

	public static final String WIKIPAGE = "SynthesisReport";

	private static final String USE_GROUP_NAME = "useGroupName"; //$NON-NLS-1$

	private static final String SAVE_HEADERS = "saveHeaders"; //$NON-NLS-1$

	private static final String[] COLUMNS = { "sampler_label", //$NON-NLS-1$
			"aggregate_report_count", //$NON-NLS-1$ 
			"average", //$NON-NLS-1$
			"aggregate_report_min", //$NON-NLS-1$
			"aggregate_report_max", //$NON-NLS-1$
			"aggregate_report_90%_line", //$NON-NLS-1$
			"aggregate_report_stddev", //$NON-NLS-1$
			"aggregate_report_error%", //$NON-NLS-1$
			"aggregate_report_rate", //$NON-NLS-1$
			"aggregate_report_bandwidth", //$NON-NLS-1$
			"average_bytes" }; //$NON-NLS-1$

	private final String TOTAL_ROW_LABEL = JMeterUtils
			.getResString("aggregate_report_total_label"); //$NON-NLS-1$

	private JTable myJTable;

	private JScrollPane myScrollPane;

	private final JButton saveTable = new JButton(
			JMeterUtils.getResString("aggregate_graph_save_table")); //$NON-NLS-1$

	private final JCheckBox saveHeaders = // should header be saved with the
											// data?
	new JCheckBox(
			JMeterUtils.getResString("aggregate_graph_save_table_header"), true); //$NON-NLS-1$

	private final JCheckBox useGroupName = new JCheckBox(
			JMeterUtils.getResString("aggregate_graph_use_group_name")); //$NON-NLS-1$

	private transient ObjectTableModel model;

	/**
	 * Lock used to protect tableRows update + model update
	 */
	private final transient Object lock = new Object();

	private final Map<String, SamplingStatCalculator> tableRows = new ConcurrentHashMap<String, SamplingStatCalculator>();

	public SynthesisReport() {
		super();
		model = new ObjectTableModel(COLUMNS, SamplingStatCalculator.class,
				new Functor[] {
						new Functor("getLabel"), //$NON-NLS-1$
						new Functor("getCount"), //$NON-NLS-1$
						new Functor("getMeanAsNumber"), //$NON-NLS-1$
						new Functor("getMin"), //$NON-NLS-1$
						new Functor("getMax"), //$NON-NLS-1$
						new Functor("getPercentPoint", //$NON-NLS-1$
								new Object[] { new Float(.900) }),
						new Functor("getStandardDeviation"), //$NON-NLS-1$
						new Functor("getErrorPercentage"), //$NON-NLS-1$
						new Functor("getRate"), //$NON-NLS-1$
						new Functor("getKBPerSecond"), //$NON-NLS-1$
						new Functor("getAvgPageBytes"), //$NON-NLS-1$
				}, new Functor[] { null, null, null, null, null, null, null,
						null, null, null, null }, new Class[] { String.class,
						Long.class, Long.class, Long.class, Long.class,
						Long.class, String.class, String.class, String.class,
						String.class, String.class });
		clearData();
		init();
	}

	// Column renderers
	private static final TableCellRenderer[] RENDERERS = new TableCellRenderer[] {
			null, // Label
			null, // count
			null, // Mean
			null, // Min
			null, // Max
			null, // 90%
			new NumberRenderer("#0.00"), // Std Dev.
			new NumberRenderer("#0.00%"), // Error %age //$NON-NLS-1$
			new RateRenderer("#.0"), // Throughput //$NON-NLS-1$
			new NumberRenderer("#0.00"), // kB/sec
			new NumberRenderer("#.0"), // avg. pageSize
	};

	/** @deprecated - only for use in testing */
	@Deprecated
	public static boolean testFunctors() {
		SynthesisReport instance = new SynthesisReport();
		return instance.model.checkFunctors(null, instance.getClass());
	}

	@Override
	public String getLabelResource() {
		return this.getClass().getSimpleName(); //$NON-NLS-1$
	}

	@Override
	public String getStaticLabel() {
		return JMeterPluginsUtils.prefixLabel("Synthesis Report (filtered)");
	}

	@Override
	public void add(final SampleResult res) {
		JMeterUtils.runSafe(new Runnable() {
			@Override
			public void run() {
				if (isSampleIncluded(res)) {
					SamplingStatCalculator row = null;
					final String sampleLabel = res.getSampleLabel(useGroupName
							.isSelected());
					synchronized (lock) {
						row = tableRows.get(sampleLabel);
						if (row == null) {
							row = new SamplingStatCalculator(sampleLabel);
							tableRows.put(row.getLabel(), row);
							model.insertRow(row, model.getRowCount() - 1);
						}
					}
					/*
					 * Synch is needed because multiple threads can update the
					 * counts.
					 */
					synchronized (row) {
						row.addSample(res);
					}
					SamplingStatCalculator tot = tableRows.get(TOTAL_ROW_LABEL);
					synchronized (tot) {
						tot.addSample(res);
					}
					model.fireTableDataChanged();
				}
			}
		});
	}

	/**
	 * Clears this visualizer and its model, and forces a repaint of the table.
	 */
	@Override
	public void clearData() {
		synchronized (lock) {
			model.clearData();
			tableRows.clear();
			tableRows.put(TOTAL_ROW_LABEL, new SamplingStatCalculator(
					TOTAL_ROW_LABEL));
			model.addRow(tableRows.get(TOTAL_ROW_LABEL));
		}
	}

	/**
	 * Main visualizer setup.
	 */
	private void init() {
		this.setLayout(new BorderLayout());

		// MAIN PANEL
		JPanel mainPanel = new JPanel();
		Border margin = new EmptyBorder(10, 10, 5, 10);

		mainPanel.setBorder(margin);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(),
				WIKIPAGE));

		// SortFilterModel mySortedModel =
		// new SortFilterModel(myStatTableModel);
		myJTable = new JTable(model);
		myJTable.getTableHeader().setDefaultRenderer(
				new HeaderAsPropertyRenderer());
		myJTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		RendererUtils.applyRenderers(myJTable, RENDERERS);
		myScrollPane = new JScrollPane(myJTable);
		this.add(mainPanel, BorderLayout.NORTH);
		this.add(myScrollPane, BorderLayout.CENTER);
		saveTable.addActionListener(this);
		JPanel opts = new JPanel();
		opts.add(useGroupName, BorderLayout.WEST);
		opts.add(saveTable, BorderLayout.CENTER);
		opts.add(saveHeaders, BorderLayout.EAST);
		this.add(opts, BorderLayout.SOUTH);
	}

	@Override
	public void modifyTestElement(TestElement c) {
		super.modifyTestElement(c);
		c.setProperty(USE_GROUP_NAME, useGroupName.isSelected(), false);
		c.setProperty(SAVE_HEADERS, saveHeaders.isSelected(), true);
	}

	@Override
	public void configure(TestElement el) {
		super.configure(el);
		useGroupName
				.setSelected(el.getPropertyAsBoolean(USE_GROUP_NAME, false));
		saveHeaders.setSelected(el.getPropertyAsBoolean(SAVE_HEADERS, true));
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == saveTable) {
			JFileChooser chooser = FileDialoger
					.promptToSaveFile("synthesis.csv");//$NON-NLS-1$
			if (chooser == null) {
				return;
			}
			FileWriter writer = null;
			try {
				writer = new FileWriter(chooser.getSelectedFile()); // TODO
																	// Charset ?
				CSVSaveService.saveCSVStats(model, writer,
						saveHeaders.isSelected());
			} catch (FileNotFoundException e) {
				log.warn(e.getMessage());
			} catch (IOException e) {
				log.warn(e.getMessage());
			} finally {
				JOrphanUtils.closeQuietly(writer);
			}
		}
	}
}
