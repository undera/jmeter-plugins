package com.blazemeter.jmeter;

import kg.apc.emulators.TestJMeterUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlazemeterUploaderTest {

    @BeforeClass
    public static void setUpClass()
            throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testFlow() throws Exception {
        BlazemeterUploader uploader = new BlazemeterUploader();
        uploader.setGui(new BlazemeterUploaderGui());
        uploader.setAnonymousTest(true);
        uploader.setShareTest(true);
        uploader.setWorkspace("workspace");
        uploader.setProject("project");
        uploader.setTitle("title");
        uploader.setUploadToken("token");
        uploader.testStarted();
        uploader.testEnded();

        assertEquals(true, uploader.isAnonymousTest());
        assertEquals(true, uploader.isShareTest());
        assertEquals("workspace", uploader.getWorkspace());
        assertEquals("project", uploader.getProject());
        assertEquals("title", uploader.getTitle());
        assertEquals("token", uploader.getUploadToken());
    }

    @Test
    public void testClone() throws Exception {
        BlazemeterUploader uploader = new BlazemeterUploader();
        BlazemeterUploaderGui gui = new BlazemeterUploaderGui();
        uploader.setGui(gui);
        BlazemeterUploader clone = (BlazemeterUploader) uploader.clone();
        Assert.assertEquals(gui, clone.gui);
    }
}