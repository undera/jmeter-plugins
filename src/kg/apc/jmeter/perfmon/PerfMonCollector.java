package kg.apc.jmeter.perfmon;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author APC
 */
public class PerfMonCollector
        extends ResultCollector
        implements Runnable {

    private static final long MEGABYTE = 1024L * 1024L;
    private static final String PERFMON = "PerfMon";
    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String DATA_PROPERTY = "metricConnections";
    private Thread workerThread;
    private AgentConnector[] connectors = null;
    private HashMap<String, Long> oldValues = new HashMap<String, Long>();

    public void setData(CollectionProperty rows) {
        setProperty(rows);
    }

    public JMeterProperty getData() {
        return getProperty(DATA_PROPERTY);
    }

    @Override
    public void sampleOccurred(SampleEvent event) {
        // just dropping regular test samples
    }

    @Override
    public synchronized void run() {
        while (true) {
            processConnectors();
            try {
                this.wait(1000);
            } catch (InterruptedException ex) {
                log.debug("Monitoring thread was interrupted", ex);
                break;
            }
        }
    }

    @Override
    public void testStarted(String host) {
        initiateConnectors();

        workerThread = new Thread(this);
        workerThread.start();

        super.testStarted(host);
    }

    @Override
    public void testEnded(String host) {
        workerThread.interrupt();
        shutdownConnectors();

        super.testEnded(host);
    }

    private void initiateConnectors() {
        oldValues.clear();
        JMeterProperty prop = getData();
        connectors = new AgentConnector[0];
        if (!(prop instanceof CollectionProperty)) {
            log.warn("Got unexpected property: " + prop);
            return;
        }
        CollectionProperty rows = (CollectionProperty) prop;

        connectors = new AgentConnector[rows.size()];

        for (int i = 0; i < connectors.length; i++) {
            ArrayList<Object> row = (ArrayList<Object>) rows.get(i).getObjectValue();
            String host = ((JMeterProperty) row.get(0)).getStringValue();
            int port = ((JMeterProperty) row.get(1)).getIntValue();
            String metric = ((JMeterProperty) row.get(2)).getStringValue();

            AgentConnector connector = new AgentConnector(host, port);
            connector.setMetricType(metric);

            try {
                Socket sock = createSocket(connector.getHost(), connector.getPort());
                connector.connect(sock);
                connectors[i] = connector;
            } catch (UnknownHostException e) {
                String msg = "Unknown host exception occured. Please verify access to the server '" + connector.getHost() + "'. (required for " + AgentConnector.metrics.get(connector.getMetricType()) + ")";
                log.error(msg, e);
                generateErrorSample("Agent Connnection", msg);
                connectors[i] = null;
            } catch (IOException e) {
                String msg = "Unable to connect to server '" + connector.getHost() + "'. Please verify the agent is running on port " + connector.getPort() + ". (required for " + AgentConnector.metrics.get(connector.getMetricType()) + ")";
                log.error(msg, e);
                generateErrorSample("Agent Connnection", msg);
                connectors[i] = null;
            }
        }
    }

    private void shutdownConnectors() {
        for (int i = 0; i < connectors.length; i++) {
            if (connectors[i] != null) {
                connectors[i].disconnect();
            }
        }
    }

    protected Socket createSocket(String host, int port) throws UnknownHostException, IOException {
        return new Socket(host, port);
    }

    private void processConnectors() {
        String label;
        boolean cnxLost;
        for (int i = 0; i < connectors.length; i++) {
            cnxLost = false;
            if (connectors[i] != null) {
                label = connectors[i].getHost() + " - " + AgentConnector.metrics.get(connectors[i].getMetricType());

                switch (connectors[i].getMetricType()) {
                    case AbstractPerformanceMonitoringGui.PERFMON_CPU:
                        double cpuMetric = connectors[i].getCpu();
                        if (cpuMetric != AgentConnector.AGENT_ERROR) {
                            generateSample(100 * cpuMetric, label+", %");
                        } else {
                            cnxLost = true;
                        }
                        break;
                    case AbstractPerformanceMonitoringGui.PERFMON_MEM:
                        long memMetric = connectors[i].getMem();
                        if (memMetric != AgentConnector.AGENT_ERROR) {
                            generateSample((double) memMetric / MEGABYTE, label+ ", MB");
                        } else {
                            cnxLost = true;
                        }
                        break;
                    case AbstractPerformanceMonitoringGui.PERFMON_SWAP:
                        long[] swapMetrics = connectors[i].getSwap();
                        if (swapMetrics[0] != AgentConnector.AGENT_ERROR && swapMetrics[1] != AgentConnector.AGENT_ERROR) {
                            generate2Samples(swapMetrics, label + " page in", label + " page out");
                        } else {
                            cnxLost = true;
                        }
                        break;
                    case AbstractPerformanceMonitoringGui.PERFMON_DISKS_IO:
                        long[] dioMetrics = connectors[i].getDisksIO();
                        if (dioMetrics[0] != AgentConnector.AGENT_ERROR && dioMetrics[1] != AgentConnector.AGENT_ERROR) {
                            generate2Samples(dioMetrics, label + " reads", label + " writes");
                        } else {
                            cnxLost = true;
                        }
                        break;
                    case AbstractPerformanceMonitoringGui.PERFMON_NETWORKS_IO:
                        long[] nioMetrics = connectors[i].getNetIO();
                        if (nioMetrics[0] != AgentConnector.AGENT_ERROR && nioMetrics[1] != AgentConnector.AGENT_ERROR) {
                            generate2Samples(nioMetrics, label + " recv, KB", label + " send, KB");
                        } else {
                            cnxLost = true;
                        }

                        break;
                    default:
                        log.error("Unknown metric index: " + connectors[i].getMetricType());
                }
                //if cnx lost, notify
                if (cnxLost) {
                    String msg = "Connection lost with '" + connectors[i].getHost() + "'! (required for " + label + ")";
                    generateErrorSample(label, msg);
                    log.error(msg);
                    connectors[i] = null;
                }
            }
        }
    }

    //need floating point precision for memory and cpu
    private void generateSample(double value, String label) {
        if (value != AgentConnector.AGENT_ERROR) {
            PerfMonSampleResult res = new PerfMonSampleResult();
            res.setSampleLabel(label);
            res.setValue(value);
            res.setSuccessful(true);
            SampleEvent e = new SampleEvent(res, PERFMON);
            super.sampleOccurred(e);
        }
    }

    private void generateErrorSample(String label, String errorMsg) {
        PerfMonSampleResult res = new PerfMonSampleResult();
        res.setSampleLabel(label);
        res.setValue(-1L);
        res.setResponseMessage(errorMsg);
        res.setSuccessful(false);
        SampleEvent e = new SampleEvent(res, PERFMON);
        super.sampleOccurred(e);
    }

    //here long precision is enough as monitored values are counts
    private void generate2Samples(long[] values, String label1, String label2) {
        if (oldValues.containsKey(label1) && oldValues.containsKey(label2)) {
            generateSample(values[0] - oldValues.get(label1).longValue(), label1);
            generateSample(values[1] - oldValues.get(label2).longValue(), label2);
        }
        oldValues.put(label1, new Long(values[0]));
        oldValues.put(label2, new Long(values[1]));
    }
}
