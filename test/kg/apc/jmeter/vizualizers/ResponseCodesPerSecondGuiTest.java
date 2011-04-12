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
 * @author Stephane Hoblingre
 */
public class ResponseCodesPerSecondGuiTest {

    public ResponseCodesPerSecondGuiTest() {
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
     * Test of getLabelResource method, of class ResponseCodesPerSecondGui.
     */
    @Test
    public void testGetLabelResource()
    {
        System.out.println("getLabelResource");
        ResponseCodesPerSecondGui instance = new ResponseCodesPerSecondGui();
        String expResult = "ResponseCodesPerSecondGui";
        String result = instance.getLabelResource();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStaticLabel method, of class ResponseCodesPerSecondGui.
     */
    @Test
    public void testGetStaticLabel()
    {
        System.out.println("getStaticLabel");
        ResponseCodesPerSecondGui instance = new ResponseCodesPerSecondGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length()>0);
    }

    /**
     * Test of add method, of class ResponseCodesPerSecondGui.
     */
    @Test
    public void testAdd()
    {
        System.out.println("add");
        SampleResult res = new SampleResult();
        res.setAllThreads(1);
        res.setThreadName("test 1-2");
        res.setResponseCode("200");
        ResponseCodesPerSecondGui instance = new ResponseCodesPerSecondGui();
        instance.add(res);
        res.sampleStart();
        try
        {
            Thread.sleep(10);
        } catch (InterruptedException ex)
        {
        }
        res.sampleEnd();
        instance.add(res);
    }

    /**
     * Test of getSettingsPanel method, of class ResponseCodesPerSecondGui.
     */
    @Test
    public void testGetSettingsPanel()
    {
        System.out.println("getSettingsPanel");
        ResponseCodesPerSecondGui instance = new ResponseCodesPerSecondGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

    /**
     * Test of clearData method, of class ResponseCodesPerSecondGui.
     */
    @Test
    public void testClearData() {
        System.out.println("clearData");
        ResponseCodesPerSecondGui instance = new ResponseCodesPerSecondGui();
        instance.clearData();
    }

    /**
     * Test of getWikiPage method, of class ResponseCodesPerSecondGui.
     */
    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        ResponseCodesPerSecondGui instance = new ResponseCodesPerSecondGui();
        String expResult = "";
        String result = instance.getWikiPage();
        assertTrue(result.length()>0);
    }

    /**
     * Test of createSettingsPanel method, of class ResponseCodesPerSecondGui.
     */
    @Test
    public void testCreateSettingsPanel() {
        System.out.println("createSettingsPanel");
        ResponseCodesPerSecondGui instance = new ResponseCodesPerSecondGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

}