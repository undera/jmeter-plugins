package kg.apc.jmeter.perfmon;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import kg.apc.perfmon.client.Transport;
import kg.apc.perfmon.client.TransportFactory;
import kg.apc.perfmon.metrics.MetricParams;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author APC
 */
public class PerfMonCollector
        extends ResultCollector
        implements Runnable, PerfMonSampleGenerator {

    private static boolean autoGenerateFiles = false;
    public static final long MEGABYTE = 1024L * 1024L;
    private static final String PERFMON = "PerfMon";
    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String DATA_PROPERTY = "metricConnections";
    private int interval;
    private Thread workerThread;
    private Map<Object, PerfMonAgentConnector> connectors = new HashMap<Object, PerfMonAgentConnector>();
    private HashMap<String, Long> oldValues = new HashMap<String, Long>();
    private static String autoFileBaseName = null;
    private static int counter = 0;

    static {
        autoGenerateFiles = (JMeterUtils.getPropDefault("forcePerfmonFile", "false")).trim().equalsIgnoreCase("true");
    }

    private static synchronized String getAutoFileName() {
        String ret = "";
        counter++;
        if (autoFileBaseName == null) {
            Calendar now = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss");
            autoFileBaseName = "perfMon_" + formatter.format(now.getTime());
        }
        ret = ret + autoFileBaseName;
        if (counter > 1) {
            ret = ret + "_" + counter;
        }
        ret = ret + ".csv";

        return ret;
    }

    public PerfMonCollector() {
        // TODO: document it
        interval = JMeterUtils.getPropDefault("jmeterPlugin.perfmon.interval", 1000);
    }

    public void setData(CollectionProperty rows) {
        setProperty(rows);
    }

    public JMeterProperty getMetricSettings() {
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
                this.wait(interval);
            } catch (InterruptedException ex) {
                log.debug("Monitoring thread was interrupted", ex);
                break;
            }
        }
    }

    @Override
    public void testStarted(String host) {
        //if we run in non gui mode, ensure the data will be saved
        if (GuiPackage.getInstance() == null) {
            if (getProperty(FILENAME) == null || getProperty(FILENAME).getStringValue().trim().length() == 0) {
                if (autoGenerateFiles) {
                    setupSaving();
                } else {
                    log.warn("PerfMon metrics will not be recorded! Please run the test with -JforcePerfmonFile=true");
                }
            } else {
                log.info("PerfMon metrics will be stored in " + getProperty(FILENAME));
            }
        }

        initiateConnectors();

        workerThread = new Thread(this);
        workerThread.start();

        super.testStarted(host);
    }

    private void setupSaving() {
        String fileName = getAutoFileName();
        setFilename(fileName);
        log.info("PerfMon metrics will be stored in " + new File(fileName).getAbsolutePath());
    }

    @Override
    public void testEnded(String host) {
        workerThread.interrupt();
        shutdownConnectors();

        //reset autoFileName for next test run
        autoFileBaseName = null;
        counter = 0;

        super.testEnded(host);
    }

    private void initiateConnectors() {
        oldValues.clear();
        JMeterProperty prop = getMetricSettings();
        connectors.clear();
        if (!(prop instanceof CollectionProperty)) {
            log.warn("Got unexpected property: " + prop);
            return;
        }
        CollectionProperty rows = (CollectionProperty) prop;

        for (int i = 0; i < rows.size(); i++) {
            ArrayList<Object> row = (ArrayList<Object>) rows.get(i).getObjectValue();
            String host = ((JMeterProperty) row.get(0)).getStringValue();
            int port = ((JMeterProperty) row.get(1)).getIntValue();
            String metric = ((JMeterProperty) row.get(2)).getStringValue();
            String params = ((JMeterProperty) row.get(3)).getStringValue();
            initiateConnector(host, port, i, metric, params);
        }

        Iterator<Object> it = connectors.keySet().iterator();
        while (it.hasNext()) {
            Object key = it.next();
            try {
                connectors.get(key).connect();
            } catch (IOException ex) {
                log.error("Error connecting to agent", ex);
                connectors.put(key, new UnavailableAgentConnector(ex));
            }
        }
    }

    private void initiateConnector(String host, int port, int index, String metric, String params) {
        InetSocketAddress addr = new InetSocketAddress(host, port);
        String stringKey = addr.toString() + "#" + index;

        // handle label parameter
        MetricParams paramsParsed = MetricParams.createFromString(params);
        String label;
        if (paramsParsed.getLabel().isEmpty()) {
            label = host + " " + metric + " " + params;
        } else {
            label = host + " " + metric + " " + paramsParsed.getLabel();
            // TODO: God, I need to test this case properly or find better way to do this
            params = params.replaceAll(":label=" + paramsParsed.getLabel() + ":", "");
            params = params.replaceAll("label=" + paramsParsed.getLabel() + ":", "");
            params = params.replaceAll(":label=" + paramsParsed.getLabel(), "");
        }

        try {
            if (connectors.containsKey(addr)) {
                connectors.get(addr).addMetric(metric, params, label);
            } else {
                PerfMonAgentConnector connector = getConnector(host, port);
                connector.addMetric(metric, params, label);

                if (connector instanceof OldAgentConnector) {
                    connectors.put(stringKey, connector);
                } else {
                    connectors.put(addr, connector);
                }
            }
        } catch (IOException e) {
            log.error("Problems creating connector", e);
            connectors.put(stringKey, new UnavailableAgentConnector(e));
        }
    }

    protected PerfMonAgentConnector getConnector(String host, int port) throws IOException {
        try {
            log.debug("Trying new connector");
            Transport transport = TransportFactory.getTransport(new InetSocketAddress(host, port));
            NewAgentConnector conn = new NewAgentConnector();
            conn.setTransport(transport);
            return conn;
        } catch (IOException e) {
            log.debug("Using old connector");
            return new OldAgentConnector(host, port);
        }
    }

    private void shutdownConnectors() {
        log.debug("Shutting down connectors");
        Iterator<Object> it = connectors.keySet().iterator();
        while (it.hasNext()) {
            Object key = it.next();
            final PerfMonAgentConnector conn = connectors.get(key);
            log.debug("Shutting down " + conn.toString());
            //Fix ConcurrentModificationException if more than one host
            //connectors.remove(key);
            it.remove();
            conn.disconnect();
        }
    }

    private void processConnectors() {
        Iterator<Object> it = connectors.keySet().iterator();
        while (it.hasNext()) {
            Object key = it.next();
            PerfMonAgentConnector connector = connectors.get(key);
            try {
                connector.generateSamples(this);
            } catch (IOException e) {
                log.error(e.getMessage());
                connectors.put(key, new UnavailableAgentConnector(e));
            }
        }
    }

    //need floating point precision for memory and cpu
    @Override
    public void generateSample(double value, String label) {
        if (value != AgentConnector.AGENT_ERROR) {
            PerfMonSampleResult res = new PerfMonSampleResult();
            res.setSampleLabel(label);
            res.setValue(value);
            res.setSuccessful(true);
            SampleEvent e = new SampleEvent(res, PERFMON);
            super.sampleOccurred(e);
        }
    }

    @Override
    public void generateErrorSample(String label, String errorMsg) {
        PerfMonSampleResult res = new PerfMonSampleResult();
        res.setSampleLabel(label);
        res.setValue(-1L);
        res.setResponseMessage(errorMsg);
        res.setSuccessful(false);
        SampleEvent e = new SampleEvent(res, PERFMON);
        super.sampleOccurred(e);
        log.error("Perfmon plugin error: " + errorMsg);
    }

    @Override
    public void generate2Samples(long[] values, String label1, String label2) {
        generate2Samples(values, label1, label2, 1d);
    }

    //float precision required for net io
    @Override
    public void generate2Samples(long[] values, String label1, String label2, double dividingFactor) {
        if (oldValues.containsKey(label1) && oldValues.containsKey(label2)) {
            generateSample(((double) (values[0] - oldValues.get(label1).longValue())) / dividingFactor, label1);
            generateSample(((double) (values[1] - oldValues.get(label2).longValue())) / dividingFactor, label2);
        }
        oldValues.put(label1, new Long(values[0]));
        oldValues.put(label2, new Long(values[1]));
    }
}
