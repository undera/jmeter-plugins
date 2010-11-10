/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.util.TestJMeterUtils;
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
public class ResponseTimesPercentilesGUITest {

    public ResponseTimesPercentilesGUITest() {
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
        ResponseTimesPercentilesGUI instance = new ResponseTimesPercentilesGUI();
        String expResult = "ResponseTimesPercentilesGUI";
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
        ResponseTimesPercentilesGUI instance = new ResponseTimesPercentilesGUI();
        String expResult = "Response Times Percentiles";
        String result = instance.getStaticLabel();
        assertEquals(expResult, result);
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
        ResponseTimesPercentilesGUI instance = new ResponseTimesPercentilesGUI();
        instance.add(res);
    }

    /**
     * Test of getSettingsPanel method, of class ResponseTimesPercentilesGUI.
     */
    @Test
    public void testGetSettingsPanel()
    {
        System.out.println("getSettingsPanel");
        ResponseTimesPercentilesGUI instance = new ResponseTimesPercentilesGUI();
        JSettingsPanel result = instance.getSettingsPanel();
        assertNotNull(result);
    }

}