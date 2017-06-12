package com.blazemeter.jmeter;

import com.blazemeter.api.BlazemeterAPIClient;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlazemeterBackendListenerClientTest {

    @Test
    public void testGetAndSet() throws Exception {
        BlazemeterBackendListenerClient client = new BlazemeterBackendListenerClient();

        StatusNotifierCallback informer = new StatusNotifierCallbackTest();
        client.setInformer(informer);
        assertEquals(informer, client.getInformer());

        BlazemeterAPIClient apiClient = new BlazemeterAPIClient(null, null, null, null, null, null);
        client.setApiClient(apiClient);
        assertEquals(apiClient, client.getApiClient());

        String project = "project";
        client.setProject(project);
        assertEquals(project, client.getProject());

        String title = "title";
        client.setTitle(title);
        assertEquals(title, client.getTitle());

        String workspace = "workspace";
        client.setWorkspace(workspace);
        assertEquals(workspace, client.getWorkspace());

        String token = "token";
        client.setToken(token);
        assertEquals(token, client.getToken());
    }
}