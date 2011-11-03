package kg.apc.charting.plotters;

import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.rows.GraphRowAverages;
import java.awt.Color;
import kg.apc.charting.ChartSettings;
import kg.apc.emulators.Graphics2DEmul;
import org.apache.jorphan.gui.NumberRenderer;
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
public class BarRowPlotterTest {

   private AbstractGraphRow testRow;
   private long minXVal, maxXVal;
   private double minYVal, maxYVal;

   public BarRowPlotterTest() {
   }

   @BeforeClass
   public static void setUpClass() throws Exception {
   }

   @AfterClass
   public static void tearDownClass() throws Exception {
   }

   @Before
   public void setUp() {
      testRow = new GraphRowAverages();
      testRow.add(1000, 100);
      testRow.add(1300, 70);
      testRow.add(1500, 90);

      testRow.setMarkerSize(3);

      minXVal = 1000;
      maxXVal = 2000;
      minYVal = 0;
      maxYVal = 100;
   }

   @After
   public void tearDown() {
   }

   /**
    * Test of processPoint method, of class BarRowPlotter.
    */
   @Test
   public void testProcessPoint() {
      System.out.println("processPoint");
      Graphics2DEmul g2d = new Graphics2DEmul();
      Color color = Color.red;
      int granulation = 100;
      testRow.setGranulationValue(granulation);
      BarRowPlotter instance = new BarRowPlotter(new ChartSettings(), new NumberRenderer());
      instance.setBoundsValues(g2d.getBounds(), minXVal, maxXVal, minYVal, maxYVal);
      //instance.processPoint(g2d, granulation);
      instance.paintRow(g2d, testRow, color, 1, 1);
      String expResult = "setColor: java.awt.Color[r=255,g=0,b=0]|setComposite: java.awt.AlphaComposite|fillRect: (10;9) - w:79 h:601|setComposite: java.awt.AlphaComposite|setComposite: java.awt.AlphaComposite|fillRect: (250;189) - w:79 h:421|setComposite: java.awt.AlphaComposite|setComposite: java.awt.AlphaComposite|fillRect: (410;69) - w:79 h:541|setComposite: java.awt.AlphaComposite|";
      String result = g2d.getResult();
      assertEquals(expResult, result);
   }
}
