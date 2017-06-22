package com.blazemeter.jmeter;

import com.blazemeter.api.BlazemeterAPIClient;
import com.blazemeter.api.BlazemeterReport;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
    }
}