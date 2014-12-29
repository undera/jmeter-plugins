package kg.apc.jmeter.vizualizers;

import java.util.concurrent.ConcurrentSkipListMap;
import javax.swing.JPanel;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.dbmon.DbMonCollector;
import kg.apc.jmeter.dbmon.DbMonSampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DbMonGuiTest {

    private static class DbMonGuiEmul
            extends DbMonGui {

        public ConcurrentSkipListMap<String, AbstractGraphRow> getModel_multi() {
            return model;
        }

        public ConcurrentSkipListMap<String, AbstractGraphRow> getModel_aggr() {
            return modelAggregate;
        }
    }

    public DbMonGuiTest() {
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
        DbMonGui instance = new DbMonGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        DbMonGui instance = new DbMonGui();
        String result = instance.getWikiPage();
        assertTrue(result.length() > 0);
    }

    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        DbMonGui instance = new DbMonGui();
        String result = instance.getLabelResource();
        assertTrue(result.length() > 0);
    }

    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        DbMonGui instance = new DbMonGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        DbMonGui instance = new DbMonGui();
        TestElement result = instance.createTestElement();
        assertTrue(result instanceof DbMonCollector);
    }

    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement c = new DbMonCollector();
        DbMonGui instance = new DbMonGui();
        instance.modifyTestElement(c);
    }

    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement el = new DbMonCollector();
        DbMonGui instance = new DbMonGui();
        instance.configure(el);
    }

    @Test
    public void testAdd() {
        System.out.println("add");
        DbMonSampleResult res = new DbMonSampleResult();
        res.setSuccessful(true);
        res.setValue(1.0);
        DbMonGuiEmul instance = new DbMonGuiEmul();
        instance.add(res);
        assertEquals(1, instance.getModel_multi().size());
        assertEquals(1, instance.getModel_multi().firstEntry().getValue().size());
        assertEquals(0, instance.getModel_aggr().size());
    }

    /**
     * Test of getGraphPanelContainer method, of class DbMonGui.
     */
    @Test
    public void testGetGraphPanelContainer() {
        System.out.println("getGraphPanelContainer");
        DbMonGui instance = new DbMonGui();
        JPanel result = instance.getGraphPanelContainer();
        assertNotNull(result);
    }

    /**
     * Test of clearErrorMessage method, of class DbMonGui.
     */
    @Test
    public void testClearErrorMessage() {
        System.out.println("clearErrorMessage");
        DbMonGui instance = new DbMonGui();
        instance.clearErrorMessage();
    }

    /**
     * Test of clearData method, of class DbMonGui.
     */
    @Test
    public void testClearData() {
        System.out.println("clearData");
        DbMonGui instance = new DbMonGui();
        instance.clearData();
    }
}
