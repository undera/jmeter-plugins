package kg.apc.jmeter.samplers;

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
 * @author apc
 */
public class DummySamplerGuiTest
{
   /**
    *
    */
   public DummySamplerGuiTest()
   {
   }

   /**
    *
    * @throws Exception
    */
   @BeforeClass
   public static void setUpClass() throws Exception
   {
      TestJMeterUtils.createJmeterEnv();
   }

   /**
    *
    * @throws Exception
    */
   @AfterClass
   public static void tearDownClass() throws Exception
   {
   }

   /**
    *
    */
   @Before
   public void setUp()
   {
   }

   /**
    *
    */
   @After
   public void tearDown()
   {
   }

   /**
    * Test of getStaticLabel method, of class DummySamplerGui.
    */
   @Test
   public void testGetStaticLabel()
   {
      System.out.println("getStaticLabel");
      DummySamplerGui instance = new DummySamplerGui();
      String result = instance.getStaticLabel();
      assertTrue(result.length()>0);
   }

   /**
    * Test of configure method, of class DummySamplerGui.
    */
   @Test
   public void testConfigure()
   {
      System.out.println("configure");
      TestElement element = new DummySampler();
      DummySamplerGui instance = new DummySamplerGui();
      instance.configure(element);
   }

   /**
    * Test of createTestElement method, of class DummySamplerGui.
    */
   @Test
   public void testCreateTestElement()
   {
      System.out.println("createTestElement");
      DummySamplerGui instance = new DummySamplerGui();
      TestElement result = instance.createTestElement();
      assertNotNull(result);
   }

   /**
    * Test of modifyTestElement method, of class DummySamplerGui.
    */
   @Test
   public void testModifyTestElement()
   {
      System.out.println("modifyTestElement");
      TestElement sampler = new DummySampler();
      DummySamplerGui instance = new DummySamplerGui();
      instance.modifyTestElement(sampler);
   }

   /**
    * Test of clearGui method, of class DummySamplerGui.
    */
   @Test
   public void testClearGui()
   {
      System.out.println("clearGui");
      DummySamplerGui instance = new DummySamplerGui();
      instance.clearGui();
   }

   /**
    * Test of getLabelResource method, of class DummySamplerGui.
    */
   @Test
   public void testGetLabelResource()
   {
      System.out.println("getLabelResource");
      DummySamplerGui instance = new DummySamplerGui();
      String expResult = "DummySamplerGui";
      String result = instance.getLabelResource();
      assertEquals(expResult, result);
   }
}
