package kg.apc.jmeter.perfmon;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
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
public class PerfMonCollectorTest {

    public PerfMonCollectorTest() {
    }

   @BeforeClass
   public static void setUpClass() throws Exception {
      TestJMeterUtils.createJmeterEnv();
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
   public void testSetData() {
      System.out.println("setData");
      CollectionProperty rows = new CollectionProperty();
      PerfMonCollector instance = new PerfMonCollector();
      instance.setData(rows);
   }

   @Test
   public void testGetData() {
      System.out.println("getData");
      PerfMonCollector instance = new PerfMonCollector();
      JMeterProperty result = instance.getData();
      assertNotNull(result);
   }

   @Test
   public void testSampleOccurred() {
      System.out.println("sampleOccurred");
      SampleEvent event = null;
      PerfMonCollector instance = new PerfMonCollector();
      instance.sampleOccurred(event);
   }

   @Test
   public void testRun() throws InterruptedException {
      System.out.println("run");
      PerfMonCollector instance = new PerfMonCollector();
      instance.testStarted();
      Thread.sleep(1500);
      instance.testEnded();
   }

   @Test
   public void testTestStarted() {
      System.out.println("testStarted");
      PerfMonCollector instance = new PerfMonCollector();
      instance.testStarted();
   }

   @Test
   public void testTestEnded() {
      System.out.println("testEnded");
      PerfMonCollector instance = new PerfMonCollector();
      instance.testStarted();
      instance.testEnded();
   }
}