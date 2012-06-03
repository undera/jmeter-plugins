package kg.apc.jmeter.vizualizers;

import kg.apc.emulators.TestJMeterUtils;
import javax.swing.JPanel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stephane Hoblingre
 */
public class PageDataExtractorOverTimeGuiTest {

    public PageDataExtractorOverTimeGuiTest() {
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
    * Test of createSettingsPanel method, of class PageDataExtractorOverTimeGui.
    */
   @Test
   public void testCreateSettingsPanel() {
      System.out.println("createSettingsPanel");
      PageDataExtractorOverTimeGui instance = new PageDataExtractorOverTimeGui();
      JSettingsPanel result = instance.createSettingsPanel();
      assertNotNull(result);
   }

   /**
    * Test of getStaticLabel method, of class PageDataExtractorOverTimeGui.
    */
   @Test
   public void testGetStaticLabel() {
      System.out.println("getStaticLabel");
      PageDataExtractorOverTimeGui instance = new PageDataExtractorOverTimeGui();
      String result = instance.getStaticLabel();
      assertTrue(result.length() > 0);
   }

   /**
    * Test of getLabelResource method, of class PageDataExtractorOverTimeGui.
    */
   @Test
   public void testGetLabelResource() {
      System.out.println("getLabelResource");
      PageDataExtractorOverTimeGui instance = new PageDataExtractorOverTimeGui();
      String result = instance.getLabelResource();
      assertTrue(result.length() > 0);
   }

   /**
    * Test of getWikiPage method, of class PageDataExtractorOverTimeGui.
    */
   @Test
   public void testGetWikiPage() {
      System.out.println("getWikiPage");
      PageDataExtractorOverTimeGui instance = new PageDataExtractorOverTimeGui();
      String result = instance.getWikiPage();
      assertTrue(result.length() > 0);
   }

   /**
    * Test of getGraphPanelContainer method, of class PageDataExtractorOverTimeGui.
    */
   @Test
   public void testGetGraphPanelContainer() {
      System.out.println("getGraphPanelContainer");
      PageDataExtractorOverTimeGui instance = new PageDataExtractorOverTimeGui();
      JPanel result = instance.getGraphPanelContainer();
      assertNotNull(result);
   }

}