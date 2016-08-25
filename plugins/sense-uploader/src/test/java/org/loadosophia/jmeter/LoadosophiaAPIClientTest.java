package org.loadosophia.jmeter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.PrintStream;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class LoadosophiaAPIClientTest implements StatusNotifierCallback {
    @Test
    public void testSendFiles() throws Exception {
        System.out.println("sendFiles");
        File targetFile = File.createTempFile(".jtl", "temp");
        PrintStream ps = new PrintStream(targetFile);
        ps.print("test");
        ps.close();
        LinkedList<String> perfMonFiles = new LinkedList<>();
        LoadosophiaAPIClientEmul instance = new LoadosophiaAPIClientEmul(this);

        JSONObject resp1 = new JSONObject();
        resp1.put("QueueID", 1);
        instance.addEmul(resp1);

        JSONObject resp2 = new JSONObject();
        resp2.put("status", 0);
        instance.addEmul(resp2);

        JSONObject resp3 = new JSONObject();
        resp3.put("status", 4);
        resp3.put("TestID", 2);
        instance.addEmul(resp3);

        JSONObject resp4 = new JSONObject();
        instance.addEmul(resp4);

        LoadosophiaUploadResults result = instance.sendFiles(targetFile, perfMonFiles);
        assertEquals(1, result.getQueueID());
    }

    @Test
    public void testStartOnline() throws Exception {
        System.out.println("startOnline");
        LoadosophiaAPIClientEmul instance = new LoadosophiaAPIClientEmul(this);
        JSONObject resp = new JSONObject();
        resp.put("OnlineID", "123123");
        instance.addEmul(resp);
        String expResult = "http://localhost/gui/active/123123/";
        String result = instance.startOnline();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testEndOnline() throws Exception {
        System.out.println("endOnline");
        LoadosophiaAPIClientEmul instance = new LoadosophiaAPIClientEmul(this);
        instance.addEmul(new JSONObject());
        instance.endOnline("");
    }

    @Test
    public void testSendOnlineData() throws Exception {
        System.out.println("sendOnlineData");
        JSONArray data = new JSONArray();
        LoadosophiaAPIClientEmul instance = new LoadosophiaAPIClientEmul(this);
        instance.addEmul(new JSONObject());
        instance.sendOnlineData(data);
    }

    @Override
    public void notifyAbout(String info) {

    }
}
