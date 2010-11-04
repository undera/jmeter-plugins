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
 * @author z000205
 */
public class TransactionsPerSecondGuiTest {

    public TransactionsPerSecondGuiTest() {
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
     * Test of getLabelResource method, of class TransactionsPerSecondGui.
     */
    @Test
    public void testGetLabelResource()
    {
        System.out.println("getLabelResource");
        TransactionsPerSecondGui instance = new TransactionsPerSecondGui();
        String expResult = "TransactionsPerSecondGui";
        String result = instance.getLabelResource();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStaticLabel method, of class TransactionsPerSecondGui.
     */
    @Test
    public void testGetStaticLabel()
    {
        System.out.println("getStaticLabel");
        TransactionsPerSecondGui instance = new TransactionsPerSecondGui();
        String expResult = "Transactions per Second";
        String result = instance.getStaticLabel();
        assertEquals(expResult, result);
    }

    /**
     * Test of add method, of class TransactionsPerSecondGui.
     */
    @Test
    public void testAdd()
    {
        System.out.println("add");

        SampleResult res = new SampleResult();
        res.setAllThreads(1);
        res.setThreadName("test 1-2");
        TransactionsPerSecondGui instance = new TransactionsPerSecondGui();
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
      TransactionsPerSecondGui instance = new TransactionsPerSecondGui();
      JSettingsPanel result = instance.getSettingsPanel();
      assertNotNull(result);
   }

}