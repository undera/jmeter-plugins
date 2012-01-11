package kg.apc.jmeter.vizualizers;
// TODO: rows in settings should have color markers for better experience
import java.util.List;
import kg.apc.jmeter.graphs.AbstractOverTimeVisualizer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.jmeter.gui.ButtonPanelAddCopyRemove;
import kg.apc.jmeter.gui.ComponentBorder;
import kg.apc.jmeter.gui.DialogFactory;
import kg.apc.jmeter.gui.GuiBuilderHelper;
import kg.apc.jmeter.perfmon.AgentConnector;
import kg.apc.jmeter.perfmon.PerfMonCollector;
import kg.apc.jmeter.perfmon.PerfMonSampleResult;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author APC
 */
public class PerfMonGui
        extends AbstractOverTimeVisualizer {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private PowerTableModel tableModel;
    private JTable grid;
    private JComboBox metricTypesBox;
    private JTextArea errorTextArea;
    private JScrollPane errorPane;
    public static final String[] columnIdentifiers = new String[]{
        "Host / IP", "Port", "Metric to collect", "Metric parameter (see help)"
    };
    public static final Class[] columnClasses = new Class[]{
        String.class, String.class, String.class, String.class
    };
    private static String[] defaultValues = new String[]{
        "localhost", "4444", "CPU", ""
    };

    public PerfMonGui() {
        super();
        setGranulation(1000);
        graphPanel.getGraphObject().setYAxisLabel("Performance Metrics");
        graphPanel.getGraphObject().getChartSettings().setExpendRows(true);
    }

    @Override
    protected JSettingsPanel createSettingsPanel() {
        return new JSettingsPanel(this,
                JSettingsPanel.GRADIENT_OPTION
                | JSettingsPanel.LIMIT_POINT_OPTION
                | JSettingsPanel.MAXY_OPTION
                | JSettingsPanel.RELATIVE_TIME_OPTION
                | JSettingsPanel.AUTO_EXPAND_OPTION);
    }

    @Override
    public String getWikiPage() {
        return "PerfMon";
    }

    @Override
    public String getLabelResource() {
        return getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("PerfMon Metrics Collector");
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

        innerTopPanel.add(createConnectionsPanel(), BorderLayout.NORTH);
        innerTopPanel.add(errorPane, BorderLayout.SOUTH);
        innerTopPanel.add(getFilePanel(), BorderLayout.CENTER);

        panel.add(innerTopPanel, BorderLayout.NORTH);

        errorPane.setVisible(false);

        return panel;
    }

    private void addErrorMessage(String msg, long time) {
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

    private void registerPopup() {
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

    private Component createConnectionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Servers to Monitor (ServerAgent must be started, see help)"));
        panel.setPreferredSize(new Dimension(150, 150));

        JScrollPane scroll = new JScrollPane(createGrid());
        scroll.setPreferredSize(scroll.getMinimumSize());
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(new ButtonPanelAddCopyRemove(grid, tableModel, defaultValues), BorderLayout.SOUTH);

        List<String> items = new LinkedList<String>(AgentConnector.metrics);
        // add metrics from new agent
        items.add("TCP");
        items.add("EXEC");
        items.add("TAIL");
        metricTypesBox = new JComboBox(items.toArray());
        grid.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(metricTypesBox));

        final JTextField wizEditor = new JTextField();
        wizEditor.setBorder(null);
        JButton wiz = new JButton("...");
        if(!GraphicsEnvironment.isHeadless()) {
            wiz.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    Frame parent = GuiPackage.getInstance().getMainFrame();
                    String type = grid.getValueAt(grid.getSelectedRow(),2).toString();

                    JPerfmonParamsPanel dlgContent = new JPerfmonParamsPanel(type, wizEditor);
                    dlgContent.setMinWidth(400);
                    JDialog dlg = DialogFactory.getJDialogInstance(parent, "PerfMon [" + type + "] Parameters Helper",
                            true, dlgContent, "/kg/apc/jmeter/vizualizers/wand.png");

                    DialogFactory.centerDialog(parent, dlg);

                    dlg.setVisible(true);
                }
            });
        }
        wiz.setMargin(new Insets(0, 6, 5, 6));
        GuiBuilderHelper.strechButtonToComponent(wizEditor, wiz);
        ComponentBorder bd = new ComponentBorder(wiz);

        bd.install(wizEditor);

        grid.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(wizEditor));
        grid.getTableHeader().setReorderingAllowed(false);

        return panel;
    }

    private JTable createGrid() {
        grid = new JTable();
        createTableModel();
        grid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        grid.setMinimumSize(new Dimension(200, 100));

        grid.getColumnModel().getColumn(0).setPreferredWidth(170);
        grid.getColumnModel().getColumn(1).setPreferredWidth(80);
        grid.getColumnModel().getColumn(2).setPreferredWidth(120);
        grid.getColumnModel().getColumn(3).setPreferredWidth(500);

        return grid;
    }

    private void createTableModel() {
        tableModel = new PowerTableModel(columnIdentifiers, columnClasses);
        grid.setModel(tableModel);
    }

    @Override
    public TestElement createTestElement() {
        TestElement te = new PerfMonCollector();
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

        if (te instanceof PerfMonCollector) {
            PerfMonCollector pmte = (PerfMonCollector) te;
            CollectionProperty rows = JMeterPluginsUtils.tableModelRowsToCollectionProperty(tableModel, PerfMonCollector.DATA_PROPERTY);
            pmte.setData(rows);
        }
        super.configureTestElement(te);
    }

    @Override
    public void configure(TestElement te) {
        super.configure(te);
        PerfMonCollector pmte = (PerfMonCollector) te;
        JMeterProperty perfmonValues = pmte.getMetricSettings();
        if (!(perfmonValues instanceof NullProperty)) {
            JMeterPluginsUtils.collectionPropertyToTableModelRows((CollectionProperty) perfmonValues, tableModel);
        } else {
            log.warn("Received null property instead of collection");
        }
    }

    @Override
    public void add(SampleResult res) {
        if (res.isSuccessful()) {
            super.add(res);
            addPerfMonRecord(res.getSampleLabel(), normalizeTime(res.getStartTime()), PerfMonSampleResult.getValue(res));
            updateGui(null);
        } else {
            addErrorMessage(res.getResponseMessage(), res.getStartTime());
        }
    }

    private void addPerfMonRecord(String rowName, long time, double value) {
        AbstractGraphRow row = model.get(rowName);
        if (row == null) {
            row = getNewRow(model, AbstractGraphRow.ROW_AVERAGES, rowName,
                    AbstractGraphRow.MARKER_SIZE_NONE, false, false, false, true, true);
        }
        row.add(time, value);
    }

    private class HideAction
            implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            errorPane.setVisible(false);
            updateGui();
        }
    }
}
