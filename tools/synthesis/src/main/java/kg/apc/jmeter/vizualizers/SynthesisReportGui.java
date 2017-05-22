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

import kg.apc.charting.GraphPanelChart;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.graphs.AbstractGraphPanelVisualizer;
import org.apache.jmeter.gui.util.FileDialoger;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.save.CSVSaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.StringProperty;
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
import org.jmeterplugins.visualizers.gui.FilterPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Synthesis Table-Based Reporting Visualizer for JMeter.
 */
public class SynthesisReportGui extends AbstractGraphPanelVisualizer implements
        Clearable, ActionListener {

    private static final long serialVersionUID = 240L;

    private static final String pct1Label = JMeterUtils.getPropDefault("aggregate_rpt_pct1", "90");

    private static final Float pct1Value = Float.parseFloat(pct1Label) / 100;

    protected FilterPanel jPanelFilter;

    private static final Logger log = LoggingManager.getLoggerForClass();

    public static final String WIKIPAGE = "SynthesisReport";

    private static final String USE_GROUP_NAME = "useGroupName";

    private static final String SAVE_HEADERS = "saveHeaders";

    private static final String[] COLUMNS_BEFORE_JM_2_13 = {"sampler_label",
            "aggregate_report_count",
            "average",
            "aggregate_report_min",
            "aggregate_report_max",
            "aggregate_report_90%_line",
            "aggregate_report_stddev",
            "aggregate_report_error%",
            "aggregate_report_rate",
            "aggregate_report_bandwidth",
            "average_bytes"};

    private static final String[] COLUMNS_AFTER_OR_EQUAL_JM_2_13 = {"sampler_label",
            "aggregate_report_count",
            "average",
            "aggregate_report_min",
            "aggregate_report_max",
            "aggregate_report_xx_pct1_line",
            "aggregate_report_stddev",
            "aggregate_report_error%",
            "aggregate_report_rate",
            "aggregate_report_bandwidth",
            "average_bytes"};

    private static boolean bOldVersion = false;

    static {
        // jmeterVer could be for example : 2.12 r1636949, 2.13 r1665067, 2.14 r18888888 or r1701891 (no version like <major>.<minor> for nightly builds)
        String jmeterVer = JMeterUtils.getJMeterVersion();

        // older than de 2.13 version like 2.12
        if ("2.13".compareTo(jmeterVer) > 0) {
            bOldVersion = true;
        }
        //System.out.println("jmeterVer = " + jmeterVer + ", bOldVersion = " + bOldVersion);
    }

    private static final String[] COLUMNS = bOldVersion ? COLUMNS_BEFORE_JM_2_13 : COLUMNS_AFTER_OR_EQUAL_JM_2_13;

    static final Object[][] COLUMNS_MSG_PARAMETERS = {null,
            null,
            null,
            null,
            null,
            new Object[]{pct1Label},
            null,
            null,
            null,
            null,
            null};

    private final String TOTAL_ROW_LABEL = JMeterUtils
            .getResString("aggregate_report_total_label");

    private final JButton saveTable = new JButton(
            JMeterUtils.getResString("aggregate_graph_save_table"));

    private final JCheckBox saveHeaders = // should header be saved with the
            // data?
            new JCheckBox(
                    JMeterUtils.getResString("aggregate_graph_save_table_header"), true);

    private final JCheckBox useGroupName = new JCheckBox(
            JMeterUtils.getResString("aggregate_graph_use_group_name"));

    private transient ObjectTableModel model;

    /**
     * Lock used to protect tableRows update + model update
     */
    private final transient Object lock = new Object();

    private final Map<String, SamplingStatCalculator> tableRows = new ConcurrentHashMap<>();

    public SynthesisReportGui() {
        super();
        model = createObjectTableModel();
        clearData();
        init();
    }

    /**
     * Creates that Table model
     *
     * @return ObjectTableModel
     */
    static ObjectTableModel createObjectTableModel() {
        return new ObjectTableModel(COLUMNS, SamplingStatCalculator.class,
                new Functor[]{
                        new Functor("getLabel"),
                        new Functor("getCount"),
                        new Functor("getMeanAsNumber"),
                        new Functor("getMin"),
                        new Functor("getMax"),
                        new Functor("getPercentPoint",
                                new Object[]{pct1Value}),
                        new Functor("getStandardDeviation"),
                        new Functor("getErrorPercentage"),
                        new Functor("getRate"),
                        new Functor("getKBPerSecond"),
                        new Functor("getAvgPageBytes"),
                }, new Functor[]{null, null, null, null, null, null, null,
                null, null, null, null}, new Class[]{String.class,
                Long.class, Long.class, Long.class, Long.class,
                Long.class, String.class, String.class, String.class,
                String.class, String.class});
    }

    // Column renderers
    private static final TableCellRenderer[] RENDERERS = new TableCellRenderer[]{
            null, // Label
            null, // count
            null, // Mean
            null, // Min
            null, // Max
            null, // 90%
            new NumberRenderer("#0.00"), // Std Dev. 
            new NumberRenderer("#0.00%"), // Error %age 
            new RateRenderer("#.0"), // Throughput 
            new NumberRenderer("#0.00"), // kB/sec 
            new NumberRenderer("#.0"), // avg. pageSize 
    };

    // Column formats
    static final Format[] FORMATS = new Format[]{
            null, // Label
            null, // count
            null, // Mean
            null, // Min
            null, // Max
            null, // 90%
            new DecimalFormat("#0.00"), // Std Dev. 
            new DecimalFormat("#0.00%"), // Error %age 
            new DecimalFormat("#.0"),      // Throughput 
            new DecimalFormat("#0.00"),  // kB/sec 
            new DecimalFormat("#.0"),    // avg. pageSize 
    };

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
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
                    SamplingStatCalculator row;
                    final String sampleLabel = res.getSampleLabel(useGroupName.isSelected());
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
        JTable myJTable = new JTable(model);
        myJTable.getTableHeader().setDefaultRenderer(new JMeterHeaderAsPropertyRenderer(COLUMNS_MSG_PARAMETERS));
        myJTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        RendererUtils.applyRenderers(myJTable, RENDERERS);
        JScrollPane myScrollPane = new JScrollPane(myJTable);
        this.add(mainPanel, BorderLayout.NORTH);
        this.add(myScrollPane, BorderLayout.CENTER);
        saveTable.addActionListener(this);
        JPanel opts = new JPanel();
        opts.add(useGroupName, BorderLayout.WEST);
        opts.add(saveTable, BorderLayout.CENTER);
        opts.add(saveHeaders, BorderLayout.EAST);
        this.add(opts, BorderLayout.SOUTH);
    }

    /**
     * Invoked when the target of the listener has changed its state. This
     * implementation assumes that the target is the FilePanel, and will update
     * the result collector for the new filename.
     *
     * @param e the event that has occurred
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        log.debug("getting new collector");
        collector = (CorrectedResultCollector) createTestElement();
        if (collector instanceof CorrectedResultCollector) {
            setUpFiltering((CorrectedResultCollector) collector);
        }
        collector.loadExistingFile();
    }

    @Override
    public void modifyTestElement(TestElement c) {
        super.modifyTestElement(c);
        c.setProperty(USE_GROUP_NAME, useGroupName.isSelected(), false);
        c.setProperty(SAVE_HEADERS, saveHeaders.isSelected(), true);
        c.setProperty(new StringProperty(
                CorrectedResultCollector.INCLUDE_SAMPLE_LABELS, jPanelFilter
                .getIncludeSampleLabels()));
        c.setProperty(new StringProperty(
                CorrectedResultCollector.EXCLUDE_SAMPLE_LABELS, jPanelFilter
                .getExcludeSampleLabels()));

        c.setProperty(new StringProperty(CorrectedResultCollector.START_OFFSET,
                jPanelFilter.getStartOffset()));
        c.setProperty(new StringProperty(CorrectedResultCollector.END_OFFSET,
                jPanelFilter.getEndOffset()));

        c.setProperty(new BooleanProperty(
                CorrectedResultCollector.INCLUDE_REGEX_CHECKBOX_STATE,
                jPanelFilter.isSelectedRegExpInc()));
        c.setProperty(new BooleanProperty(
                CorrectedResultCollector.EXCLUDE_REGEX_CHECKBOX_STATE,
                jPanelFilter.isSelectedRegExpExc()));
    }

    @Override
    public void configure(TestElement el) {
        super.configure(el);
        useGroupName
                .setSelected(el.getPropertyAsBoolean(USE_GROUP_NAME, false));
        saveHeaders.setSelected(el.getPropertyAsBoolean(SAVE_HEADERS, true));

        jPanelFilter
                .setIncludeSampleLabels(el
                        .getPropertyAsString(CorrectedResultCollector.INCLUDE_SAMPLE_LABELS));
        jPanelFilter
                .setExcludeSampleLabels(el
                        .getPropertyAsString(CorrectedResultCollector.EXCLUDE_SAMPLE_LABELS));

        if (!CorrectedResultCollector.EMPTY_FIELD.equals(el
                .getPropertyAsString(CorrectedResultCollector.START_OFFSET))) {
            jPanelFilter.setStartOffset((el
                    .getPropertyAsLong(CorrectedResultCollector.START_OFFSET)));
        }
        if (!CorrectedResultCollector.EMPTY_FIELD.equals(el
                .getPropertyAsString(CorrectedResultCollector.END_OFFSET))) {
            jPanelFilter.setEndOffset((el
                    .getPropertyAsLong(CorrectedResultCollector.END_OFFSET)));
        }

        jPanelFilter
                .setSelectedRegExpInc(el
                        .getPropertyAsBoolean(CorrectedResultCollector.INCLUDE_REGEX_CHECKBOX_STATE));
        jPanelFilter
                .setSelectedRegExpExc(el
                        .getPropertyAsBoolean(CorrectedResultCollector.EXCLUDE_REGEX_CHECKBOX_STATE));

        if (el instanceof CorrectedResultCollector) {
            setUpFiltering((CorrectedResultCollector) el);
        }
    }

    @Override
    protected Container makeTitlePanel() {
        jPanelFilter = new FilterPanel();
        Container panel = super.makeTitlePanel();
        panel.add(jPanelFilter);
        return panel;
    }

    @Override
    public void clearGui() {
        super.clearGui();
        jPanelFilter.clearGui();
    }

    /**
     * We use this method to get the data, since we are using
     * ObjectTableModel, so the calling getDataVector doesn't
     * work as expected.
     *
     * @param model   {@link ObjectTableModel}
     * @param formats Array of {@link Format} array can contain null formatters in this case value is added as is
     * @return the data from the model
     */
    public static List<List<Object>> getAllTableData(ObjectTableModel model, Format[] formats) {
        List<List<Object>> data = new ArrayList<>();
        if (model.getRowCount() > 0) {
            for (int rw = 0; rw < model.getRowCount(); rw++) {
                int cols = model.getColumnCount();
                List<Object> column = new ArrayList<>();
                data.add(column);
                for (int idx = 0; idx < cols; idx++) {
                    Object val = model.getValueAt(rw, idx);
                    if (formats[idx] != null) {
                        column.add(formats[idx].format(val));
                    } else {
                        column.add(val);
                    }
                }
            }
        }
        return data;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == saveTable) {
            JFileChooser chooser = FileDialoger
                    .promptToSaveFile("synthesis.csv");
            if (chooser == null) {
                return;
            }
            FileWriter writer = null;
            try {
                writer = new FileWriter(chooser.getSelectedFile()); // TODO
                // Charset ?
                CSVSaveService.saveCSVStats(getAllDataAsTable(model, FORMATS, getLabels(COLUMNS)), writer, saveHeaders.isSelected());
            } catch (IOException e) {
                log.warn(e.getMessage());
            } finally {
                JOrphanUtils.closeQuietly(writer);
            }
        }
    }

    /**
     * Present data in javax.swing.table.DefaultTableModel form.
     *
     * @param model   {@link ObjectTableModel}
     * @param formats Array of {@link Format} array can contain null formatters in this case value is added as is
     * @param columns Columns headers
     * @return data in table form
     */
    public static DefaultTableModel getAllDataAsTable(ObjectTableModel model, Format[] formats, String[] columns) {
        final List<List<Object>> table = getAllTableData(model, formats);

        final DefaultTableModel tableModel = new DefaultTableModel();

        for (String header : columns) {
            tableModel.addColumn(header);
        }

        for (List<Object> row : table) {
            tableModel.addRow(new Vector(row));
        }

        return tableModel;
    }

    /**
     * @param keys I18N keys
     * @return labels
     */
    static String[] getLabels(String[] keys) {
        String[] labels = new String[keys.length];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = MessageFormat.format(JMeterUtils.getResString(keys[i]), COLUMNS_MSG_PARAMETERS[i]);
        }
        return labels;
    }

    @Override
    public String getWikiPage() {
        return WIKIPAGE;
    }

    @Override
    public GraphPanelChart getGraphPanelChart() {
        return new FakeGraphPanelChart();
    }

    @Override
    protected JSettingsPanel createSettingsPanel() {
        return new JSettingsPanel(this, 0);
    }

    private class FakeGraphPanelChart extends GraphPanelChart {

        public FakeGraphPanelChart() {
            super(false);
        }

        @Override
        public void saveGraphToCSV(File file) throws IOException {
            log.info("Saving CSV to " + file.getAbsolutePath());

            FileWriter writer = null;
            try {
                writer = new FileWriter(file);
                CSVSaveService.saveCSVStats(getAllDataAsTable(model, FORMATS, getLabels(COLUMNS)), writer, saveHeaders.isSelected());
            } catch (IOException e) {
                log.warn(e.getMessage());
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException ex) {
                    log.warn("There was problem closing file stream", ex);
                }
            }
        }

        @Override
        public void saveGraphToPNG(File file, int w, int h) throws IOException {
            throw new UnsupportedOperationException(
                    "This plugin type cannot be saved as image");
        }
    }

    /**
     * Renders items in a JTable by converting from resource names.
     */
    private class JMeterHeaderAsPropertyRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 240L;
        private Object[][] columnsMsgParameters;

        /**
         *
         */
        public JMeterHeaderAsPropertyRenderer() {
            this(null);
        }

        /**
         * @param columnsMsgParameters Optional parameters of i18n keys
         */
        public JMeterHeaderAsPropertyRenderer(Object[][] columnsMsgParameters) {
            super();
            this.columnsMsgParameters = columnsMsgParameters;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (table != null) {
                JTableHeader header = table.getTableHeader();
                if (header != null) {
                    setForeground(header.getForeground());
                    setBackground(header.getBackground());
                    setFont(header.getFont());
                }
                setText(getText(value, row, column));
                setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                setHorizontalAlignment(SwingConstants.CENTER);
            }
            return this;
        }

        /**
         * Get the text for the value as the translation of the resource name.
         *
         * @param value  value for which to get the translation
         * @param column index which column message parameters should be used
         * @param row    not used
         * @return the text
         */
        protected String getText(Object value, int row, int column) {
            if (value == null) {
                return "";
            }
            if (columnsMsgParameters != null && columnsMsgParameters[column] != null) {
                return MessageFormat.format(JMeterUtils.getResString(value.toString()), columnsMsgParameters[column]);
            } else {
                return JMeterUtils.getResString(value.toString());
            }
        }
    }
}
