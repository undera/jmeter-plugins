package kg.apc.jmeter.modifiers;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.testelement.TestElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class FifoPutPostProcessorGuiTest {

    public FifoPutPostProcessorGuiTest() {
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
     * Test of getStaticLabel method, of class FifoPutPreProcessorGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        FifoPutPostProcessorGui instance = new FifoPutPostProcessorGui();
        String result = instance.getStaticLabel();
        assertNotNull(result);
    }

    /**
     * Test of getLabelResource method, of class FifoPutPreProcessorGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        FifoPutPostProcessorGui instance = new FifoPutPostProcessorGui();
        String result = instance.getLabelResource();
        assertNotNull(result);
    }

    /**
     * Test of configure method, of class FifoPutPreProcessorGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement element = new FifoPopPreProcessor();
        FifoPutPostProcessorGui instance = new FifoPutPostProcessorGui();
        instance.configure(element);
    }

    /**
     * Test of createTestElement method, of class FifoPutPreProcessorGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        FifoPutPostProcessorGui instance = new FifoPutPostProcessorGui();
        TestElement result = instance.createTestElement();
        assertNotNull(result);
    }

    /**
     * Test of modifyTestElement method, of class FifoPutPreProcessorGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement te = new FifoPutPostProcessor();
        FifoPutPostProcessorGui instance = new FifoPutPostProcessorGui();
        instance.modifyTestElement(te);
    }

    /**
     * Test of clearGui method, of class FifoPutPreProcessorGui.
     */
    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        FifoPutPostProcessorGui instance = new FifoPutPostProcessorGui();
        instance.clearGui();
    }
}
