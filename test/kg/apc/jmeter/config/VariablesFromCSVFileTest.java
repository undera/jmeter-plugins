package kg.apc.jmeter.config;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class VariablesFromCSVFileTest
{
   public VariablesFromCSVFileTest()
   {
   }

   @BeforeClass
   public static void setUpClass()
        throws Exception
   {
   }

   @AfterClass
   public static void tearDownClass()
        throws Exception
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
    * Test of iterationStart method, of class VariablesFromCSVFile.
    */
   @Test
   public void testIterationStart()
   {
      System.out.println("iterationStart");
      LoopIterationEvent iterEvent = new LoopIterationEvent(null, 1);
      VariablesFromCSVFile instance = new VariablesFromCSVFile();
      instance.iterationStart(iterEvent);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }
}
