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

package kg.apc.jmeter.vizualizers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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

import kg.apc.charting.GraphPanelChart;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.graphs.AbstractGraphPanelVisualizer;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.SavePropertyDialog;
import org.apache.jmeter.gui.util.FilePanel;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleResult;
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

public class MergeResultsGui extends AbstractGraphPanelVisualizer implements
        TableModelListener, CellEditorListener, ChangeListener, ActionListener {

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
    private static final String[] EXTS = { ".jtl", ".csv" }; 
                                                             // $NON-NLS-2$

    public static final String DEFAULT_FILENAME = "";
    public static final String FILENAME = "MergeResultsControlGui.filename"; 
    public static final String DATA_PROPERTY = "MergeResultsControlGui.data"; 

    public static final int INPUT_FILE_NAME = 0;
    public static final int PREFIX_LABEL = 1;
    public static final int OFFSET_START = 2;
    public static final int OFFSET_END = 3;
    public static final int INCLUDE_LABEL = 4;
    public static final int REGEX_INCLUDE = 5;
    public static final int EXCLUDE_LABEL = 6;
    public static final int REGEX_EXCLUDE = 7;

    private static final int MAX_FILE_HANDLES = 4;
    private static final long REF_START_TIME = 946681200000L; // 2000-01-01 00:00:00.000

    private static final String ACTION_ADD = "add"; 
    private static final String ACTION_COPY = "copy"; 
    private static final String ACTION_DELETE = "delete"; 
    private static final String ACTION_MERGE = "merge"; 
    private static final String ACTION_SAVE_CONFIG = "save_config"; 

    protected PowerTableModel tableModel;
    protected JTable grid;
    protected JButton addRowButton;
    protected JButton copyRowButton;
    protected JButton deleteRowButton;
    protected JButton mergeButton;
    private FilePanel mergeAndWritePanel;

    private String prefixLabel = "";

    private List<SampleResult> samples = new ArrayList<>(0);
    private MergeResultsService mergeService;

    public MergeResultsGui() {
        super();
        init();
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName(); 
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Merge Results");
    }

    @Override
    public Collection<String> getMenuCategories() {
        return Arrays.asList(new String[] { MenuFactory.NON_TEST_ELEMENTS });
    }

    protected Component getMergeAndWritePanel() {
        return mergeAndWritePanel;
    }

    public void setFile(String filename) {
        mergeAndWritePanel.setFilename(filename);
    }

    public String getFile() {
        return mergeAndWritePanel.getFilename();
    }

    public String getWikiPage() {
        return "MergeResults";
    }

    /** Only for use in testing */
    public JTable getGrid() {
        return grid;
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
        setFile(el.getPropertyAsString(CorrectedResultCollector.FILENAME));
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

        startTimeRef = 0;
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
                JMeterUtils.getResString("config_save_settings")); 
        saveConfigButton.addActionListener(this);
        saveConfigButton.setActionCommand(ACTION_SAVE_CONFIG);

        mergeAndWritePanel = new FilePanel(
                "Merge and write results to output file", EXTS); 
        mergeAndWritePanel.addChangeListener(this);
        mergeAndWritePanel.add(saveConfigButton);

        mergeButton = new JButton("Merge");
        mergeButton.addActionListener(this);
        mergeButton.setActionCommand(ACTION_MERGE);

        JPanel controlPanel = new JPanel();
        controlPanel.add(mergeButton);

        containerPanel.add(createParamsPanel());
        containerPanel.add(getMergeAndWritePanel());
        containerPanel.add(controlPanel);

        add(mainPanel, BorderLayout.NORTH);
        add(containerPanel, BorderLayout.CENTER);

        hideFilePanel();
        enableMaximize(false);
    }

    private boolean loadFilesFromTable(CorrectedResultCollector crc) {
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            startTimeRef = 0;
            crc.setFilename(String.valueOf(tableModel.getValueAt(row,
                    INPUT_FILE_NAME)));
            if (crc.getFilename().isEmpty()) {
                GuiPackage.showErrorMessage(
                        "Error merging results files - could not open file [row: "
                                + row + ";filename: " + collector.getFilename()
                                + "]", "Input file error");
                return false;
            }
            crc.setStartOffset(String.valueOf(tableModel.getValueAt(row,
                    OFFSET_START)));
            crc.setEndOffset(String.valueOf(tableModel.getValueAt(row,
                    OFFSET_END)));
            crc.setIncludeLabels(String.valueOf(tableModel.getValueAt(row,
                    INCLUDE_LABEL)));
            crc.setExcludeLabels(String.valueOf(tableModel.getValueAt(row,
                    EXCLUDE_LABEL)));
            crc.setEnabledIncludeRegex(Boolean.valueOf(String
                    .valueOf((tableModel.getValueAt(row, REGEX_INCLUDE)))));
            crc.setEnabledExcludeRegex(Boolean.valueOf(String
                    .valueOf((tableModel.getValueAt(row, REGEX_EXCLUDE)))));
            crc.setPrefixLabel(String.valueOf((tableModel.getValueAt(row,
                    PREFIX_LABEL))));
            setUpFiltering(crc);
            crc.loadExistingFile();
        }
        return true;
    }

    @Override
    public void add(SampleResult res) {
        if (!isSampleIncluded(res)) {
            return;
        }

        res.setSaveConfig(collector.getSaveConfig());
        if (!prefixLabel.isEmpty()) {
            String resLabel = res.getSampleLabel();
            res.setSampleLabel(prefixLabel + resLabel);
        }
        res.setTimeStamp(res.getTimeStamp() - startTimeRef + REF_START_TIME);
        samples.add(res);
    }

    @Override
    public void setUpFiltering(CorrectedResultCollector rc) {
        super.setUpFiltering(rc);
        prefixLabel = rc.getPrefixLabel(CorrectedResultCollector.PREFIX_LABEL);
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

        addRowButton.addActionListener(this);
        addRowButton.setActionCommand(ACTION_ADD);
        copyRowButton.addActionListener(this);
        copyRowButton.setActionCommand(ACTION_COPY);
        deleteRowButton.addActionListener(this);
        deleteRowButton.setActionCommand(ACTION_DELETE);

        buttonsPanel.add(addRowButton);
        buttonsPanel.add(copyRowButton);
        buttonsPanel.add(deleteRowButton);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    public void actionPerformed(ActionEvent action) {
        String command = action.getActionCommand();

        if (grid.isEditing()) {
            TableCellEditor cellEditor = grid.getCellEditor(
                    grid.getEditingRow(), grid.getEditingColumn());
            cellEditor.stopCellEditing();
        }

        switch (command) {
            case ACTION_ADD: {
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

                break;
            }
            case ACTION_COPY: {
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

                break;
            }
            case ACTION_DELETE:
                final int rowSelected = grid.getSelectedRow();
                if (rowSelected >= 0) {
                    tableModel.removeRow(rowSelected);
                    tableModel.fireTableDataChanged();

                    if (tableModel.getRowCount() < MAX_FILE_HANDLES) {
                        addRowButton.setEnabled(true);
                        copyRowButton.setEnabled(true);
                    }
                    // Disable DELETE and MERGE if there are no rows in the
                    // table to delete.
                    if (tableModel.getRowCount() == 0) {
                        deleteRowButton.setEnabled(false);
                        mergeButton.setEnabled(false);
                    }
                    // Table still contains one or more rows, so highlight
                    // (select) the appropriate one.
                    else {
                        int rowToSelect = rowSelected;

                        if (rowSelected >= tableModel.getRowCount()) {
                            rowToSelect = rowSelected - 1;
                        }

                        grid.setRowSelectionInterval(rowToSelect, rowToSelect);
                    }
                    updateUI();
                }

                break;
            case ACTION_MERGE:
                String output = getFile();
                if (output.isEmpty()) {
                    GuiPackage.showErrorMessage(
                            "Error merging results files - empty output filename",
                            "Output file error");
                    return;
                }
                boolean isSuccess = loadFilesFromTable((CorrectedResultCollector) collector);

                if (isSuccess) {
                    mergeService = new MergeResultsService();
                    collector.setFilename(output);
                    mergeService.mergeSamples((CorrectedResultCollector) collector,
                            samples);
                }
                samples.clear();

                break;
            case ACTION_SAVE_CONFIG:
                SavePropertyDialog d = new SavePropertyDialog(GuiPackage
                        .getInstance().getMainFrame(),
                        JMeterUtils
                                .getResString("sample_result_save_configuration"),
                        true, collector.getSaveConfig());
                d.pack();
                ComponentUtil.centerComponentInComponent(GuiPackage.getInstance()
                        .getMainFrame(), d);
                d.setVisible(true);
                break;
        }
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
        mergeAndWritePanel.clearGui();
    }

    @Override
    protected JSettingsPanel createSettingsPanel() {
        return new JSettingsPanel(this, 0);
    }

    @Override
    public GraphPanelChart getGraphPanelChart() {
        return new FakeGraphPanelChart();
    }

    private class FakeGraphPanelChart extends GraphPanelChart {

        public FakeGraphPanelChart() {
            super(false);
        }

        @Override
        public void saveGraphToCSV(File file) throws IOException {
            log.info("Saving CSV to " + file.getAbsolutePath());
            mergeService = new MergeResultsService();
            collector.setFilename(file.getName());
            mergeService.mergeSamples((CorrectedResultCollector) collector,
                    samples);
        }

        @Override
        public void saveGraphToPNG(File file, int w, int h) throws IOException {
            throw new UnsupportedOperationException(
                    "This plugin type cannot be saved as image");
        }
    }
}
