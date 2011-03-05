package kg.apc.jmeter.perfmon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentSkipListMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import kg.apc.jmeter.charting.AbstractGraphRow;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.visualizers.gui.AbstractListenerGui;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.charting.GraphPanelChart;
import kg.apc.jmeter.charting.ColorsDispatcher;
import kg.apc.jmeter.charting.DateTimeRenderer;
import kg.apc.jmeter.vizualizers.GraphPanel;
import kg.apc.jmeter.vizualizers.JSettingsPanel;
import kg.apc.jmeter.vizualizers.SettingsInterface;
import org.apache.jmeter.gui.UnsharedComponent;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.visualizers.GraphListener;
import org.apache.jmeter.visualizers.Sample;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author Stephane Hoblingre
 */
public abstract class AbstractPerformanceMonitoringGui extends AbstractListenerGui implements Clearable, TableModelListener, CellEditorListener, GraphListener, UnsharedComponent, SettingsInterface
{
    private static final Logger log = LoggingManager.getLoggerForClass();
    protected GraphPanel graphPanel;
    protected ConcurrentSkipListMap<String, AbstractGraphRow> model;
    protected ColorsDispatcher colors;
    private PowerTableModel tableModel;
    JTable grid;
    JButton addRowButton;
    JButton deleteRowButton;
    private JTextArea errorTextArea;
    private JScrollPane scrollPan;
    private long relativeStartTime=0;
    public static final String[] columnIdentifiers = new String[]
    {
        "Host / IP", "Port"
    };
    /**
     *
     */
    public static final Class[] columnClasses = new Class[]
    {
        String.class, String.class
    };
    private static Object[] defaultValues = new Object[]
    {
        "localhost", "4444"
    };

    private ButtonGroup group = new ButtonGroup();

    private JRadioButton[] types = new JRadioButton[] {
        new JRadioButton("CPU"),
        new JRadioButton("Memory"),
        new JRadioButton("Swap"),
        new JRadioButton("Disks I/O"),
        new JRadioButton("Networks I/O")
    };

    public final static int PERFMON_CPU = 0;
    public final static int PERFMON_MEM = 1;
    public final static int PERFMON_SWAP = 2;
    public final static int PERFMON_DISKS_IO = 3;
    public final static int PERFMON_NETWORKS_IO = 4;

    protected int selectedPerfMonType = -1;

    private JSettingsPanel settingsPanel = null;

    public AbstractPerformanceMonitoringGui()
    {
        super();
        model = new ConcurrentSkipListMap<String, AbstractGraphRow>();
        colors = new ColorsDispatcher();
        initGui();
    }

    protected abstract JSettingsPanel getSettingsPanel();

    private void initGui()
    {
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BorderLayout());


        JPanel containerPanel = new VerticalPanel();

        containerPanel.add(makeTitlePanel(), BorderLayout.NORTH);
        containerPanel.add(createParamsPanel(), BorderLayout.CENTER);
        containerPanel.add(createMonitoringTypePanel(), BorderLayout.SOUTH);

       

        scrollPan = new JScrollPane();
        scrollPan.setMinimumSize(new Dimension(100, 50));
        scrollPan.setPreferredSize(new Dimension(100, 50));
  
        errorTextArea = new JTextArea();
        errorTextArea.setForeground(Color.red);
        errorTextArea.setBackground(new Color(255,255,153));
        errorTextArea.setEditable(false);
        //errorTextArea.setText("Error!!!\nError!!!\nError!!!\nError!!!\nError!!!\n");
        scrollPan.setViewportView(errorTextArea);

        topContainer.add(containerPanel, BorderLayout.CENTER);
        topContainer.add(scrollPan, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(topContainer, BorderLayout.NORTH);
        add(createGraphPanel(), BorderLayout.CENTER);

        scrollPan.setVisible(false);

        settingsPanel = getSettingsPanel();
        graphPanel.getSettingsTab().add(settingsPanel, BorderLayout.CENTER);
        registerPopup();
    }

    private void registerPopup()
   {
      JPopupMenu popup = new JPopupMenu();
      JMenuItem hideMessagesMenu = new JMenuItem("Hide Error Panel");
      hideMessagesMenu.addActionListener(new HideAction());
      popup.add(hideMessagesMenu);
      errorTextArea.setComponentPopupMenu(popup);
   }

    private JPanel createParamsPanel()
    {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Servers to monitor (ServerAgent must be started!)"));
        panel.setPreferredSize(new Dimension(150, 150));

        JScrollPane scroll = new JScrollPane(createGrid());
        scroll.setPreferredSize(scroll.getMinimumSize());
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(createButtons(), BorderLayout.SOUTH);

        return panel;
    }

    protected int getSelectedTypeIndex() {
        for (int i = 0; i < types.length; i++)
        {
            if(types[i].isSelected()) return i;
        }
        return -1;
    }

    private void setSelectedType(int type) {
        types[type].setSelected(true);
    }

    private JPanel createMonitoringTypePanel()
    {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Metrics to collect"));
        for (int i = 0; i < types.length; i++)
        {
            group.add(types[i]);
            panel.add(types[i]);
            
        }

        types[0].setSelected(true);

        return panel;
    }

    private JTable createGrid()
    {
        grid = new JTable();
        grid.getDefaultEditor(Integer.class).addCellEditorListener(this);
        createTableModel();
        // grid.setRowSelectionAllowed(true);
        // grid.setColumnSelectionAllowed(true);
        grid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // grid.setCellSelectionEnabled(true);
        //grid.setFillsViewportHeight(true);
        grid.setMinimumSize(new Dimension(200, 100));

        return grid;
    }

    private void createTableModel()
    {
        tableModel = new PowerTableModel(columnIdentifiers, columnClasses);
        tableModel.addTableModelListener(this);
        grid.setModel(tableModel);
    }

    private Component createButtons()
    {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));

        addRowButton = new JButton("Add One Server");
        deleteRowButton = new JButton("Remove Selected Server");

        buttonPanel.add(addRowButton);
        buttonPanel.add(deleteRowButton);

        addRowButton.addActionListener(new AddRowAction());
        deleteRowButton.addActionListener(new DeleteRowAction());

        return buttonPanel;
    }

    protected GraphPanel createGraphPanel()
    {
        graphPanel = new GraphPanel();
        graphPanel.getGraphObject().setRows(model);
        graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer("HH:mm:ss"));
        graphPanel.getGraphObject().setDisplayPrecision(true);
        graphPanel.getGraphObject().setPrecisionLabel(1000);
        return graphPanel;
    }

    @Override
    public String getLabelResource()
    {
        return "performanceMonitoring";
    }

    @Override
    public abstract String getStaticLabel();

    @Override
    public TestElement createTestElement()
    {
        PerformanceMonitoringTestElement pmte = new PerformanceMonitoringTestElement();
        modifyTestElement(pmte);
        pmte.setComment(JMeterPluginsUtils.getWikiLinkText(getWikiPage()));
        return pmte;
    }

    @Override
    public void modifyTestElement(TestElement te)
    {
        //log.info("Modify test element");
        if (grid.isEditing())
        {
            grid.getCellEditor().stopCellEditing();
        }

        if (te instanceof PerformanceMonitoringTestElement)
        {
            PerformanceMonitoringTestElement pmte = (PerformanceMonitoringTestElement) te;
            CollectionProperty rows = PerformanceMonitoringTestElement.tableModelToCollectionProperty(tableModel);
            pmte.setData(rows);
            pmte.setType(getSelectedTypeIndex());

        }
        super.configureTestElement(te);
    }

    @Override
    public void configure(TestElement te)
    {
        //log.info("Configure");
        super.configure(te);
        createTableModel();
        PerformanceMonitoringTestElement pmte = (PerformanceMonitoringTestElement) te;
        pmte.register(this);
        JMeterProperty perfmonValues = pmte.getData();
        if (!(perfmonValues instanceof NullProperty))
        {
            CollectionProperty columns = (CollectionProperty) perfmonValues;
            //log.info("Received colimns collection with no columns " + columns.size());
            PropertyIterator iter = columns.iterator();
            int count = 0;
            while (iter.hasNext())
            {
                List<?> list = (List<?>) iter.next().getObjectValue();
                //log.info("Rows: " + list.size());
                tableModel.setColumnData(count, list);
                count++;
            }

            setSelectedType(pmte.getType());
        } else
        {
            log.warn("Received null property instead of collection");
        }
    }

    @Override
    public void updateGui()
    {
        graphPanel.updateGui();
    }

    @Override
    public void updateGui(Sample sample)
    {
        graphPanel.updateGui();
    }

    @Override
    public void clearData()
    {
        model.clear();
        colors.reset();
        graphPanel.clearRowsTab();
        clearErrorMessage();
        updateGui();
        repaint();
        relativeStartTime = 0;
    }

    @Override
    public void tableChanged(TableModelEvent e)
    {
    }

    @Override
    public void editingStopped(ChangeEvent e)
    {
    }

    @Override
    public void editingCanceled(ChangeEvent e)
    {
    }

    protected abstract String getWikiPage();

    private class AddRowAction
            implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (grid.isEditing())
            {
                TableCellEditor cellEditor = grid.getCellEditor(grid.getEditingRow(), grid.getEditingColumn());
                cellEditor.stopCellEditing();
            }

            tableModel.addRow(defaultValues);
            tableModel.fireTableDataChanged();

            // Enable DELETE (which may already be enabled, but it won't hurt)
            deleteRowButton.setEnabled(true);

            // Highlight (select) the appropriate row.
            int rowToSelect = tableModel.getRowCount() - 1;
            grid.setRowSelectionInterval(rowToSelect, rowToSelect);
        }
    }

    private class DeleteRowAction
            implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (grid.isEditing())
            {
                TableCellEditor cellEditor = grid.getCellEditor(grid.getEditingRow(), grid.getEditingColumn());
                cellEditor.cancelCellEditing();
            }

            int rowSelected = grid.getSelectedRow();
            if (rowSelected >= 0)
            {
                tableModel.removeRow(rowSelected);
                tableModel.fireTableDataChanged();

                // Disable DELETE if there are no rows in the table to delete.
                if (tableModel.getRowCount() == 0)
                {
                    deleteRowButton.setEnabled(false);
                } // Table still contains one or more rows, so highlight (select)
                // the appropriate one.
                else
                {
                    int rowToSelect = rowSelected;

                    if (rowSelected >= tableModel.getRowCount())
                    {
                        rowToSelect = rowSelected - 1;
                    }

                    grid.setRowSelectionInterval(rowToSelect, rowToSelect);
                }
            }
        }
    }

    //not used for now, but needed for settings tab
    @Override
    public int getGranulation()
    {
        return 1000;
    }

    //not used for now, but needed for settings tab
    @Override
    public void setGranulation(int granulation)
    {
        //do nothing here
    }
    @Override
    public GraphPanelChart getGraphPanelChart()
    {
        return graphPanel.getGraphObject();
    }

    //not planned for these charts
    @Override
    public void switchModel(boolean aggregate)
    {
    }

    public void addPerfRecord(String serverName, double value, long time)
    {
        if (relativeStartTime==0)
        {
            //relativeStartTime = JMeterUtils.getPropDefault("TESTSTART.MS", sample.getStartTime());
            relativeStartTime = JMeterContextService.getTestStartTime();
            if(relativeStartTime == 0) relativeStartTime = time;

            if (graphPanel.getGraphObject().isUseRelativeTime())
                graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS, relativeStartTime));
            graphPanel.getGraphObject().setTestStartTime(relativeStartTime);
            graphPanel.getGraphObject().setForcedMinX(relativeStartTime);
        }
    }

    protected long normalizeTime(long time)
    {
        return time - (time - relativeStartTime)%MetricsProvider.DELAY;
    }

    public void setErrorMessage(String msg)
    {
        scrollPan.setVisible(true);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        errorTextArea.setText(errorTextArea.getText() + formatter.format(new Date()) + " - ERROR: " + msg + "\n");

        updateGui();
    }

    public void clearErrorMessage()
    {
        errorTextArea.setText("");
        scrollPan.setVisible(false);
    }

    public abstract void addPerfRecord(String serverName, double value);
    public abstract void setChartType(int monitorType);
    public abstract void setLoadMenuEnabled(boolean enabled);

    private class HideAction
         implements ActionListener
   {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            scrollPan.setVisible(false);
            updateGui();
        }
        
    }
 }
