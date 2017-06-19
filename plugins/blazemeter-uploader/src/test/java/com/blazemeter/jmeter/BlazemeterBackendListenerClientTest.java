package com.blazemeter.jmeter;

import com.blazemeter.api.BlazemeterAPIClient;
import com.blazemeter.api.BlazemeterReport;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlazemeterBackendListenerClientTest {

    @Test
    public void testGetAndSet() throws Exception {
        BlazemeterBackendListenerClient client = new BlazemeterBackendListenerClient();

        StatusNotifierCallback informer = new StatusNotifierCallbackTest();
        client.setInformer(informer);
        assertEquals(informer, client.getInformer());

        BlazemeterAPIClient apiClient = new BlazemeterAPIClient(null, null, null, null);
        client.setApiClient(apiClient);
        assertEquals(apiClient, client.getApiClient());

        BlazemeterReport report = new BlazemeterReport();
        client.setReport(report);
        assertEquals(report, client.getReport());
    }
}