package com.blazemeter.api;

import com.blazemeter.jmeter.StatusNotifierCallbackTest;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class BlazemeterAPIClientTest {

    @Test
    public void testStartAnonTest() throws Exception {
        StatusNotifierCallbackTest callback = new StatusNotifierCallbackTest();
        BlazemeterReport report = new BlazemeterReport();
        report.setAnonymousTest(true);
        BlazemeterAPIClient apiClient = new BlazemeterAPIClient(callback, "https://a.blazemeter.com/", "https://data.blazemeter.com/", report);

        String link = apiClient.startOnline();
        System.out.println(link);
        assertFalse(link.isEmpty());
        List<SampleResult> sampleResults = generateResults();
        apiClient.sendOnlineData(JSONConverter.convertToJSON(sampleResults, sampleResults));
        apiClient.endOnline();
    }

    private List<SampleResult> generateResults() {
        List<SampleResult> list = new LinkedList<>();
        list.add(new SampleResult(System.currentTimeMillis(), 1));
        list.add(new SampleResult(System.currentTimeMillis() + 1000, 1));
        list.add(new SampleResult(System.currentTimeMillis() + 2000, 1));
        list.add(new SampleResult(System.currentTimeMillis() + 3000, 1));

        SampleResult res = new SampleResult(System.currentTimeMillis() + 3000, 3);
        res.setSampleLabel("L2");
        list.add(res);

        res = new SampleResult(System.currentTimeMillis() + 3000, 3);
        res.setSampleLabel("L2");
        list.add(res);

        res = new SampleResult(System.currentTimeMillis() + 5000, 2);
        res.setSampleLabel("L2");
        list.add(res);

        res = new SampleResult(System.currentTimeMillis() + 4000, 3);
        res.setSampleLabel("L2");
        list.add(res);
        return list;
    }
}