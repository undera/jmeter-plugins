package kg.apc.jmeter.perfmon;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.vizualizers.CorrectedResultCollector;
import kg.apc.perfmon.PerfMonMetricGetter;
import kg.apc.perfmon.client.Transport;
import kg.apc.perfmon.client.TransportFactory;
import kg.apc.perfmon.metrics.MetricParams;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PerfMonCollector extends CorrectedResultCollector implements Runnable, PerfMonSampleGenerator {
    private static boolean autoGenerateFiles = false;
    private static final String PERFMON = "PerfMon";
    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String DATA_PROPERTY = "metricConnections";
    private int interval;
    private Thread workerThread = null;
    private Map<Object, PerfMonAgentConnector> connectors = new ConcurrentHashMap<>();
    private HashMap<String, Long> oldValues = new HashMap<>();
    private static String autoFileBaseName = null;
    private static int counter = 0;
    private static final LinkedList<String> filesList = new LinkedList<>();
    private static String workerHost = null;

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
        interval = JMeterUtils.getPropDefault("jmeterPlugin.perfmon.interval", 1000) / 1000;
    }

    public void setData(CollectionProperty rows) {
        setProperty(rows);
    }

    public JMeterProperty getMetricSettings() {
        return getProperty(DATA_PROPERTY);
    }

    public void sampleOccurred(SampleEvent event) {
        // just dropping regular test samples
    }

    @Override
    public synchronized void run() {
        while (true) {
            processConnectors();
            try {
                this.wait(interval * 1000);
            } catch (InterruptedException ex) {
                log.debug("Monitoring thread was interrupted", ex);
                break;
            }
        }
    }

    //ensure we start only on one host (if multiple slaves)
    private synchronized static boolean isWorkingHost(String host) {
        if (workerHost == null) {
            workerHost = host;
            return true;
        } else {
            return host.equals(workerHost);
        }
    }

    @Override
    public void testStarted(String host) {
        if (!isWorkingHost(host)) {
            return;
        }

        //ensure the data will be saved
        if (getProperty(FILENAME) == null || getProperty(FILENAME).getStringValue().trim().length() == 0) {
            if (autoGenerateFiles) {
                setupSaving(getAutoFileName());
            } else {
                try {
                    File tmpFile = File.createTempFile("perfmon_", ".jtl");
                    tmpFile.delete(); // required to have CSV header
                    setupSaving(tmpFile.getAbsolutePath());
                } catch (IOException ex) {
                    log.info("PerfMon metrics will not be recorded! Please run the test with -JforcePerfmonFile=true", ex);
                }
            }
        }

        log.debug("PerfMon metrics will be stored in " + getPropertyAsString(FILENAME));
        if (!getSaveConfig().saveAsXml() && getSaveConfig().saveFieldNames()) {
            filesList.add(getPropertyAsString(FILENAME));
        } else {
            log.warn("Perfmon file saving setting is not CSV with header line: " + getPropertyAsString(FILENAME));
        }
        initiateConnectors();

        workerThread = new Thread(this);
        workerThread.start();

        super.testStarted(host);
    }

    private void setupSaving(String fileName) {
        SampleSaveConfiguration config = getSaveConfig();
        JMeterPluginsUtils.doBestCSVSetup(config);
        setSaveConfig(config);
        setFilename(fileName);
        log.info("PerfMon metrics will be stored in " + new File(fileName).getAbsolutePath());
    }

    @Override
    public void testEnded(String host) {
        if (workerThread == null) {
            return;
        }
        workerHost = null;
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
            Object val = rows.get(i).getObjectValue();
            if (val instanceof ArrayList) {
                ArrayList<JMeterProperty> row = (ArrayList<JMeterProperty>) val;
                String host = row.get(0).getStringValue();
                int port = row.get(1).getIntValue();
                String metric = row.get(2).getStringValue();
                String params = row.get(3).getStringValue();
                initiateConnector(host, port, i, metric, params);
            }
        }

        for (Object key : connectors.keySet()) {
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
        String labelHostname = host;

        String useHostnameProp = JMeterUtils.getProperty("jmeterPlugin.perfmon.label.useHostname");
        if (useHostnameProp != null && Boolean.parseBoolean(useHostnameProp)) {
            labelHostname = JMeterPluginsUtils.getShortHostname(host);
        }

        // handle label parameter
        MetricParams paramsParsed = MetricParams.createFromString(params);
        String label;
        if (paramsParsed.getLabel().isEmpty()) {
            label = labelHostname + " " + metric;
            if (!params.isEmpty()) {
                label = label + " " + params;
            }
        } else {
            label = labelHostname + " " + metric + " " + paramsParsed.getLabel();

            String[] tokens = params.split("(?<!\\\\)" + PerfMonMetricGetter.DVOETOCHIE);

            params = "";

            for (String token : tokens) {
                if (!token.startsWith("label=")) {
                    if (params.length() != 0) {
                        params = params + PerfMonMetricGetter.DVOETOCHIE;
                    }
                    params = params + token;
                }
            }
        }

        try {
            if (connectors.containsKey(addr)) {
                connectors.get(addr).addMetric(metric, params, label);
            } else {
                PerfMonAgentConnector connector = getConnector(host, port);
                connector.addMetric(metric, params, label);
                connectors.put(addr, connector);
            }
        } catch (IOException e) {
            log.error("Problems creating connector", e);
            connectors.put(stringKey, new UnavailableAgentConnector(e));
        }
    }

    protected PerfMonAgentConnector getConnector(String host, int port) throws IOException {
        log.debug("Trying new connector");
        SocketAddress addr = new InetSocketAddress(host, port);
        Transport transport;
        try {
            transport = TransportFactory.TCPInstance(addr);
            if (!transport.test()) {
                throw new IOException("Agent is unreachable via TCP");
            }
        } catch (IOException e) {
            log.info("Can't connect TCP transport for host: " + addr.toString(), e);
            boolean useUDP = JMeterUtils.getPropDefault("jmeterPlugin.perfmon.useUDP", false);
            if (!useUDP) {
                throw e;
            } else {
                try {
                    log.debug("Connecting UDP");
                    transport = TransportFactory.UDPInstance(addr);
                    if (!transport.test()) {
                        throw new IOException("Agent is unreachable via UDP");
                    }
                } catch (IOException ex) {
                    log.info("Can't connect UDP transport for host: " + addr.toString(), ex);
                    throw ex;
                }
            }
        }
        NewAgentConnector conn = new NewAgentConnector();
        conn.setTransport(transport);
        transport.setInterval(interval);
        return conn;
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
        for (Object key : connectors.keySet()) {
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
        PerfMonSampleResult res = new PerfMonSampleResult();
        res.setSampleLabel(label);
        res.setValue(value);
        res.setSuccessful(true);
        SampleEvent e = new SampleEvent(res, PERFMON);
        super.sampleOccurred(e);
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
            generateSample(((double) (values[0] - oldValues.get(label1))) / dividingFactor, label1);
            generateSample(((double) (values[1] - oldValues.get(label2))) / dividingFactor, label2);
        }
        oldValues.put(label1, values[0]);
        oldValues.put(label2, values[1]);
    }

    public static LinkedList<String> getFiles() {
        return filesList;
    }

    public static void clearFiles() {
        filesList.clear();
    }
}
