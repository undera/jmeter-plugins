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
 * @author Stephane Hoblingre
 */
public class TotalTransactionsPerSecondGuiTest
{

    public TotalTransactionsPerSecondGuiTest()
    {
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
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of getLabelResource method, of class TotalTransactionsPerSecondGui.
     */
    @Test
    public void testGetLabelResource()
    {
        System.out.println("getLabelResource");
        TotalTransactionsPerSecondGui instance = new TotalTransactionsPerSecondGui();
        String expResult = "TotalTransactionsPerSecondGui";
        String result = instance.getLabelResource();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStaticLabel method, of class TotalTransactionsPerSecondGui.
     */
    @Test
    public void testGetStaticLabel()
    {
        System.out.println("getStaticLabel");
        TotalTransactionsPerSecondGui instance = new TotalTransactionsPerSecondGui();
        String expResult = "Total Transactions Per Second (obsolete)";
        String result = instance.getStaticLabel();
        assertEquals(expResult, result);
    }

    /**
     * Test of add method, of class TotalTransactionsPerSecondGui.
     */
    @Test
    public void testAdd()
    {
        System.out.println("add");
        SampleResult res = new SampleResult();
        res.setAllThreads(1);
        res.setThreadName("test 1-2");
        TotalTransactionsPerSecondGui instance = new TotalTransactionsPerSecondGui();
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

   @Test
   public void testGetSettingsPanel()
   {
      System.out.println("getSettingsPanel");
      TotalTransactionsPerSecondGui instance = new TotalTransactionsPerSecondGui();
      JSettingsPanel result = instance.getSettingsPanel();
      assertNotNull(result);
   }
}
