package kg.apc.jmeter.vizualizers;

import java.util.concurrent.ConcurrentSkipListMap;
import javax.swing.JPanel;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.jmxmon.JMXMonCollector;
import kg.apc.jmeter.jmxmon.JMXMonSampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JMXMonGuiTest {

    private static class JMXMonGuiEmul
            extends JMXMonGui {

        public ConcurrentSkipListMap<String, AbstractGraphRow> getModel_multi() {
            return model;
        }

        public ConcurrentSkipListMap<String, AbstractGraphRow> getModel_aggr() {
            return modelAggregate;
        }
    }

    public JMXMonGuiTest() {
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
        JMXMonGui instance = new JMXMonGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        JMXMonGui instance = new JMXMonGui();
        String result = instance.getWikiPage();
        assertTrue(result.length() > 0);
    }

    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        JMXMonGui instance = new JMXMonGui();
        String result = instance.getLabelResource();
        assertTrue(result.length() > 0);
    }

    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        JMXMonGui instance = new JMXMonGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        JMXMonGui instance = new JMXMonGui();
        TestElement result = instance.createTestElement();
        assertTrue(result instanceof JMXMonCollector);
    }

    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement c = new JMXMonCollector();
        JMXMonGui instance = new JMXMonGui();
        instance.modifyTestElement(c);
    }

    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement el = new JMXMonCollector();
        JMXMonGui instance = new JMXMonGui();
        instance.configure(el);
    }

    @Test
    public void testAdd() {
        System.out.println("add");
        JMXMonSampleResult res = new JMXMonSampleResult();
        res.setSuccessful(true);
        res.setValue(1.0);
        JMXMonGuiEmul instance = new JMXMonGuiEmul();
        instance.add(res);
        assertEquals(1, instance.getModel_multi().size());
        assertEquals(1, instance.getModel_multi().firstEntry().getValue().size());
        assertEquals(0, instance.getModel_aggr().size());
    }

    /**
     * Test of getGraphPanelContainer method, of class JMXMonGui.
     */
    @Test
    public void testGetGraphPanelContainer() {
        System.out.println("getGraphPanelContainer");
        JMXMonGui instance = new JMXMonGui();
        JPanel result = instance.getGraphPanelContainer();
        assertNotNull(result);
    }

    /**
     * Test of clearErrorMessage method, of class JMXMonGui.
     */
    @Test
    public void testClearErrorMessage() {
        System.out.println("clearErrorMessage");
        JMXMonGui instance = new JMXMonGui();
        instance.clearErrorMessage();
    }

    /**
     * Test of clearData method, of class JMXMonGui.
     */
    @Test
    public void testClearData() {
        System.out.println("clearData");
        JMXMonGui instance = new JMXMonGui();
        instance.clearData();
    }
}
