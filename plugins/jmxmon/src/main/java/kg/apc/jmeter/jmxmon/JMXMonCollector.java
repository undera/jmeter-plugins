package kg.apc.jmeter.jmxmon;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.vizualizers.CorrectedResultCollector;

public class JMXMonCollector
        extends CorrectedResultCollector
        implements Runnable, JMXMonSampleGenerator {


    private static final long serialVersionUID = 1437356057522465756L;

    private static final boolean autoGenerateFiles;
    private static final String JMXMON = "JmxMon";
    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String DATA_PROPERTY = "samplers";
    
    
    /**
     * Manage jmx connections and share existing connections
     */
    private static final JMXMonConnectionPool pool = new JMXMonConnectionPool();
      
    private int interval;
    private Thread workerThread = null;
    protected List<JMXMonSampler> jmxMonSamplers = new ArrayList<JMXMonSampler>();
    private String autoFileBaseName = null;
    private static int counter = 0;
    private String workerHost = null;

	/**
	 * Jmeter context used to share with worker thread context
	 */
	private JMeterContext ctx;

    static {
        autoGenerateFiles = (JMeterUtils.getPropDefault("forceJmxMonFile", "false")).trim().equalsIgnoreCase("true");
    }

    private synchronized String getAutoFileName() {
        String ret = "";
        counter++;
        if (autoFileBaseName == null) {
            Calendar now = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss");
            autoFileBaseName = "jmxMon_" + formatter.format(now.getTime());
        }
        ret = ret + autoFileBaseName;
        if (counter > 1) {
            ret = ret + "_" + counter;
        }
        ret = ret + ".csv";

        return ret;
    }

    public JMXMonCollector() {
        interval = JMeterUtils.getPropDefault("jmeterPlugin.jmxmon.interval", 1000);
    }

    public void setData(CollectionProperty rows) {
        setProperty(rows);
    }

    public JMeterProperty getSamplerSettings() {
        return getProperty(DATA_PROPERTY);
    }

    /**
     * Update the worker thread jmeter context with the main thread one
     * @param isInit if true the context a full copy is done, if false only update is done
     */
    private void syncContext(boolean isInit)
    {
    	// jmeter context synchronisation
    	JMeterContext current = JMeterContextService.getContext();
		JMeterContext ctx = this.getThreadContext();
		
		if (isInit)
		{
			current.setCurrentSampler(ctx.getCurrentSampler());
			current.setEngine(ctx.getEngine());
			current.setRestartNextLoop(ctx.isRestartNextLoop());
			current.setSamplingStarted(ctx.isSamplingStarted());
			current.setThread(ctx.getThread());
			current.setThreadGroup(ctx.getThreadGroup());
			current.setThreadNum(ctx.getThreadNum());
		}
		current.setVariables(ctx.getVariables());
		current.setPreviousResult(ctx.getPreviousResult());
		//current.getSamplerContext().putAll(ctx.getSamplerContext());
    }
    @Override
    public synchronized void run() {
        try {
        	
        	syncContext(true);
            while (true) {
                processConnectors();
                this.wait(interval);
                syncContext(false);
            }
        } catch (InterruptedException ex) {
            log.debug("Monitoring thread was interrupted", ex);
        }
    }

    //ensure we start only on one host (if multiple slaves)
    private synchronized boolean isWorkingHost(String host) {
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
                log.info("JmxMon metrics will not be recorded! Please specify a file name in the gui or run the test with -JforceJmxMonFile=true");
            }
        }
        
        ctx = JMeterContextService.getContext();
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
        log.info("JMXMon metrics will be stored in " + new File(fileName).getAbsolutePath());
    }

    @Override
    public void testEnded(String host) {
        log.debug("Start testEnded");
        workerHost = null;
        if (workerThread == null) {
            log.debug("End   testEnded workerThread == null");
            return;
        }

        workerThread.interrupt();
        shutdownConnectors();

        //reset autoFileName for next test run
        autoFileBaseName = null;
        counter = 0;
        super.testEnded(host);
        log.debug("End   testEnded");
    }

    private void initiateConnectors() {
        JMeterProperty prop = getSamplerSettings();
        jmxMonSamplers.clear();
        if (!(prop instanceof CollectionProperty)) {
            log.warn("Got unexpected property: " + prop);
            return;
        }
        CollectionProperty rows = (CollectionProperty) prop;

        for (int i = 0; i < rows.size(); i++) {
            ArrayList<Object> row = (ArrayList<Object>) rows.get(i).getObjectValue();
            String label = ((JMeterProperty) row.get(0)).getStringValue();
            JMeterProperty jmxUrl = (JMeterProperty) row.get(1);
            String username = ((JMeterProperty) row.get(2)).getStringValue();
            String password = ((JMeterProperty) row.get(3)).getStringValue();
            String objectName = ((JMeterProperty) row.get(4)).getStringValue();
            String attribute = ((JMeterProperty) row.get(5)).getStringValue();
            String key = ((JMeterProperty) row.get(6)).getStringValue();
            boolean isDelta = ((JMeterProperty) row.get(7)).getBooleanValue();
            boolean canRetry = ((JMeterProperty) row.get(8)).getBooleanValue();
            
            try {
            	Hashtable attributes = new Hashtable();
                String[] buffer = {username, password};
                attributes.put("jmx.remote.credentials", (String[]) buffer);
                
                initiateConnector(attributes, jmxUrl, label, isDelta, objectName, attribute, key, canRetry);
            } catch (MalformedURLException ex) {
                //throw new RuntimeException(ex);
                log.error("Malformed JMX url", ex);
            } catch (IOException ex) {
                log.error("IOException reading JMX", ex);
            }
        }
    }

    protected void initiateConnector(Hashtable attributes, JMeterProperty jmxUrl, String name, 
    		boolean delta, String objectName, String attribute, String key, boolean canRetry) throws IOException {
        
    	if (!canRetry && pool.getConnection(jmxUrl.getStringValue(), attributes, true) == null)
    		return;
    	
        jmxMonSamplers.add(new JMXMonSampler(this, pool, attributes, jmxUrl,  name, objectName, attribute, key, delta, canRetry));
    }

     private void shutdownConnectors() {
        log.debug("Start shutdownConnectors");

        jmxMonSamplers.clear();
        
        pool.closeAll();
        	
        log.debug("End  shutdownConnectors");
    }

    protected void processConnectors() {
        for (JMXMonSampler sampler : jmxMonSamplers) {
            sampler.generateSamples(this);
        }
    }

    @Override
    public void sampleOccurred(SampleEvent event) {
        // just dropping regular test samples
    	
    	// update JMeterContext for share with samplers thread in order to provide
    	// updated variables
    	this.ctx = JMeterContextService.getContext();
    }

    protected void jmxMonSampleOccurred(SampleEvent event) {
        super.sampleOccurred(event);
    }

    @Override
    public void generateSample(double value, String label) {
        JMXMonSampleResult res = new JMXMonSampleResult();
        res.setSampleLabel(label);
        res.setValue(value);
        res.setSuccessful(true);
        SampleEvent e = new SampleEvent(res, JMXMON);
        jmxMonSampleOccurred(e);
    }

	@Override
	public JMeterContext getThreadContext() {
		if (this.ctx != null)
			return this.ctx;
		return super.getThreadContext();
	}
    
    
}
