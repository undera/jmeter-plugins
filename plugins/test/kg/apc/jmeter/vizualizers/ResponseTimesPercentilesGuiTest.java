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
 * @author Stéphane Hoblingre
 */
public class ResponseTimesPercentilesGuiTest {

    public ResponseTimesPercentilesGuiTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        TestJMeterUtils.createJmeterEnv();
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getLabelResource method, of class ResponseTimesPercentilesGUI.
     */
    @Test
    public void testGetLabelResource()
    {
        System.out.println("getLabelResource");
        ResponseTimesPercentilesGui instance = new ResponseTimesPercentilesGui();
        String expResult = "ResponseTimesPercentilesGui";
        String result = instance.getLabelResource();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStaticLabel method, of class ResponseTimesPercentilesGUI.
     */
    @Test
    public void testGetStaticLabel()
    {
        System.out.println("getStaticLabel");
        ResponseTimesPercentilesGui instance = new ResponseTimesPercentilesGui();
        String result = instance.getStaticLabel();
      assertTrue(result.length()>0);
    }

    /**
     * Test of add method, of class ResponseTimesPercentilesGUI.
     */
    @Test
    public void testAdd()
    {
        System.out.println("add");
        SampleResult res = new SampleResult();
        res.setAllThreads(1);
        res.setThreadName("test 1-2");
        ResponseTimesPercentilesGui instance = new ResponseTimesPercentilesGui();
        instance.add(res);
    }

    /**
     * Test of getSettingsPanel method, of class ResponseTimesPercentilesGUI.
     */
    @Test
    public void testGetSettingsPanel()
    {
        System.out.println("getSettingsPanel");
        ResponseTimesPercentilesGui instance = new ResponseTimesPercentilesGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

    /**
     * Test of getWikiPage method, of class ResponseTimesPercentilesGui.
     */
    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        ResponseTimesPercentilesGui instance = new ResponseTimesPercentilesGui();
        String expResult = "";
        String result = instance.getWikiPage();
        assertTrue(result.length()>0);
    }

    /**
     * Test of createSettingsPanel method, of class ResponseTimesPercentilesGui.
     */
    @Test
    public void testCreateSettingsPanel() {
        System.out.println("createSettingsPanel");
        ResponseTimesPercentilesGui instance = new ResponseTimesPercentilesGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

}