package kg.apc.jmeter.vizualizers;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import kg.apc.emulators.TestJMeterUtils;
import kg.apc.charting.GraphPanelChart;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class AggregateReportGuiTest {

    public AggregateReportGuiTest() {
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
     * Test of getLabelResource method, of class StatVisualizerAccessorGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        AggregateReportGui instance = new AggregateReportGui();
        String result = instance.getLabelResource();
    }

    /**
     * Test of add method, of class StatVisualizerAccessorGui.
     */
    @Test
    public void testAdd() throws IOException {
        System.out.println("add");
        AggregateReportGui instance = new AggregateReportGui();

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
     * Test of clearData method, of class StatVisualizerAccessorGui.
     */
    @Test
    public void testClearData() {
        System.out.println("clearData");
        AggregateReportGui instance = new AggregateReportGui();
        instance.clearData();
    }

    /**
     * Test of modifyTestElement method, of class StatVisualizerAccessorGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement c = new ResultCollector();
        AggregateReportGui instance = new AggregateReportGui();
        instance.modifyTestElement(c);
    }

    /**
     * Test of configure method, of class StatVisualizerAccessorGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement c = new ResultCollector();
        AggregateReportGui instance = new AggregateReportGui();
        instance.configure(c);
    }

    /**
     * Test of getSettingsPanel method, of class StatVisualizerAccessorGui.
     */
    @Test
    public void testGetSettingsPanel() {
        System.out.println("getSettingsPanel");
        AggregateReportGui instance = new AggregateReportGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

    /**
     * Test of getWikiPage method, of class StatVisualizerAccessorGui.
     */
    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        AggregateReportGui instance = new AggregateReportGui();
        String result = instance.getWikiPage();
        assertNotNull(result);
    }

    /**
     * Test of getGraphPanelChart method, of class StatVisualizerAccessorGui.
     */
    @Test
    public void testGetGraphPanelChart() {
        System.out.println("getGraphPanelChart");
        AggregateReportGui instance = new AggregateReportGui();
        GraphPanelChart result = instance.getGraphPanelChart();
        assertNotNull(result);
    }

    /**
     * Test of createSettingsPanel method, of class AggregateReportGui.
     */
    @Test
    public void testCreateSettingsPanel() {
        System.out.println("createSettingsPanel");
        AggregateReportGui instance = new AggregateReportGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

    /**
     * Test of getMenuCategories method, of class AggregateReportGui.
     */
    @Test
    public void testGetMenuCategories() {
        System.out.println("getMenuCategories");
        AggregateReportGui instance = new AggregateReportGui();
        Collection result = instance.getMenuCategories();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        AggregateReportGui instance = new AggregateReportGui();
        String result = instance.getStaticLabel();
        assertNotNull(result);
    }
}
