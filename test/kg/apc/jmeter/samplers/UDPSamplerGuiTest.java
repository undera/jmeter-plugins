package kg.apc.jmeter.samplers;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.testelement.TestElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author undera
 */
public class UDPSamplerGuiTest {

    public UDPSamplerGuiTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getStaticLabel method, of class UDPSamplerGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        UDPSamplerGui instance = new UDPSamplerGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of getLabelResource method, of class UDPSamplerGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        UDPSamplerGui instance = new UDPSamplerGui();
        String result = instance.getLabelResource();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of configure method, of class UDPSamplerGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement element = new UDPSampler();
        UDPSamplerGui instance = new UDPSamplerGui();
        instance.configure(element);
    }

    /**
     * Test of createTestElement method, of class UDPSamplerGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        UDPSamplerGui instance = new UDPSamplerGui();
        TestElement result = instance.createTestElement();
        assertNotNull(result);
    }

    /**
     * Test of modifyTestElement method, of class UDPSamplerGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement sampler = new UDPSampler();
        UDPSamplerGui instance = new UDPSamplerGui();
        instance.modifyTestElement(sampler);
    }

    /**
     * Test of clearGui method, of class UDPSamplerGui.
     */
    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        UDPSamplerGui instance = new UDPSamplerGui();
        instance.clearGui();
    }
}
