package kg.apc.jmeter.reporters;

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
public class AutoStopGuiTest {

    public AutoStopGuiTest() {
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
     * Test of getStaticLabel method, of class AutoStopGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        AutoStopGui instance = new AutoStopGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length()>0);
    }

    /**
     * Test of getLabelResource method, of class AutoStopGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        AutoStopGui instance = new AutoStopGui();
        String result = instance.getLabelResource();
        assertTrue(result.length()>0);
    }

    /**
     * Test of createTestElement method, of class AutoStopGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        AutoStopGui instance = new AutoStopGui();
        TestElement result = instance.createTestElement();
        assertTrue(result instanceof AutoStop);
    }

    /**
     * Test of modifyTestElement method, of class AutoStopGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement te = new AutoStop();
        AutoStopGui instance = new AutoStopGui();
        instance.modifyTestElement(te);
    }

    /**
     * Test of configure method, of class AutoStopGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement element = new AutoStop();
        AutoStopGui instance = new AutoStopGui();
        instance.configure(element);
    }

    /**
     * Test of clearGui method, of class AutoStopGui.
     */
    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        AutoStopGui instance = new AutoStopGui();
        instance.clearGui();
    }
}