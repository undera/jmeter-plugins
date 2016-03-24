package kg.apc.jmeter.reporters;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.perfmon.PerfMonCollector;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.Remoteable;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.loadosophia.jmeter.LoadosophiaAPIClient;
import org.loadosophia.jmeter.LoadosophiaUploadResults;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class LoadosophiaConsolidator extends ResultCollector
        implements Serializable, Remoteable, NoThreadClone, Runnable {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final Object LOCK = new Object();
    private static LoadosophiaConsolidator instance;
    private Set<LoadosophiaUploader> sources = new HashSet<>();
    private boolean isSaving;
    private String fileName;
    private String address;
    private boolean isOnlineInitiated = false;
    private LoadosophiaAPIClient apiClient;
    private BlockingQueue<SampleEvent> processingQueue;
    private Thread processorThread;
    private LoadosophiaAggregator aggregator;

    private LoadosophiaConsolidator() {
        super();
        address = JMeterUtils.getPropDefault("loadosophia.address", "https://sense.blazemeter.com/");
    }

    public static LoadosophiaConsolidator getInstance() {
        if (instance == null) {
            instance = new LoadosophiaConsolidator();
        }
        return instance;
    }

    public void add(LoadosophiaUploader source) {
        log.debug("Add to consolidator: " + source);
        sources.add(source);

        if (sources.size() == 1) {
            log.debug("First source arrived, let's start the process");
            start(source);
        }
    }

    public void remove(LoadosophiaUploader source) {
        log.debug("Remove from consolidator: " + source);
        sources.remove(source);

        if (sources.size() == 0) {
            log.debug("Last source departed, let's finish the process");
            stop(source);
        }
    }


    private void start(LoadosophiaUploader source) {
        synchronized (LOCK) {
            this.apiClient = getAPIClient(source);

            try {
                if (!isSaving) {
                    isSaving = true;
                    setupSaving(source);
                }
            } catch (IOException ex) {
                log.error("Error setting up saving", ex);
            }

            initiateOnline(source);
        }
    }

    private void stop(LoadosophiaUploader source) {
        synchronized (LOCK) {
            // FIXME: trying to handle safe upgrade, needs to be removed in the future
            // @see https://issues.apache.org/bugzilla/show_bug.cgi?id=56807
            try {
                Class<ResultCollector> c = ResultCollector.class;
                Method m = c.getDeclaredMethod("flushFile");
                m.invoke(this);
                log.info("Successfully flushed results file");
            } catch (NoSuchMethodException ex) {
                log.warn("Cannot flush results file since you are using old version of JMeter, consider upgrading to latest. Currently the results may be incomplete.");
            } catch (InvocationTargetException | IllegalAccessException e) {
                log.error("Failed to flush file", e);
            }

            if (isOnlineInitiated) {
                finishOnline();
            }

            try {
                if (fileName == null) {
                    throw new IOException("File for upload was not set, search for errors above in log");
                }

                isSaving = false;
                LinkedList<String> monFiles;
                if (hasStandardSet()) {
                    monFiles = PerfMonCollector.getFiles();
                } else {
                    monFiles = new LinkedList<>();
                }
                LoadosophiaUploadResults uploadResult = this.apiClient.sendFiles(new File(fileName), monFiles);
                source.informUser("Uploaded successfully, go to results: " + uploadResult.getRedirectLink());
            } catch (IOException ex) {
                source.informUser("Failed to upload results to BM.Sense, see log for detais: " + ex.getMessage());
                log.error("Failed to upload results to BM.Sense", ex);
            }
        }
        clearData();
        if (hasStandardSet()) {
            PerfMonCollector.clearFiles();
        }
    }

    private boolean hasStandardSet() {
        boolean hasStandardSet = true;
        try {
            Class.forName("kg.apc.jmeter.perfmon.PerfMonCollector");
        } catch (ClassNotFoundException e) {
            hasStandardSet = false;
        }
        return hasStandardSet;
    }


    private void setupSaving(LoadosophiaUploader source) throws IOException {
        log.debug("Set up saving with " + source);
        String dir = source.getStoreDir();
        File tmpFile;
        try {
            File storeDir = null;
            if (dir != null && !dir.trim().isEmpty()) {
                storeDir = new File(dir);
            }
            tmpFile = File.createTempFile("Sense_", ".jtl", storeDir);
        } catch (IOException ex) {
            source.informUser("Unable to create temp file: " + ex.getMessage());
            source.informUser("Try to set another directory in the above field.");
            throw ex;
        }

        fileName = tmpFile.getAbsolutePath();
        tmpFile.delete(); // IMPORTANT! this is required to have CSV headers
        source.informUser("Storing results for upload to Sense: " + fileName);
        setFilename(fileName);
        // OMG, I spent 2 days finding that setting properties in testStarted
        // marks them temporary, though they cleared in some places.
        // So we do dirty(?) hack here...
        clearTemporary(getProperty(ResultCollector.FILENAME));

        SampleSaveConfiguration conf = getSaveConfig();
        JMeterPluginsUtils.doBestCSVSetup(conf);

        setSaveConfig(conf);
    }

    @Override
    public void sampleOccurred(SampleEvent event) {
        super.sampleOccurred(event);
        if (isOnlineInitiated) {
            try {
                if (!processingQueue.offer(event, 1, TimeUnit.SECONDS)) {
                    log.warn("Failed first dequeue insert try, retrying");
                    if (!processingQueue.offer(event, 1, TimeUnit.SECONDS)) {
                        log.error("Failed second try to inser into deque, dropped sample");
                    }
                }
            } catch (InterruptedException ex) {
                log.info("Interrupted while putting sample event into deque", ex);
            }
        }
    }

    @Override
    public void run() {
        while (isOnlineInitiated) {
            try {
                SampleEvent event = processingQueue.poll(1, TimeUnit.SECONDS);
                if (event != null) {
                    aggregator.addSample(event.getResult());
                }

                if (aggregator.haveDataToSend()) {
                    try {
                        apiClient.sendOnlineData(aggregator.getDataToSend());
                    } catch (IOException ex) {
                        log.warn("Failed to send active test data", ex);
                    }
                }
            } catch (InterruptedException ex) {
                log.debug("Interrupted while taking sample event from deque", ex);
                break;
            }
        }
    }

    private void initiateOnline(LoadosophiaUploader source) {
        if (source.isUseOnline()) {
            try {
                log.info("Starting BM.Sense online test");
                source.informUser("Started active test: " + apiClient.startOnline());
                aggregator = new LoadosophiaAggregator();
                processingQueue = new LinkedBlockingQueue<>();
                processorThread = new Thread(this);
                processorThread.setDaemon(true);
                isOnlineInitiated = true;
                processorThread.start();
            } catch (IOException ex) {
                source.informUser("Failed to start active test");
                log.warn("Failed to initiate active test", ex);
                this.isOnlineInitiated = false;
            }
        }
    }

    private void finishOnline() {
        isOnlineInitiated = false;
        processorThread.interrupt();
        while (processorThread.isAlive() && !processorThread.isInterrupted()) {
            log.info("Waiting for aggregator thread to stop...");
            try {
                Thread.sleep(50);
                processorThread.interrupt();
            } catch (InterruptedException ex) {
                log.warn("Interrupted sleep", ex);
            }
        }
        log.info("Ending BM.Sense online test");
        try {
            apiClient.endOnline();
        } catch (IOException ex) {
            log.warn("Failed to finalize active test", ex);
        }
    }

    protected LoadosophiaAPIClient getAPIClient(LoadosophiaUploader source) {
        return new LoadosophiaAPIClient(source, address, source.getUploadToken(), source.getProject(), source.getColorFlag(), source.getTitle());
    }


}
