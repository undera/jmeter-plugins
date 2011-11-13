package kg.apc.jmeter.perfmon;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
    private PerfMonAgentConnector[] connectors = null;
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
        connectors = new PerfMonAgentConnector[0];
        if (!(prop instanceof CollectionProperty)) {
            log.warn("Got unexpected property: " + prop);
            return;
        }
        CollectionProperty rows = (CollectionProperty) prop;

        connectors = new PerfMonAgentConnector[rows.size()];

        for (int i = 0; i < connectors.length; i++) {
            ArrayList<Object> row = (ArrayList<Object>) rows.get(i).getObjectValue();
            String host = ((JMeterProperty) row.get(0)).getStringValue();
            int port = ((JMeterProperty) row.get(1)).getIntValue();
            String metric = ((JMeterProperty) row.get(2)).getStringValue();
            String params = ((JMeterProperty) row.get(3)).getStringValue();

            try {
                PerfMonAgentConnector connector = getConnector(host, port);
                connector.setMetricType(metric);
                connector.setParams(params);

                connector.connect();
                connectors[i] = connector;
            } catch (IOException e) {
                log.error("Problems creating connector", e);
                connectors[i] = new UnavailableAgentConnector(e);
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

    private void processConnectors() {
        for (int i = 0; i < connectors.length; i++) {
            try {
                connectors[i].generateSamples(this);
            } catch (IOException e) {
                log.error(e.getMessage());
                connectors[i] = new UnavailableAgentConnector(e);
            }
        }
    }

    //need floating point precision for memory and cpu
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

    public void generateErrorSample(String label, String errorMsg) {
        PerfMonSampleResult res = new PerfMonSampleResult();
        res.setSampleLabel(label);
        res.setValue(-1L);
        res.setResponseMessage(errorMsg);
        res.setSuccessful(false);
        SampleEvent e = new SampleEvent(res, PERFMON);
        super.sampleOccurred(e);
        //add a console message for imediate user notice
        System.out.println("Perfmon plugin error: " + errorMsg);
    }

    public void generate2Samples(long[] values, String label1, String label2) {
        generate2Samples(values, label1, label2, 1d);
    }

    //float precision required for net io
    public void generate2Samples(long[] values, String label1, String label2, double dividingFactor) {
        if (oldValues.containsKey(label1) && oldValues.containsKey(label2)) {
            generateSample(((double) (values[0] - oldValues.get(label1).longValue())) / dividingFactor, label1);
            generateSample(((double) (values[1] - oldValues.get(label2).longValue())) / dividingFactor, label2);
        }
        oldValues.put(label1, new Long(values[0]));
        oldValues.put(label2, new Long(values[1]));
    }

    protected PerfMonAgentConnector getConnector(String host, int port) throws IOException {
        try {
            return new NewAgentConnector(host, port);
        } catch (IOException e) {
            return new OldAgentConnector(host, port);
        }
    }
}
