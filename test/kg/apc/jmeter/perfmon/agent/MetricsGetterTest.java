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
      MetricsGetter result = MetricsGetter.getInstance();
      assertNotNull(result);
   }

   @Test
   public void testInitNetworkInterfaces()
   {
      System.out.println("initNetworkInterfaces");
      MetricsGetter instance = MetricsGetter.getInstance();
      instance.initNetworkInterfaces();
   }

   @Test
   public void testInitFileSystems()
   {
      System.out.println("initFileSystems");
      MetricsGetter instance = MetricsGetter.getInstance();
      instance.initFileSystems();
   }

   @Test
   public void testGetValues()
   {
      System.out.println("getValues");

      MetricsGetter instance = MetricsGetter.getInstance();
      String expResult = "badCmd";
      String result = instance.getValues("invalidCmd");
      assertEquals(expResult, result);
      result = instance.getValues("mem");
      System.out.println("memory: " + result);
      assertNotNull(result);
      //assume more than 100 mb ram (value is in bytes)
      assertTrue(!"badCmd".equals(result));
      assertTrue(result.length() > 5);
      
   }

}