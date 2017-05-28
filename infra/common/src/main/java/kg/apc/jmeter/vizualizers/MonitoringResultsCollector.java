package kg.apc.jmeter.vizualizers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import kg.apc.jmeter.JMeterPluginsUtils;

public class MonitoringResultsCollector
        extends CorrectedResultCollector
        implements Runnable, MonitoringSampleGenerator {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String DATA_PROPERTY = "samplers";

    private int interval;
    private Thread workerThread;
    private volatile boolean stop = false;
    protected List<MonitoringSampler> samplers = new ArrayList<MonitoringSampler>();
    private String autoFileBaseName = null;
    private static int counter = 0;
    private String workerHost = null;
    
	/**
	 * Jmeter context used to share with worker thread context
	 */
	private JMeterContext ctx;

	protected String getPrefix() { return "BaseMon"; }

    protected String getForceFilePropertyName() { return "forceMonitoringFile"; }

    protected boolean getAutoGenerateFiles() { 
        return JMeterUtils.getPropDefault(getForceFilePropertyName(), "false").trim().equalsIgnoreCase("true"); 
    }

    private synchronized String getAutoFileName() {
        String ret = "";
        counter++;
        if (autoFileBaseName == null) {
            Calendar now = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss");
            autoFileBaseName = getPrefix() + "_" + formatter.format(now.getTime());
        }
        ret = ret + autoFileBaseName;
        if (counter > 1) {
            ret = ret + "_" + counter;
        }
        ret = ret + ".csv";

        return ret;
    }

    protected int getInterval() {
        return 1000;
    }

    public MonitoringResultsCollector() {
        interval = getInterval();
    }

    public void setData(CollectionProperty rows) {
        setProperty(rows);
    }

    public CollectionProperty getSamplerSettings() {
        JMeterProperty prop = getProperty(DATA_PROPERTY);
        if (!(prop instanceof CollectionProperty)) {
            log.warn("Got unexpected property: " + prop);
            return new CollectionProperty(); // empty collection instead of NullProperty
        }
        CollectionProperty rows = (CollectionProperty) prop;
        return rows;
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
        
            while (!stop) {
                long startTime = System.currentTimeMillis();
                processConnectors();
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (interval > elapsedTime) {
                    this.wait(interval - elapsedTime);
                }
                syncContext(false);
            }
        }
        catch (InterruptedException ex) {
            log.debug("Monitoring thread was interrupted", ex);
        }
        log.debug("exiting run()");
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
            if (getAutoGenerateFiles()) {
                setupSaving(getAutoFileName());
            } else {
                log.info(getPrefix()+" monitoring metrics will not be recorded! Please specify a file name in the gui or run the test with -J"+getForceFilePropertyName()+"=true");
            }
        }

        ctx = JMeterContextService.getContext();
        initiateConnectors();

        stop = false;
        workerThread = new Thread(this);
        workerThread.start();

        super.testStarted(host);
    }

    private void setupSaving(String fileName) {
        SampleSaveConfiguration config = getSaveConfig();
        JMeterPluginsUtils.doBestCSVSetup(config);
        setSaveConfig(config);
        setFilename(fileName);
        log.info(getPrefix()+" monitoring metrics will be stored in " + new File(fileName).getAbsolutePath());
    }

    @Override
    public void testEnded(String host) {
        log.debug("Start testEnded");
        workerHost = null;
        if (workerThread == null) {
            log.debug("End   testEnded workerThread == null");
            return;
        }

        stop = true;
        shutdownConnectors();

        //reset autoFileName for next test run
        autoFileBaseName = null;
        counter = 0;
        super.testEnded(host);
        log.debug("End   testEnded");
    }

    /**
     * No-op implementation that sub-classes are to refine as required.
     */
    protected void initiateConnectors() {
        // may be refined by sub-classes
    }

    /**
     * No-op implementation that sub-classes are to refine as required.
     */
    protected void shutdownConnectors() {
        // may be refined by sub-classes
    }
    
    protected void processConnectors() {
        for (MonitoringSampler sampler : samplers) {
            sampler.generateSamples(this);
            if (stop) {
                break;
            }
        }
    }

    @Override
    public void sampleOccurred(SampleEvent event) {
        // just dropping regular test samples
    	
    	// update JMeterContext for share with samplers thread in order to provide
    	// updated variables
    	this.ctx = JMeterContextService.getContext();
    }

    protected void monitoringSampleOccurred(SampleEvent event) {
        super.sampleOccurred(event);
    }

    @Override
    public void generateSample(double value, String label) {
        MonitoringSampleResult res = new MonitoringSampleResult();
        res.setSampleLabel(label);
        res.setValue(value);
        res.setSuccessful(true);
        SampleEvent e = new SampleEvent(res, getPrefix());
        monitoringSampleOccurred(e);
    }

	@Override
	public JMeterContext getThreadContext() {
		if (this.ctx != null)
			return this.ctx;
		return super.getThreadContext();
	}
}
