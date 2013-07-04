package kg.apc.jmeter.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apc
 */
public class VariablesFromCSVFileTest
{
   private VariablesFromCSVFile instance;
   private JMeterVariables variables;
   private LoopIterationEvent iterEvent;

   /**
    *
    */
   public VariablesFromCSVFileTest()
   {
   }

   /**
    *
    * @throws Exception
    */
   @BeforeClass
   public static void setUpClass()
         throws Exception
   {
      TestJMeterUtils.createJmeterEnv();
   }

   /**
    *
    * @throws Exception
    */
   @AfterClass
   public static void tearDownClass()
         throws Exception
   {
   }

   /**
    *
    */
   @Before
   public void setUp()
   {
      File testFile = null;
      try
      {
         testFile = File.createTempFile("jmeter-plugins", "testData");
      }
      catch (IOException ex)
      {
         Logger.getLogger(VariablesFromCSVFileTest.class.getName()).log(Level.SEVERE, null, ex);
         fail("Exception!");
      }

      // Delete temp file when program exits.
      testFile.deleteOnExit();

      // Write to temp file
      // REV SH - Issue 3: Blank lines cause Java Exception
      // Simulate the problem, and add on bad formatted line
      try
      {
         BufferedWriter out = new BufferedWriter(new FileWriter(testFile));
         out.write("testvar1\ttestval1\n");
         out.write("\n");
         out.write("testvar2\ttestval2\n");
         out.write("\n");
         out.write("badvar\tcontain\tbad\tchars\n");
         out.close();
      }
      catch (IOException ex)
      {
         Logger.getLogger(VariablesFromCSVFileTest.class.getName()).log(Level.SEVERE, null, ex);
      }

      JMeterContext jmcx = JMeterContextService.getContext();
      jmcx.setVariables(new JMeterVariables());
      variables = jmcx.getVariables();

      instance = new VariablesFromCSVFile();
      instance.setFilename(testFile.getAbsolutePath());
      instance.setDelimiter("\\t");

      iterEvent = new LoopIterationEvent(null, 1);
   }

   /**
    *
    */
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

      instance.setVariablesPrefix("");
      instance.iterationStart(iterEvent);

      assertEquals("testval1", variables.get("testvar1"));
      assertEquals("testval2", variables.get("testvar2"));
      assertNull(variables.get("testvar3"));
      assertNull(variables.get("badvar"));
      assertNull(variables.get("prefixedtestvar1"));
   }

   /**
    *
    */
   @Test
   public void testIterationStart_prefixed()
   {
      System.out.println("iterationStart");

      instance.setVariablesPrefix("prefixed");
      instance.iterationStart(iterEvent);

      assertEquals("testval1", variables.get("prefixedtestvar1"));
      assertEquals("testval2", variables.get("prefixedtestvar2"));
      assertNull(variables.get("testvar2"));
      assertNull(variables.get("testvar1"));
   }

   /**
    *
    */
   @Test
   public void testIterationStart_lessthan2()
   {
      System.out.println("iterationStart");

      instance.setDelimiter("");
      instance.setVariablesPrefix("");
      instance.iterationStart(iterEvent);

      assertNull(variables.get("testvar2"));
      assertNull(variables.get("testvar1"));
   }

   /**
    * Test of getFilename method, of class VariablesFromCSVFile.
    */
   @Test
   public void testGetFilename()
   {
   }

   /**
    * Test of setFilename method, of class VariablesFromCSVFile.
    */
   @Test
   public void testSetFilename()
   {
   }

   /**
    * Test of getVariablesPrefix method, of class VariablesFromCSVFile.
    */
   @Test
   public void testGetVariablesPrefix()
   {
   }

   /**
    * Test of setVariablesPrefix method, of class VariablesFromCSVFile.
    */
   @Test
   public void testSetVariablesPrefix()
   {
   }

   /**
    * Test of getDelimiter method, of class VariablesFromCSVFile.
    */
   @Test
   public void testGetDelimiter()
   {
   }

   /**
    * Test of setDelimiter method, of class VariablesFromCSVFile.
    */
   @Test
   public void testSetDelimiter()
   {
   }
}
