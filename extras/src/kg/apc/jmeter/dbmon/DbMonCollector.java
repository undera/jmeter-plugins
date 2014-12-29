package kg.apc.jmeter.dbmon;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

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

public class DbMonCollector
        extends CorrectedResultCollector
        implements Runnable, DbMonSampleGenerator {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2521388319652516775L;
	
	private static boolean autoGenerateFiles = false;
    private static final String DBMON = "DbMon";
    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String DATA_PROPERTY = "samplers";
    private int interval;
    private Thread workerThread = null;
    private List<DbMonSampler> dbMonSamplers = new ArrayList<DbMonSampler>();
    private static String autoFileBaseName = null;
    private static int counter = 0;
 
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
        try {
            while (true) {
                processConnectors();
                this.wait(interval);
            }
        } catch (InterruptedException ex) {
            log.debug("Monitoring thread was interrupted", ex);
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

    public void testStarted() {
    	log.debug("Start testStarted");
        super.testStarted();
        log.debug("End   testStarted host");
    }

    @Override
    public void testStarted(String host) {
    	log.debug("Start testStarted host = " + host);

        if(!isWorkingHost(host)) {
           return;
        }

        initCollector();
        super.testStarted(host);
        log.debug("End   testStarted host = " + host);
    }

    private void initCollector() {
    	log.debug("Start initCollector");
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
        log.debug("End   initCollector");
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
    	log.debug("Start testEnded");
        if(workerThread == null) {
        	log.debug("End   testEnded workerThread == null");
        	return;
        }
        workerHost = null;
        
        workerThread.interrupt();
        shutdownConnectors();

        //reset autoFileName for next test run
        autoFileBaseName = null;
        counter = 0;
        super.testEnded(host);
        log.debug("End   testEnded");
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
            String label = ((JMeterProperty) row.get(1)).getStringValue();
            boolean isDelta = ((JMeterProperty) row.get(2)).getBooleanValue();
            String sql = ((JMeterProperty) row.get(3)).getStringValue();
            initiateConnector(connectionPool, label, isDelta, sql);
        }
    }

    private void initiateConnector(String connectionPoolName, String name, boolean delta, String sql) throws SQLException {
    	Connection conn = findConnectionSamePoolName(connectionPoolName);
    	
    	if (conn == null) {
    		log.debug("create new connection");
    		conn = DataSourceElement.getConnection(connectionPoolName);
    	}
    	else {
    		log.debug("use same connection");
    	}
        dbMonSamplers.add(new DbMonSampler(conn, connectionPoolName, name, delta, sql));
    }

    
    private Connection findConnectionSamePoolName(String connectionPoolName) {
    	Connection conn = null;
    	boolean continueFind = true;
    	Iterator<DbMonSampler> it = dbMonSamplers.iterator();
    	
    	while (it.hasNext() && continueFind) {
    		DbMonSampler dbMonSampler = it.next();
    		String connectionPoolNameTmp = dbMonSampler.getPoolName();
    		if (connectionPoolNameTmp != null && connectionPoolNameTmp.equals(connectionPoolName)) {
    			conn = dbMonSampler.getConnection();
    			continueFind = false;
    		}
    	}
    	
    	return conn;
    }

    private void shutdownConnectors() {
    	log.debug("Start shutdownConnectors");
    	Iterator<DbMonSampler> it = dbMonSamplers.iterator();
    	
    	while (it.hasNext()) {
    		DbMonSampler dbMonSampler = it.next();
    		Connection connJdbc = dbMonSampler.getConnection();
    		if (connJdbc != null) {
    			try {
    				connJdbc.close();
    				log.debug("connJdbc is closed");
    			}
    			catch (Exception ex) {
    				log.debug("Can't close jdbc connector, but continue");
    			}
    		}
    		else {
    			log.debug("connJdbc == null, don't try to close connection");
    		}
    	}
        dbMonSamplers.clear();
        log.debug("End   shutdownConnectors");
    }

    protected void processConnectors() {
        for (DbMonSampler sampler: dbMonSamplers) {
            sampler.generateSamples(this);
        }        
    }

    @Override
    public void sampleOccurred(SampleEvent event) {
        // just dropping regular test samples
    }
    
    protected void dbMonSampleOccurred(SampleEvent event) {
        super.sampleOccurred(event);
    }

    @Override
    public void generateSample(double value, String label) {
        DbMonSampleResult res = new DbMonSampleResult();
        res.setSampleLabel(label);
        res.setValue(value);
        res.setSuccessful(true);
        SampleEvent e = new SampleEvent(res, DBMON);
        dbMonSampleOccurred(e);
    }
}
