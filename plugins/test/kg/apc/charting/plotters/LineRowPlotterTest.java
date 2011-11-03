package kg.apc.charting.plotters;

import kg.apc.charting.ChartSettings;
import org.apache.jorphan.gui.NumberRenderer;
import java.awt.Color;
import kg.apc.emulators.Graphics2DEmul;
import kg.apc.charting.rows.GraphRowAverages;
import kg.apc.charting.AbstractGraphRow;
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
public class LineRowPlotterTest {

    private AbstractGraphRow testRow;
    private long minXVal, maxXVal;
    private double minYVal, maxYVal;

    public LineRowPlotterTest() {
    }

   @BeforeClass
   public static void setUpClass() throws Exception {
   }

   @AfterClass
   public static void tearDownClass() throws Exception {
   }

    @Before
    public void setUp() {
       testRow =  new GraphRowAverages();
       long now = System.currentTimeMillis();
       testRow.add(now, 1);
       testRow.add(now+5000, 20);
       testRow.add(now+10000, 50);

       testRow.setMarkerSize(3);
       testRow.setGranulationValue(500);

       minXVal = now;
       maxXVal = now+10000;
       minYVal = 0;
       maxYVal = 50;
    }

    @After
    public void tearDown() {
    }

   /**
    * Test of processPoint method, of class LineRowPlotter.
    */
   @Test
   public void testProcessPoint() {
      System.out.println("processPoint");
      Graphics2DEmul g2d = new Graphics2DEmul();
      Color color = Color.red;
      double zoomFactor = 1.0;
      int limitPointFactor = 1;
      ChartSettings chartSettings = new ChartSettings();
      chartSettings.setDrawFinalZeroingLines(true);
      LineRowPlotter instance = new LineRowPlotter(chartSettings, new NumberRenderer());
      instance.setBoundsValues(g2d.getBounds(), minXVal, maxXVal, minYVal, maxYVal);
      instance.paintRow(g2d, testRow, color, zoomFactor, limitPointFactor);
      String expResult = "setColor: java.awt.Color[r=255,g=0,b=0]|drawLine: (10;610) - (10;598)|fillOval: (7;595) - w:6 h:6|drawLine: (10;598) - (410;370)|fillOval: (407;367) - w:6 h:6|drawLine: (410;370) - (810;10)|fillOval: (807;7) - w:6 h:6|";
      String result = g2d.getResult();
      assertEquals(expResult, result);
   }

}