package kg.apc.jmeter.threads;

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
 * @author apc
 */
public class SteppingThreadGroupGuiTest {

    /**
     *
     */
    public SteppingThreadGroupGuiTest() {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of init method, of class SteppingThreadGroupGui.
     */
    @Test
    public void testInit() {
        System.out.println("init");
        SteppingThreadGroupGui instance = new SteppingThreadGroupGui();
        instance.init();
    }

    /**
     * Test of getLabelResource method, of class SteppingThreadGroupGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        SteppingThreadGroupGui instance = new SteppingThreadGroupGui();
        String result = instance.getLabelResource();
        assertNotNull(result);
    }

    /**
     * Test of getStaticLabel method, of class SteppingThreadGroupGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        SteppingThreadGroupGui instance = new SteppingThreadGroupGui();
        String result = instance.getStaticLabel();
        assertNotNull(result);
    }

    /**
     * Test of createTestElement method, of class SteppingThreadGroupGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        SteppingThreadGroupGui instance = new SteppingThreadGroupGui();
        TestElement expResult = new SteppingThreadGroup();
        TestElement result = instance.createTestElement();
        assertEquals(expResult.getClass().getName(), result.getClass().getName());
    }

    /**
     * Test of modifyTestElement method, of class SteppingThreadGroupGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        SteppingThreadGroup tg = new SteppingThreadGroup();
        tg.setNumThreads(100);
        SteppingThreadGroupGui instance = new SteppingThreadGroupGui();
        instance.modifyTestElement(tg);
    }

    /**
     * Test of configure method, of class SteppingThreadGroupGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement tg = new SteppingThreadGroup();
        SteppingThreadGroupGui instance = new SteppingThreadGroupGui();
        instance.createTestElement();
        instance.configure(tg);
    }

    /**
     * Test of clearGui method, of class SteppingThreadGroupGui.
     */
    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        SteppingThreadGroupGui instance = new SteppingThreadGroupGui();
        instance.clearGui();
    }
}
