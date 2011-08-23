/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.charting;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Stephane Hoblingre
 */
public class ChartSettingsTest {

    public ChartSettingsTest() {
    }

   @BeforeClass
   public static void setUpClass() throws Exception {
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
    * Test of getConfigCsvSeparator method, of class ChartSettings.
    */
   @Test
   public void testGetConfigCsvSeparator() {
      System.out.println("getConfigCsvSeparator");
      ChartSettings instance = new ChartSettings();
      String result = instance.getConfigCsvSeparator();
   }

   /**
    * Test of setConfigCsvSeparator method, of class ChartSettings.
    */
   @Test
   public void testSetConfigCsvSeparator() {
      System.out.println("setConfigCsvSeparator");
      String configCsvSeparator = "";
      ChartSettings instance = new ChartSettings();
      instance.setConfigCsvSeparator(configCsvSeparator);
   }

   /**
    * Test of setConfigNeverDrawCurrentX method, of class ChartSettings.
    */
   @Test
   public void testSetConfigNeverDrawCurrentX() {
      System.out.println("setConfigNeverDrawCurrentX");
      boolean configNeverDrawCurrentX = false;
      ChartSettings instance = new ChartSettings();
      instance.setConfigNeverDrawCurrentX(configNeverDrawCurrentX);
   }

   /**
    * Test of setConfigNeverDrawFinalZeroingLines method, of class ChartSettings.
    */
   @Test
   public void testSetConfigNeverDrawFinalZeroingLines() {
      System.out.println("setConfigNeverDrawFinalZeroingLines");
      boolean configNeverDrawFinalZeroingLines = false;
      ChartSettings instance = new ChartSettings();
      instance.setConfigNeverDrawFinalZeroingLines(configNeverDrawFinalZeroingLines);
   }

   /**
    * Test of isConfigOptimizeYAxis method, of class ChartSettings.
    */
   @Test
   public void testIsConfigOptimizeYAxis() {
      System.out.println("isConfigOptimizeYAxis");
      ChartSettings instance = new ChartSettings();
      instance.isConfigOptimizeYAxis();
   }

   /**
    * Test of setConfigOptimizeYAxis method, of class ChartSettings.
    */
   @Test
   public void testSetConfigOptimizeYAxis() {
      System.out.println("setConfigOptimizeYAxis");
      boolean configOptimizeYAxis = false;
      ChartSettings instance = new ChartSettings();
      instance.setConfigOptimizeYAxis(configOptimizeYAxis);
   }

   /**
    * Test of isDrawCurrentX method, of class ChartSettings.
    */
   @Test
   public void testIsDrawCurrentX() {
      System.out.println("isDrawCurrentX");
      ChartSettings instance = new ChartSettings();
      instance.isDrawCurrentX();
   }

   /**
    * Test of setDrawCurrentX method, of class ChartSettings.
    */
   @Test
   public void testSetDrawCurrentX() {
      System.out.println("setDrawCurrentX");
      boolean drawCurrentX = false;
      ChartSettings instance = new ChartSettings();
      instance.setDrawCurrentX(drawCurrentX);
   }

   /**
    * Test of isDrawFinalZeroingLines method, of class ChartSettings.
    */
   @Test
   public void testIsDrawFinalZeroingLines() {
      System.out.println("isDrawFinalZeroingLines");
      ChartSettings instance = new ChartSettings();
      instance.isDrawFinalZeroingLines();
   }

   /**
    * Test of setDrawFinalZeroingLines method, of class ChartSettings.
    */
   @Test
   public void testSetDrawFinalZeroingLines() {
      System.out.println("setDrawFinalZeroingLines");
      boolean drawFinalZeroingLines = false;
      ChartSettings instance = new ChartSettings();
      instance.setDrawFinalZeroingLines(drawFinalZeroingLines);
   }

   /**
    * Test of isDrawGradient method, of class ChartSettings.
    */
   @Test
   public void testIsDrawGradient() {
      System.out.println("isDrawGradient");
      ChartSettings instance = new ChartSettings();
      instance.isDrawGradient();
   }

   /**
    * Test of setDrawGradient method, of class ChartSettings.
    */
   @Test
   public void testSetDrawGradient() {
      System.out.println("setDrawGradient");
      boolean drawGradient = false;
      ChartSettings instance = new ChartSettings();
      instance.setDrawGradient(drawGradient);
   }

   /**
    * Test of getHideNonRepValLimit method, of class ChartSettings.
    */
   @Test
   public void testGetHideNonRepValLimit() {
      System.out.println("getHideNonRepValLimit");
      ChartSettings instance = new ChartSettings();
      instance.getHideNonRepValLimit();
   }

   /**
    * Test of setHideNonRepValLimit method, of class ChartSettings.
    */
   @Test
   public void testSetHideNonRepValLimit() {
      System.out.println("setHideNonRepValLimit");
      int hideNonRepValLimit = 0;
      ChartSettings instance = new ChartSettings();
      instance.setHideNonRepValLimit(hideNonRepValLimit);
   }

   /**
    * Test of isPreventXAxisOverScaling method, of class ChartSettings.
    */
   @Test
   public void testIsPreventXAxisOverScaling() {
      System.out.println("isPreventXAxisOverScaling");
      ChartSettings instance = new ChartSettings();
      instance.isPreventXAxisOverScaling();

   }

   /**
    * Test of setPreventXAxisOverScaling method, of class ChartSettings.
    */
   @Test
   public void testSetPreventXAxisOverScaling() {
      System.out.println("setPreventXAxisOverScaling");
      boolean preventXAxisOverScaling = false;
      ChartSettings instance = new ChartSettings();
      instance.setPreventXAxisOverScaling(preventXAxisOverScaling);
   }

   /**
    * Test of isUseRelativeTime method, of class ChartSettings.
    */
   @Test
   public void testIsUseRelativeTime() {
      System.out.println("isUseRelativeTime");
      ChartSettings instance = new ChartSettings();
      instance.isUseRelativeTime();
   }

   /**
    * Test of setUseRelativeTime method, of class ChartSettings.
    */
   @Test
   public void testSetUseRelativeTime() {
      System.out.println("setUseRelativeTime");
      boolean useRelativeTime = false;
      ChartSettings instance = new ChartSettings();
      instance.setUseRelativeTime(useRelativeTime);
   }

   /**
    * Test of getMaxPointPerRow method, of class ChartSettings.
    */
   @Test
   public void testGetMaxPointPerRow() {
      System.out.println("getMaxPointPerRow");
      ChartSettings instance = new ChartSettings();
      instance.getMaxPointPerRow();
   }

   /**
    * Test of setMaxPointPerRow method, of class ChartSettings.
    */
   @Test
   public void testSetMaxPointPerRow() {
      System.out.println("setMaxPointPerRow");
      int maxPointPerRow = 0;
      ChartSettings instance = new ChartSettings();
      instance.setMaxPointPerRow(maxPointPerRow);
   }

   /**
    * Test of getForcedMaxY method, of class ChartSettings.
    */
   @Test
   public void testGetForcedMaxY() {
      System.out.println("getForcedMaxY");
      ChartSettings instance = new ChartSettings();
      instance.getForcedMaxY();
   }

   /**
    * Test of setForcedMaxY method, of class ChartSettings.
    */
   @Test
   public void testSetForcedMaxY() {
      System.out.println("setForcedMaxY");
      long forcedMaxY = 0L;
      ChartSettings instance = new ChartSettings();
      instance.setForcedMaxY(forcedMaxY);
   }

   /**
    * Test of initDrawFinalZeroingLines method, of class ChartSettings.
    */
   @Test
   public void testInitDrawFinalZeroingLines() {
      System.out.println("initDrawFinalZeroingLines");
      boolean draw = false;
      ChartSettings instance = new ChartSettings();
      instance.initDrawFinalZeroingLines(draw);
   }

   /**
    * Test of initDrawCurrentX method, of class ChartSettings.
    */
   @Test
   public void testInitDrawCurrentX() {
      System.out.println("initDrawCurrentX");
      boolean draw = false;
      ChartSettings instance = new ChartSettings();
      instance.initDrawCurrentX(draw);
   }
}