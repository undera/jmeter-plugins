package com.blazemeter.jmeter;

import com.blazemeter.api.BlazemeterAPIClient;
import com.blazemeter.api.BlazemeterReport;
import com.blazemeter.api.data.JSONConverter;
import kg.apc.jmeter.JMeterPluginsUtils;
import net.sf.json.JSONObject;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.backend.BackendListenerClient;
import org.apache.jmeter.visualizers.backend.BackendListenerContext;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlazemeterBackendListenerClient implements BackendListenerClient {

    private static final Logger log = LoggingManager.getLoggerForClass();

    protected String address = JMeterUtils.getPropDefault("blazemeter.address", "https://a.blazemeter.com/");
    protected String dataAddress = JMeterUtils.getPropDefault("blazemeter.dataAddress", "https://data.blazemeter.com/");
    protected long delay = JMeterUtils.getPropDefault("blazemeter.delay", 5000);
    protected BlazemeterAPIClient apiClient;
    protected BlazemeterReport report;

    private final List<SampleResult> accumulator = new ArrayList<>();

    // this field set from BlazemeterUploader after BackendListener created instance of this class
    protected StatusNotifierCallback informer;

    // BackendListener called this method when test was started
    @Override
    public void setupTest(BackendListenerContext context) throws Exception {
        init(context);
        accumulator.clear();
    }

    private void init(BackendListenerContext context) {
        report = new BlazemeterReport();
        report.setAnonymousTest(Boolean.valueOf(context.getParameter(BlazemeterUploader.ANONYMOUS_TEST)));
        report.setShareTest(Boolean.valueOf(context.getParameter(BlazemeterUploader.SHARE_TEST)));
        report.setWorkspace(context.getParameter(BlazemeterUploader.WORKSPACE));
        report.setProject(context.getParameter(BlazemeterUploader.PROJECT));
        report.setTitle(context.getParameter(BlazemeterUploader.TITLE));
        report.setToken(context.getParameter(BlazemeterUploader.UPLOAD_TOKEN));
    }

    public void initiateOnline() {
        apiClient = new BlazemeterAPIClient(informer, address, dataAddress, report);
        apiClient.prepare();
        try {
            log.info("Starting BlazeMeter test");
            String url = apiClient.startOnline();
            informer.notifyAbout("<p>Started active test: <a href='" + url + "'>" + url + "</a></p>");
            try {
                JMeterPluginsUtils.openInBrowser(url);
            } catch (UnsupportedOperationException ex) {
                log.debug("Failed to open in browser", ex);
            }
        } catch (IOException ex) {
            informer.notifyAbout("Failed to start active test");
            log.warn("Failed to initiate active test", ex);
        }
    }

    @Override
    public void handleSampleResults(List<SampleResult> list, BackendListenerContext backendListenerContext) {
        accumulator.addAll(list);
        JSONObject data = JSONConverter.convertToJSON(accumulator, list);
        try {
            apiClient.sendOnlineData(data);
        } catch (IOException e) {
            log.warn("Failed to send data: " + data, e);
        }

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            log.warn("Backend listener client thread was interrupted");
        }
    }

    @Override
    public void teardownTest(BackendListenerContext backendListenerContext) throws Exception {
        apiClient.endOnline();
        accumulator.clear();
        informer.notifyAbout("<p>Upload finished successfully</p>");
    }

    @Override
    public Arguments getDefaultParameters() {
        return null;
    }

    @Override
    public SampleResult createSampleResult(BackendListenerContext backendListenerContext, SampleResult sampleResult) {
        return sampleResult;
    }

    public BlazemeterAPIClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(BlazemeterAPIClient apiClient) {
        this.apiClient = apiClient;
    }

    public StatusNotifierCallback getInformer() {
        return informer;
    }

    public void setInformer(StatusNotifierCallback informer) {
        this.informer = informer;
    }

    public BlazemeterReport getReport() {
        return report;
    }

    public void setReport(BlazemeterReport report) {
        this.report = report;
    }
}
