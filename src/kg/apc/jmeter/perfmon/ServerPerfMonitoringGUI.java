package kg.apc.jmeter.perfmon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.GraphPanelChart;
import kg.apc.jmeter.charting.GraphRowExactValues;
import kg.apc.jmeter.charting.RowsCollector;
import kg.apc.jmeter.vizualizers.JSettingsPanel;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author Stephane Hoblingre
 */
public class ServerPerfMonitoringGUI extends AbstractPerformanceMonitoringGui
{
   private static final Logger log = LoggingManager.getLoggerForClass();
   private static String loadPath = null;
   JMenuItem loadMenu = null;

   public ServerPerfMonitoringGUI()
   {
      super();
      registerSpecificPopup();
   }
   
   @Override
   public void clearData()
   {
       super.clearData();
       RowsCollector.getInstance().clearRows(createTestElement().getName());      
   }

   private void registerSpecificPopup()
   {
      JPopupMenu popup = graphPanel.getGraphObject().getComponentPopupMenu();
      popup.addSeparator();
      loadMenu = new JMenuItem("Load PerfMon File...");
      loadMenu.addActionListener(new LoadAction());
      popup.add(loadMenu);
   }

   public void setLoadMenuEnabled(boolean enabled)
   {
       loadMenu.setEnabled(enabled);
   }

   @Override
   public String getStaticLabel()
   {
      return "Servers Performance Monitoring";
   }

   private synchronized AbstractGraphRow getNewRow(String label)
   {
      AbstractGraphRow row = null;
      if (!model.containsKey(label))
      {
         row = new GraphRowExactValues();
         row.setLabel(label);
         row.setColor(colors.getNextColor());
         row.setDrawLine(true);
         row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_NONE);
         model.put(label, row);
         graphPanel.addRow(row);
         RowsCollector.getInstance().addRow(createTestElement().getName(), row);
      }
      else
      {
         row = model.get(label);
      }

      return row;
   }

    @Override
   public void addPerfRecord(String serverName, double value)
   {
      long now = System.currentTimeMillis();
      addPerfRecord(serverName, value, now - now % MetricsProvider.DELAY);
   }

    @Override
   public void addPerfRecord(String serverName, double value, long time)
   {
      AbstractGraphRow row = (AbstractGraphRow) model.get(serverName);
      if (row == null)
      {
         row = getNewRow(serverName);
      }
      row.add(time, value);

      updateGui(null);
   }

    @Override
   public void setErrorMessage(String msg)
   {
        graphPanel.getGraphObject().setErrorMessage(msg);
        graphPanel.getGraphObject().repaint();
   }

    @Override
   public void clearErrorMessage()
   {
       graphPanel.getGraphObject().clearErrorMessage();
       graphPanel.getGraphObject().repaint();
   }

    @Override
   public void setChartType(int monitorType)
   {
        graphPanel.getGraphObject().setxAxisLabel("Elapsed time");
        int chartType = GraphPanelChart.CHART_DEFAULT;

        switch(monitorType)
        {
            case AbstractPerformanceMonitoringGui.PERFMON_CPU:
                graphPanel.getGraphObject().setyAxisLabel("Combined CPU usage in %");
                chartType = GraphPanelChart.CHART_PERCENTAGE;
                break;
            case AbstractPerformanceMonitoringGui.PERFMON_MEM:
                graphPanel.getGraphObject().setyAxisLabel("Memory used in MB");
                break;
            case AbstractPerformanceMonitoringGui.PERFMON_DISKS_IO:
                graphPanel.getGraphObject().setyAxisLabel("Number of disks access");
                break;
            case AbstractPerformanceMonitoringGui.PERFMON_NETWORKS_IO:
                graphPanel.getGraphObject().setyAxisLabel("Number of KBytes");
                break;
            case AbstractPerformanceMonitoringGui.PERFMON_SWAP:
                graphPanel.getGraphObject().setyAxisLabel("Number of pages");
                break;
            default:
                graphPanel.getGraphObject().setyAxisLabel("Unknown moitoring metric");
                break;
        }
        graphPanel.getGraphObject().setChartType(chartType);
   }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, false, true, false, false, true);
    }

       private class LoadAction
         implements ActionListener
   {
      @Override
      public void actionPerformed(final ActionEvent e)
      {
         JFileChooser chooser = loadPath != null ? new JFileChooser(new File(loadPath)) : new JFileChooser();
         chooser.setFileFilter(new FileNameExtensionFilter("PerfMon files", "jppm"));

         int returnVal = chooser.showOpenDialog(ServerPerfMonitoringGUI.this);
         if (returnVal == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();
            loadPath = file.getParent();
            MetricsProvider loader = new MetricsProvider(getSelectedTypeIndex(), ServerPerfMonitoringGUI.this, null);
            setChartType(getSelectedTypeIndex());
            loader.loadFile(file);
         }
      }
   }
}
