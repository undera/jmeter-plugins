package kg.apc.jmeter.perfmon;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.GraphPanelChart;
import kg.apc.jmeter.charting.GraphRowExactValues;
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

   public ServerPerfMonitoringGUI()
   {
      super();
      registerSpecificPopup();
   }

   private void registerSpecificPopup()
   {
      JPopupMenu popup = graphPanel.getGraphObject().getComponentPopupMenu();
      popup.addSeparator();
      JMenuItem menu = new JMenuItem("Load PerfMon File...");
      menu.setEnabled(false);
      popup.add(menu);
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
       //set special chart type
      if (monitorType == AbstractPerformanceMonitoringGui.PERFMON_CPU)
      {
         graphPanel.getGraphObject().setChartType(GraphPanelChart.CHART_PERCENTAGE);
      }
      else
      {
         graphPanel.getGraphObject().setChartType(GraphPanelChart.CHART_DEFAULT);
      }
   }

   public void clearGUI() {
       graphPanel.getGraphObject().clearErrorMessage();
   }
   

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, false, true, false, false, true);
    }
}
