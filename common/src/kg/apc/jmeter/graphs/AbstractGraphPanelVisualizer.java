// fixme: perhaps we should replace model via setModel and use ONE model...
// TODO: https://groups.google.com/forum/#!topic/jmeter-plugins/qflK3oCjv4c
package kg.apc.jmeter.graphs;

import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.ChartSettings;
import kg.apc.charting.ColorsDispatcher;
import kg.apc.charting.GraphPanelChart;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.vizualizers.CompositeResultCollector;
import kg.apc.jmeter.vizualizers.CorrectedResultCollector;
import kg.apc.jmeter.vizualizers.JSettingsPanel;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.tree.JMeterTreeModel;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.LongProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.GraphListener;
import org.apache.jmeter.visualizers.ImageVisualizer;
import org.apache.jmeter.visualizers.Sample;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author apc
 */
public abstract class AbstractGraphPanelVisualizer
        extends AbstractVisualizer
        implements Clearable,
        GraphListener,
        ImageVisualizer,
        SettingsInterface {

    private static final Logger log = LoggingManager.getLoggerForClass();
    /**
     *
     */
    protected ConcurrentSkipListMap<String, AbstractGraphRow> model;
    protected ConcurrentSkipListMap<String, AbstractGraphRow> modelAggregate;
    /**
     *
     */
    protected long lastRepaint = 0;
    /**
     *
     */
    private int interval = 500;
    protected boolean isAggregate = false;
    /**
     *
     */
    protected GraphPanel graphPanel;
    /**
     *
     */
    protected ColorsDispatcher colors;
    private static final long REPAINT_INTERVAL = 500;
    public static final String INTERVAL_PROPERTY = "interval_grouping";
    public static final String GRAPH_AGGREGATED = "graph_aggregated";
    private JSettingsPanel settingsPanel = null;
    //
    private List<String> includes = new ArrayList<String>(0);
    private List<String> excludes = new ArrayList<String>(0);

    private JPanel container;
    private boolean filePanelVisible = true;
    private boolean maximized = false;
    private JButton maximizeButton;
    private boolean ignoreCurrentTestStartTime;

    /**
     *
     */
    public AbstractGraphPanelVisualizer() {
        super();
        model = new ConcurrentSkipListMap<String, AbstractGraphRow>();
        modelAggregate = new ConcurrentSkipListMap<String, AbstractGraphRow>();
        colors = new ColorsDispatcher();
        //RowsProviderResultCollector resCollector = new RowsProviderResultCollector();
        //setModel(resCollector);
        initGui();
    }

    protected abstract JSettingsPanel createSettingsPanel();

    @Override
    public abstract String getStaticLabel();

    private void initGui() {
        setBorder(makeBorder());
        setLayout(new BorderLayout());
        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), getWikiPage()), BorderLayout.NORTH);
        container = getGraphPanelContainer();
        container.add(createGraphPanel(), BorderLayout.CENTER);
        add(container, BorderLayout.CENTER);
    }


    @Override
    protected Component createTitleLabel() {
        JPanel pan = new JPanel(new GridBagLayout());
        JLabel titleLabel = new JLabel(getStaticLabel());
        Font curFont = titleLabel.getFont();
        titleLabel.setFont(curFont.deriveFont((float) curFont.getSize() + 4));

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.weightx = 1.0;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;

        pan.add(titleLabel, labelConstraints);

        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 1;
        buttonConstraints.gridy = 0;
        buttonConstraints.insets = new java.awt.Insets(0, 0, 0, 1);

        maximizeButton = new JButton(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/img/maximize.png")));
        maximizeButton.setFocusable(false);
        maximizeButton.setPreferredSize(new java.awt.Dimension(20, 20));
        maximizeButton.setToolTipText("Maximize Panel");

        maximizeButton.addActionListener(new MaximizeAction());

        pan.add(maximizeButton, buttonConstraints);
        return pan;
    }

    protected void enableMaximize(boolean enable) {
        maximizeButton.setVisible(enable);
    }

    protected void hideFilePanel() {
        filePanelVisible = false;
        getFilePanel().setVisible(false);
    }

    private void toogleMaximize() {
        maximized = !maximized;
        Component[] components = container.getComponents();
        for (Component cmp : components) {
            if (!(cmp instanceof GraphPanel)) {
                cmp.setVisible(!maximized);
            }
        }

        getFilePanel().setVisible(!maximized && filePanelVisible);
    }

    /**
     * Provide a JPanel with BorderLayout, holder of the GraphPanelChart, which
     * will be placed in the BorderLayout.CENTER. It can be overridden to create
     * custom Visualizer layout. Note the JMeter file panel can be retrieved
     * with getFilePanel() and moved in.
     *
     * @return a JPanel with a BorderLayout
     */
    protected JPanel getGraphPanelContainer() {
        return new JPanel(new BorderLayout());
    }

    protected GraphPanel createGraphPanel() {
        graphPanel = new GraphPanel();
        graphPanel.getGraphObject().setRows(model);
        graphPanel.getGraphObject().setPrecisionLabel(interval);
        setOptionsFromProperties(graphPanel.getGraphObject());
        setExtraChartSettings();
        // should be placed after creating graph panel
        settingsPanel = createSettingsPanel();
        graphPanel.getSettingsTab().add(getSettingsPanel(), BorderLayout.CENTER);
        //graphPanel.setSettingsTabPanel(settingsPanel);
        return graphPanel;
    }

    /*
     * Extra chart settings must be placed here as they must be called before createSettingsPanel()
     * Basically, all what is calling getChartSettings()
     */
    protected void setExtraChartSettings() {
    }

    @Override
    public void updateGui(Sample sample) {
        long time = System.currentTimeMillis();
        if ((time - lastRepaint) >= REPAINT_INTERVAL) {
            updateGui();
            repaint();
            lastRepaint = time;
        }
    }

    /**
     *
     */
    @Override
    public void updateGui() {
        graphPanel.updateGui();
    }

    @Override
    public void clearData() {
        clearRowsFromCompositeModels(getModel().getName());
        model.clear();
        modelAggregate.clear();
        colors.reset();
        graphPanel.clearRowsTab();
        updateGui();
        repaint();
    }

    @Override
    public Image getImage() {
        return graphPanel.getGraphImage();
    }

    @Override
    public int getGranulation() {
        return interval;
    }

    @Override
    public void setGranulation(int granulation) {
        if (granulation < 1) {
            throw new IllegalArgumentException("Interval cannot be less than 1");
        }
        interval = granulation;
        getSettingsPanel().setGranulationValue(granulation);
        graphPanel.getGraphObject().setPrecisionLabel(granulation);
    }

    @Override
    public TestElement createTestElement() {
        if (collector == null || !(collector instanceof CorrectedResultCollector)) {
            collector = new CorrectedResultCollector();
        }
        return super.createTestElement();
    }

    @Override
    public void modifyTestElement(TestElement c) {
        super.modifyTestElement(c);
        c.setProperty(new LongProperty(INTERVAL_PROPERTY, interval));
        c.setProperty(new BooleanProperty(GRAPH_AGGREGATED, isAggregate));
        c.setProperty(new StringProperty(CorrectedResultCollector.INCLUDE_SAMPLE_LABELS, graphPanel.getRowSelectorPanel().getIncludeSampleLabels()));
        c.setProperty(new StringProperty(CorrectedResultCollector.EXCLUDE_SAMPLE_LABELS, graphPanel.getRowSelectorPanel().getExcludeSampleLabels()));
    }

    @Override
    public void configure(TestElement el) {
        super.configure(el);
        int intervalProp = el.getPropertyAsInt(INTERVAL_PROPERTY);
        boolean aggregatedProp = el.getPropertyAsBoolean(GRAPH_AGGREGATED, false);
        if (intervalProp > 0) {
            setGranulation(intervalProp);
        }
        graphPanel.getRowSelectorPanel().setIncludeSampleLabels(el.getPropertyAsString(CorrectedResultCollector.INCLUDE_SAMPLE_LABELS));
        graphPanel.getRowSelectorPanel().setExcludeSampleLabels(el.getPropertyAsString(CorrectedResultCollector.EXCLUDE_SAMPLE_LABELS));

        if (el instanceof CorrectedResultCollector) {
            setUpFiltering((CorrectedResultCollector) el);
        }

        switchModel(aggregatedProp);
    }

    @Override
    public GraphPanelChart getGraphPanelChart() {
        return graphPanel.getGraphObject();
    }

    @Override
    public void switchModel(boolean aggregate) {
        ConcurrentSkipListMap<String, AbstractGraphRow> selectedModel;
        if (aggregate) {
            // issue 64: we must fail requests for aggregate in unsupported cases
            if (modelAggregate.isEmpty() && !model.isEmpty()) {
                throw new UnsupportedOperationException("Seems you've requested "
                        + "aggregate mode for graph that don't support it. We apologize...");
            }

            selectedModel = modelAggregate;
        } else {
            selectedModel = model;
        }

        graphPanel.getGraphObject().setRows(selectedModel);
        graphPanel.clearRowsTab();

        for (AbstractGraphRow abstractGraphRow : selectedModel.values()) {
            graphPanel.addRow(abstractGraphRow);
        }

        isAggregate = aggregate;
        getSettingsPanel().setAggregateMode(aggregate);
    }

    private void addRowToCompositeModels(String rowName, AbstractGraphRow row) {
        GuiPackage gui = GuiPackage.getInstance();
        if (gui == null) {
            log.debug("No GUI Package present, ignored adding to composite");
            return;
        }

        JMeterTreeModel testTree = gui.getTreeModel();

        for (JMeterTreeNode obj : testTree.getNodesOfType(CompositeResultCollector.class)) {
            CompositeResultCollector compositeResultCollector = (CompositeResultCollector) obj.getTestElement();
            compositeResultCollector.getCompositeModel().addRow(rowName, row);
        }
    }

    private void clearRowsFromCompositeModels(String vizualizerName) {
        GuiPackage gui = GuiPackage.getInstance();
        JMeterTreeModel testTree = gui.getTreeModel();

        for (JMeterTreeNode obj : testTree.getNodesOfType(CompositeResultCollector.class)) {
            //System.out.println("obj");
            CompositeResultCollector compositeResultCollector = (CompositeResultCollector) obj.getTestElement();
            compositeResultCollector.getCompositeModel().clearRows(vizualizerName);
        }
    }

    protected synchronized AbstractGraphRow getNewRow(
            ConcurrentSkipListMap<String, AbstractGraphRow> model,
            int rowType,
            String label,
            int markerSize,
            boolean isBarRow,
            boolean displayLabel,
            boolean thickLines,
            boolean showInLegend,
            Color color,
            boolean canCompose) {
        AbstractGraphRow row;
        if (!model.containsKey(label)) {
            row = AbstractGraphRow.instantiateNewRow(rowType);
            row.setLabel(label);
            row.setMarkerSize(markerSize);
            row.setDrawBar(isBarRow);
            row.setDrawLine(!isBarRow);
            row.setDrawValueLabel(displayLabel);
            row.setDrawThickLines(thickLines);
            row.setShowInLegend(showInLegend);
            if (color == null) {
                row.setColor(colors.getNextColor());
            } else {
                row.setColor(color);
            }
            model.put(label, row);
            graphPanel.addRow(row);
            if (canCompose) {
                addRowToCompositeModels(getModel().getName(), row);
            }
        } else {
            row = model.get(label);
        }

        return row;
    }

    protected synchronized AbstractGraphRow getNewRow(
            ConcurrentSkipListMap<String, AbstractGraphRow> model,
            int rowType,
            String label,
            int markerSize,
            boolean isBarRow,
            boolean displayLabel,
            boolean thickLines,
            boolean showInLegend,
            boolean canCompose) {
        return getNewRow(model, rowType, label, markerSize, isBarRow, displayLabel, thickLines, showInLegend, null, canCompose);
    }

    protected boolean isFromTransactionControler(SampleResult res) {
        return res.getResponseMessage() != null && res.getResponseMessage().startsWith("Number of samples in transaction");
    }

    /**
     * @return the settingsPanel
     */
    public JSettingsPanel getSettingsPanel() {
        return settingsPanel;
    }

    private void setOptionsFromProperties(GraphPanelChart graph) {
        // moved this into jmeter part, because charting package knows nothing on JMEters
        //properties from user.properties
        String cfgDrawGradient = JMeterUtils.getProperty("jmeterPlugin.drawGradient");
        if (cfgDrawGradient != null) {
            graph.getChartSettings().setDrawGradient("true".equalsIgnoreCase(cfgDrawGradient.trim()));
        }
        String cfgNeverDrawFinalZeroingLines = JMeterUtils.getProperty("jmeterPlugin.neverDrawFinalZeroingLines");
        if (cfgNeverDrawFinalZeroingLines != null) {
            graph.getChartSettings().setConfigNeverDrawFinalZeroingLines("true".equalsIgnoreCase(cfgNeverDrawFinalZeroingLines.trim()));
        }

        String cfgOptimizeYAxis = JMeterUtils.getProperty("jmeterPlugin.optimizeYAxis");
        if (cfgOptimizeYAxis != null) {
            graph.getChartSettings().setConfigOptimizeYAxis("true".equalsIgnoreCase(cfgOptimizeYAxis.trim()));
        }

        String cfgNeverDrawFinalCurrentX = JMeterUtils.getProperty("jmeterPlugin.neverDrawCurrentX");
        if (cfgNeverDrawFinalCurrentX != null) {
            graph.getChartSettings().setConfigNeverDrawCurrentX("true".equalsIgnoreCase(cfgNeverDrawFinalCurrentX.trim()));
        }
        String cfgCsvSeparator = JMeterUtils.getProperty("jmeterPlugin.csvSeparator");
        if (cfgCsvSeparator != null) {
            graph.getChartSettings().setConfigCsvSeparator(cfgCsvSeparator);
        }
        String cfgUseRelativeTime = JMeterUtils.getProperty("jmeterPlugin.useRelativeTime");
        if (cfgUseRelativeTime != null) {
            graph.getChartSettings().setUseRelativeTime("true".equalsIgnoreCase(cfgUseRelativeTime.trim()));
        }
        String cfgGraphLineWitdh = JMeterUtils.getProperty("jmeterPlugin.graphLineWidth");
        if (cfgGraphLineWitdh != null) {
            graph.getChartSettings().setLineWidth(JMeterPluginsUtils.getFloatFromString(cfgGraphLineWitdh, 1.0f));
        }
        String cfgGraphLineMarker = JMeterUtils.getProperty("jmeterPlugin.drawLineMarker");
        if (cfgGraphLineMarker != null) {
            boolean removeMarkers = "false".equalsIgnoreCase(cfgGraphLineMarker.trim());
            if (removeMarkers) {
                graph.getChartSettings().setChartMarkers(ChartSettings.CHART_MARKERS_NO);
            }
        }
    }

    protected boolean isSampleIncluded(SampleResult res) {
        if (!includes.isEmpty() && !includes.contains(res.getSampleLabel())) {
            return false;
        }

        if (!excludes.isEmpty() && excludes.contains(res.getSampleLabel())) {
            return false;
        }
        return true;
    }

    protected boolean isSampleIncluded(String sampleLabel) {
        if (!includes.isEmpty() && !includes.contains(sampleLabel)) {
            return false;
        }

        if (!excludes.isEmpty() && excludes.contains(sampleLabel)) {
            return false;
        }
        return true;
    }

    public void setUpFiltering(CorrectedResultCollector rc) {
        includes = rc.getList(CorrectedResultCollector.INCLUDE_SAMPLE_LABELS);
        excludes = rc.getList(CorrectedResultCollector.EXCLUDE_SAMPLE_LABELS);
    }

    private class MaximizeAction
            implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            toogleMaximize();
            if (!maximized) {
                maximizeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/img/maximize.png")));
                maximizeButton.setToolTipText("Maximize Panel");
            } else {
                maximizeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/img/restore.png")));
                maximizeButton.setToolTipText("Restore Panel");
            }
        }

    }

    /**
     * Ignore current test start time(ie take it from reload test data)
     * this is needed for visualizer that don't use current tests data
     * but reload from file 
     */
    public void setIgnoreCurrentTestStartTime() {
        this.ignoreCurrentTestStartTime = true;
    }

    /**
     * Ignore current test start time(ie take it from reload test data)
     * this is needed for visualizer that don't use current tests data
     * but reload from file 
     * @return the ignoreTestStartTime
     */
    public boolean isIgnoreCurrentTestStartTime() {
        return ignoreCurrentTestStartTime;
    }
}
