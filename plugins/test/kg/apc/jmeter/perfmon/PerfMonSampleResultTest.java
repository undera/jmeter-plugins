package kg.apc.jmeter.perfmon;

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
public class PerfMonSampleResultTest {

    public PerfMonSampleResultTest() {
    }

   @BeforeClass
   public static void setUpClass() throws Exception {
   }

   @AfterClass
   public static void tearDownClass() throws Exception {
   }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

   @Test
   public void testSomeMethod() {
     // fail("The test case is a prototype.");
   }

   @Test
   public void testSetValue() {
      System.out.println("setValue");
      long value = 0L;
      PerfMonSampleResult instance = new PerfMonSampleResult();
      instance.setValue(value);
   }

    /**
     * Test of getValue method, of class PerfMonSampleResult.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        PerfMonSampleResult instance = new PerfMonSampleResult();
        instance.setValue(123.0);
        double expResult = 123.0;
        double result = instance.getValue();
        assertEquals(expResult, result, 0.0);
    }
}