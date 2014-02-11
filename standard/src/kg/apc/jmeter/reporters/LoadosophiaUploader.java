package kg.apc.jmeter.reporters;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.loadosophia.jmeter.LoadosophiaAPIClient;
import org.loadosophia.jmeter.LoadosophiaUploadResults;
import org.loadosophia.jmeter.StatusNotifierCallback;

public class LoadosophiaUploader extends ResultCollector implements StatusNotifierCallback, Runnable, TestListener {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String TITLE = "title";
    public static final String COLOR = "color";
    public static final String UPLOAD_TOKEN = "uploadToken";
    public static final String PROJECT = "project";
    public static final String STORE_DIR = "storeDir";
    public static final String USE_ONLINE = "useOnline";
    private String fileName;
    private static final Object LOCK = new Object();
    private boolean isSaving;
    private LoadosophiaUploadingNotifier perfMonNotifier = LoadosophiaUploadingNotifier.getInstance();
    private String address;
    private boolean isOnlineInitiated = false;
    private LoadosophiaAPIClient apiClient;
    private BlockingQueue<SampleEvent> processingQueue;
    private Thread processorThread;
    private LoadosophiaAggregator aggregator;

    public LoadosophiaUploader() {
        super();
        address = JMeterUtils.getPropDefault("loadosophia.address", "https://loadosophia.org/");
    }

    @Override
    public void testStarted(String host) {
        synchronized (LOCK) {
            this.apiClient = getAPIClient();

            try {
                if (!isSaving) {
                    isSaving = true;
                    setupSaving();
                }
            } catch (IOException ex) {
                log.error("Error setting up saving", ex);
            }

            initiateOnline();
        }
        super.testStarted(host);
        perfMonNotifier.startCollecting();
    }

    @Override
    public void testEnded(String host) {
        super.testEnded(host);
        synchronized (LOCK) {
            if (isOnlineInitiated) {
                finishOnline();
            }

            try {
                if (fileName == null) {
                    throw new IOException("File for upload was not set, search for errors above in log");
                }

                isSaving = false;
                LoadosophiaUploadResults uploadResult = this.apiClient.sendFiles(new File(fileName), perfMonNotifier.getFiles());
                informUser("Uploaded successfully, go to results: " + uploadResult.getRedirectLink());
            } catch (IOException ex) {
                informUser("Failed to upload results to Loadosophia.org, see log for detais");
                log.error("Failed to upload results to loadosophia", ex);
            }
        }
        clearData();
        perfMonNotifier.endCollecting();
    }

    private void setupSaving() throws IOException {
        String dir = getStoreDir();
        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }

        File tmpFile;
        try {
            tmpFile = File.createTempFile("Loadosophia_", ".jtl", new File(dir));
        } catch (IOException ex) {
            informUser("Unable to create temp file: " + ex.getMessage());
            informUser("Try to set another directory in the above field.");
            throw ex;
        }

        fileName = tmpFile.getAbsolutePath();
        tmpFile.delete(); // IMPORTANT! this is required to have CSV headers
        informUser("Storing results for upload to Loadosophia.org: " + fileName);
        setFilename(fileName);
        // OMG, I spent 2 days finding that setting properties in testStarted
        // marks them temporary, though they cleared in some places.
        // So we do dirty(?) hack here...
        clearTemporary(getProperty(FILENAME));

        SampleSaveConfiguration conf = getSaveConfig();
        JMeterPluginsUtils.doBestCSVSetup(conf);

        setSaveConfig(conf);
    }

    public void setProject(String proj) {
        setProperty(PROJECT, proj);
    }

    public String getProject() {
        return getPropertyAsString(PROJECT);
    }

    public void setUploadToken(String token) {
        setProperty(UPLOAD_TOKEN, token);
    }

    public String getUploadToken() {
        return getPropertyAsString(UPLOAD_TOKEN);
    }

    public void setTitle(String prefix) {
        setProperty(TITLE, prefix);
    }

    public String getTitle() {
        return getPropertyAsString(TITLE);
    }

    private void informUser(String string) {
        log.info(string);
        if (getVisualizer() != null && getVisualizer() instanceof LoadosophiaUploaderGui) {
            ((LoadosophiaUploaderGui) getVisualizer()).inform(string);
        } else {
            log.info(string);
        }
    }

    public String getStoreDir() {
        return getPropertyAsString(STORE_DIR);
    }

    public void setStoreDir(String prefix) {
        setProperty(STORE_DIR, prefix);
    }

    @Override
    public void testIterationStart(LoopIterationEvent lie) {
    }

    public void setColorFlag(String color) {
        setProperty(COLOR, color);
    }

    public String getColorFlag() {
        return getPropertyAsString(COLOR);
    }

    protected LoadosophiaAPIClient getAPIClient() {
        return new LoadosophiaAPIClient(this, address, getUploadToken(), getProject(), getColorFlag(), getTitle());
    }

    @Override
    public void notifyAbout(String info) {
        informUser(info);
    }

    public boolean isUseOnline() {
        return getPropertyAsBoolean(USE_ONLINE);
    }

    public void setUseOnline(boolean selected) {
        setProperty(USE_ONLINE, selected);
    }

    @Override
    public void sampleOccurred(SampleEvent event) {
        super.sampleOccurred(event);
        if (isOnlineInitiated) {
            processingQueue.add(event);
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
                log.info("Interrupted while taking sample event from deque");
                break;
            }
        }
    }

    private void initiateOnline() {
        if (isUseOnline()) {
            try {
                log.info("Starting Loadosophia online test");
                informUser("Started active test: " + apiClient.startOnline());
                aggregator = new LoadosophiaAggregator();
                processingQueue = new LinkedBlockingQueue<SampleEvent>();
                processorThread = new Thread(this);
                processorThread.setDaemon(true);
                isOnlineInitiated = true;
                processorThread.start();
            } catch (IOException ex) {
                informUser("Failed to start active test");
                log.warn("Failed to initiate active test", ex);
                this.isOnlineInitiated = false;
            }
        }
    }

    private void finishOnline() {
        isOnlineInitiated = false;
        processorThread.interrupt();
        while (!processorThread.isInterrupted()) {
            log.debug("Waiting for bg thread to stop...");
            try {
                Thread.sleep(50);
                processorThread.interrupt();
            } catch (InterruptedException ex) {
                log.warn("Interrupted sleep", ex);
            }
        }
        log.info("Ending Loadosophia online test");
        try {
            apiClient.endOnline();
        } catch (IOException ex) {
            log.warn("Failed to finalize active test", ex);
        }
    }
}
