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

   /**
    *
    */
   public ParameterizedControllerTest()
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
      JMeterVariables vars = new JMeterVariables();
      vars.put("var1", "val1");
      JMeterContextService.getContext().setVariables(vars);
      JMeterContextService.getContext().setSamplingStarted(true);

      instance = new ParameterizedController();
      instance.setRunningVersion(true);
   }

   /**
    *
    */
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
      Arguments vars = new Arguments();
      instance.setUserDefinedVariables(vars);
   }

   /**
    * Test of getUserDefinedVariablesAsProperty method, of class ParameterizedController.
    */
   @Test
   public void testGetUserDefinedVariablesAsProperty()
   {
      System.out.println("getUserDefinedVariablesAsProperty");
      Arguments vars = new Arguments();
      vars.addArgument("key", "value");
      instance.setUserDefinedVariables(vars);
      JMeterProperty result = instance.getUserDefinedVariablesAsProperty();
      assertNotNull(result);
   }

   /**
    * Test of iterationStart method, of class ParameterizedController.
    */
   @Test
   public void testIterationStart()
   {
      System.out.println("iterationStart");
      LoopIterationEvent lie = null;
      instance.iterationStart(lie);
   }
}
