package kg.apc.jmeter.reporters;

import kg.apc.emulators.TestJMeterUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loadosophia.jmeter.LoadosophiaAPIClient;
import org.loadosophia.jmeter.LoadosophiaAPIClientEmul;
import org.loadosophia.jmeter.StatusNotifierCallback;
import org.loadosophia.jmeter.StatusNotifierCallbackTest;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class LoadosophiaClientTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testGetDataToSend() {
        System.out.println("addSample");
        LoadosophiaClient instance = new LoadosophiaClient();
        List<SampleResult> list = new LinkedList<>();
        list.add(new SampleResult(System.currentTimeMillis(), 1));
        list.add(new SampleResult(System.currentTimeMillis() + 1000, 1));
        list.add(new SampleResult(System.currentTimeMillis() + 2000, 1));
        list.add(new SampleResult(System.currentTimeMillis() + 3000, 1));
        list.add(new SampleResult(System.currentTimeMillis() + 3000, 3));
        list.add(new SampleResult(System.currentTimeMillis() + 3000, 2));
        list.add(new SampleResult(System.currentTimeMillis() + 4000, 1));
        list.add(new SampleResult(System.currentTimeMillis() + 5000, 1));
        list.add(new SampleResult(System.currentTimeMillis() + 6000, 1));

        String str = instance.getDataToSend(list).toString();
        System.out.println("JSON: " + str);
        assertTrue(!str.equals("[]"));
        assertTrue(!str.equals(""));
        JSONArray test = JSONArray.fromObject(str);
        assertEquals(7, test.size());
    }

    @Test
    public void testGetQuantiles() {
        System.out.println("getQuantiles");
        Long[] rtimes = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L};
        JSONObject result = LoadosophiaClient.getQuantilesJSON(rtimes);
        Assert.assertEquals("{\"100.0\":10,\"99.0\":10,\"98.0\":10,\"95.0\":10,\"90.0\":9,\"80.0\":8,\"75.0\":8,\"50.0\":5,\"25.0\":3}", result.toString());
    }

    /**
     * Test of getQuantilesJSON method, of class LoadosophiaAggregator.
     */
    @Test
    public void testGetQuantilesJSON() {
        System.out.println("getQuantilesJSON");
        Long[] rtimes = new Long[0];
        JSONObject result = LoadosophiaClient.getQuantilesJSON(rtimes);
        assertNotNull(result);
    }

    @Test
    public void testHandleSamples() throws Exception {
        JMeterUtils.setProperty("sense.delay", "10000");
        LoadosophiaClient client = new LoadosophiaClient();
        client.setOnlineInitiated(true);
        client.setResultCollector(new ResultCollector());
        client.setFileName("");

        StatusNotifierCallbackTest.StatusNotifierCallbackImpl  notifierCallback = new StatusNotifierCallbackTest.StatusNotifierCallbackImpl();
        client.setInformer(notifierCallback);
        client.setApiClient(new LoadosophiaAPIClientEmul(notifierCallback));

        List<SampleResult> list = new LinkedList<>();
        list.add(new SampleResult(System.currentTimeMillis(), 1));
        list.add(new SampleResult(System.currentTimeMillis() + 1000, 1));
        list.add(new SampleResult(System.currentTimeMillis() + 2000, 1));

        long start = System.currentTimeMillis();
        client.handleSampleResults(list, null);
        long end = System.currentTimeMillis();
        assertTrue((end - start) > 9999);
    }

    @Test
    public void testStop() throws Exception {
        LoadosophiaClient client = new LoadosophiaClient();
        client.setOnlineInitiated(true);
        client.setResultCollector(new ResultCollector());
        client.setFileName("");

        StatusNotifierCallbackTest.StatusNotifierCallbackImpl  notifierCallback = new StatusNotifierCallbackTest.StatusNotifierCallbackImpl();
        client.setInformer(notifierCallback);
        client.setApiClient(new LoadosophiaAPIClientEmul(notifierCallback));

        client.teardownTest(null);
        String results = notifierCallback.getBuffer().toString();

        assertTrue(results.contains("Failed to upload results to BM.Sense, see log for detais"));
    }

    @Test
    public void testGetAndSet() throws Exception {
        LoadosophiaClient client = new LoadosophiaClient();

        LoadosophiaUploader informer = new LoadosophiaUploader();
        client.setInformer(informer);
        assertEquals(informer, client.getInformer());

        LoadosophiaAPIClient apiClient = new LoadosophiaAPIClient(null, null, null, null, null, null);
        client.setApiClient(apiClient);
        assertEquals(apiClient, client.getApiClient());

        ResultCollector collector = new ResultCollector();
        client.setResultCollector(collector);
        assertEquals(collector, client.getResultCollector());

        String address = "ADDRESS";
        client.setAddress(address);
        assertEquals(address, client.getAddress());

        String fileName = "tmpFile.txt";
        client.setFileName(fileName);
        assertEquals(fileName, client.getFileName());

        String project = "DAFULT_PROJECT";
        client.setProject(project);
        assertEquals(project, client.getProject());

        String title = "DAFULT_TITLE";
        client.setTitle(title);
        assertEquals(title, client.getTitle());

        String color = "red";
        client.setColor(color);
        assertEquals(color, client.getColor());

        client.setOnlineInitiated(true);
        assertTrue(client.isOnlineInitiated());
        client.setOnlineInitiated(false);
        assertFalse(client.isOnlineInitiated());
    }
}