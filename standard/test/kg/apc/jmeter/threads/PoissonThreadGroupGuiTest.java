package kg.apc.jmeter.threads;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import kg.apc.emulators.TestJMeterUtils;

import org.apache.jmeter.testelement.TestElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class PoissonThreadGroupGuiTest {

    /**
    *
    */
	public PoissonThreadGroupGuiTest() {
	}

    /**
    *
    * @throws Exception
    */
   @BeforeClass
   public static void setUpClass() throws Exception {
       TestJMeterUtils.createJmeterEnv();
   }

   /**
    *
    * @throws Exception
    */
   @AfterClass
   public static void tearDownClass() throws Exception {
   }

   /**
    *
    */
   @Before
   public void setUp() {
   }

   /**
    *
    */
   @After
   public void tearDown() {
   }
   
   /**
    * Test of init method, of class PoissonThreadGroupGui.
    */
   @Test
   public void testInit() {
       System.out.println("init");
       PoissonThreadGroupGui instance = new PoissonThreadGroupGui();
       instance.init();
   }

   /**
   * Test of getLabelResource method, of class PoissonThreadGroupGui.
   */
   @Test
   public void testGetLabelResource() {
	   System.out.println("getLabelResource");
	   PoissonThreadGroupGui instance = new PoissonThreadGroupGui();
	   String expResult = "PoissonThreadGroupGui";
	   String result = instance.getLabelResource();
	   assertEquals(expResult, result);
   }

   /**
    * Test of getStaticLabel method, of class PoissonThreadGroupGui.
    */
   @Test
   public void testGetStaticLabel() {
	   System.out.println("getStaticLabel");
	   PoissonThreadGroupGui instance = new PoissonThreadGroupGui();
	   String result = instance.getStaticLabel();
	   assertTrue(result.length() > 0);
   }

   /**
    * Test of createTestElement method, of class PoissonThreadGroupGui.
    */
   @Test
   public void testCreateTestElement() {
	   System.out.println("createTestElement");
	   PoissonThreadGroupGui instance = new PoissonThreadGroupGui();
	   TestElement result = instance.createTestElement();
	   assertTrue(result instanceof PoissonThreadGroup);
   }
   
   /**
    * Test of modifyTestElement method, of class PoissonThreadGroupGui.
    */
   @Test
   public void testModifyTestElement() {
       System.out.println("modifyTestElement");
       PoissonThreadGroup tg = new PoissonThreadGroup();
       tg.setNumThreads(100);
       PoissonThreadGroupGui instance = new PoissonThreadGroupGui();
       instance.modifyTestElement(tg);
   }
   
   /**
    * Test of configure method, of class PoissonThreadGroupGui.
    */
   @Test
   public void testConfigure() {
       System.out.println("configure");
       TestElement tg = new PoissonThreadGroup();
       PoissonThreadGroupGui instance = new PoissonThreadGroupGui();
       instance.createTestElement();
       instance.configure(tg);
   }
   
   /**
    * Test of clearGui method, of class PoissonThreadGroupGui.
    */
   @Test
   public void testClearGui() {
       System.out.println("clearGui");
       PoissonThreadGroupGui instance = new PoissonThreadGroupGui();
       instance.clearGui();
   }
   
   
   /**
    * Test of updateUI method, of class PoissonThreadGroupGui.
    */
   @Test
   public void testUpdateUI() {
       System.out.println("updateUI");
       PoissonThreadGroupGui instance = new PoissonThreadGroupGui();
       instance.updateUI();
   }
}
