package kg.apc.jmeter.perfmon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.net.SocketFactory;
import kg.apc.jmeter.perfmon.agent.AgentCommandsInterface;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author Stephane Hoblingre
 */
public class MetricsProvider implements Runnable, AgentCommandsInterface
{

    private static final Logger log = LoggingManager.getLoggerForClass();
    public final static int DELAY = 1000;
    private boolean testIsRunning = false;
    //for delta calculation
    private HashMap<String, Long> oldValues = new HashMap<String, Long>();
    private SocketFactory socketFactory;
    private AbstractPerformanceMonitoringGui gui = null;
    private int monitorType = -1;
    private AgentConnector[] connectors = null;
    private static BufferedWriter outWriter = null;
    private final static String FILE_HEADER = "PerfMon";

    public MetricsProvider(int monitorType, AbstractPerformanceMonitoringGui gui, AgentConnector[] connectors)
    {
        socketFactory = new TCPSocketFactory();
        this.gui = gui;
        this.connectors = connectors;
        this.monitorType = monitorType;
    }

    private static synchronized void openOutputFile() {
        if(outWriter == null)
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss");
            File f = new File("Perfmon_" + formatter.format(System.currentTimeMillis()) + ".jppm");
            try
            {
                outWriter = new BufferedWriter(new FileWriter(f));
                outWriter.write(FILE_HEADER);
                outWriter.newLine();
                outWriter.flush();
            } catch (IOException ex)
            {
                log.error("Impossible to open file " + f.getAbsolutePath() + " for writing perfmon data.");
            }
        }
    }

    private void addLine(String line) {
        try
        {
            String[] data = line.split(";");
            long time = Long.valueOf(data[0]);
            int type = Integer.valueOf(data[1]);
            double value = Double.valueOf(data[3]);

            if(type == monitorType)
            {
                gui.addPerfRecord(data[2], value, time);
            }
        } catch (Exception e)
        {
            log.warn("Perfmon line not valid: " + line);
        }
    }

    public void loadFile(File file)
    {
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            if (line == null || !FILE_HEADER.equals(line))
            {
                reportError(file.getAbsolutePath() + " is not a valid PerfMon file.");
            } else
            {
                gui.clearData();
                gui.clearErrorMessage();
                line = reader.readLine();
                while (line != null)
                {
                    addLine(line);
                    line = reader.readLine();
                }
            }
        } catch (Exception ex)
        {
            reportError("Failed to read " + file.getAbsolutePath());
        } finally
        {
            try
            {
                reader.close();
            } catch (IOException ex)
            {
                reportError("Failed to close " + file.getAbsolutePath());
            }
        }
    }

    public MetricsProvider(int monitorType, AgentConnector[] connectors)
    {
        socketFactory = new TCPSocketFactory();
        this.connectors = connectors;
        this.monitorType = monitorType;
        openOutputFile();
    }

    private synchronized static void writeRecord(String line) {
        try
        {
            if(outWriter != null)
            {
                outWriter.write(line);
                outWriter.newLine();
                outWriter.flush();
            }
        } catch (IOException ex)
        {
            log.error("Failed to write perfmon data.");
        }
    }

    private void addPerfRecord(String label, double value)
    {
        if (gui != null)
        {
            gui.addPerfRecord(label, value);
        } else
        {
            long now = System.currentTimeMillis();
            writeRecord("" + (now - now % DELAY) + ";" + monitorType + ";"  + label + ";" + value );
        }
    }

    private void reportError(String msg)
    {
        if (gui != null)
        {
            gui.setErrorMessage(msg);
        } else
        {
            log.error(msg);
        }
    }

    @Override
    @SuppressWarnings("SleepWhileHoldingLock")
    public void run()
    {
        while (testIsRunning)
        {
            try
            {
                processConnectors();
                Thread.sleep(DELAY);
            } catch (Exception e)
            {
                log.error("Error in ServerPerfMonitoringGUI loop thread: ", e);
                testIsRunning = false;
            }
        }
    }

    private void processConnectors() throws IllegalArgumentException
    {
        for (int i = 0; i < connectors.length; i++)
        {
            boolean success = true;
            switch (monitorType)
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
                    throw new IllegalArgumentException("Unhandled perfmon type:" + monitorType);
            }
            if (!success)
            {
                //don't display error message if test is stopping
                if (testIsRunning)
                {
                    reportError("Connection lost with '" + connectors[i].getHost() + "'!");
                    testEnded();
                }
            }
        }
    }

    private boolean addCPURecord(AgentConnector connector)
    {
        long value = (long) (100 * connector.getCpu());
        if (value >= 0)
        {
            addPerfRecord(connector.getRemoteServerName(), value);
            return true;
        } else
        {
            return false;
        }
    }

    private boolean addMemRecord(AgentConnector agentConnector)
    {
        //change to double precision
        double value = (double) agentConnector.getMem() / (1024L * 1024L);
        if (value >= 0)
        {
            addPerfRecord(agentConnector.getRemoteServerName(), value);
            return true;
        } else
        {
            return false;
        }
    }

    private boolean addSwapRecord(AgentConnector agentConnector)
    {
        long[] values = agentConnector.getSwap();
        if (values[0] == AGENT_ERROR || values[1] == AGENT_ERROR)
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
        if (values[0] == AGENT_ERROR || values[1] == AGENT_ERROR)
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
        if (values[0] == AGENT_ERROR || values[1] == AGENT_ERROR)
        {
            return false;
        }

        String keyReads = agentConnector.getRemoteServerName() + " RECEIVED";
        String keyWrites = agentConnector.getRemoteServerName() + " TRANSFERED";
        if (oldValues.containsKey(keyReads) && oldValues.containsKey(keyWrites))
        {
            addPerfRecord(keyReads, (double) (values[0] - oldValues.get(keyReads).longValue()) / 1024.0d);
            addPerfRecord(keyWrites, (double) (values[1] - oldValues.get(keyWrites).longValue()) / 1024.0d);
        }
        oldValues.put(keyReads, new Long(values[0]));
        oldValues.put(keyWrites, new Long(values[1]));
        return true;
    }

    public void testStarted()
    {
        if (connectors != null)
        {
            AgentConnector connector = null;
            oldValues.clear();
            try
            {
                if (!testIsRunning)
                {
                    //graphPanel.getGraphObject().clearForcedMessage();
                    for (int i = 0; i < connectors.length; i++)
                    {
                        connector = connectors[i];
                        connector.connect(socketFactory.createSocket(connector.getHost(), connector.getPort()));
                    }
                    Thread t = new Thread(this);
                    testIsRunning = true;
                    t.start();
                    if(gui != null)
                    {
                        gui.setLoadMenuEnabled(false);
                    }
                }
            } catch (UnknownHostException e)
            {
                reportError("Unknown host exception occured. Please verify access to the server '" + connector.getHost() + "'.");
            } catch (IOException e)
            {
                reportError("Enable to connect to server '" + connector.getHost() + "'. Please verify the agent is running on port " + connector.getPort() + ".");
            }
        }
    }

    public void testEnded()
    {
        testIsRunning = false;
        if(gui != null)
        {
            gui.setLoadMenuEnabled(true);
        }
        if (connectors != null)
        {
            for (int i = 0; i < connectors.length; i++)
            {
                connectors[i].disconnect();
            }
        }
        if(outWriter != null)
        {
            try
            {
                outWriter.flush();
                outWriter.close();
                outWriter = null;
            } catch (IOException ex)
            {
                log.error("Failed to close perfmon file.");
            }
        }
    }

    void setSocketFactory(SocketFactory sf)
    {
        socketFactory = sf;
    }
}
