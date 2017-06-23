package com.blazemeter.jmeter;

import com.blazemeter.api.BlazemeterAPIClient;
import com.blazemeter.api.BlazemeterAPIClientTest;
import com.blazemeter.api.BlazemeterReport;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.visualizers.backend.BackendListenerContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BlazemeterBackendListenerClientTest {

    @Test
    public void testGetAndSet() throws Exception {
        BlazemeterBackendListenerClient client = new BlazemeterBackendListenerClient();

        StatusNotifierCallback informer = new StatusNotifierCallbackTest();
        client.setInformer(informer);
        assertEquals(informer, client.getInformer());

        BlazemeterReport report = new BlazemeterReport();
        report.setAnonymousTest(true);
        BlazemeterAPIClient apiClient = new BlazemeterAPIClient(null, "http://a.blazemeter.com", null, report);
        client.setApiClient(apiClient);
        assertEquals(apiClient, client.getApiClient());

        BlazemeterReport report1 = new BlazemeterReport();
        client.setReport(report1);
        assertEquals(report1, client.getReport());

        assertNull(client.getDefaultParameters());
        assertNull(client.createSampleResult(null, null));
    }

    @Test
    public void testFlow() throws Exception {
        StatusNotifierCallbackTest notifier = new StatusNotifierCallbackTest();
        BlazemeterBackendListenerClient client = new BlazemeterBackendListenerClient();
        final Arguments arguments = new Arguments();
        arguments.addArgument(BlazemeterUploader.ANONYMOUS_TEST, Boolean.toString(true));
        arguments.addArgument(BlazemeterUploader.SHARE_TEST, Boolean.toString(false));
        arguments.addArgument(BlazemeterUploader.WORKSPACE, "workspace");
        arguments.addArgument(BlazemeterUploader.PROJECT, "project");
        arguments.addArgument(BlazemeterUploader.TITLE, "title");
        arguments.addArgument(BlazemeterUploader.UPLOAD_TOKEN, "token");
        client.setupTest(new BackendListenerContext(arguments));
        client.setInformer(notifier);
        client.initiateOnline();
        assertNotNull(client.getApiClient());

        client.handleSampleResults(BlazemeterAPIClientTest.generateResults(), null);

        client.teardownTest(null);

        String output = notifier.getBuffer().toString();
        assertTrue(output.contains("No BlazeMeter API key provided, will upload anonymously"));
    }
}