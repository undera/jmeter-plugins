package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.perfmon.PerfMonCollector;
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
public class PerfMonGuiTest {

    public PerfMonGuiTest() {
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

   @Test
   public void testCreateSettingsPanel() {
      System.out.println("createSettingsPanel");
      PerfMonGui instance = new PerfMonGui();
      JSettingsPanel result = instance.createSettingsPanel();
      assertNotNull(result);
   }

   @Test
   public void testGetWikiPage() {
      System.out.println("getWikiPage");
      PerfMonGui instance = new PerfMonGui();
      String result = instance.getWikiPage();
      assertTrue(result.length()>0);
   }

   @Test
   public void testGetLabelResource() {
      System.out.println("getLabelResource");
      PerfMonGui instance = new PerfMonGui();
      String result = instance.getLabelResource();
      assertTrue(result.length()>0);
   }

   @Test
   public void testGetStaticLabel() {
      System.out.println("getStaticLabel");
      PerfMonGui instance = new PerfMonGui();
      String result = instance.getStaticLabel();
      assertTrue(result.length()>0);
   }

   @Test
   public void testCreateTestElement() {
      System.out.println("createTestElement");
      PerfMonGui instance = new PerfMonGui();
      TestElement result = instance.createTestElement();
      assertTrue(result instanceof PerfMonCollector);
   }

   @Test
   public void testModifyTestElement() {
      System.out.println("modifyTestElement");
      TestElement c = new PerfMonCollector();
      PerfMonGui instance = new PerfMonGui();
      instance.modifyTestElement(c);
   }

   @Test
   public void testConfigure() {
      System.out.println("configure");
      TestElement el = new PerfMonCollector();
      PerfMonGui instance = new PerfMonGui();
      instance.configure(el);
   }
}