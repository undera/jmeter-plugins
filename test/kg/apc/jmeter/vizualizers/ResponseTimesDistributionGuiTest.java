/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.vizualizers;

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
      String expResult = "";
      String result = instance.getLabelResource();
      assertEquals(expResult, result);
      fail("The test case is a prototype.");
   }

   @Test
   public void testGetStaticLabel()
   {
      System.out.println("getStaticLabel");
      ResponseTimesDistributionGui instance = new ResponseTimesDistributionGui();
      String expResult = "";
      String result = instance.getStaticLabel();
      assertEquals(expResult, result);
      fail("The test case is a prototype.");
   }

   @Test
   public void testAdd()
   {
      System.out.println("add");
      SampleResult res = null;
      ResponseTimesDistributionGui instance = new ResponseTimesDistributionGui();
      instance.add(res);
      fail("The test case is a prototype.");
   }

   @Test
   public void testGetSettingsPanel()
   {
      System.out.println("getSettingsPanel");
      ResponseTimesDistributionGui instance = new ResponseTimesDistributionGui();
      JSettingsPanel expResult = null;
      JSettingsPanel result = instance.getSettingsPanel();
      assertEquals(expResult, result);
      fail("The test case is a prototype.");
   }

}