package kg.apc.jmeter.samplers;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.testelement.TestElement;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DummySamplerGuiTest {
    public DummySamplerGuiTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        DummySamplerGui instance = new DummySamplerGui();
        String result = instance.getStaticLabel();
        Assert.assertTrue(result.length() > 0);
    }

    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement element = new DummySampler();
        DummySamplerGui instance = new DummySamplerGui();
        instance.configure(element);
    }

    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        DummySamplerGui instance = new DummySamplerGui();
        TestElement result = instance.createTestElement();
        Assert.assertNotNull(result);
    }

    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement sampler = new DummySampler();
        DummySamplerGui instance = new DummySamplerGui();
        instance.modifyTestElement(sampler);
    }

    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        DummySamplerGui instance = new DummySamplerGui();
        instance.clearGui();
    }

    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        DummySamplerGui instance = new DummySamplerGui();
        String expResult = "DummySamplerGui";
        String result = instance.getLabelResource();
        Assert.assertEquals(expResult, result);
    }
}
