package com.blazemeter.api;

import com.blazemeter.jmeter.StatusNotifierCallbackTest;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlazemeterAPIClientTest {

    @Test
    public void testStartAnonTest() throws Exception {
        StatusNotifierCallbackTest callback = new StatusNotifierCallbackTest();
        BlazemeterAPIClient apiClient = new BlazemeterAPIClient(callback, "https://a.blazemeter.com/", null, null, null, null);

        String link = apiClient.startOnline();
        System.out.println(link);
        assertFalse(link.isEmpty());

        apiClient.endOnline();

    }
}