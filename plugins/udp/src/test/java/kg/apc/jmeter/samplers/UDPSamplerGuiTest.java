package kg.apc.jmeter.samplers;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.testelement.TestElement;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UDPSamplerGuiTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    //@Test
    public void displayGUI() throws InterruptedException {
        if (!GraphicsEnvironment.isHeadless()) {
            UDPSamplerGui obj = new UDPSamplerGui();
            UDPSampler te = (UDPSampler) obj.createTestElement();
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
        UDPSamplerGui instance = new UDPSamplerGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        UDPSamplerGui instance = new UDPSamplerGui();
        String result = instance.getLabelResource();
        assertTrue(result.length() > 0);
    }

    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement element = new UDPSampler();
        UDPSamplerGui instance = new UDPSamplerGui();
        instance.configure(element);
    }

    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        UDPSamplerGui instance = new UDPSamplerGui();
        TestElement result = instance.createTestElement();
        assertNotNull(result);
    }

    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement sampler = new UDPSampler();
        UDPSamplerGui instance = new UDPSamplerGui();
        instance.modifyTestElement(sampler);
    }

    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        UDPSamplerGui instance = new UDPSamplerGui();
        instance.clearGui();
    }
}
