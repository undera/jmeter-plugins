package kg.apc.jmeter.modifiers;

import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.samplers.DummySampler;
import kg.apc.jmeter.samplers.DummySamplerGui;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.Assert.*;

public class DummySubPostProcessorGuiTest {
    @BeforeClass
    public static void setUpClass() {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void getters() throws InterruptedException {
        DummySubPostProcessorGui obj = new DummySubPostProcessorGui();
        Assert.assertEquals("jp@gc - Add Dummy Subresult", obj.getStaticLabel());
        Assert.assertEquals("DummySubPostProcessorGui", obj.getLabelResource());
    }

    @Test
    public void displayGUI() throws InterruptedException {
        if (!GraphicsEnvironment.isHeadless()) {
            DummySubPostProcessorGui obj = new DummySubPostProcessorGui();
            DummySubPostProcessor te = (DummySubPostProcessor) obj.createTestElement();
            obj.configure(te);
            obj.clearGui();
            obj.modifyTestElement(te);

            JFrame frame = new JFrame(obj.getStaticLabel());

            frame.setPreferredSize(new Dimension(1024, 768));
            frame.getContentPane().add(obj, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);

            while (frame.isVisible()) {
                Thread.sleep(1000);
            }
        }
    }
}