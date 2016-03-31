package kg.apc.jmeter.vizualizers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.*;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.jmxmon.JMXMonCollector;
import kg.apc.jmeter.jmxmon.JMXMonSampleResult;
import kg.apc.jmeter.graphs.AbstractOverTimeVisualizer;
import kg.apc.jmeter.gui.ButtonPanelAddCopyRemove;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class JMXMonGui
        extends AbstractOverTimeVisualizer {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private PowerTableModel tableModel;
    private JTable grid;
    private JTextArea errorTextArea;
    private JScrollPane errorPane;
    public static final String[] columnIdentifiers = new String[]{
        "Label", "URL", "Username", "Password", "Object Name", "Attribute", "Key", "Delta", "Retry"
    };
    public static final Class[] columnClasses = new Class[]{
        String.class, String.class, String.class, String.class, String.class, String.class, String.class, Boolean.class, Boolean.class
    };
    private static Object[] defaultValues = new Object[]{
        "", "", "", "", "", "", "", false, false
    };

    public JMXMonGui() {
        super();
        setGranulation(1000);
        graphPanel.getGraphObject().setYAxisLabel("Query results");
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
    public String getWikiPage() {
        return "JMXMon";
    }

    @Override
    public String getLabelResource() {
        return getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("JMXMon Samples Collector");
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

    private Component createSamplerPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("JMX Samplers"));
        panel.setPreferredSize(new Dimension(150, 150));

        JScrollPane scroll = new JScrollPane(createGrid());
        scroll.setPreferredSize(scroll.getMinimumSize());
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(new ButtonPanelAddCopyRemove(grid, tableModel, defaultValues), BorderLayout.SOUTH);

        grid.getTableHeader().setReorderingAllowed(false);

        return panel;
    }

    private JTable createGrid() {
        grid = new JTable();
        createTableModel();
        grid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        grid.setMinimumSize(new Dimension(200, 100));

        grid.getColumnModel().getColumn(0).setPreferredWidth(50);
        grid.getColumnModel().getColumn(1).setPreferredWidth(300);
        grid.getColumnModel().getColumn(2).setPreferredWidth(50);
        grid.getColumnModel().getColumn(3).setPreferredWidth(50);
        grid.getColumnModel().getColumn(4).setPreferredWidth(200);
        grid.getColumnModel().getColumn(5).setPreferredWidth(100);
        grid.getColumnModel().getColumn(6).setPreferredWidth(50);
        grid.getColumnModel().getColumn(7).setPreferredWidth(50);
        grid.getColumnModel().getColumn(7).setPreferredWidth(50);
        return grid;
    }

    private void createTableModel() {
        tableModel = new PowerTableModel(columnIdentifiers, columnClasses);
        grid.setModel(tableModel);
    }

    @Override
    public TestElement createTestElement() {
        TestElement te = new JMXMonCollector();
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

        if (te instanceof JMXMonCollector) {
            JMXMonCollector dmte = (JMXMonCollector) te;
            CollectionProperty rows = JMeterPluginsUtils.tableModelRowsToCollectionProperty(tableModel, JMXMonCollector.DATA_PROPERTY);
            dmte.setData(rows);
        }
        super.configureTestElement(te);
    }

    @Override
    public void configure(TestElement te) {
        super.configure(te);
        JMXMonCollector dmte = (JMXMonCollector) te;
        JMeterProperty jmxmonValues = dmte.getSamplerSettings();
        if (!(jmxmonValues instanceof NullProperty)) {
            JMeterPluginsUtils.collectionPropertyToTableModelRows((CollectionProperty) jmxmonValues, tableModel, columnClasses);
        } else {
            log.warn("Received null property instead of collection");
        }
    }

    @Override
    public void add(SampleResult res) {
        if (res.isSuccessful()) {
            if (isSampleIncluded(res)) {
                super.add(res);
                addJmxMonRecord(res.getSampleLabel(), normalizeTime(res.getStartTime()), JMXMonSampleResult.getValue(res));
                updateGui(null);
            }
        } else {
            addErrorMessage(res.getResponseMessage(), res.getStartTime());
        }
    }

    private void addJmxMonRecord(String rowName, long time, double value) {
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
