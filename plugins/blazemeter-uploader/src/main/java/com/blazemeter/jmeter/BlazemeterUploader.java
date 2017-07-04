package com.blazemeter.jmeter;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.gui.MainFrame;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.backend.BackendListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.lang.reflect.Field;

public class BlazemeterUploader extends BackendListener implements StatusNotifierCallback {

    private static final Logger log = LoggingManager.getLoggerForClass();

    public static final String UPLOAD_TOKEN = "token";
    public static final String PROJECT = "project";
    public static final String WORKSPACE = "workspace";
    public static final String TITLE = "title";
    public static final String ANONYMOUS_TEST = "anonymous";
    public static final String SHARE_TEST = "share";

    protected BlazemeterUploaderGui gui;

    public BlazemeterUploader() {
        super();
        setClassname(JMeterUtils.getPropDefault("blazemeter.client", BlazemeterBackendListenerClient.class.getName()));
        setProperty(TestElement.GUI_CLASS, BlazemeterUploaderGui.class.getName());
    }

    @Override
    public void testStarted() {
        testStarted(MainFrame.LOCAL);
    }

    @Override
    public void testStarted(String host) {
        setArguments(createArguments());
        super.testStarted(host);
        initClient();
    }

    private Arguments createArguments() {
        final Arguments arguments = new Arguments();
        arguments.addArgument(ANONYMOUS_TEST, Boolean.toString(isAnonymousTest()));
        arguments.addArgument(SHARE_TEST, Boolean.toString(isShareTest()));
        arguments.addArgument(WORKSPACE, getWorkspace());
        arguments.addArgument(PROJECT, getProject());
        arguments.addArgument(TITLE, getTitle());
        arguments.addArgument(UPLOAD_TOKEN, getUploadToken());
        return arguments;
    }

    @Override
    public void notifyAbout(String info) {
        if (gui != null) {
            gui.inform(info);
        }
        log.info(info);
        System.out.println(info);
    }

    @Override
    public Object clone() {
        BlazemeterUploader clone = (BlazemeterUploader) super.clone();
        clone.gui = this.gui;
        return clone;
    }

    @Override
    public void testEnded(String host) {
        super.testEnded(host);
    }

    @Override
    public void testEnded() {
        testEnded(MainFrame.LOCAL);
    }

    public boolean isAnonymousTest() {
        return getPropertyAsBoolean(ANONYMOUS_TEST);
    }

    public void setAnonymousTest(boolean selected) {
        setProperty(ANONYMOUS_TEST, selected);
    }

    public boolean isShareTest() {
        return getPropertyAsBoolean(SHARE_TEST);
    }

    public void setShareTest(boolean selected) {
        setProperty(SHARE_TEST, selected);
    }

    public void setWorkspace(String workspace) {
        setProperty(WORKSPACE, workspace);
    }

    public String getWorkspace() {
        return getPropertyAsString(WORKSPACE);
    }

    public void setProject(String proj) {
        setProperty(PROJECT, proj);
    }

    public String getProject() {
        return getPropertyAsString(PROJECT);
    }

    public void setTitle(String prefix) {
        setProperty(TITLE, prefix);
    }

    public String getTitle() {
        return getPropertyAsString(TITLE);
    }

    public void setUploadToken(String token) {
        setProperty(UPLOAD_TOKEN, token);
    }

    public String getUploadToken() {
        return getPropertyAsString(UPLOAD_TOKEN).trim();
    }

    public void setGui(BlazemeterUploaderGui gui) {
        this.gui = gui;
    }

    // Inject StatusNotifierCallback (this) and resultCollector into private backendListenerClient
    // call initiateOnline()
    private void initClient() {
        try {
            Field listenerClientData = BlazemeterUploader.class.getSuperclass().getDeclaredField("listenerClientData");
            listenerClientData.setAccessible(true);
            Object clientData = listenerClientData.get(this);
            Field clientField = clientData.getClass().getDeclaredField("client");
            clientField.setAccessible(true);
            BlazemeterBackendListenerClient client = (BlazemeterBackendListenerClient) clientField.get(clientData);
            client.setInformer(this);
            client.initiateOnline();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            log.error("Cannot inject links into backend listener client", e);
        }
    }
}
