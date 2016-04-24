package kg.apc.jmeter.vizualizers;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.junit.*;

import javax.swing.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PageDataExtractorOverTimeGuiTest {

    public PageDataExtractorOverTimeGuiTest() {
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
     * Test of createSettingsPanel method, of class PageDataExtractorOverTimeGui.
     */
    @Test
    public void testCreateSettingsPanel() {
        System.out.println("createSettingsPanel");
        PageDataExtractorOverTimeGui instance = new PageDataExtractorOverTimeGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

    /**
     * Test of getStaticLabel method, of class PageDataExtractorOverTimeGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        PageDataExtractorOverTimeGui instance = new PageDataExtractorOverTimeGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of getLabelResource method, of class PageDataExtractorOverTimeGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        PageDataExtractorOverTimeGui instance = new PageDataExtractorOverTimeGui();
        String result = instance.getLabelResource();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of getWikiPage method, of class PageDataExtractorOverTimeGui.
     */
    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        PageDataExtractorOverTimeGui instance = new PageDataExtractorOverTimeGui();
        String result = instance.getWikiPage();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of getGraphPanelContainer method, of class PageDataExtractorOverTimeGui.
     */
    @Test
    public void testGetGraphPanelContainer() {
        System.out.println("getGraphPanelContainer");
        PageDataExtractorOverTimeGui instance = new PageDataExtractorOverTimeGui();
        JPanel result = instance.getGraphPanelContainer();
        assertNotNull(result);
    }

    /**
     * Test of createTestElement method, of class PageDataExtractorOverTimeGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        PageDataExtractorOverTimeGui instance = new PageDataExtractorOverTimeGui();
        TestElement result = instance.createTestElement();
        assertTrue(result instanceof ResultCollector);
    }

    /**
     * Test of modifyTestElement method, of class PageDataExtractorOverTimeGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement te = new ResultCollector();
        PageDataExtractorOverTimeGui instance = new PageDataExtractorOverTimeGui();
        instance.modifyTestElement(te);
    }

    /**
     * Test of configure method, of class PageDataExtractorOverTimeGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement te = new CorrectedResultCollector();
        PageDataExtractorOverTimeGui instance = new PageDataExtractorOverTimeGui();
        instance.configure(te);
    }

    /**
     * Test of add method, of class PageDataExtractorOverTimeGui.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        SampleResult res = new SampleResult();
        res.setAllThreads(1);
        res.setThreadName("test 1-2");
        res.setResponseData("aa=10\nbb=20");
        PageDataExtractorOverTimeGui instance = new PageDataExtractorOverTimeGui();
        instance.add(res);
    }

    /**
     * Test of clearData method, of class PageDataExtractorOverTimeGui.
     */
    @Test
    public void testClearData() {
        System.out.println("clearData");
        PageDataExtractorOverTimeGui instance = new PageDataExtractorOverTimeGui();
        instance.clearData();
    }
}