/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.control;

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
public class ParameterizedControllerGuiTest {

    public ParameterizedControllerGuiTest() {
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
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

   /**
    * Test of createTestElement method, of class ParameterizedControllerGui.
    */
   @Test
   public void testCreateTestElement()
   {
      System.out.println("createTestElement");
      ParameterizedControllerGui instance = new ParameterizedControllerGui();
      TestElement expResult = null;
      TestElement result = instance.createTestElement();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of modifyTestElement method, of class ParameterizedControllerGui.
    */
   @Test
   public void testModifyTestElement()
   {
      System.out.println("modifyTestElement");
      TestElement te = null;
      ParameterizedControllerGui instance = new ParameterizedControllerGui();
      instance.modifyTestElement(te);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of getLabelResource method, of class ParameterizedControllerGui.
    */
   @Test
   public void testGetLabelResource()
   {
      System.out.println("getLabelResource");
      ParameterizedControllerGui instance = new ParameterizedControllerGui();
      String expResult = "";
      String result = instance.getLabelResource();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of getStaticLabel method, of class ParameterizedControllerGui.
    */
   @Test
   public void testGetStaticLabel()
   {
      System.out.println("getStaticLabel");
      ParameterizedControllerGui instance = new ParameterizedControllerGui();
      String expResult = "";
      String result = instance.getStaticLabel();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of clearGui method, of class ParameterizedControllerGui.
    */
   @Test
   public void testClearGui()
   {
      System.out.println("clearGui");
      ParameterizedControllerGui instance = new ParameterizedControllerGui();
      instance.clearGui();
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of configure method, of class ParameterizedControllerGui.
    */
   @Test
   public void testConfigure()
   {
      System.out.println("configure");
      TestElement te = null;
      ParameterizedControllerGui instance = new ParameterizedControllerGui();
      instance.configure(te);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

}