package kg.apc.jmeter.reporters;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

public class LoadosophiaUploaderGuiTest {


    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    //@Test
    public void displayGUI() throws InterruptedException {
        if (!GraphicsEnvironment.isHeadless()) {
            LoadosophiaUploaderGui obj = new LoadosophiaUploaderGui();
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
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        String result = instance.getStaticLabel();
        Assert.assertTrue(result.length() > 0);
    }

    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        String result = instance.getLabelResource();
        Assert.assertTrue(result.length() > 0);
    }

    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        TestElement result = instance.createTestElement();
        Assert.assertTrue(result instanceof LoadosophiaUploader);
    }

    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement te = new LoadosophiaUploader();
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        instance.modifyTestElement(te);
    }

    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement element = new LoadosophiaUploader();
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        instance.configure(element);
    }

    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        instance.clearGui();
    }

    @Test
    public void testAdd() {
        System.out.println("add");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        instance.add((SampleResult) null);
    }

    @Test
    public void testClearData() {
        System.out.println("clearData");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        instance.clearData();
    }

    @Test
    public void testInform() {
        System.out.println("inform");
        String string = "";
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        instance.inform(string);
    }

    @Test
    public void testIsStats() {
        System.out.println("isStats");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        boolean result = instance.isStats();
        Assert.assertEquals(false, result);
    }

    @Test
    public void testGetFilePanel() {
        System.out.println("getFilePanel");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        Component result = instance.getFilePanel();
        Assert.assertNotNull(result);
    }
}
