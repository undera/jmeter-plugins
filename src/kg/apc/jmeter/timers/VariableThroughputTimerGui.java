package kg.apc.jmeter.timers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
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
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.DateTimeRenderer;
import kg.apc.jmeter.charting.GraphPanelChart;
import kg.apc.jmeter.charting.GraphRowSumValues;
import kg.apc.jmeter.charting.ColorsDispatcher;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.timers.gui.AbstractTimerGui;
import org.apache.jorphan.collections.HashTree;
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
                 CellEditorListener
{
   private static final Logger log = LoggingManager.getLoggerForClass();
   /**
    *
    */
   protected ConcurrentHashMap<String, AbstractGraphRow> model;
   private GraphPanelChart chart;
   /**
    *
    */
   public static final String[] columnIdentifiers = new String[]
   {
      "Start RPS", "End RPS", "Duration, sec"
   };
   /**
    *
    */
   public static final Class[] columnClasses = new Class[]
   {
      String.class, String.class, String.class
   };
   private static Integer[] defaultValues = new Integer[]
   {
      0, 1000, 60
   };
   private PowerTableModel tableModel;
   JTable grid;
   JButton addRowButton;
   JButton deleteRowButton;

   /**
    *
    */
   public VariableThroughputTimerGui()
   {
      super();
      init();
   }

   /**
    *
    */
   protected final void init()
   {
       setLayout(new BorderLayout());
        add(makeTitlePanel(), BorderLayout.NORTH);
        JPanel containerPanel = new VerticalPanel();

      containerPanel.add(createParamsPanel(), BorderLayout.NORTH);
      containerPanel.add(createChart(), BorderLayout.CENTER);
      add(containerPanel, BorderLayout.CENTER);
   }

   private JPanel createParamsPanel()
   {
      JPanel panel = new JPanel(new BorderLayout(5, 5));
      panel.setBorder(BorderFactory.createTitledBorder("Request Per Second (RPS) Schedule"));
      panel.setPreferredSize(new Dimension(200, 200));

      JScrollPane scroll = new JScrollPane(createGrid());
      scroll.setPreferredSize(scroll.getMinimumSize());
      panel.add(scroll, BorderLayout.CENTER);
      panel.add(createButtons(), BorderLayout.SOUTH);

      return panel;
   }

   private JTable createGrid()
   {
      grid = new JTable();
      grid.getDefaultEditor(String.class).addCellEditorListener(this);
      createTableModel();
      // grid.setRowSelectionAllowed(true);
      // grid.setColumnSelectionAllowed(true);
      grid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      // grid.setCellSelectionEnabled(true);
      //grid.setFillsViewportHeight(true);
      grid.setMinimumSize(new Dimension(200, 100));

      return grid;
   }

   public String getLabelResource()
   {
      return this.getClass().getSimpleName();
   }

   @Override
   public String getStaticLabel()
   {
      return JMeterPluginsUtils.prefixLabel("Variable Throughput Timer");
   }

   public TestElement createTestElement()
   {
      //log.info("Create test element");
      VariableThroughputTimer tg = new VariableThroughputTimer();
      modifyTestElement(tg);
      return tg;
   }

   public void modifyTestElement(TestElement tg)
   {
      //log.info("Modify test element");
      if (grid.isEditing())
      {
         grid.getCellEditor().stopCellEditing();
      }

      if (tg instanceof VariableThroughputTimer)
      {
         VariableThroughputTimer utg = (VariableThroughputTimer) tg;
         VariableThroughputTimer utgForPreview = new VariableThroughputTimer();
         CollectionProperty rows = tableModelToCollectionProperty(tableModel);
         utg.setData(rows);
         utgForPreview.setData(rows);

         updateChart(utgForPreview);
      }
      
      super.configureTestElement(tg);
   }

   public static CollectionProperty tableModelToCollectionProperty(PowerTableModel model)
   {
      CollectionProperty rows = new CollectionProperty(VariableThroughputTimer.DATA_PROPERTY, new ArrayList<Object>());
      for (int row = 0; row < model.getRowCount(); row++)
      {
         rows.addItem(model.getRowData(row));
      }
      return rows;
   }

   @Override
   public void configure(TestElement tg)
   {
      //log.info("Configure");
      super.configure(tg);
      createTableModel();
      VariableThroughputTimer utg = (VariableThroughputTimer) tg;
      JMeterProperty threadValues = utg.getData();
      if (!(threadValues instanceof NullProperty))
      {
         CollectionProperty columns = (CollectionProperty) threadValues;
         PropertyIterator iter = columns.iterator();
         int count = 0;
         while (iter.hasNext())
         {
            List<?> list = (List<?>) iter.next().getObjectValue();
            tableModel.setColumnData(count, list);
            count++;
         }
      }
      else
      {
         log.warn("Received null property instead of collection");
      }
   }

   private void updateChart(VariableThroughputTimer tg)
   {
      model.clear();
      GraphRowSumValues row = new GraphRowSumValues();
      row.setColor(ColorsDispatcher.RED);
      row.setDrawLine(true);
      row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_NONE);
      row.setDrawThickLines(true);

      final HashTree hashTree = new HashTree();
      hashTree.add(new LoopController());
      //JMeterThread thread = new JMeterThread(hashTree, null, null);

      long now = System.currentTimeMillis();

      chart.setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS, now-1)); //-1 because row.add(thread.getStartTime() - 1, 0)
      chart.setForcedMinX(now);

      row.add(now, 0);

      /*
      // users in
      int numThreads = tg.getNumThreads();
      for (int n = 0; n < numThreads; n++)
      {
         thread.setThreadNum(n);
         tg.scheduleThread(thread);
         row.add(thread.getStartTime() - 1, 0);
         row.add(thread.getStartTime(), 1);
      }

      // users out
      for (int n = 0; n < tg.getNumThreads(); n++)
      {
         thread.setThreadNum(n);
         tg.scheduleThread(thread);
         row.add(thread.getEndTime() - 1, 0);
         row.add(thread.getEndTime(), -1);
      }
      */
      model.put("Expected RPS", row);
      chart.repaint();
   }

   private Component createChart()
   {
      chart = new GraphPanelChart(false);
      model = new ConcurrentHashMap<String, AbstractGraphRow>();
      chart.setRows(model);
      chart.setDrawFinalZeroingLines(true);
      chart.setxAxisLabel("Elapsed time");
      chart.setyAxisLabel("RPS");
      return chart;
   }

   private Component createButtons()
   {
      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new GridLayout(1, 2));

      addRowButton = new JButton("Add Row");
      deleteRowButton = new JButton("Delete Row");

      buttonPanel.add(addRowButton);
      buttonPanel.add(deleteRowButton);

      addRowButton.addActionListener(new AddRowAction());
      deleteRowButton.addActionListener(new DeleteRowAction());

      return buttonPanel;
   }

   public void tableChanged(TableModelEvent e)
   {
      //log.info("Model changed");
      updateChart();
   }

   private void createTableModel()
   {
      tableModel = new PowerTableModel(columnIdentifiers, columnClasses);
      tableModel.addTableModelListener(this);
      grid.setModel(tableModel);
   }

   public void editingStopped(ChangeEvent e)
   {
      //log.info("Editing stopped");
      updateChart();
   }

   public void editingCanceled(ChangeEvent e)
   {
      // no action needed
   }

   private void updateChart()
   {
      GuiPackage.getInstance().updateCurrentGui();
   }

   private class AddRowAction
         implements ActionListener
   {
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

   @Override
   public void clearGui()
   {
      super.clearGui();
      tableModel.clearData();
   }

   private class DeleteRowAction
         implements ActionListener
   {
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
}
