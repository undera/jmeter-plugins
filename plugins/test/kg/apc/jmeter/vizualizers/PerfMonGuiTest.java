package kg.apc.jmeter.vizualizers;

import java.util.concurrent.ConcurrentSkipListMap;
import javax.swing.JPanel;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.jmeter.perfmon.PerfMonCollector;
import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.perfmon.PerfMonSampleResult;
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
 * @author APC
 */
public class PerfMonGuiTest {

    private static class PerfMonGuiEmul
            extends PerfMonGui {

        public ConcurrentSkipListMap<String, AbstractGraphRow> getModel_multi() {
            return model;
        }

        public ConcurrentSkipListMap<String, AbstractGraphRow> getModel_aggr() {
            return modelAggregate;
        }
    }

    public PerfMonGuiTest() {
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

    @Test
    public void testCreateSettingsPanel() {
        System.out.println("createSettingsPanel");
        PerfMonGui instance = new PerfMonGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        PerfMonGui instance = new PerfMonGui();
        String result = instance.getWikiPage();
        assertTrue(result.length() > 0);
    }

    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        PerfMonGui instance = new PerfMonGui();
        String result = instance.getLabelResource();
        assertTrue(result.length() > 0);
    }

    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        PerfMonGui instance = new PerfMonGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        PerfMonGui instance = new PerfMonGui();
        TestElement result = instance.createTestElement();
        assertTrue(result instanceof PerfMonCollector);
    }

    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement c = new PerfMonCollector();
        PerfMonGui instance = new PerfMonGui();
        instance.modifyTestElement(c);
    }

    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement el = new PerfMonCollector();
        PerfMonGui instance = new PerfMonGui();
        instance.configure(el);
    }

    @Test
    public void testAdd() {
        System.out.println("add");
        SampleResult res = new PerfMonSampleResult();
        res.setSuccessful(true);
        PerfMonGuiEmul instance = new PerfMonGuiEmul();
        instance.add(res);
        assertEquals(1, instance.getModel_multi().size());
        assertEquals(1, instance.getModel_multi().firstEntry().getValue().size());
        assertEquals(0, instance.getModel_aggr().size());
    }

    @Test
    public void testWork() throws InterruptedException {
        System.out.println("work");
        PerfMonGui instance = new PerfMonGui();
        PerfMonCollector collector = (PerfMonCollector) instance.createTestElement();
        //collector.setListener(instance);
        collector.testStarted();
        collector.testIterationStart(null);
        Thread.sleep(2000);
        collector.testEnded();
        //assertEquals(5, instance.model.firstEntry().getValue().size());
    }

    /**
     * Test of getGraphPanelContainer method, of class PerfMonGui.
     */
    @Test
    public void testGetGraphPanelContainer() {
        System.out.println("getGraphPanelContainer");
        PerfMonGui instance = new PerfMonGui();
        JPanel result = instance.getGraphPanelContainer();
        assertNotNull(result);
    }

    /**
     * Test of clearErrorMessage method, of class PerfMonGui.
     */
    @Test
    public void testClearErrorMessage() {
        System.out.println("clearErrorMessage");
        PerfMonGui instance = new PerfMonGui();
        instance.clearErrorMessage();
    }

    /**
     * Test of clearData method, of class PerfMonGui.
     */
    @Test
    public void testClearData() {
        System.out.println("clearData");
        PerfMonGui instance = new PerfMonGui();
        instance.clearData();
    }
}
