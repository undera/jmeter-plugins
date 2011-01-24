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
 * @author z000205
 */
public class HitsPerSecondGuiTest {

    public HitsPerSecondGuiTest() {
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
     * Test of getLabelResource method, of class HitsPerSecondGui.
     */
    @Test
    public void testGetLabelResource()
    {
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
    public void testGetStaticLabel()
    {
        System.out.println("getStaticLabel");
        HitsPerSecondGui instance = new HitsPerSecondGui();
        String expResult = "Hits Per Second";
        String result = instance.getStaticLabel();
        assertEquals(expResult, result);
    }

    /**
     * Test of add method, of class HitsPerSecondGui.
     */
    @Test
    public void testAdd()
    {
        System.out.println("add");
        SampleResult res = new SampleResult();
        res.setAllThreads(1);
        res.setThreadName("test 1-2");
        HitsPerSecondGui instance = new HitsPerSecondGui();
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
     * Test of getSettingsPanel method, of class HitsPerSecondGui.
     */
    @Test
    public void testGetSettingsPanel()
    {
      System.out.println("getSettingsPanel");
      HitsPerSecondGui instance = new HitsPerSecondGui();
      JSettingsPanel result = instance.getSettingsPanel();
      assertNotNull(result);
    }

}