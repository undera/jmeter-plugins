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
public class HTTPRawSamplerGuiTest {

    public HTTPRawSamplerGuiTest() {
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
     * Test of getStaticLabel method, of class HTTPRawSamplerGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        HTTPRawSamplerGui instance = new HTTPRawSamplerGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of configure method, of class HTTPRawSamplerGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        HTTPRawSampler sampler = new HTTPRawSampler();
        HTTPRawSamplerGui instance = new HTTPRawSamplerGui();
        instance.configure(sampler);
    }

    /**
     * Test of createTestElement method, of class HTTPRawSamplerGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        HTTPRawSamplerGui instance = new HTTPRawSamplerGui();
        TestElement result = instance.createTestElement();
        assertTrue(result instanceof HTTPRawSampler);
    }

    /**
     * Test of modifyTestElement method, of class HTTPRawSamplerGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        HTTPRawSampler sampler = new HTTPRawSampler();
        HTTPRawSamplerGui instance = new HTTPRawSamplerGui();
        instance.modifyTestElement(sampler);
        assertEquals("localhost", sampler.getHostName());
    }

    /**
     * Test of clearGui method, of class HTTPRawSamplerGui.
     */
    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        HTTPRawSamplerGui instance = new HTTPRawSamplerGui();
        instance.clearGui();
    }

    /**
     * Test of getLabelResource method, of class HTTPRawSamplerGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        HTTPRawSamplerGui instance = new HTTPRawSamplerGui();
        String result = instance.getLabelResource();
        assertTrue(result.length() > 0);
    }
}
