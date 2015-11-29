package org.loadosophia.jmeter;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;

import kg.apc.jmeter.reporters.LoadosophiaAPIClient;
import org.loadosophia.jmeter.LoadosophiaUploadResults;
import org.loadosophia.jmeter.StatusNotifierCallback;
import net.sf.json.JSONArray;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoadosophiaAPIClientTest {

    public LoadosophiaAPIClientTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSendFiles() throws Exception {
        System.out.println("sendFiles");
        File targetFile = File.createTempFile(".jtl", "temp");
        PrintStream ps = new PrintStream(targetFile);
        ps.print("test");
        ps.close();
        LinkedList<String> perfMonFiles = new LinkedList<String>();
        String[] fake = {"4", "4"};
        LoadosophiaAPIClient instance = new LoadosophiaAPIClientEmul(fake);
        LoadosophiaUploadResults result = instance.sendFiles(targetFile, perfMonFiles);
        assertEquals(4, result.getQueueID());
    }

    @Test
    public void testStartOnline() throws Exception {
        System.out.println("startOnline");
        String[] fake = {"{\"OnlineID\": \"test\"}", ""};
        LoadosophiaAPIClient instance = new LoadosophiaAPIClientEmul(fake);
        String expResult = "http://localhost:9999/gui/active/test/";
        String result = instance.startOnline();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testEndOnline() throws Exception {
        System.out.println("endOnline");
        String[] fake = {"4", "4"};
        LoadosophiaAPIClient instance = new LoadosophiaAPIClientEmul(fake);
        instance.endOnline();
    }

    @Test
    public void testGetUploadStatus() throws Exception {
        System.out.println("getUploadStatus");
        int queueID = 0;
        String[] fake = {"4", "4"};
        LoadosophiaAPIClient instance = new LoadosophiaAPIClientEmul(fake);
        String[] expResult = {"4", "4"};
        String[] result = instance.getUploadStatus(queueID);
        Assert.assertArrayEquals(expResult, result);
    }

    @Test
    public void testMultipartPost() throws Exception {
        System.out.println("multipartPost");
        LinkedList<Part> parts = null;
        String URL = "";
        int expectedSC = 0;
        String[] fake = {"0", ""};
        LoadosophiaAPIClient instance = new LoadosophiaAPIClientEmul(fake);
        String[] expResult = {"0", ""};
        String[] result = instance.multipartPost(parts, URL, expectedSC);
        Assert.assertArrayEquals(expResult, result);
    }

    private static class LoadosophiaAPIClientEmul extends LoadosophiaAPIClient {

        private final String[] arr;

        public LoadosophiaAPIClientEmul(String[] exp) {
            super(new FakeInformer(), "http://localhost:9999/", COLOR_NONE, COLOR_NONE, COLOR_NONE, COLOR_NONE);
            arr = exp;
        }

        @Override
        protected String[] multipartPost(LinkedList<Part> parts, String URL, int expectedSC) throws IOException {
            return arr;
        }

        @Override
        protected String[] getUploadStatus(int queueID) throws IOException {
            return arr;
        }
    }

    private static class FakeInformer implements StatusNotifierCallback {

        public FakeInformer() {
        }

        @Override
        public void notifyAbout(String info) {
            System.out.println(info);
        }
    }

    /**
     * Test of sendOnlineData method, of class LoadosophiaAPIClient.
     */
    @Test
    public void testSendOnlineData() throws Exception {
        System.out.println("sendOnlineData");
        JSONArray data = new JSONArray();
        String[] fake = {"0", ""};
        LoadosophiaAPIClient instance = new LoadosophiaAPIClientEmul(fake);
        instance.sendOnlineData(data);

    }
}
