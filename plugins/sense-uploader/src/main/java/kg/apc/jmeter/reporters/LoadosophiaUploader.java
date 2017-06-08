package kg.apc.jmeter.reporters;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.vizualizers.CorrectedResultCollector;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.gui.MainFrame;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.backend.BackendListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.loadosophia.jmeter.StatusNotifierCallback;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class LoadosophiaUploader extends BackendListener implements StatusNotifierCallback {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String TITLE = "title";
    public static final String COLOR = "color";
    public static final String UPLOAD_TOKEN = "uploadToken";
    public static final String PROJECT = "project";
    public static final String STORE_DIR = "storeDir";
    public static final String USE_ONLINE = "useOnline";

    protected CorrectedResultCollector resultCollector = new CorrectedResultCollector();
    protected String fileName;
    protected LoadosophiaUploaderGui gui;
    public static final String FILE_NAME = "fileName";

    public LoadosophiaUploader() {
        super();
        setClassname(JMeterUtils.getPropDefault("sense.client", LoadosophiaClient.class.getName()));
    }

    @Override
    public void testStarted() {
        testStarted(MainFrame.LOCAL);
    }

    @Override
    public void testEnded() {
        testEnded(MainFrame.LOCAL);
    }

    @Override
    public void testStarted(String host) {
        try {
            setupSaving();
        } catch (IOException ex) {
            log.error("Unable to set up saving config", ex);
        }
        setArguments(createArguments());
        super.testStarted(host);
        initClient();
        resultCollector.testStarted(host);
    }


    private Arguments createArguments() {
        final Arguments arguments = new Arguments();
        arguments.addArgument(PROJECT, getProject());
        arguments.addArgument(TITLE, getTitle());
        arguments.addArgument(COLOR, getColorFlag());
        arguments.addArgument(UPLOAD_TOKEN, getUploadToken());
        arguments.addArgument(USE_ONLINE, Boolean.toString(isUseOnline()));
        arguments.addArgument(STORE_DIR, getStoreDir());
        arguments.addArgument(FILE_NAME, fileName);
        return arguments;
    }

    @Override
    public void testEnded(String host) {
        super.testEnded(host);
        resultCollector.testEnded(host);
    }

    @Override
    public void sampleOccurred(SampleEvent event) {
        super.sampleOccurred(event);
        resultCollector.sampleOccurred(event);
    }

    @Override
    public void notifyAbout(String info) {
        informUser(info);
    }

    public void informUser(String string) {
        if (gui != null) {
            gui.inform(string);
        }
        log.info(string);
    }

    private void setupSaving() throws IOException {
        log.debug("Set up saving with " + this);
        String dir = getStoreDir();
        File tmpFile;
        try {
            if (dir == null || dir.trim().isEmpty()) {
                tmpFile = File.createTempFile("Sense_", ".jtl");
            } else {
                File storeDir = new File(dir);
                storeDir.mkdirs();
                tmpFile = File.createTempFile("Sense_", ".jtl", storeDir);
            }
        } catch (IOException ex) {
            informUser("Unable to create temp file: " + ex.getMessage());
            informUser("Try to set another directory in the above field.");
            throw ex;
        }

        fileName = tmpFile.getAbsolutePath();
        tmpFile.delete(); // IMPORTANT! this is required to have CSV headers
        informUser("Storing results for upload to Sense: " + fileName);
        resultCollector.setFilename(fileName);
        // OMG, I spent 2 days finding that setting properties in testStarted
        // marks them temporary, though they cleared in some places.
        // So we do dirty(?) hack here...
        clearTemporary(getProperty(ResultCollector.FILENAME));

        SampleSaveConfiguration conf = resultCollector.getSaveConfig();
        JMeterPluginsUtils.doBestCSVSetup(conf);

        resultCollector.setSaveConfig(conf);
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
        return getPropertyAsString(UPLOAD_TOKEN).trim();
    }

    public void setTitle(String prefix) {
        setProperty(TITLE, prefix);
    }

    public String getTitle() {
        return getPropertyAsString(TITLE);
    }

    public String getStoreDir() {
        return getPropertyAsString(STORE_DIR);
    }

    public void setStoreDir(String prefix) {
        setProperty(STORE_DIR, prefix);
    }

    public void setColorFlag(String color) {
        setProperty(COLOR, color);
    }

    public String getColorFlag() {
        return getPropertyAsString(COLOR);
    }

    public boolean isUseOnline() {
        return getPropertyAsBoolean(USE_ONLINE);
    }

    public void setUseOnline(boolean selected) {
        setProperty(USE_ONLINE, selected);
    }

    public void setGui(LoadosophiaUploaderGui gui) {
        this.gui = gui;
    }

    @Override
    public Object clone() {
        LoadosophiaUploader clone = (LoadosophiaUploader) super.clone();
        clone.gui = this.gui;
        return clone;
    }

    // Inject StatusNotifierCallback (this) and resultCollector into private backendListenerClient
    // call initiateOnline()
    private void initClient() {
        try {
            Field listenerClientData = getClass().getSuperclass().getDeclaredField("listenerClientData");
            listenerClientData.setAccessible(true);
            Object clientData = listenerClientData.get(this);
            Field clientField = clientData.getClass().getDeclaredField("client");
            clientField.setAccessible(true);
            LoadosophiaClient client = (LoadosophiaClient) clientField.get(clientData);
            client.setInformer(this);
            client.setResultCollector(resultCollector);
            client.initiateOnline();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            log.error("Cannot inject links into backend listener client", e);
        }
    }
}
