/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.perfmon.agent;

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
public class MetricsGetterTest {

    public MetricsGetterTest() {
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
   public void testGetInstance()
   {
      System.out.println("getInstance");
      MetricsGetter expResult = null;
      MetricsGetter result = MetricsGetter.getInstance();
      assertEquals(expResult, result);
      fail("The test case is a prototype.");
   }

   @Test
   public void testInitNetworkInterfaces()
   {
      System.out.println("initNetworkInterfaces");
      MetricsGetter instance = null;
      instance.initNetworkInterfaces();
      fail("The test case is a prototype.");
   }

   @Test
   public void testInitFileSystems()
   {
      System.out.println("initFileSystems");
      MetricsGetter instance = null;
      instance.initFileSystems();
      fail("The test case is a prototype.");
   }

   @Test
   public void testGetValues()
   {
      System.out.println("getValues");
      String value = "";
      MetricsGetter instance = null;
      String expResult = "";
      String result = instance.getValues(value);
      assertEquals(expResult, result);
      fail("The test case is a prototype.");
   }

}