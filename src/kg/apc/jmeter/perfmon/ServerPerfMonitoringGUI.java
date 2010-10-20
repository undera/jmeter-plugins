package kg.apc.jmeter.perfmon;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.GraphPanelChart;
import kg.apc.jmeter.charting.GraphRowExactValues;
import kg.apc.jmeter.perfmon.agent.MetricsGetter;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author Stephane Hoblingre
 */
public class ServerPerfMonitoringGUI
      extends AbstractPerformanceMonitoringGui
      implements Runnable
{
   private static final Logger log = LoggingManager.getLoggerForClass();
   private boolean testIsRunning = false;
   private int delay = 1000;
   //for delta calculation
   private HashMap<String, Long> oldValues = new HashMap<String, Long>();

   public ServerPerfMonitoringGUI()
   {
      super();
   }

   @Override
   public String getStaticLabel()
   {
      return "Servers Performance Monitoring";
   }

   private void addPerfRecord(String serverName, long value)
   {
      AbstractGraphRow row = (AbstractGraphRow) model.get(serverName);
      if (row == null)
      {
         row = new GraphRowExactValues();
         row.setLabel(serverName);
         row.setColor(colors.getNextColor());
         row.setDrawLine(true);
         row.setMarkerSize(0);
         model.put(serverName, row);
         graphPanel.addRow(row);
      }
      long now = System.currentTimeMillis();
      row.add(now - now % delay, value);
   }

   @Override
   public void testStarted()
   {

      graphPanel.getGraphObject().clearErrorMessage();
      oldValues.clear();
      updateAgentConnectors();

      if (isConnectorsValid())
      {
         AgentConnector connector = null;

         try
         {
            if (!testIsRunning)
            {
               //graphPanel.getGraphObject().clearForcedMessage();
               for (int i = 0; i < connectors.length; i++)
               {
                  connector = connectors[i];
                  connector.connect(new Socket(connector.getHost(), connector.getPort()));
               }
               Thread t = new Thread(this);
               testIsRunning = true;
               t.start();
            }
         }
         catch (UnknownHostException e)
         {
            graphPanel.getGraphObject().setErrorMessage("Unknown host exception occured. Please verify access to the server '" + connector.getHost() + "'.");
            graphPanel.getGraphObject().repaint();
         }
         catch (IOException e)
         {
            graphPanel.getGraphObject().setErrorMessage("Enable to connect to server '" + connector.getHost() + "'. Please verify the agent is running on port " + connector.getPort() + ".");
            graphPanel.getGraphObject().repaint();
         }
      }
   }

   @Override
   public void testEnded()
   {
      testIsRunning = false;
      if (isConnectorsValid())
      {
         for (int i = 0; i < connectors.length; i++)
         {
            connectors[i].disconnect();
         }
      }
   }

   @Override
   public void run()
   {
      while (testIsRunning)
      {
         setSpecialChartType();

         try
         {
            for (int i = 0; i < connectors.length; i++)
            {
               boolean success = true;

               switch (selectedPerfMonType)
               {
                  case AbstractPerformanceMonitoringGui.PERFMON_CPU:
                     success = addCPURecord(connectors[i]);
                     break;

                  case AbstractPerformanceMonitoringGui.PERFMON_MEM:
                     success = addMemRecord(connectors[i]);
                     break;

                  case AbstractPerformanceMonitoringGui.PERFMON_SWAP:
                     success = addSwapRecord(connectors[i]);
                     break;

                  case AbstractPerformanceMonitoringGui.PERFMON_DISKS_IO:
                     success = addDisksIORecord(connectors[i]);
                     break;

                  case AbstractPerformanceMonitoringGui.PERFMON_NETWORKS_IO:
                     success = addNetworkRecord(connectors[i]);
                     break;

                  default:
                     throw new IllegalArgumentException("Unhandled perfmon type:" + selectedPerfMonType);
               }

               if (!success)
               {
                  //don't display error message if test is stopping
                  if (testIsRunning)
                  {
                     graphPanel.getGraphObject().setErrorMessage("Connection lost with '" + connectors[i].getHost() + "'!");
                  }
               }
            }
            updateGui();
            Thread.sleep(delay);
         }
         catch (Exception e)
         {
            log.error("Error in ServerPerfMonitoringGUI loop thread: ", e);
         }
      }
   }

   private void setSpecialChartType()
   {
      //set special chart type
      if (selectedPerfMonType == AbstractPerformanceMonitoringGui.PERFMON_CPU)
      {
         graphPanel.getGraphObject().setChartType(GraphPanelChart.CHART_PERCENTAGE);
      }
      else
      {
         graphPanel.getGraphObject().setChartType(GraphPanelChart.CHART_DEFAULT);
      }
   }

   private boolean addCPURecord(AgentConnector connector)
   {
      long value = (long) (100 * connector.getCpu());
      if (value >= 0)
      {
         addPerfRecord(connector.getRemoteServerName(), value);
         return true;
      }
      else
      {
         return false;
      }
   }

   private boolean addMemRecord(AgentConnector agentConnector)
   {
      long value = agentConnector.getMem() / (1024L * 1024L);
      if (value >= 0)
      {
         addPerfRecord(agentConnector.getRemoteServerName(), value);
         return true;
      }
      else
      {
         return false;
      }
   }

   private boolean addSwapRecord(AgentConnector agentConnector)
   {
      long[] values = agentConnector.getSwap();
      if (values[0] == MetricsGetter.AGENT_ERROR || values[1] == MetricsGetter.AGENT_ERROR)
      {
         return false;
      }

      String keyPageIn = agentConnector.getRemoteServerName() + " page IN";
      String keyPageOut = agentConnector.getRemoteServerName() + " page OUT";
      if (oldValues.containsKey(keyPageIn) && oldValues.containsKey(keyPageOut))
      {
         addPerfRecord(keyPageIn, values[0] - oldValues.get(keyPageIn).longValue());
         addPerfRecord(keyPageOut, values[1] - oldValues.get(keyPageOut).longValue());
      }
      oldValues.put(keyPageIn, new Long(values[0]));
      oldValues.put(keyPageOut, new Long(values[1]));
      return true;
   }

   private boolean addDisksIORecord(AgentConnector agentConnector)
   {
      long[] values = agentConnector.getDisksIO();
      if (values[0] == MetricsGetter.AGENT_ERROR || values[1] == MetricsGetter.AGENT_ERROR)
      {
         return false;
      }

      String keyReads = agentConnector.getRemoteServerName() + " READS";
      String keyWrites = agentConnector.getRemoteServerName() + " WRITES";
      if (oldValues.containsKey(keyReads) && oldValues.containsKey(keyWrites))
      {
         addPerfRecord(keyReads, values[0] - oldValues.get(keyReads).longValue());
         addPerfRecord(keyWrites, values[1] - oldValues.get(keyWrites).longValue());
      }
      oldValues.put(keyReads, new Long(values[0]));
      oldValues.put(keyWrites, new Long(values[1]));
      return true;
   }

   private boolean addNetworkRecord(AgentConnector agentConnector)
   {
      long[] values = agentConnector.getNetIO();
      if (values[0] == MetricsGetter.AGENT_ERROR || values[1] == MetricsGetter.AGENT_ERROR)
      {
         return false;
      }

      String keyReads = agentConnector.getRemoteServerName() + " RECEIVED";
      String keyWrites = agentConnector.getRemoteServerName() + " TRANSFERED";
      if (oldValues.containsKey(keyReads) && oldValues.containsKey(keyWrites))
      {
         addPerfRecord(keyReads, (values[0] - oldValues.get(keyReads).longValue()) / 1024);
         addPerfRecord(keyWrites, (values[1] - oldValues.get(keyWrites).longValue()) / 1024);
      }
      oldValues.put(keyReads, new Long(values[0]));
      oldValues.put(keyWrites, new Long(values[1]));
      return true;
   }
}
