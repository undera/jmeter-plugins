package kg.apc.jmeter.timers;

// TODO: make abstract classes for GUI or at least interfaces
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.DateTimeRenderer;
import kg.apc.jmeter.charting.GraphPanelChart;
import kg.apc.jmeter.charting.GraphRowExactValues;
import kg.apc.jmeter.gui.ButtonPanelAddCopyRemove;
import kg.apc.jmeter.threads.UltimateThreadGroupGui;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.timers.gui.AbstractTimerGui;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author apc
 * @see UltimateThreadGroupGui
 */
public class VariableThroughputTimerGui
        extends AbstractTimerGui
        implements TableModelListener,
        CellEditorListener {

    public static final String WIKIPAGE = "ThroughputShapingTimer";
    private static final Logger log = LoggingManager.getLoggerForClass();
    /**
     *
     */
    protected ConcurrentHashMap<String, AbstractGraphRow> model;
    protected GraphPanelChart chart;
    /**
     *
     */
    private static Integer[] defaultValues = new Integer[]{
        1, 1000, 60
    };
    protected PowerTableModel tableModel;
    protected JTable grid;

    /**
     *
     */
    public VariableThroughputTimerGui() {
        super();
        init();
    }

    /**
     *
     */
    protected final void init() {
        setBorder(makeBorder());
        setLayout(new BorderLayout());
        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);
        JPanel containerPanel = new VerticalPanel();

        containerPanel.add(createParamsPanel(), BorderLayout.NORTH);
        containerPanel.add(createChart(), BorderLayout.CENTER);
        add(containerPanel, BorderLayout.CENTER);
    }

    private JPanel createParamsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Requests Per Second (RPS) Schedule"));
        panel.setPreferredSize(new Dimension(200, 200));

        JScrollPane scroll = new JScrollPane(createGrid());
        scroll.setPreferredSize(scroll.getMinimumSize());
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(new ButtonPanelAddCopyRemove(grid, tableModel, defaultValues), BorderLayout.SOUTH);

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

    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Throughput Shaping Timer");
    }

    public TestElement createTestElement() {
        //log.info("Create test element");
        VariableThroughputTimer tg = new VariableThroughputTimer();
        modifyTestElement(tg);
        tg.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return tg;
    }

    public void modifyTestElement(TestElement tg) {
        //log.info("Modify test element");
        super.configureTestElement(tg);

        if (grid.isEditing()) {
            grid.getCellEditor().stopCellEditing();
        }

        if (tg instanceof VariableThroughputTimer) {
            VariableThroughputTimer utg = (VariableThroughputTimer) tg;
            CollectionProperty rows = JMeterPluginsUtils.tableModelRowsToCollectionProperty(tableModel, VariableThroughputTimer.DATA_PROPERTY);
            utg.setData(rows);
        }
    }

    @Override
    public void configure(TestElement tg) {
        //log.info("Configure");
        super.configure(tg);
        VariableThroughputTimer utg = (VariableThroughputTimer) tg;
        JMeterProperty threadValues = utg.getData();
        if (threadValues instanceof NullProperty) {
            log.warn("Received null property instead of collection");
            return;
        }

        CollectionProperty columns = (CollectionProperty) threadValues;

        tableModel.removeTableModelListener(this);
        try {
            JMeterPluginsUtils.collectionPropertyToTableModelRows(columns, tableModel);
        } catch (IllegalArgumentException ex) {
            log.error("Error loading data from property, will try to upgrade property", ex);
            JMeterPluginsUtils.collectionPropertyToTableModelCols(columns, tableModel);
        }
        tableModel.addTableModelListener(this);
        
        VariableThroughputTimer utgForPreview = new VariableThroughputTimer();
        utgForPreview.setData(JMeterPluginsUtils.tableModelRowsToCollectionPropertyEval(tableModel, VariableThroughputTimer.DATA_PROPERTY));
        updateChart(utgForPreview);
    }

    private void updateChart(VariableThroughputTimer tg) {
        model.clear();
        AbstractGraphRow row = new GraphRowExactValues();
        row.setColor(Color.RED);
        row.setDrawLine(true);
        row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_NONE);
        row.setDrawThickLines(true);

        long now = System.currentTimeMillis();

        chart.setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS, now - 1)); //-1 because row.add(thread.getStartTime() - 1, 0)
        chart.setForcedMinX(now);
        row.add(now, 0);

        int n = 0;
        int rps = 0;
        while ((rps = tg.getRPSForSecond(n)) >= 0) {
            row.add(now + n * 1000, rps);
            log.debug("Rps " + rps);
            n++;
        }

        row.setColor(Color.blue);
        model.put("Expected RPS", row);
        chart.repaint();
    }

    private Component createChart() {
        chart = new GraphPanelChart(false);
        model = new ConcurrentHashMap<String, AbstractGraphRow>();
        chart.setRows(model);
        chart.setDrawFinalZeroingLines(true);
        chart.setxAxisLabel("Elapsed Time");
        chart.setyAxisLabel("Number of requests /sec");
        return chart;
    }

    private void createTableModel() {
        tableModel = new PowerTableModel(VariableThroughputTimer.columnIdentifiers, VariableThroughputTimer.columnClasses);
        tableModel.addTableModelListener(this);
        grid.setModel(tableModel);
    }

    public void editingStopped(ChangeEvent e) {
        chart.repaint();
    }

    public void editingCanceled(ChangeEvent e) {
        chart.repaint();
    }

    @Override
    public void clearGui() {
        super.clearGui();
        tableModel.clearData();
        tableModel.fireTableDataChanged();
    }

    public void tableChanged(TableModelEvent e) {
        GuiPackage.getInstance().updateCurrentGui();
    }
}
