package com.blazemeter.jmeter;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.testelement.TestElement;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.Assert.*;

public class BlazemeterUploaderGuiTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

//    @Test
    public void displayGUI() throws InterruptedException {
        if (!GraphicsEnvironment.isHeadless()) {
            BlazemeterUploaderGui obj = new BlazemeterUploaderGui();
            TestElement te = obj.createTestElement();
            obj.configure(te);
            obj.clearGui();
            obj.modifyTestElement(te);

            JFrame frame = new JFrame(obj.getStaticLabel());
            frame.setPreferredSize(new Dimension(800, 600));
            frame.getContentPane().add(obj, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);

            while (frame.isVisible()) {
                Thread.sleep(1000);
            }
        }
    }

    @Test
    public void testGui() throws Exception {
        BlazemeterUploaderGui gui = new BlazemeterUploaderGui();

        assertEquals(BlazemeterUploaderGui.class.getCanonicalName(), gui.getLabelResource());
        assertEquals("bzm - BlazeMeter Uploader", gui.getStaticLabel());

        BlazemeterUploader element1 = (BlazemeterUploader) gui.createTestElement();
        BlazemeterUploader element2 = (BlazemeterUploader) gui.createTestElement();

        element1.setWorkspace("test_workspace");
        element1.setProject("test_project");
        element1.setTitle("test_title");
        element1.setUploadToken("test_token");
        element1.setAnonymousTest(true);
        element1.setShareTest(true);

        gui.configure(element1);
        gui.modifyTestElement(element2);

        assertEquals(element1.getWorkspace(), element2.getWorkspace());
        assertEquals(element1.getProject(), element2.getProject());
        assertEquals(element1.getTitle(), element2.getTitle());
        assertEquals(element1.getUploadToken(), element2.getUploadToken());
        assertEquals(element1.isAnonymousTest(), element2.isAnonymousTest());
        assertEquals(element1.isShareTest(), element2.isShareTest());

        gui.clearGui();
        gui.modifyTestElement(element2);

        assertEquals("", element2.getTitle());
        assertEquals("Default workspace", element2.getWorkspace());
        assertEquals("Default project", element2.getProject());
        assertEquals("", element2.getUploadToken());
        assertEquals(false, element2.isAnonymousTest());
        assertEquals(false, element2.isShareTest());
    }
}