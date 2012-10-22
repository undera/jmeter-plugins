package kg.apc.jmeter.dbmon;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.vizualizers.CorrectedResultCollector;
import org.apache.jmeter.protocol.jdbc.config.DataSourceElement;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author Marten Bohlin
 */
public class DbMonCollector
        extends CorrectedResultCollector
        implements Runnable, DbMonSampleGenerator {

    private static boolean autoGenerateFiles = false;
    private static final String DBMON = "DbMon";
    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String DATA_PROPERTY = "samplers";
    private int interval;
    private Thread workerThread = null;
    private List<DbMonSampler> dbMonSamplers = new ArrayList<DbMonSampler>();
    private static String autoFileBaseName = null;
    private static int counter = 0;
    /* uncomment to upload to Loadsophia
    private LoadosophiaUploadingNotifier dbMonNotifier = LoadosophiaUploadingNotifier.getInstance();
    */
    private static String workerHost = null;

    static {
        autoGenerateFiles = (JMeterUtils.getPropDefault("forceDbmonFile", "false")).trim().equalsIgnoreCase("true");
    }

    private static synchronized String getAutoFileName() {
        String ret = "";
        counter++;
        if (autoFileBaseName == null) {
            Calendar now = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss");
            autoFileBaseName = "dbMon_" + formatter.format(now.getTime());
        }
        ret = ret + autoFileBaseName;
        if (counter > 1) {
            ret = ret + "_" + counter;
        }
        ret = ret + ".csv";

        return ret;
    }

    public DbMonCollector() {
        // TODO: document it
        interval = JMeterUtils.getPropDefault("jmeterPlugin.dbmon.interval", 1000);
    }

    public void setData(CollectionProperty rows) {
        setProperty(rows);
    }

    public JMeterProperty getSamplerSettings() {
        return getProperty(DATA_PROPERTY);
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

    //ensure we start only on one host (if multiple slaves)
    private synchronized static boolean isWorkingHost(String host) {
       if(workerHost == null) {
          workerHost = host;
          return true;
       } else {
          return host.equals(workerHost);
       }
    }

    @Override
    public void testStarted(String host) {

        if(!isWorkingHost(host)) {
           return;
        }

        //ensure the data will be saved
        if (getProperty(FILENAME) == null || getProperty(FILENAME).getStringValue().trim().length() == 0) {
            if (autoGenerateFiles) {
                setupSaving(getAutoFileName());
            } else {
                /* autosave should be activated if loadsophia uses these data
                try {
                    File tmpFile = File.createTempFile("dbmon_", ".jtl");
                    tmpFile.delete(); // required to have CSV header
                    setupSaving(tmpFile.getAbsolutePath());
                } catch (IOException ex) {
                    log.info("DbMon metrics will not be recorded! Please run the test with -JforceDbmonFile=true", ex);
                }*/
                log.info("DbMon metrics will not be recorded! Please specify a file name in the gui or run the test with -JforceDbmonFile=true");
            }
        }
        
        /* uncomment to upload to Loadsophia
        log.debug("DbMon metrics will be stored in " + getPropertyAsString(FILENAME));
        if (!getSaveConfig().saveAsXml() && getSaveConfig().saveFieldNames()) {
            dbMonNotifier.addFile(getPropertyAsString(FILENAME));
        } else {
            log.warn("Dbmon file saving setting is not CSV with header line, cannot upload it to Loadosophia.org: " + getPropertyAsString(FILENAME));
        }*/
        try {
            initiateConnectors();
        } catch (SQLException ex) {
            //throw new RuntimeException(ex);
            log.error("Enable to start dbmon", ex);
        }

        workerThread = new Thread(this);
        workerThread.start();

        super.testStarted(host);
    }

    private void setupSaving(String fileName) {
        SampleSaveConfiguration config = getSaveConfig();
        JMeterPluginsUtils.doBestCSVSetup(config);
        setSaveConfig(config);
        setFilename(fileName);
        log.info("DbMon metrics will be stored in " + new File(fileName).getAbsolutePath());
    }

    @Override
    public void testEnded(String host) {
        if(workerThread == null) {
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

    private void initiateConnectors() throws SQLException {
        JMeterProperty prop = getSamplerSettings();
        dbMonSamplers.clear();
        if (!(prop instanceof CollectionProperty)) {
            log.warn("Got unexpected property: " + prop);
            return;
        }
        CollectionProperty rows = (CollectionProperty) prop;

        for (int i = 0; i < rows.size(); i++) {
            ArrayList<Object> row = (ArrayList<Object>) rows.get(i).getObjectValue();
            String connectionPool = ((JMeterProperty) row.get(0)).getStringValue();
            Connection conn = DataSourceElement.getConnection(connectionPool);
            String label = ((JMeterProperty) row.get(1)).getStringValue();
            boolean isDelta = ((JMeterProperty) row.get(2)).getBooleanValue();
            String sql = ((JMeterProperty) row.get(3)).getStringValue();
            initiateConnector(conn, label, isDelta, sql);
        }
    }

    private void initiateConnector(Connection conn, String name, boolean delta, String sql) {
        dbMonSamplers.add(new DbMonSampler(conn, name, delta, sql));
    }


    private void shutdownConnectors() {
        dbMonSamplers.clear();
    }

    private void processConnectors() {
        for (DbMonSampler sampler: dbMonSamplers) {
            sampler.generateSamples(this);
        }        
    }

    @Override
    public void sampleOccurred(SampleEvent event) {
        // just dropping regular test samples
    }

    @Override
    public void generateSample(double value, String label) {
        DbMonSampleResult res = new DbMonSampleResult();
        res.setSampleLabel(label);
        res.setValue(value);
        res.setSuccessful(true);
        SampleEvent e = new SampleEvent(res, DBMON);
        super.sampleOccurred(e);
    }
}
