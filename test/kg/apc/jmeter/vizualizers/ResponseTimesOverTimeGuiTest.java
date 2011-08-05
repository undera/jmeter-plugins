package kg.apc.jmeter.vizualizers;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apc
 */
public class ResponseTimesOverTimeGuiTest {

    /**
     * 
     */
    public ResponseTimesOverTimeGuiTest() {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    /**
     *
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        ResponseTimesOverTimeGui instance = new ResponseTimesOverTimeGui();
        String expResult = "ResponseTimesOverTimeGui";
        String result = instance.getLabelResource();
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        ResponseTimesOverTimeGui instance = new ResponseTimesOverTimeGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    /**
     *
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        SampleResult res = new SampleResult();
        res.setAllThreads(1);
        res.setThreadName("test 1-2");
        ResponseTimesOverTimeGui instance = new ResponseTimesOverTimeGui();
        instance.add(res);
    }

    @Test
    public void testGetSettingsPanel() {
        System.out.println("getSettingsPanel");
        ResponseTimesOverTimeGui instance = new ResponseTimesOverTimeGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

    /**
     * Test of getWikiPage method, of class ResponseTimesOverTimeGui.
     */
    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        ResponseTimesOverTimeGui instance = new ResponseTimesOverTimeGui();
        String expResult = "";
        String result = instance.getWikiPage();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of createSettingsPanel method, of class ResponseTimesOverTimeGui.
     */
    @Test
    public void testCreateSettingsPanel() {
        System.out.println("createSettingsPanel");
        ResponseTimesOverTimeGui instance = new ResponseTimesOverTimeGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }
}
