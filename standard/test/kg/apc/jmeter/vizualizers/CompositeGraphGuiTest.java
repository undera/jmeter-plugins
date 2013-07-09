package kg.apc.jmeter.vizualizers;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleResult;
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
public class CompositeGraphGuiTest {

    public CompositeGraphGuiTest() {
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
     * Test of getSettingsPanel method, of class CompositeGraphGui.
     */
    @Test
    public void testGetSettingsPanel() {
        System.out.println("getSettingsPanel");
        CompositeGraphGui instance = new CompositeGraphGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

    /**
     * Test of getLabelResource method, of class CompositeGraphGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        CompositeGraphGui instance = new CompositeGraphGui();
        String result = instance.getLabelResource();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of getStaticLabel method, of class CompositeGraphGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        CompositeGraphGui instance = new CompositeGraphGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of createTestElement method, of class CompositeGraphGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        CompositeGraphGui instance = new CompositeGraphGui();
        TestElement result = instance.createTestElement();
        assertTrue(result instanceof CompositeResultCollector);
    }

    /**
     * Test of configure method, of class CompositeGraphGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement te = new CompositeResultCollector();
        CompositeGraphGui instance = new CompositeGraphGui();
        instance.configure(te);
    }

    /**
     * Test of updateGui method, of class CompositeGraphGui.
     */
    @Test
    public void testUpdateGui() {
        System.out.println("updateGui");
        CompositeGraphGui instance = new CompositeGraphGui();
        instance.updateGui();
    }

    /**
     * Test of add method, of class CompositeGraphGui.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        SampleResult sr = new SampleResult();
        CompositeGraphGui instance = new CompositeGraphGui();
        instance.add(sr);
    }

    /**
     * Test of modifyTestElement method, of class CompositeGraphGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement te = new CompositeResultCollector();
        CompositeGraphGui instance = new CompositeGraphGui();
        instance.modifyTestElement(te);
    }

    /**
     * Test of getWikiPage method, of class CompositeGraphGui.
     */
    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        CompositeGraphGui instance = new CompositeGraphGui();
        String result = instance.getWikiPage();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of clearData method, of class CompositeGraphGui.
     */
    @Test
    public void testClearData() {
        System.out.println("clearData");
        CompositeGraphGui instance = new CompositeGraphGui();
        instance.clearData();
    }

    /**
     * Test of createSettingsPanel method, of class CompositeGraphGui.
     */
    @Test
    public void testCreateSettingsPanel() {
        System.out.println("createSettingsPanel");
        CompositeGraphGui instance = new CompositeGraphGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }
}
