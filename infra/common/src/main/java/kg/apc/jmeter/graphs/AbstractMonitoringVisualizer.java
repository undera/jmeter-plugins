package kg.apc.jmeter.graphs;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.*;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.jmeter.gui.ButtonPanelAddCopyRemove;
import kg.apc.jmeter.graphs.AbstractOverTimeVisualizer;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.vizualizers.JSettingsPanel;
import kg.apc.jmeter.vizualizers.MonitoringResultsCollector;
import kg.apc.jmeter.vizualizers.MonitoringSampleResult;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Generic class for plotting monitoring results over time.
 * 
 */
public abstract class AbstractMonitoringVisualizer
        extends AbstractOverTimeVisualizer {

    private static final Logger log = LoggingManager.getLoggerForClass();
    protected PowerTableModel tableModel;
    protected JTable grid;
    protected JTextArea errorTextArea;
    protected JScrollPane errorPane;
	
    // The following column definitions are to be provided by sub-classes:
    abstract protected String[] getColumnIdentifiers();
    abstract protected Class[] getColumnClasses();
    abstract protected Object[] getDefaultValues();
    abstract protected int[] getColumnWidths();

    // Sub-classes to construct the actual collector:
	abstract protected MonitoringResultsCollector createMonitoringResultsCollector();
    
    @Override
    abstract public String getWikiPage();

    @Override
    abstract public String getStaticLabel();

    public AbstractMonitoringVisualizer() {
        super();
        setGranulation(1000);
        graphPanel.getGraphObject().setYAxisLabel("Monitoring results");
        graphPanel.getGraphObject().getChartSettings().setExpendRows(true);
    }

    @Override
    protected JSettingsPanel createSettingsPanel() {
        return new JSettingsPanel(this,
                JSettingsPanel.GRADIENT_OPTION
                | JSettingsPanel.LIMIT_POINT_OPTION
                | JSettingsPanel.MAXY_OPTION
                | JSettingsPanel.RELATIVE_TIME_OPTION
                | JSettingsPanel.AUTO_EXPAND_OPTION
                | JSettingsPanel.MARKERS_OPTION_DISABLED);
    }
    
    @Override
    public String getLabelResource() {
        return getClass().getSimpleName();
    }

    @Override
    protected JPanel getGraphPanelContainer() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel innerTopPanel = new JPanel(new BorderLayout());

        errorPane = new JScrollPane();
        errorPane.setMinimumSize(new Dimension(100, 50));
        errorPane.setPreferredSize(new Dimension(100, 50));

        errorTextArea = new JTextArea();
        errorTextArea.setForeground(Color.red);
        errorTextArea.setBackground(new Color(255, 255, 153));
        errorTextArea.setEditable(false);
        errorPane.setViewportView(errorTextArea);

        registerPopup();

        innerTopPanel.add(createSamplerPanel(), BorderLayout.NORTH);
        innerTopPanel.add(errorPane, BorderLayout.SOUTH);
        innerTopPanel.add(getFilePanel(), BorderLayout.CENTER);

        panel.add(innerTopPanel, BorderLayout.NORTH);

        errorPane.setVisible(false);

        return panel;
    }

    protected void addErrorMessage(String msg, long time) {
        errorPane.setVisible(true);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String newLine = "";
        if (errorTextArea.getText().length() != 0) {
            newLine = "\n";
        }
        errorTextArea.setText(errorTextArea.getText() + newLine + formatter.format(time) + " - ERROR: " + msg);
        errorTextArea.setCaretPosition(errorTextArea.getDocument().getLength());
        updateGui();
    }

    public void clearErrorMessage() {
        errorTextArea.setText("");
        errorPane.setVisible(false);
    }

    protected void registerPopup() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem hideMessagesMenu = new JMenuItem("Hide Error Panel");
        hideMessagesMenu.addActionListener(new HideAction());
        popup.add(hideMessagesMenu);
        errorTextArea.setComponentPopupMenu(popup);
    }

    @Override
    public void clearData() {
        clearErrorMessage();
        super.clearData();
    }

    protected Component createSamplerPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Monitoring Samplers"));
        panel.setPreferredSize(new Dimension(150, 150));

        JScrollPane scroll = new JScrollPane(createGrid());
        scroll.setPreferredSize(scroll.getMinimumSize());
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(new ButtonPanelAddCopyRemove(grid, tableModel, getDefaultValues()), BorderLayout.SOUTH);

        grid.getTableHeader().setReorderingAllowed(false);

        return panel;
    }

    protected JTable createGrid() {
        grid = new JTable();
        createTableModel();
        grid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        grid.setMinimumSize(new Dimension(200, 100));

        for (int i=0; i < getColumnWidths().length; i++) {
            grid.getColumnModel().getColumn(i).setPreferredWidth(getColumnWidths()[i]);
        }

        return grid;
    }

    protected void createTableModel() {
        tableModel = new PowerTableModel(getColumnIdentifiers(), getColumnClasses());
        grid.setModel(tableModel);
    }
    
    @Override
    public TestElement createTestElement() {
        TestElement te = createMonitoringResultsCollector();
        modifyTestElement(te);
        te.setComment(JMeterPluginsUtils.getWikiLinkText(getWikiPage()));
        return te;
    }
    
    @Override
    public void modifyTestElement(TestElement te) {
        super.modifyTestElement(te);
        if (grid.isEditing()) {
            grid.getCellEditor().stopCellEditing();
        }

        if (te instanceof MonitoringResultsCollector) {
            MonitoringResultsCollector mrc = (MonitoringResultsCollector) te;
            CollectionProperty rows = JMeterPluginsUtils.tableModelRowsToCollectionProperty(tableModel, MonitoringResultsCollector.DATA_PROPERTY);
            mrc.setData(rows);
        }
        super.configureTestElement(te);
    }
    
    @Override
    public void configure(TestElement te) {
        super.configure(te);
        MonitoringResultsCollector mrc = (MonitoringResultsCollector) te;
        JMeterProperty samplerValues = mrc.getSamplerSettings();
        if (!(samplerValues instanceof NullProperty)) {
            JMeterPluginsUtils.collectionPropertyToTableModelRows((CollectionProperty) samplerValues, tableModel, getColumnClasses());
        } else {
            log.warn("Received null property instead of collection");
        }
    }
    
    @Override
    public void add(SampleResult res) {
        if (res.isSuccessful()) {
            if(isSampleIncluded(res)) {
                super.add(res);
                addMonitoringRecord(res.getSampleLabel(), normalizeTime(res.getStartTime()), MonitoringSampleResult.getValue(res));
                updateGui(null);
            }
        } else {
            addErrorMessage(res.getResponseMessage(), res.getStartTime());
        }
    }
    
    protected void addMonitoringRecord(String rowName, long time, double value) {
        AbstractGraphRow row = model.get(rowName);
        if (row == null) {
            row = getNewRow(model, AbstractGraphRow.ROW_AVERAGES, rowName,
                    AbstractGraphRow.MARKER_SIZE_NONE, false, false, false, true, true);
        }
        row.add(time, value);
    }

    class HideAction
            implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            errorPane.setVisible(false);
            updateGui();
        }
    }
}