package kg.apc.jmeter.vizualizers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import kg.apc.charting.GraphPanelChart;
import kg.apc.emulators.TestJMeterUtils;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SynthesisReportGuiTest {

    public SynthesisReportGuiTest() {
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
     * Test of getLabelResource method, of class SynthesisReportGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        SynthesisReportGui instance = new SynthesisReportGui();
        String expResult = "SynthesisReportGui";
        String result = instance.getLabelResource();
        assertEquals(expResult, result);
    }

    /**
     * Test of add method, of class SynthesisReportGui.
     */
    @Test
    public void testAdd() throws IOException {
        System.out.println("add");
        SynthesisReportGui instance = new SynthesisReportGui();

        SampleResult res = new SampleResult();
        instance.add(res);

        SampleResult res2 = new SampleResult();
        instance.add(res2);

        ResultCollector rc = new ResultCollector();
        rc.setListener(instance);
        rc.sampleOccurred(new SampleEvent(res, ""));
        rc.sampleOccurred(new SampleEvent(res2, ""));

        File f = File.createTempFile("test", ".csv");
        instance.getGraphPanelChart().saveGraphToCSV(f);
    }

    /**
     * Test of clearData method, of class SynthesisReportGui.
     */
    @Test
    public void testClearData() {
        System.out.println("clearData");
        SynthesisReportGui instance = new SynthesisReportGui();
        instance.clearData();
    }

    /**
     * Test of modifyTestElement method, of class SynthesisReportGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement c = new ResultCollector();
        SynthesisReportGui instance = new SynthesisReportGui();
        instance.modifyTestElement(c);
    }

    /**
     * Test of configure method, of class SynthesisReportGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement c = new CorrectedResultCollector();
        SynthesisReportGui instance = new SynthesisReportGui();
        instance.configure(c);
    }

    /**
     * Test of configure method, of class SynthesisReportGui.
     */
    @Test
    public void testConfigure_NotEmptyFields() {
        System.out.println("configure");
        TestElement c = new CorrectedResultCollector();
        SynthesisReportGui instance = new SynthesisReportGui();
        c.setProperty(CorrectedResultCollector.START_OFFSET, "180");
        c.setProperty(CorrectedResultCollector.END_OFFSET, "360");
        instance.configure(c);
    }

    /**
     * Test of getSettingsPanel method, of class SynthesisReportGui.
     */
    @Test
    public void testGetSettingsPanel() {
        System.out.println("getSettingsPanel");
        SynthesisReportGui instance = new SynthesisReportGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

    /**
     * Test of getWikiPage method, of class SynthesisReportGui.
     */
    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        SynthesisReportGui instance = new SynthesisReportGui();
        String expResult = "SynthesisReport";
        String result = instance.getWikiPage();
        assertEquals(expResult, result);
    }

    /**
     * Test of getGraphPanelChart method, of class SynthesisReportGui.
     */
    @Test
    public void testGetGraphPanelChart() {
        System.out.println("getGraphPanelChart");
        SynthesisReportGui instance = new SynthesisReportGui();
        GraphPanelChart result = instance.getGraphPanelChart();
        assertNotNull(result);
    }

    /**
     * Test of createSettingsPanel method, of class SynthesisReportGui.
     */
    @Test
    public void testCreateSettingsPanel() {
        System.out.println("createSettingsPanel");
        SynthesisReportGui instance = new SynthesisReportGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

    /**
     * Test of getMenuCategories method, of class SynthesisReportGui.
     */
    @Test
    public void testGetMenuCategories() {
        System.out.println("getMenuCategories");
        SynthesisReportGui instance = new SynthesisReportGui();
        Collection result = instance.getMenuCategories();
        assertTrue(!result.isEmpty());
    }

    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        SynthesisReportGui instance = new SynthesisReportGui();
        String expResult = "jp@gc - Synthesis Report (filtered)";
        String result = instance.getStaticLabel();
        assertEquals(expResult, result);
    }

    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        SynthesisReportGui instance = new SynthesisReportGui();
        instance.clearGui();
    }
}
