/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.perfmon;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
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

    //for delta calculation
    private HashMap<String, Long> oldValues = new HashMap<String, Long>();

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
        long time = now - now % delay;
        if (value != -1)
        {
            row.add(time, value);
        }
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

                    if (selectedPerfMonType == AbstractPerformanceMonitoringGui.PERFMON_CPU)
                    {
                        value = (long) (100 * connectors[i].getCpu());
                        addPerfRecord(connectors[i].getRemoteServerName(), value);
                    } else if (selectedPerfMonType == AbstractPerformanceMonitoringGui.PERFMON_MEM)
                    {
                        value = connectors[i].getMem() / (1024L * 1024L);
                        addPerfRecord(connectors[i].getRemoteServerName(), value);
                    } else if (selectedPerfMonType == AbstractPerformanceMonitoringGui.PERFMON_SWAP)
                    {
                        long[] values = connectors[i].getSwap();
                        if(values[0] < 0 || values[1] < 0) {
                            value = -1;
                        } else {
                            value = values[0];
                        }
                            String keyPageIn = connectors[i].getRemoteServerName() + " page IN";
                            String keyPageOut = connectors[i].getRemoteServerName() + " page OUT";
                            if(oldValues.containsKey(keyPageIn) && oldValues.containsKey(keyPageOut)) {
                                addPerfRecord(keyPageIn, values[0] - oldValues.get(keyPageIn).longValue());
                                addPerfRecord(keyPageOut, values[1] - oldValues.get(keyPageOut).longValue());
                            }
                            oldValues.put(keyPageIn, new Long(values[0]));
                            oldValues.put(keyPageOut, new Long(values[1]));
                    }

                    if (value < 0)
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
