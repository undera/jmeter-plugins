package kg.apc.jmeter.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class VariablesFromCSVFileTest
{
   private VariablesFromCSVFile instance;
   private JMeterVariables variables;
   private LoopIterationEvent iterEvent;

   public VariablesFromCSVFileTest()
   {
   }

   @BeforeClass
   public static void setUpClass()
         throws Exception
   {
      File propsFile = null;
      try
      {
         propsFile = File.createTempFile("jmeter-plugins", "testProps");
         JMeterUtils.loadJMeterProperties(propsFile.getAbsolutePath());
      }
      catch (IOException ex)
      {
         Logger.getLogger(VariablesFromCSVFile.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @AfterClass
   public static void tearDownClass()
         throws Exception
   {
   }

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
      try
      {
         BufferedWriter out = new BufferedWriter(new FileWriter(testFile));
         out.write("testvar1\ttestval1\n");
         out.write("testvar2\ttestval2\n");
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
      assertNull(variables.get("prefixedtestvar1"));
   }

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
}
