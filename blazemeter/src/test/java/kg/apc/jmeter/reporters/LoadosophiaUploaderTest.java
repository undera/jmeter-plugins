package kg.apc.jmeter.reporters;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;

public class LoadosophiaUploaderTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testTestStarted() {
        System.out.println("testStarted");
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        instance.setTitle("UnitTest");
        instance.setStoreDir(TestJMeterUtils.getTempDir());
        instance.testStarted();
    }

    @Test
    public void testTestEnded() throws IOException {
        System.out.println("testEnded");
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        instance.setStoreDir(TestJMeterUtils.getTempDir());
        instance.setTitle("UnitTest");
        instance.setColorFlag("gray");
        instance.setProject("DEFAULT");
        instance.setUploadToken("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        instance.testStarted();

        SampleResult res = new SampleResult();
        res.sampleStart();
        res.sampleEnd();
        SampleEvent event = new SampleEvent(res, "test");
        instance.sampleOccurred(event);

        instance.testEnded();
    }

    @Test
    public void testTestEndedWithNoStoreDir() throws IOException {
        System.out.println("testEnded");
        JMeterUtils.setProperty("loadosophia.address", "http://localhost/");
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        instance.setStoreDir("");
        instance.setTitle("UnitTest");
        instance.setColorFlag("gray");
        instance.setProject("DEFAULT");
        instance.setUploadToken("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        instance.testStarted();

        SampleResult res = new SampleResult();
        res.sampleStart();
        res.sampleEnd();
        SampleEvent event = new SampleEvent(res, "test");
        instance.sampleOccurred(event);

        instance.testEnded();
    }

    @Test
    public void testSetFilePrefix() {
        System.out.println("setFilePrefix");
        String prefix = "";
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        instance.setTitle(prefix);
    }

    @Test
    public void testSetProject() {
        System.out.println("setProject");
        String proj = "";
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        instance.setProject(proj);
    }

    @Test
    public void testSetUploadToken() {
        System.out.println("setUploadToken");
        String token = "";
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        instance.setUploadToken(token);
    }

    @Test
    public void testGetProject() {
        System.out.println("getProject");
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        String expResult = "";
        String result = instance.getProject();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testGetUploadToken() {
        System.out.println("getUploadToken");
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        String expResult = "";
        String result = instance.getUploadToken();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testGetFilePrefix() {
        System.out.println("getFilePrefix");
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        String expResult = "";
        String result = instance.getTitle();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testGetStoreDir() {
        System.out.println("getStoreDir");
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        String expResult = "";
        String result = instance.getStoreDir();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testSetStoreDir() {
        System.out.println("setStoreDir");
        String prefix = "";
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        instance.setStoreDir(prefix);
    }

    private static class LoadosophiaUploaderEmul extends LoadosophiaUploader {
        public LoadosophiaUploaderEmul() {
            super();
            consolidator = new ConsolidatorEmul(new LinkedList<String[]>());
        }
    }

    @Test
    public void testSetTitle() {
        System.out.println("setTitle");
        String prefix = "";
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        instance.setTitle(prefix);
    }

    @Test
    public void testGetTitle() {
        System.out.println("getTitle");
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        String expResult = "";
        String result = instance.getTitle();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testSetColorFlag() {
        System.out.println("setColorFlag");
        String color = "";
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        instance.setColorFlag(color);
    }

    @Test
    public void testGetColorFlag() {
        System.out.println("getColorFlag");
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        String expResult = "";
        String result = instance.getColorFlag();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testNotifyAbout() {
        System.out.println("notifyAbout");
        String info = "";
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        instance.notifyAbout(info);
    }

    @Test
    public void testIsUseOnline() {
        System.out.println("isUseOnline");
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        boolean result = instance.isUseOnline();
        Assert.assertEquals(false, result);
    }

    @Test
    public void testSetUseOnline() {
        System.out.println("setUseOnline");
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        instance.setUseOnline(false);
    }

    @Test
    public void testSampleOccurred() {
        System.out.println("sampleOccurred");
        SampleResult res = new SampleResult();
        SampleEvent event = new SampleEvent(res, "test");
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        instance.sampleOccurred(event);
    }

    @Test
    public void testOnlineProcessor() throws InterruptedException {
        System.out.println("onlineProcessor");
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        instance.setUseOnline(true);
        instance.setStoreDir(TestJMeterUtils.getTempDir());
        instance.testStarted("");
        for (int i = 0; i < 100; i++) {
            SampleResult res = new SampleResult();
            res.setThreadName("test " + i);
            SampleEvent event = new SampleEvent(res, "test " + i);
            instance.sampleOccurred(event);
        }
        Thread.sleep(10);
        instance.testEnded("");
    }
}
