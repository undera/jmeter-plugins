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
 * @author APC
 */
public class ResponseTimesDistributionGuiTest {

    public ResponseTimesDistributionGuiTest() {
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

   @Test
   public void testGetLabelResource()
   {
      System.out.println("getLabelResource");
      ResponseTimesDistributionGui instance = new ResponseTimesDistributionGui();
      String expResult = "ResponseTimesDistributionGui";
      String result = instance.getLabelResource();
      assertEquals(expResult, result);
   }

   @Test
   public void testGetStaticLabel()
   {
      System.out.println("getStaticLabel");
      ResponseTimesDistributionGui instance = new ResponseTimesDistributionGui();
      String result = instance.getStaticLabel();
      assertTrue(result.length()>0);
   }

   @Test
   public void testAdd()
   {
      System.out.println("add");
      SampleResult res = new SampleResult();
      res.setAllThreads(1);
      res.setThreadName("test 1-2");
      res.setStampAndTime(System.currentTimeMillis(), 1000);
      ResponseTimesDistributionGui instance = new ResponseTimesDistributionGui();
      instance.add(res);
   }

   @Test
   public void testGetSettingsPanel()
   {
      System.out.println("getSettingsPanel");
      ResponseTimesDistributionGui instance = new ResponseTimesDistributionGui();
      JSettingsPanel result = instance.createSettingsPanel();
      assertNotNull(result);
   }

    /**
     * Test of getWikiPage method, of class ResponseTimesDistributionGui.
     */
    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        ResponseTimesDistributionGui instance = new ResponseTimesDistributionGui();
        String expResult = "";
        String result = instance.getWikiPage();
        assertTrue(result.length()>0);
    }

    /**
     * Test of createSettingsPanel method, of class ResponseTimesDistributionGui.
     */
    @Test
    public void testCreateSettingsPanel() {
        System.out.println("createSettingsPanel");
        ResponseTimesDistributionGui instance = new ResponseTimesDistributionGui();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

}