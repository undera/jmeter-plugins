package kg.apc.jmeter.modifiers;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.testelement.TestElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class FifoPopPreProcessorGuiTest {

    public FifoPopPreProcessorGuiTest() {
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
     * Test of getStaticLabel method, of class FifoPopPreProcessorGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        FifoPopPreProcessorGui instance = new FifoPopPreProcessorGui();
        String result = instance.getStaticLabel();
        assertNotNull(result);
    }

    /**
     * Test of getLabelResource method, of class FifoPopPreProcessorGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        FifoPopPreProcessorGui instance = new FifoPopPreProcessorGui();
        String result = instance.getLabelResource();
        assertNotNull(result);
    }

    /**
     * Test of configure method, of class FifoPopPreProcessorGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement element = new FifoPopPreProcessor();
        FifoPopPreProcessorGui instance = new FifoPopPreProcessorGui();
        instance.configure(element);
    }

    /**
     * Test of createTestElement method, of class FifoPopPreProcessorGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        FifoPopPreProcessorGui instance = new FifoPopPreProcessorGui();
        TestElement result = instance.createTestElement();
        assertNotNull(result);
    }

    /**
     * Test of modifyTestElement method, of class FifoPopPreProcessorGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement te = new FifoPopPreProcessor();
        FifoPopPreProcessorGui instance = new FifoPopPreProcessorGui();
        instance.modifyTestElement(te);
    }

    /**
     * Test of clearGui method, of class FifoPopPreProcessorGui.
     */
    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        FifoPopPreProcessorGui instance = new FifoPopPreProcessorGui();
        instance.clearGui();
    }
}
