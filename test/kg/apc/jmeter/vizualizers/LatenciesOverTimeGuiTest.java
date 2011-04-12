/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.vizualizers;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author z000205
 */
public class LatenciesOverTimeGuiTest {

    public LatenciesOverTimeGuiTest() {
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
     * Test of getLabelResource method, of class LatenciesOverTimeGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        LatenciesOverTimeGui instance = new LatenciesOverTimeGui();
        String expResult = "LatenciesOverTimeGui";
        String result = instance.getLabelResource();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStaticLabel method, of class LatenciesOverTimeGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        LatenciesOverTimeGui instance = new LatenciesOverTimeGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of add method, of class LatenciesOverTimeGui.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        SampleResult res = new SampleResult();
        res.setAllThreads(1);
        res.setThreadName("test 1-2");
        LatenciesOverTimeGui instance = new LatenciesOverTimeGui();
        instance.add(res);
    }

    /**
     * Test of getSettingsPanel method, of class LatenciesOverTimeGui.
     */
    @Test
    public void testGetSettingsPanel() {
        System.out.println("getSettingsPanel");
        LatenciesOverTimeGui instance = new LatenciesOverTimeGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

    /**
     * Test of getWikiPage method, of class LatenciesOverTimeGui.
     */
    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        LatenciesOverTimeGui instance = new LatenciesOverTimeGui();
        String result = instance.getWikiPage();
        assertTrue(result.length() > 0);

    }

    /**
     * Test of createSettingsPanel method, of class LatenciesOverTimeGui.
     */
    @Test
    public void testCreateSettingsPanel() {
        System.out.println("createSettingsPanel");
        LatenciesOverTimeGui instance = new LatenciesOverTimeGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }
}
