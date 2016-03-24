package kg.apc.jmeter.reporters;

import org.apache.jmeter.gui.MainFrame;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.visualizers.Visualizer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.loadosophia.jmeter.StatusNotifierCallback;

public class LoadosophiaUploader extends ResultCollector implements StatusNotifierCallback {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String TITLE = "title";
    public static final String COLOR = "color";
    public static final String UPLOAD_TOKEN = "uploadToken";
    public static final String PROJECT = "project";
    public static final String STORE_DIR = "storeDir";
    public static final String USE_ONLINE = "useOnline";
    private final LoadosophiaConsolidator consolidator;

    public LoadosophiaUploader() {
        super();
        consolidator = LoadosophiaConsolidator.getInstance();
    }

    @Override
    public void testStarted() {
        testStarted(MainFrame.LOCAL);
        consolidator.testStarted();
    }

    @Override
    public void testEnded() {
        consolidator.testEnded();
        testEnded(MainFrame.LOCAL);
    }

    @Override
    public void testStarted(String host) {
        consolidator.add(this);
    }

    @Override
    public void testEnded(String host) {
        consolidator.remove(this);
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

    public void informUser(String string) {
        Visualizer vis = getVisualizer();
        if (vis != null && vis instanceof LoadosophiaUploaderGui) {
            log.info(string);
            ((LoadosophiaUploaderGui) vis).inform(string);
        } else {
            log.debug("Visualizer: " + vis);
            log.info(string);
        }
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
    public void sampleOccurred(SampleEvent e) {
        consolidator.sampleOccurred(e);
    }

    @Override
    public void sampleStarted(SampleEvent e) {
        consolidator.sampleStarted(e);
    }

    @Override
    public void sampleStopped(SampleEvent e) {
        consolidator.sampleStopped(e);
    }
}
