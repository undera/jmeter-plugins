/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.perfmon;

import java.io.IOException;
import java.net.UnknownHostException;
import org.apache.jmeter.engine.StandardJMeterEngine;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.GraphRowExactValues;

/**
 *
 * @author Stephane Hoblingre
 */
public class ServerPerfMonitoringGUI extends AbstractPerformanceMonitoringGui implements Runnable
{

    private boolean testIsRunning = false;
    private int delay = 1000;

    public ServerPerfMonitoringGUI()
    {
        super();
        StandardJMeterEngine.register(this);
    }

    @Override
    public String getStaticLabel()
    {
        return "Servers Performance Monitoring";
    }

    private void addPerfRecord(String serverName, long time, long value)
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

        row.add(time, value);
    }

    @Override
    public void testStarted()
    {

        graphPanel.getGraphObject().clearErrorMessage();
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
                        connector.connect();
                    }
                    Thread t = new Thread(this);
                    testIsRunning = true;
                    t.start();
                }
            } catch (UnknownHostException e)
            {
                graphPanel.getGraphObject().setErrorMessage("Unknown host exception occured. Please verify access to the server '" + connector.getHost() + "'.");
                graphPanel.getGraphObject().repaint();
            } catch (IOException e)
            {
                graphPanel.getGraphObject().setErrorMessage("Enable to connect to server '" + connector.getHost() + "'. Please verify the agent is running on port " + connector.getPort() + ".");
                graphPanel.getGraphObject().repaint();
            }
        }
    }

    @Override
    public void testEnded()
    {
        StandardJMeterEngine.register(this);
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
            try
            {
                for (int i = 0; i < connectors.length; i++)
                {
                    //we cast as long as anyway the GraphRowExactValue uses Long type
                    long value = -1;
                    if(selectedPerfMonType == AbstractPerformanceMonitoringGui.PERFMON_CPU) {
                        value =  (long)(100 * connectors[i].getCpu());
                    } else if(selectedPerfMonType == AbstractPerformanceMonitoringGui.PERFMON_MEM) {
                        value = connectors[i].getMem()/(1024L*1024L);
                    }

                    if (value != -1)
                    {
                        long now = System.currentTimeMillis();
                        addPerfRecord(connectors[i].getRemoteServerName(), now - now
                                % delay, value);
                    } else
                    {
                        graphPanel.getGraphObject().setErrorMessage("Connection lost with '" + connectors[i].getHost() + "'!");
                    }
                }
                updateGui();
                Thread.sleep(delay);
            } catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
