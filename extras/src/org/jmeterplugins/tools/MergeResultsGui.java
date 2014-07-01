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

package org.jmeterplugins.tools;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.vizualizers.CorrectedResultCollector;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.SavePropertyDialog;
import org.apache.jmeter.gui.util.FilePanel;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.save.CSVSaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.ComponentUtil;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.jmeterplugins.save.MergeResultsService;
import org.jmeterplugins.visualizers.gui.AbstractLightVisualizer;

/**
 * 
 * @author Félix Henry
 * @author Vincent Daburon
 */
public class MergeResultsGui extends AbstractLightVisualizer implements
		TableModelListener, CellEditorListener, ChangeListener {

	private static final long serialVersionUID = 240L;

	private static final Logger log = LoggingManager.getLoggerForClass();

	public static final String WIKIPAGE = "MergeResults";

	public static final String[] columnIdentifiers = new String[] {
			"Input File Name", "Prefix label", "Offset start (sec)",
			"Offset end (sec)", "Include label", "RegExp Inc.",
			"Exclude label", "RegExp Exc." };

	public static final Class<?>[] columnClasses = { String.class,
			String.class, String.class, String.class, String.class,
			Boolean.class, String.class, Boolean.class };

	public static final Object[] defaultValues = { "", "", "", "", "", false,
			"", false };

	/** File Extensions */
	private static final String[] EXTS = { ".jtl", ".csv" }; // $NON-NLS-1$
																// $NON-NLS-2$

	public static final String DEFAULT_FILENAME = "";
	public static final String FILENAME = "MergeResultsControlGui.filename"; // $NON-NLS-1$
	public static final String DATA_PROPERTY = "MergeResultsControlGui.data"; // $NON-NLS-1$

	public static final int INPUT_FILE_NAME = 0;
	public static final int PREFIX_LABEL = 1;
	public static final int OFFSET_START = 2;
	public static final int OFFSET_END = 3;
	public static final int INCLUDE_LABEL = 4;
	public static final int REGEX_INCLUDE = 5;
	public static final int EXCLUDE_LABEL = 6;
	public static final int REGEX_EXCLUDE = 7;

	private static final int MAX_FILE_HANDLES = 4;
	private static final long REF_START_TIME = 946682000;

	protected PowerTableModel tableModel;
	protected JTable grid;
	protected JButton addRowButton;
	protected JButton copyRowButton;
	protected JButton deleteRowButton;
	protected JButton mergeButton;
	private FilePanel filePanel;

	private List<String> includes = new ArrayList<String>(0);
	private List<String> excludes = new ArrayList<String>(0);
	private String incRegex;
	private String excRegex;
	private String prefixLabel;
	private boolean includeRegexChkboxState;
	private boolean excludeRegexChkboxState;
	protected long startTimeRef = 0;
	protected long startTimeInf;
	protected long startTimeSup;
	private long startOffset;
	private long endOffset;

	private List<SampleResult> samples = new ArrayList<SampleResult>(0);

	public MergeResultsGui() {
		super();
		init();
	}

	@Override
	public String getLabelResource() {
		return this.getClass().getSimpleName(); //$NON-NLS-1$
	}

	@Override
	public String getStaticLabel() {
		return JMeterPluginsUtils.prefixLabel("Merge Results");
	}

	@Override
	public Collection<String> getMenuCategories() {
		return Arrays.asList(new String[] { MenuFactory.NON_TEST_ELEMENTS });
	}

	protected Component getFilePanel() {
		return filePanel;
	}

	public void setFile(String filename) {
		filePanel.setFilename(filename);
	}

	public String getFile() {
		return filePanel.getFilename();
	}

	public String getWikiPage() {
		return "MergeResults";
	}

	/**
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
	 */
	@Override
	public TestElement createTestElement() {
		if (collector == null
				|| !(collector instanceof CorrectedResultCollector)) {
			collector = new CorrectedResultCollector();
		}
		return super.createTestElement();
	}

	/**
	 * Modifies a given TestElement to mirror the data in the gui components.
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
	 */
	@Override
	public void modifyTestElement(TestElement c) {
		super.modifyTestElement(c);
		if (c instanceof ResultCollector) {
			ResultCollector rc = (ResultCollector) c;
			rc.setFilename(getFile());

			rc.setProperty(new StringProperty(
					CorrectedResultCollector.FILENAME, getFile()));
			CollectionProperty rows = JMeterPluginsUtils
					.tableModelRowsToCollectionProperty(tableModel,
							DATA_PROPERTY);
			rc.setProperty(rows);
			collector = rc;
		}
	}

	@Override
	public void configure(TestElement el) {
		super.configure(el);
		setFile(el.getPropertyAsString(ResultCollector.FILENAME));
		JMeterProperty fileValues = el.getProperty(DATA_PROPERTY);

		if (!(fileValues instanceof NullProperty)) {
			CollectionProperty columns = (CollectionProperty) fileValues;

			tableModel.removeTableModelListener(this);
			JMeterPluginsUtils.collectionPropertyToTableModelRows(columns,
					tableModel, columnClasses);
			tableModel.addTableModelListener(this);
			updateUI();
		} else {
			log.warn("Received null property instead of collection");
		}
		checkDeleteButtonStatus();
		checkMergeButtonStatus();

		setFile(el.getPropertyAsString(CorrectedResultCollector.FILENAME));
	}

	@Override
	public void updateUI() {
		super.updateUI();

		if (tableModel != null) {
			CorrectedResultCollector crc = new CorrectedResultCollector();
			crc.setProperty(JMeterPluginsUtils
					.tableModelRowsToCollectionPropertyEval(tableModel,
							DATA_PROPERTY));
		}
	}

	private void init() {
		this.setLayout(new BorderLayout());

		// MAIN PANEL
		JPanel mainPanel = new JPanel();
		Border margin = new EmptyBorder(10, 10, 5, 10);

		mainPanel.setBorder(margin);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(),
				WIKIPAGE));

		JPanel containerPanel = new VerticalPanel();

		JButton saveConfigButton = new JButton(
				JMeterUtils.getResString("config_save_settings")); // $NON-NLS-1$
		saveConfigButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SavePropertyDialog d = new SavePropertyDialog(GuiPackage
						.getInstance().getMainFrame(), JMeterUtils
						.getResString("sample_result_save_configuration"), // $NON-NLS-1$
						true, collector.getSaveConfig());
				d.pack();
				ComponentUtil.centerComponentInComponent(GuiPackage
						.getInstance().getMainFrame(), d);
				d.setVisible(true);
			}
		});

		filePanel = new FilePanel("Merge and write results to output file",
				EXTS); // $NON-NLS-1$
		filePanel.addChangeListener(this);
		filePanel.add(saveConfigButton);

		mergeButton = new JButton("Merge");
		mergeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (grid.isEditing()) {
					TableCellEditor cellEditor = grid.getCellEditor(
							grid.getEditingRow(), grid.getEditingColumn());
					cellEditor.stopCellEditing();
				}
				String filename = getFile();
				if (filename.isEmpty()) {
					GuiPackage
							.showErrorMessage(
									"Error merging results files - empty output filename",
									"Output file error");
					return;
				}
				boolean isSuccess = true;
				File output;
				for (int row = 0; row < tableModel.getRowCount(); row++) {
					startTimeRef = 0;
					collector.setFilename(String.valueOf(tableModel.getValueAt(
							row, INPUT_FILE_NAME)));
					output = new File(collector.getFilename());
					if (collector.getFilename().isEmpty() || !output.exists()) {
						GuiPackage.showErrorMessage(
								"Error merging results files - could not open file [row: "
										+ row + ";filename: "
										+ collector.getFilename() + "]",
								"Input file error");
						isSuccess = false;
						break;
					}
					CorrectedResultCollector crc = new CorrectedResultCollector();
					crc.setStartOffset(String.valueOf(tableModel.getValueAt(
							row, OFFSET_START)));
					crc.setEndOffset(String.valueOf(tableModel.getValueAt(row,
							OFFSET_END)));
					crc.setIncludeLabels(String.valueOf(tableModel.getValueAt(
							row, INCLUDE_LABEL)));
					crc.setExcludeLabels(String.valueOf(tableModel.getValueAt(
							row, EXCLUDE_LABEL)));
					crc.setEnabledIncludeRegex(Boolean.valueOf(String
							.valueOf((tableModel.getValueAt(row, REGEX_INCLUDE)))));
					crc.setEnabledExcludeRegex(Boolean.valueOf(String
							.valueOf((tableModel.getValueAt(row, REGEX_EXCLUDE)))));
					crc.setPrefixLabel(String.valueOf((tableModel.getValueAt(
							row, PREFIX_LABEL))));
					setUpFiltering(crc);
					collector.loadExistingFile();
				}
				collector.setFilename(filename);
				if (isSuccess) {
					MergeResultsService.mergeSamples(filename, CSVSaveService
							.printableFieldNamesToString(collector
									.getSaveConfig()), samples);
				}
				samples.clear();
			}
		});
		JPanel controlPanel = new JPanel();
		controlPanel.add(mergeButton);

		containerPanel.add(createParamsPanel());
		containerPanel.add(getFilePanel());
		containerPanel.add(controlPanel);

		add(mainPanel, BorderLayout.NORTH);
		add(containerPanel, BorderLayout.CENTER);
	}

	@Override
	public void add(SampleResult res) {
		if (!isSampleIncluded(res)) {
			return;
		}
		res.setSaveConfig(collector.getSaveConfig());
		res.setSampleLabel(prefixLabel + ":" + res.getSampleLabel());
		res.setTimeStamp(res.getTimeStamp() - startTimeRef + REF_START_TIME);
		samples.add(res);
	}

	protected boolean isSampleIncluded(SampleResult res) {
		if (null == res) {
			return true;
		}

		if (startTimeRef == 0) {
			startTimeRef = res.getStartTime();
			startTimeInf = startTimeRef - startTimeRef % 1000;
			startTimeSup = startTimeRef + (1000 - startTimeRef % 1000) % 1000;
		}

		if (includeRegexChkboxState && !incRegex.isEmpty()
				&& !res.getSampleLabel().matches(incRegex)) {
			return false;
		}

		if (excludeRegexChkboxState && !excRegex.isEmpty()
				&& res.getSampleLabel().matches(excRegex)) {
			return false;
		}

		if (!includeRegexChkboxState && !includes.isEmpty()
				&& !includes.contains(res.getSampleLabel())) {
			return false;
		}

		if (!excludeRegexChkboxState && !excludes.isEmpty()
				&& excludes.contains(res.getSampleLabel())) {
			return false;
		}

		if (startOffset > res.getStartTime() - startTimeInf) {
			return false;
		}

		if (endOffset < res.getStartTime() - startTimeSup) {
			return false;
		}
		return true;
	}

	public void setUpFiltering(CorrectedResultCollector rc) {
		prefixLabel = rc.getPrefixLabel(CorrectedResultCollector.PREFIX_LABEL);
		startOffset = rc.getTimeDelimiter(
				CorrectedResultCollector.START_OFFSET, Long.MIN_VALUE);
		endOffset = rc.getTimeDelimiter(CorrectedResultCollector.END_OFFSET,
				Long.MAX_VALUE);
		includeRegexChkboxState = rc
				.getRegexChkboxState(CorrectedResultCollector.INCLUDE_REGEX_CHECKBOX_STATE);
		excludeRegexChkboxState = rc
				.getRegexChkboxState(CorrectedResultCollector.EXCLUDE_REGEX_CHECKBOX_STATE);
		if (includeRegexChkboxState)
			incRegex = rc
					.getRegex(CorrectedResultCollector.INCLUDE_SAMPLE_LABELS);
		else
			includes = rc
					.getList(CorrectedResultCollector.INCLUDE_SAMPLE_LABELS);
		if (excludeRegexChkboxState)
			excRegex = rc
					.getRegex(CorrectedResultCollector.EXCLUDE_SAMPLE_LABELS);
		else
			excludes = rc
					.getList(CorrectedResultCollector.EXCLUDE_SAMPLE_LABELS);
	}

	protected JPanel createParamsPanel() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Merge Options"));
		panel.setPreferredSize(new Dimension(200, 200));

		JScrollPane scroll = new JScrollPane(createGrid());
		scroll.setPreferredSize(scroll.getMinimumSize());

		JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));
		addRowButton = new JButton("Add Row");
		copyRowButton = new JButton("Copy Row");
		deleteRowButton = new JButton("Delete Row");

		addRowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (grid.isEditing()) {
					TableCellEditor cellEditor = grid.getCellEditor(
							grid.getEditingRow(), grid.getEditingColumn());
					cellEditor.stopCellEditing();
				}

				tableModel.addRow(defaultValues);
				tableModel.fireTableDataChanged();

				if (tableModel.getRowCount() >= MAX_FILE_HANDLES) {
					addRowButton.setEnabled(false);
					copyRowButton.setEnabled(false);
				}
				// Enable DELETE and MERGE
				deleteRowButton.setEnabled(true);
				mergeButton.setEnabled(true);

				// Highlight (select) the appropriate row.
				int rowToSelect = tableModel.getRowCount() - 1;
				if (rowToSelect < grid.getRowCount()) {
					grid.setRowSelectionInterval(rowToSelect, rowToSelect);
				}
				updateUI();
			}
		});
		copyRowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (grid.isEditing()) {
					TableCellEditor cellEditor = grid.getCellEditor(
							grid.getEditingRow(), grid.getEditingColumn());
					cellEditor.stopCellEditing();
				}
				final int selectedRow = grid.getSelectedRow();

				if (tableModel.getRowCount() == 0 || selectedRow < 0) {
					return;
				}

				tableModel.addRow(tableModel.getRowData(selectedRow));
				tableModel.fireTableDataChanged();

				if (tableModel.getRowCount() >= MAX_FILE_HANDLES) {
					addRowButton.setEnabled(false);
					copyRowButton.setEnabled(false);
				}
				// Enable DELETE and MERGE
				deleteRowButton.setEnabled(true);
				mergeButton.setEnabled(true);

				// Highlight (select) the appropriate row.
				int rowToSelect = selectedRow + 1;
				grid.setRowSelectionInterval(rowToSelect, rowToSelect);
				updateUI();
			}
		});
		deleteRowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (grid.isEditing()) {
					TableCellEditor cellEditor = grid.getCellEditor(
							grid.getEditingRow(), grid.getEditingColumn());
					cellEditor.cancelCellEditing();
				}

				int rowSelected = grid.getSelectedRow();
				if (rowSelected >= 0) {
					tableModel.removeRow(rowSelected);
					tableModel.fireTableDataChanged();

					if (tableModel.getRowCount() < MAX_FILE_HANDLES) {
						addRowButton.setEnabled(true);
						copyRowButton.setEnabled(true);
					}
					// Disable DELETE and MERGE if there are no rows in the
					// table to
					// delete.
					if (tableModel.getRowCount() == 0) {
						deleteRowButton.setEnabled(false);
						mergeButton.setEnabled(false);
					} // Table still contains one or more rows, so highlight
						// (select)
						// the appropriate one.
					else {
						int rowToSelect = rowSelected;

						if (rowSelected >= tableModel.getRowCount()) {
							rowToSelect = rowSelected - 1;
						}

						grid.setRowSelectionInterval(rowToSelect, rowToSelect);
					}
					updateUI();
				}
			}
		});

		buttonsPanel.add(addRowButton);
		buttonsPanel.add(copyRowButton);
		buttonsPanel.add(deleteRowButton);

		panel.add(scroll, BorderLayout.CENTER);
		panel.add(buttonsPanel, BorderLayout.SOUTH);

		return panel;
	}

	public void checkDeleteButtonStatus() {
		deleteRowButton.setEnabled(tableModel != null
				&& tableModel.getRowCount() > 0);
	}

	public void checkMergeButtonStatus() {
		mergeButton.setEnabled(tableModel != null
				&& tableModel.getRowCount() > 0);
	}

	private JTable createGrid() {
		grid = new JTable();
		grid.getDefaultEditor(String.class).addCellEditorListener(this);
		createTableModel();
		grid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		grid.setMinimumSize(new Dimension(200, 100));

		return grid;
	}

	private void createTableModel() {
		tableModel = new PowerTableModel(columnIdentifiers, columnClasses);
		tableModel.addTableModelListener(this);
		grid.setModel(tableModel);
	}

	public void tableChanged(TableModelEvent e) {
		// log.info("Model changed");
		updateUI();
	}

	public void editingStopped(ChangeEvent e) {
		// log.info("Editing stopped");
		updateUI();
	}

	public void editingCanceled(ChangeEvent e) {
		// no action needed
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		log.debug("getting new collector");
		collector = (ResultCollector) createTestElement();
	}

	@Override
	public Dimension getPreferredSize() {
		return getMinimumSize();
	}

	/**
	 * Implements JMeterGUIComponent.clearGui
	 */
	@Override
	public void clearGui() {
		super.clearGui();
		tableModel.clearData();
		filePanel.clearGui();
	}
}
