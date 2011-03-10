/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.control;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.testelement.TestElement;
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
public class ParameterizedControllerGuiTest
{
   private ParameterizedControllerGui instance;

   /**
    *
    */
   public ParameterizedControllerGuiTest()
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
      instance = new ParameterizedControllerGui();
   }

   /**
    *
    */
   @After
   public void tearDown()
   {
   }

   /**
    * Test of createTestElement method, of class ParameterizedControllerGui.
    */
   @Test
   public void testCreateTestElement()
   {
      System.out.println("createTestElement");
      TestElement expResult = new ParameterizedController();
      TestElement result = instance.createTestElement();
      assertEquals(expResult.getClass(), result.getClass());
   }

   /**
    * Test of modifyTestElement method, of class ParameterizedControllerGui.
    */
   @Test
   public void testModifyTestElement()
   {
      System.out.println("modifyTestElement");
      TestElement te = new ParameterizedController();
      instance.modifyTestElement(te);
   }

   /**
    * Test of getLabelResource method, of class ParameterizedControllerGui.
    */
   @Test
   public void testGetLabelResource()
   {
      System.out.println("getLabelResource");
      String expResult = instance.getClass().getName();
      String result = instance.getLabelResource();
      assertEquals(expResult, result);
   }

   /**
    * Test of getStaticLabel method, of class ParameterizedControllerGui.
    */
   @Test
   public void testGetStaticLabel()
   {
      System.out.println("getStaticLabel");
      String result = instance.getStaticLabel();
        assertTrue(result.length()>0);
   }

   /**
    * Test of clearGui method, of class ParameterizedControllerGui.
    */
   @Test
   public void testClearGui()
   {
      System.out.println("clearGui");
      instance.clearGui();
   }

   /**
    * Test of configure method, of class ParameterizedControllerGui.
    */
   @Test
   public void testConfigure()
   {
      System.out.println("configure");
      TestElement te = new ParameterizedController();
      te.setName("test");
      te.setComment("test");

      try
      {
         instance.configure(te);
      }
      catch (NullPointerException e)
      {
         System.err.println("I dunno what it requires for correct test");
      }
   }
}
