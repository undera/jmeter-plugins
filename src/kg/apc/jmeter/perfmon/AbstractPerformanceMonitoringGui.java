package kg.apc.jmeter.perfmon;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JRadioButton;
import kg.apc.jmeter.vizualizers.ColorsDispatcher;
import kg.apc.jmeter.vizualizers.DateTimeRenderer;
import kg.apc.jmeter.vizualizers.GraphPanel;
import org.apache.jmeter.gui.UnsharedComponent;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.visualizers.GraphListener;
import org.apache.jmeter.visualizers.Sample;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author Stephane Hoblingre
 */
public abstract class AbstractPerformanceMonitoringGui extends AbstractListenerGui implements Clearable, TableModelListener, CellEditorListener, GraphListener, UnsharedComponent
{

    private static final Logger log = LoggingManager.getLoggerForClass();
    protected GraphPanel graphPanel;
    protected ConcurrentSkipListMap<String, AbstractGraphRow> model;
    protected ColorsDispatcher colors;
    private PowerTableModel tableModel;
    JTable grid;
    JButton addRowButton;
    JButton deleteRowButton;
    protected AgentConnector[] connectors = null;
    public static final String[] columnIdentifiers = new String[]
    {
        "Host / IP", "Port"
    };
    /**
     *
     */
    public static final Class[] columnClasses = new Class[]
    {
        String.class, Integer.class
    };
    private static Object[] defaultValues = new Object[]
    {
        "localhost", 4444
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

    public AbstractPerformanceMonitoringGui()
    {
        super();
        model = new ConcurrentSkipListMap<String, AbstractGraphRow>();
        colors = new ColorsDispatcher();
        initGui();
    }

    public boolean isConnectorsValid()
    {
        return connectors != null && connectors.length > 0;
    }

    protected void updateAgentConnectors()
    {
        JMeterProperty props = ((PerformanceMonitoringTestElement) createTestElement()).getData();

        if (!(props instanceof NullProperty))
        {
            CollectionProperty columns = (CollectionProperty) props;
            //log.info("Received colimns collection with no columns " + columns.size());
            PropertyIterator iter = columns.iterator();
            List<?> hosts = (List<?>) iter.next().getObjectValue();
            List<?> ports = (List<?>) iter.next().getObjectValue();
            if (hosts.size() > 0)
            {
                connectors = new AgentConnector[hosts.size()];
                for (int i = 0; i < hosts.size(); i++)
                {
                    StringProperty host = (StringProperty) hosts.get(i);
                    StringProperty port = (StringProperty) ports.get(i);
                    connectors[i] = new AgentConnector(host.getStringValue(), Integer.valueOf(port.getStringValue()));
                }

                selectedPerfMonType = getSelectedTypeIndex();
            } else
            {
                connectors = new AgentConnector[0];
            }
        } else
        {
            connectors = null;
        }
    }

    private void initGui()
    {
        JPanel containerPanel = new VerticalPanel();

        containerPanel.add(makeTitlePanel(), BorderLayout.NORTH);
        containerPanel.add(createParamsPanel(), BorderLayout.CENTER);
        containerPanel.add(createMonitoringTypePanel(), BorderLayout.SOUTH);


        setLayout(new BorderLayout());
        add(containerPanel, BorderLayout.NORTH);

        add(createGraphPanel(), BorderLayout.CENTER);
    }

    private JPanel createParamsPanel()
    {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Servers to monitor"));
        panel.setPreferredSize(new Dimension(150, 150));

        JScrollPane scroll = new JScrollPane(createGrid());
        scroll.setPreferredSize(scroll.getMinimumSize());
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(createButtons(), BorderLayout.SOUTH);

        return panel;
    }

    private int getSelectedTypeIndex() {
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
        graphPanel.getGraphObject().clearErrorMessage();
        updateGui();
        repaint();
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

    public abstract void testStarted();
    public abstract void testEnded();
}
