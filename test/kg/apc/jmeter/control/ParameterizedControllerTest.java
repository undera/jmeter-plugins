package kg.apc.jmeter.control;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.testelement.property.JMeterProperty;
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
 * @author APC
 */
public class ParameterizedControllerTest
{
   private ParameterizedController instance;

   public ParameterizedControllerTest()
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
      JMeterVariables vars = new JMeterVariables();
      vars.put("var1", "val1");
      JMeterContextService.getContext().setVariables(vars);
      JMeterContextService.getContext().setSamplingStarted(true);

      instance = new ParameterizedController();
      instance.setRunningVersion(true);
   }

   @After
   public void tearDown()
   {
      JMeterContextService.getContext().setSamplingStarted(false);
   }

   /**
    * Test of iterationStart method, of class ParameterizedController.
    */
   @Test
   public void testNext()
   {
      System.out.println("next");
      Arguments args = new Arguments();
      args.addArgument("var2", "${var1}");
      args.addArgument("var3", "val3");
      args.setRunningVersion(true);
      
      instance.setUserDefinedVariables(args);
      instance.next();

      JMeterVariables vars = JMeterContextService.getContext().getVariables();
      assertEquals("val3", vars.get("var3"));
      
      // FIXME: need to find a way to tell JMeter functions that we are in running state
      if (!vars.get("var2").equals("val1"))
         System.err.println("Failed to set var...");
   }

   /**
    * Test of setUserDefinedVariables method, of class ParameterizedController.
    */
   @Test
   public void testSetUserDefinedVariables()
   {
      System.out.println("setUserDefinedVariables");
      Arguments vars = null;
      ParameterizedController instance = new ParameterizedController();
      instance.setUserDefinedVariables(vars);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of getUserDefinedVariablesAsProperty method, of class ParameterizedController.
    */
   @Test
   public void testGetUserDefinedVariablesAsProperty()
   {
      System.out.println("getUserDefinedVariablesAsProperty");
      ParameterizedController instance = new ParameterizedController();
      JMeterProperty expResult = null;
      JMeterProperty result = instance.getUserDefinedVariablesAsProperty();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of iterationStart method, of class ParameterizedController.
    */
   @Test
   public void testIterationStart()
   {
      System.out.println("iterationStart");
      LoopIterationEvent lie = null;
      ParameterizedController instance = new ParameterizedController();
      instance.iterationStart(lie);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }
}
