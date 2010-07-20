// TODO: сделать чтобы при переходе между контролами обновлялся график
package kg.apc.jmeter.threads;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.vizualizers.DateTimeRenderer;
import kg.apc.jmeter.charting.GraphPanelChart;
import kg.apc.jmeter.charting.GraphRowExactValues;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.AbstractThreadGroup;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.gui.AbstractThreadGroupGui;
import org.apache.jorphan.collections.HashTree;

public class UltimateThreadGroupGui
      extends AbstractThreadGroupGui
{
   protected ConcurrentHashMap<String, AbstractGraphRow> model;
   private GraphPanelChart chart;
   private LoopControlPanel loopPanel;
   private PowerTableModel tableModel;
   JTable grid;
   JButton addRowButton;
   JButton deleteRowButton;

   public UltimateThreadGroupGui()
   {
      super();
      init();
   }

   protected final void init()
   {
      JPanel containerPanel = new VerticalPanel();

      containerPanel.add(createParamsPanel(), BorderLayout.NORTH);
      containerPanel.add(createChart(), BorderLayout.CENTER);
      add(containerPanel, BorderLayout.CENTER);

      // this magic LoopPanel provides functionality for thread loops
      // TODO: find a way without magic
      createControllerPanel();
   }

   private JPanel createParamsPanel()
   {
      JPanel panel = new JPanel(new BorderLayout(5, 5));
      panel.setBorder(BorderFactory.createTitledBorder("Threads Schedule"));
      panel.setPreferredSize(new Dimension(200, 200));

      JScrollPane scroll = new JScrollPane(createGrid());
      scroll.setPreferredSize(scroll.getMinimumSize());
      panel.add(scroll, BorderLayout.CENTER);
      panel.add(createButtons(), BorderLayout.SOUTH);

      return panel;
   }

   private JTable createGrid()
   {
      initTableModel();
      grid = new JTable(tableModel);
      // grid.setRowSelectionAllowed(true);
      // grid.setColumnSelectionAllowed(true);
      grid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      // grid.setCellSelectionEnabled(true);
      //grid.setFillsViewportHeight(true);
      grid.setMinimumSize(new Dimension(200, 100));

      return grid;
   }

   protected void initTableModel()
   {
      tableModel = new PowerTableModel(
            // column names
            new String[]
            {
               "Start Threads Count", "Initial Delay, sec", "RampUp Time, sec", "Hold Load For, sec"
            },
            // column classes
            new Class[]
            {
               Integer.class, Integer.class, Integer.class, Integer.class
            });
   }

   public String getLabelResource()
   {
      return this.getClass().getSimpleName();
   }

   @Override
   public String getStaticLabel()
   {
      return "Ultimate Thread Group";
   }

   public TestElement createTestElement()
   {
      UltimateThreadGroup tg = new UltimateThreadGroup();
      modifyTestElement(tg);
      return tg;
   }

   public void modifyTestElement(TestElement tg)
   {
      super.configureTestElement(tg);
      /*
      tg.setProperty(SteppingThreadGroup.NUM_THREADS, totalThreads.getText());
      tg.setProperty(SteppingThreadGroup.THREAD_GROUP_DELAY, initialDelay.getText());
      tg.setProperty(SteppingThreadGroup.INC_USER_COUNT, incUserCount.getText());
      tg.setProperty(SteppingThreadGroup.INC_USER_PERIOD, incUserPeriod.getText());
      tg.setProperty(SteppingThreadGroup.DEC_USER_COUNT, decUserCount.getText());
      tg.setProperty(SteppingThreadGroup.DEC_USER_PERIOD, decUserPeriod.getText());
      tg.setProperty(SteppingThreadGroup.FLIGHT_TIME, flightTime.getText());
       *
       */
      if (tg instanceof UltimateThreadGroup)
      {
         updateChart((UltimateThreadGroup) tg);
         ((AbstractThreadGroup) tg).setSamplerController((LoopController) loopPanel.createTestElement());
      }
   }

   @Override
   public void configure(TestElement tg)
   {
      super.configure(tg);
      /*
      totalThreads.setText(Integer.toString(tg.getPropertyAsInt(SteppingThreadGroup.NUM_THREADS)));
      initialDelay.setText(Integer.toString(tg.getPropertyAsInt(SteppingThreadGroup.THREAD_GROUP_DELAY)));
      incUserCount.setText(Integer.toString(tg.getPropertyAsInt(SteppingThreadGroup.INC_USER_COUNT)));
      incUserPeriod.setText(Integer.toString(tg.getPropertyAsInt(SteppingThreadGroup.INC_USER_PERIOD)));
      decUserCount.setText(Integer.toString(tg.getPropertyAsInt(SteppingThreadGroup.DEC_USER_COUNT)));
      decUserPeriod.setText(Integer.toString(tg.getPropertyAsInt(SteppingThreadGroup.DEC_USER_PERIOD)));
      flightTime.setText(Integer.toString(tg.getPropertyAsInt(SteppingThreadGroup.FLIGHT_TIME)));
       *
       */

      TestElement te = (TestElement) tg.getProperty(AbstractThreadGroup.MAIN_CONTROLLER).getObjectValue();
      if (te != null)
      {
         loopPanel.configure(te);
      }
   }

   private void updateChart(UltimateThreadGroup tg)
   {
      model.clear();
      GraphRowExactValues row = new GraphRowExactValues();
      row.setColor(Color.RED);
      row.setDrawLine(true);
      row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_SMALL);

      final HashTree hashTree = new HashTree();
      hashTree.add(new LoopController());
      JMeterThread thread = new JMeterThread(hashTree, null, null);

      // test start
      row.add(System.currentTimeMillis(), 0);
      row.add(System.currentTimeMillis() + tg.getThreadGroupDelay(), 0);

      // users in
      for (int n = 0; n < tg.getNumThreads(); n++)
      {
         thread.setThreadNum(n);
         tg.scheduleThread(thread);
         row.add(thread.getStartTime(), n + 1);
      }

      // users out
      for (int n = 0; n < tg.getNumThreads(); n++)
      {
         thread.setThreadNum(n);
         tg.scheduleThread(thread);
         row.add(thread.getEndTime(), tg.getNumThreads() - n);
      }

      // final point
      row.add(thread.getEndTime() + tg.getOutUserPeriod() * 1000, 0);

      model.put("Expected parallel users count", row);
      chart.repaint();
   }

   private JPanel createControllerPanel()
   {
      loopPanel = new LoopControlPanel(false);
      LoopController looper = (LoopController) loopPanel.createTestElement();
      looper.setLoops(-1);
      looper.setContinueForever(true);
      loopPanel.configure(looper);
      return loopPanel;
   }

   private Component createChart()
   {
      chart = new GraphPanelChart();
      model = new ConcurrentHashMap<String, AbstractGraphRow>();
      chart.setRows(model);
      chart.setDrawFinalZeroingLines(true);
      chart.setxAxisLabelRenderer(new DateTimeRenderer("HH:mm:ss"));
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

         tableModel.addRow(new Object[]
               {
                  1, 1, 1, 1
               });
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
