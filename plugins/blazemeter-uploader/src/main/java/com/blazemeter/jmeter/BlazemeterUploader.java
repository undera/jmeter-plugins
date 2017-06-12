package com.blazemeter.jmeter;

import org.apache.jmeter.visualizers.backend.BackendListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.lang.reflect.Field;

public class BlazemeterUploader extends BackendListener implements StatusNotifierCallback {

    private static final Logger log = LoggingManager.getLoggerForClass();

    public static final String UPLOAD_TOKEN = "tocken";
    public static final String PROJECT = "project";
    public static final String WORKSPACE = "workspace";
    public static final String TITLE = "title";

    protected BlazemeterUploaderGui gui;



    @Override
    public void notifyAbout(String info) {
        if (gui != null) {

        }
        log.info(info);
    }

    @Override
    public Object clone() {
        BlazemeterUploader clone = (BlazemeterUploader) super.clone();
        clone.gui = this.gui;
        return clone;
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
