/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.config;

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
 * @author z000205
 */
public class VariablesFromCSVGuiTest {

    public VariablesFromCSVGuiTest() {
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

   /**
    * Test of getStaticLabel method, of class VariablesFromCSVGui.
    */
   @Test
   public void testGetStaticLabel() {
      System.out.println("getStaticLabel");
      VariablesFromCSVGui instance = new VariablesFromCSVGui();
      String result = instance.getStaticLabel();
      assertTrue(result.length()>0);
   }

   /**
    * Test of getLabelResource method, of class VariablesFromCSVGui.
    */
   @Test
   public void testGetLabelResource() {
      System.out.println("getLabelResource");
      VariablesFromCSVGui instance = new VariablesFromCSVGui();
      String result = instance.getLabelResource();
      assertTrue(result.length()>0);
   }

   /**
    * Test of configure method, of class VariablesFromCSVGui.
    */
   @Test
   public void testConfigure() {
      System.out.println("configure");
      TestElement element = new VariablesFromCSV();
      VariablesFromCSVGui instance = new VariablesFromCSVGui();
      instance.configure(element);
   }

   /**
    * Test of createTestElement method, of class VariablesFromCSVGui.
    */
   @Test
   public void testCreateTestElement() {
      System.out.println("createTestElement");
      VariablesFromCSVGui instance = new VariablesFromCSVGui();
      TestElement result = instance.createTestElement();
      assertTrue(result instanceof VariablesFromCSV);
   }

   /**
    * Test of modifyTestElement method, of class VariablesFromCSVGui.
    */
   @Test
   public void testModifyTestElement() {
      System.out.println("modifyTestElement");
      TestElement te = new VariablesFromCSV();
      VariablesFromCSVGui instance = new VariablesFromCSVGui();
      instance.modifyTestElement(te);
   }

   /**
    * Test of clearGui method, of class VariablesFromCSVGui.
    */
   @Test
   public void testClearGui() {
      System.out.println("clearGui");
      VariablesFromCSVGui instance = new VariablesFromCSVGui();
      instance.clearGui();
   }

}