package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.graphs.AbstractGraphPanelVisualizer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import kg.apc.charting.GraphPanelChart;
import org.apache.jmeter.gui.util.HeaderAsPropertyRenderer;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.save.CSVSaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.SamplingStatCalculator;
import org.apache.jmeter.visualizers.StatVisualizer;
import org.apache.jorphan.gui.NumberRenderer;
import org.apache.jorphan.gui.ObjectTableModel;
import org.apache.jorphan.gui.RateRenderer;
import org.apache.jorphan.gui.RendererUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.reflect.Functor;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

/**
 * OMG, I had to copy full contents of StatVisualizer
 * just to make its data visible to JMeterPluginsCMD
 * This class is just invisible in menu;
 *
 * @see StatVisualizer
 * @author undera
 */
public class AggregateReportGui extends AbstractGraphPanelVisualizer {

    private Collection<String> emptyCollection = new ArrayList<String>();
    private static final long serialVersionUID = 240L;
    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final String USE_GROUP_NAME = "useGroupName";
    private static final String SAVE_HEADERS = "saveHeaders";
    private static final String[] COLUMNS = {
        "sampler_label",
        "aggregate_report_count",
        "average",
        "aggregate_report_median",
        "aggregate_report_90%_line",
        "aggregate_report_min",
        "aggregate_report_max",
        "aggregate_report_error%",
        "aggregate_report_rate",
        "aggregate_report_bandwidth"};
    private final String TOTAL_ROW_LABEL = JMeterUtils.getResString("aggregate_report_total_label");
    private JTable myJTable;
    private JScrollPane myScrollPane;
    private final JButton saveTable =
            new JButton(JMeterUtils.getResString("aggregate_graph_save_table"));
    private final JCheckBox saveHeaders = // should header be saved with the data?
            new JCheckBox(JMeterUtils.getResString("aggregate_graph_save_table_header"), true);
    private final JCheckBox useGroupName =
            new JCheckBox(JMeterUtils.getResString("aggregate_graph_use_group_name"));
    private transient ObjectTableModel statModel;
    private final Map<String, SamplingStatCalculator> tableRows =
            new ConcurrentHashMap<String, SamplingStatCalculator>();

    public AggregateReportGui() {
        super();
        statModel = new ObjectTableModel(COLUMNS,
                SamplingStatCalculator.class,
                new Functor[]{
                    new Functor("getLabel"),
                    new Functor("getCount"),
                    new Functor("getMeanAsNumber"),
                    new Functor("getMedian"),
                    new Functor("getPercentPoint",
                    new Object[]{new Float(.900)}),
                    new Functor("getMin"),
                    new Functor("getMax"),
                    new Functor("getErrorPercentage"),
                    new Functor("getRate"),
                    new Functor("getKBPerSecond")
                },
                new Functor[]{null, null, null, null, null, null, null, null, null, null},
                new Class[]{String.class, Long.class, Long.class, Long.class, Long.class,
                    Long.class, Long.class, String.class, String.class, String.class});
        clearData();
        init();
    }

    //do not insert this vizualiser in any JMeter menu
    @Override
    public Collection<String> getMenuCategories() {
        return emptyCollection;
    }
    // Column renderers
    private static final TableCellRenderer[] RENDERERS =
            new TableCellRenderer[]{
        null, // Label
        null, // count
        null, // Mean
        null, // median
        null, // 90%
        null, // Min
        null, // Max
        new NumberRenderer("#0.00%"), // Error %age 
        new RateRenderer("#.0"), // Throughput 
        new NumberRenderer("#.0"), // pageSize   
    };

    @Override
    public String getLabelResource() {
        return "aggregate_report";
    }

    @Override
    public void add(SampleResult res) {
        SamplingStatCalculator row = null;
        final String sampleLabel = res.getSampleLabel(useGroupName.isSelected());
        synchronized (tableRows) {
            row = tableRows.get(sampleLabel);
            if (row == null) {
                row = new SamplingStatCalculator(sampleLabel);
                tableRows.put(row.getLabel(), row);
                statModel.insertRow(row, statModel.getRowCount() - 1);
            }
        }
        /*
         * Synch is needed because multiple threads can update the counts.
         */
        synchronized (row) {
            row.addSample(res);
        }
        SamplingStatCalculator tot = tableRows.get(TOTAL_ROW_LABEL);
        synchronized (tot) {
            tot.addSample(res);
        }
        //statModel.fireTableDataChanged();
    }

    /**
     * Clears this visualizer and its model, and forces a repaint of the table.
     */
    @Override
    public final void clearData() {
        synchronized (tableRows) {
            statModel.clearData();
            tableRows.clear();
            tableRows.put(TOTAL_ROW_LABEL, new SamplingStatCalculator(TOTAL_ROW_LABEL));
            statModel.addRow(tableRows.get(TOTAL_ROW_LABEL));
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

        mainPanel.add(makeTitlePanel());

        // SortFilterModel mySortedModel =
        // new SortFilterModel(myStatTableModel);
        myJTable = new JTable(statModel);
        myJTable.getTableHeader().setDefaultRenderer(new HeaderAsPropertyRenderer());
        myJTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        RendererUtils.applyRenderers(myJTable, RENDERERS);
        myScrollPane = new JScrollPane(myJTable);
        this.add(mainPanel, BorderLayout.NORTH);
        this.add(myScrollPane, BorderLayout.CENTER);
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
        useGroupName.setSelected(el.getPropertyAsBoolean(USE_GROUP_NAME, false));
        saveHeaders.setSelected(el.getPropertyAsBoolean(SAVE_HEADERS, true));
    }

    @Override
    protected JSettingsPanel createSettingsPanel() {
        return new JSettingsPanel(this, 0);
    }

    @Override
    public String getWikiPage() {
        return "JMeterPluginsCMD";
    }

    @Override
    public GraphPanelChart getGraphPanelChart() {
        return new FakeGraphPanelChart();
    }

    @Override
    public String getStaticLabel() {
        return "Nobody never should not see this. No, no, no.";
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
                CSVSaveService.saveCSVStats(statModel, writer, saveHeaders.isSelected());
            } catch (FileNotFoundException e) {
                log.warn(e.getMessage());
            } catch (IOException e) {
                log.warn(e.getMessage());
            } finally {
                JOrphanUtils.closeQuietly(writer);
            }
        }

        @Override
        public void saveGraphToPNG(File file, int w, int h) throws IOException {
            throw new UnsupportedOperationException("This plugin type cannot be saved as image");
        }
    }
}
