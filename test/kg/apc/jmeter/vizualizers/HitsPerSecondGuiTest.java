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
public class HitsPerSecondGuiTest {

    public HitsPerSecondGuiTest() {
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
     * Test of getLabelResource method, of class HitsPerSecondGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        HitsPerSecondGui instance = new HitsPerSecondGui();
        String expResult = "HitsPerSecondGui";
        String result = instance.getLabelResource();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStaticLabel method, of class HitsPerSecondGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        HitsPerSecondGui instance = new HitsPerSecondGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of add method, of class HitsPerSecondGui.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        SampleResult res = new SampleResult();
        res.setAllThreads(1);
        res.setThreadName("test 1-2");
        HitsPerSecondGui instance = new HitsPerSecondGui();
        instance.add(res);
        res.sampleStart();
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
        }
        res.sampleEnd();
        instance.add(res);
    }

    @Test
    public void testAdd_issue48() {
        System.out.println("add48");
        SampleResult res = new SampleResult();
        res.setAllThreads(0);
        res.setThreadName("test 1-2");
        res.setResponseMessage(null);

        SampleResult subres = new SampleResult();
        subres.sampleStart();
        subres.sampleEnd();
        res.sampleStart();
        res.addSubResult(subres);

        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
        }
        HitsPerSecondGui instance = new HitsPerSecondGui();
        instance.add(res);
    }

    /**
     * Test of getSettingsPanel method, of class HitsPerSecondGui.
     */
    @Test
    public void testGetSettingsPanel() {
        System.out.println("getSettingsPanel");
        HitsPerSecondGui instance = new HitsPerSecondGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

    /**
     * Test of getWikiPage method, of class HitsPerSecondGui.
     */
    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        HitsPerSecondGui instance = new HitsPerSecondGui();
        String result = instance.getWikiPage();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of createSettingsPanel method, of class HitsPerSecondGui.
     */
    @Test
    public void testCreateSettingsPanel() {
        System.out.println("createSettingsPanel");
        HitsPerSecondGui instance = new HitsPerSecondGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }
}