package kg.apc.jmeter.timers;

import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.DateTimeRenderer;
import kg.apc.charting.GraphPanelChart;
import kg.apc.charting.rows.GraphRowExactValues;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.gui.ButtonPanelAddCopyRemove;
import kg.apc.jmeter.gui.GuiBuilderHelper;
import kg.apc.jmeter.threads.UltimateThreadGroupGui;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.timers.gui.AbstractTimerGui;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see UltimateThreadGroupGui
 */
public class VariableThroughputTimerGui
        extends AbstractTimerGui
        implements TableModelListener,
        CellEditorListener, ChangeListener {

    public static final String WIKIPAGE = "ThroughputShapingTimer";
    private static final Logger log = LoggingManager.getLoggerForClass();
    protected ConcurrentHashMap<String, AbstractGraphRow> model;
    protected GraphPanelChart chart;
    private static Integer[] defaultValues = new Integer[]{
            1, 1000, 60
    };
    protected PowerTableModel tableModel;
    protected JTable grid;
    protected ButtonPanelAddCopyRemove buttons;
    private JCheckBox override;
    private JTextField override_value;
    private VariableThroughputTimer testElement;
    private DocumentListener docListener;

    public VariableThroughputTimerGui() {
        super();
        final VariableThroughputTimerGui par = this;
        docListener = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                change();
            }

            private void change() {
                if (testElement != null) {
                    log.debug("Change " + testElement + " to " + override_value.getText());
                    //testElement.setOverrideValue(override_value.getText());
                } else {
                    log.debug("No test element");
                }
            }

            public void removeUpdate(DocumentEvent e) {
                change();
            }

            public void insertUpdate(DocumentEvent e) {
                change();
            }
        };
        init();
    }

    protected final void init() {
        setBorder(makeBorder());
        setLayout(new BorderLayout());
        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);

        JPanel schedulePanel = new VerticalPanel();
        schedulePanel.add(createParamsPanel(), BorderLayout.NORTH);
        schedulePanel.add(GuiBuilderHelper.getComponentWithMargin(createChart(), 2, 2, 0, 2), BorderLayout.CENTER);

        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("RPS Schedule", schedulePanel);
        tabbedPane.add("Real-Time RPS Control", createRTControlPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private Component createRTControlPanel() {
        JPanel panel = new VerticalPanel();

        HorizontalPanel hpanel = new HorizontalPanel();

        override = new JCheckBox("override RPS on-the-fly to ");
        hpanel.add(override);

        override_value = new JTextField();
        hpanel.add(override_value);
        panel.add(hpanel);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 99, 1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(this);
        slider.firePropertyChange(null, 0, 0);
        // panel.add(slider);

        return panel;
    }

    private JPanel createParamsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Requests Per Second (RPS) Schedule"));
        panel.setPreferredSize(new Dimension(200, 200));

        JScrollPane scroll = new JScrollPane(createGrid());
        scroll.setPreferredSize(scroll.getMinimumSize());
        panel.add(scroll, BorderLayout.CENTER);
        buttons = new ButtonPanelAddCopyRemove(grid, tableModel, defaultValues);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private JTable createGrid() {
        grid = new JTable();
        grid.getDefaultEditor(String.class).addCellEditorListener(this);
        createTableModel();
        grid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        grid.setMinimumSize(new Dimension(200, 100));
        return grid;
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Throughput Shaping Timer");
    }

    @Override
    public TestElement createTestElement() {
        log.debug("Create test element");
        VariableThroughputTimer tg = new VariableThroughputTimer();
        modifyTestElement(tg);
        tg.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return tg;
    }

    @Override
    public void modifyTestElement(TestElement tg) {
        log.debug("Modify test element");
        configureTestElement(tg);

        if (grid.isEditing()) {
            grid.getCellEditor().stopCellEditing();
        }

        if (tg instanceof VariableThroughputTimer) {
            VariableThroughputTimer utg = (VariableThroughputTimer) tg;
            CollectionProperty rows = JMeterPluginsUtils.tableModelRowsToCollectionProperty(tableModel, VariableThroughputTimer.DATA_PROPERTY);
            utg.setData(rows);
            utg.setOverrideRPS(override.isSelected());
            utg.setOverrideValue(override_value.getText());
        }
    }

    @Override
    public void configure(TestElement tg) {
        log.debug("Configure");
        super.configure(tg);
        VariableThroughputTimer utg = (VariableThroughputTimer) tg;
        JMeterProperty threadValues = utg.getData();
        if (threadValues instanceof NullProperty) {
            log.warn("Received null property instead of collection");
            return;
        }

        CollectionProperty columns = (CollectionProperty) threadValues;

        tableModel.removeTableModelListener(this);
        JMeterPluginsUtils.collectionPropertyToTableModelRows(columns, tableModel);
        tableModel.addTableModelListener(this);
        buttons.checkDeleteButtonStatus();
        override.setSelected(utg.isOverrideRPS());
        override_value.getDocument().addDocumentListener(docListener);
        override_value.setText(utg.getOverrideValue());
        updateUI();
    }

    @Override
    public void updateUI() {
        super.updateUI();

        if (tableModel != null) {
            VariableThroughputTimer utgForPreview = new VariableThroughputTimer();
            utgForPreview.setData(JMeterPluginsUtils.tableModelRowsToCollectionPropertyEval(tableModel, VariableThroughputTimer.DATA_PROPERTY));
            updateChart(utgForPreview);
        }
    }

    private int getIntFromRow(int row, int col) {
        int ret;
        try {
            ret = Integer.valueOf(new CompoundVariable(tableModel.getValueAt(row, col).toString()).execute());
        } catch (NumberFormatException ex) {
            ret = -1;
        }
        return ret;
    }

    private void updateChart(VariableThroughputTimer tg) {
        model.clear();
        chart.clearErrorMessage();
        AbstractGraphRow row = new GraphRowExactValues();
        row.setColor(Color.BLUE);
        row.setDrawLine(true);
        row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_NONE);
        row.setDrawThickLines(true);

        long now = System.currentTimeMillis();

        int rowsCount = tableModel.getRowCount();
        row.add(now, 0);
        row.add(now, tg.getRPSForSecond(0));

        int duration = 0;
        for (int i = 0; i < rowsCount; i++) {
            row.add(now + (duration + 1) * 1000, tg.getRPSForSecond(duration + 1));
            int rowVal = getIntFromRow(i, 2);
            if (rowVal < 0) {
                chart.setErrorMessage("The values entered cannot be rendered in preview...");
                break;
            }
            duration = duration + rowVal;
            row.add(now + duration * 1000, tg.getRPSForSecond(duration));
        }

        chart.setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS, now - 1)); //-1 because row.add(thread.getStartTime() - 1, 0)
        chart.setForcedMinX(now);

        model.put("Expected RPS", row);
        chart.invalidateCache();
        chart.repaint();
    }

    private Component createChart() {
        chart = new GraphPanelChart(false, true);
        model = new ConcurrentHashMap<String, AbstractGraphRow>();
        chart.setRows(model);
        chart.getChartSettings().setDrawFinalZeroingLines(true);
        chart.setxAxisLabel("Elapsed Time");
        chart.setYAxisLabel("Number of requests /sec");
        chart.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        return chart;
    }

    private void createTableModel() {
        tableModel = new PowerTableModel(VariableThroughputTimer.columnIdentifiers, VariableThroughputTimer.columnClasses);
        tableModel.addTableModelListener(this);
        grid.setModel(tableModel);
    }

    @Override
    public void editingStopped(ChangeEvent e) {
        updateUI();
    }

    @Override
    public void editingCanceled(ChangeEvent e) {
        // no action needed
    }

    @Override
    public void clearGui() {
        super.clearGui();
        override_value.getDocument().removeDocumentListener(docListener);
        tableModel.clearData();
        tableModel.fireTableDataChanged();
        override.setSelected(false);
        override_value.setText("");
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        updateUI();
    }

    /**
     * Called when slider changes
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider slider = (JSlider) e.getSource();

        if (slider.getValueIsAdjusting()) {
            return;
        }

        if (override_value.getText().isEmpty() || StringUtils.isNumeric(override_value.getText())) {
            override_value.setText(String.valueOf(slider.getValue()));
        }

        int value = slider.getValue();
        log.debug("Current value: " + value);
        if (value >= slider.getMaximum() * 0.9 + 1) {
            if (slider.getMaximum() < 99999) {
                slider.setMaximum(slider.getMaximum() * 10);
            }
        } else if (value <= slider.getMaximum() * 0.09 - 1) {
            if (slider.getMaximum() > 99) {
                slider.setMaximum(slider.getMaximum() / 10);
            }
        }

        slider.setMajorTickSpacing((slider.getMaximum() + 1) / 10);
        slider.setMinorTickSpacing((slider.getMaximum() + 1) / 20);
        slider.setLabelTable(slider.createStandardLabels(slider.getMaximum() / 10));
    }

    public void setTestElement(VariableThroughputTimer testElement) {
        log.debug("Set test element " + testElement);
        this.testElement = testElement;
    }
}
