package kg.apc.jmeter.perfmon.agent;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author APC
 */
public class MetricsGetterTest extends TestCase{

    public MetricsGetterTest() {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(MetricsGetterTest.class);
        return suite;
    }

   public void testGetInstance()
   {
      System.out.println("getInstance");
      MetricsGetter result = MetricsGetter.getInstance();
      assertNotNull(result);
   }

   public void testInitNetworkInterfaces()
   {
      System.out.println("initNetworkInterfaces");
      MetricsGetter instance = MetricsGetter.getInstance();
      instance.initNetworkInterfaces();
   }

   public void testInitFileSystems()
   {
      System.out.println("initFileSystems");
      MetricsGetter instance = MetricsGetter.getInstance();
      instance.initFileSystems();
   }

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

    /**
     * Test of setPidToMonitor method, of class MetricsGetter.
     */
    public void testSetPidToMonitor() {
        System.out.println("setPidToMonitor");
        long pid = 1234L;
        MetricsGetter instance = MetricsGetter.getInstance();
        instance.setPidToMonitor(pid);
    }

    /**
     * Test of isPidFound method, of class MetricsGetter.
     */
    public void testIsPidFound() {
        System.out.println("isPidFound");
        long pid = 0L;
        MetricsGetter instance = MetricsGetter.getInstance();
        boolean expResult = false;
        boolean result = instance.isPidFound(pid);
        assertEquals(expResult, result);
    }
}