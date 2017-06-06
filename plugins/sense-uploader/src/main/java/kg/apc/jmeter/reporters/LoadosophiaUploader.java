package kg.apc.jmeter.reporters;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.gui.MainFrame;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.backend.BackendListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.loadosophia.jmeter.StatusNotifierCallback;

import java.lang.reflect.Field;

public class LoadosophiaUploader extends BackendListener implements StatusNotifierCallback {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String TITLE = "title";
    public static final String COLOR = "color";
    public static final String UPLOAD_TOKEN = "uploadToken";
    public static final String PROJECT = "project";
    public static final String STORE_DIR = "storeDir";
    public static final String USE_ONLINE = "useOnline";

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
        setArguments(createArguments());
        super.testStarted(host);
        addClientAsTestElement();
    }

    private void addClientAsTestElement() {
        try {
            Field listenerClientData = getClass().getSuperclass().getDeclaredField("listenerClientData");
            listenerClientData.setAccessible(true);
            Object clientData = listenerClientData.get(this);
            Field client = clientData.getClass().getDeclaredField("client");
            client.setAccessible(true);
            ResultCollector collector = (ResultCollector) client.get(clientData);
            addTestElement(collector);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private Arguments createArguments() {
        final Arguments arguments = new Arguments();
        arguments.addArgument(PROJECT, getProject());
        arguments.addArgument(TITLE, getTitle());
        arguments.addArgument(COLOR, getColorFlag());
        arguments.addArgument(UPLOAD_TOKEN, getUploadToken());
        arguments.addArgument(USE_ONLINE, Boolean.toString(isUseOnline()));
        arguments.addArgument(STORE_DIR, getStoreDir());
        return arguments;
    }

    @Override
    public void testEnded(String host) {
        super.testEnded(host);
    }

    @Override
    public void notifyAbout(String info) {
        informUser(info);
    }

    public void informUser(String string) {
//        Visualizer vis = getVisualizer();
//        if (vis != null && vis instanceof LoadosophiaUploaderGui) {
//            log.info(string);
//            ((LoadosophiaUploaderGui) vis).inform(string);
//        } else {
//            log.info(string);
//        }
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

}
