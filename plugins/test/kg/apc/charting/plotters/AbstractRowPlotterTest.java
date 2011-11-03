package kg.apc.charting.plotters;

import java.awt.Color;
import java.awt.Graphics2D;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.ChartSettings;
import kg.apc.charting.rows.GraphRowAverages;
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
public class AbstractRowPlotterTest {

    private AbstractGraphRow testRow;
    private long minXVal, maxXVal;
    private double minYVal, maxYVal;

    public AbstractRowPlotterTest() {
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

       minXVal = now;
       maxXVal = now+10000;
       minYVal = 1;
       maxYVal = 50;
    }

    @After
    public void tearDown() {
    }

   /**
    * Test of setBoundsValues method, of class AbstractRowPlotter.
    */
   @Test
   public void testSetBoundsValues() {
      System.out.println("setBoundsValues");
      AbstractRowPlotter instance = new AbstractRowPlotterImpl();
      instance.setBoundsValues(new Graphics2DEmul().getBounds(), minXVal, maxXVal, minYVal, maxYVal);
   }

   /**
    * Test of isChartPointValid method, of class AbstractRowPlotter.
    */
   @Test
   public void testIsChartPointValid() {
      System.out.println("isChartPointValid");
      int xx = 365;
      int yy = 378;
      AbstractRowPlotter instance = new AbstractRowPlotterImpl();
      instance.setBoundsValues(new Graphics2DEmul().getBounds(), minXVal, maxXVal, minYVal, maxYVal);
      boolean expResult = true;
      boolean result = instance.isChartPointValid(xx, yy);
      assertEquals(expResult, result);
   }

   /**
    * Test of paintRow method, of class AbstractRowPlotter.
    */
   @Test
   public void testPaintRow() {
      System.out.println("paintRow");
      Graphics2DEmul g2d = new Graphics2DEmul();
      Color color = Color.red;
      double zoomFactor = 1.0;
      int limitPointFactor = 1;
      AbstractRowPlotter instance = new AbstractRowPlotterImpl();
      instance.setBoundsValues(g2d.getBounds(), minXVal, maxXVal, minYVal, maxYVal);
      instance.paintRow(g2d, testRow, color, zoomFactor, limitPointFactor);
      String expResult = "setColor: java.awt.Color[r=255,g=0,b=0]|fillOval: (7;607) - w:6 h:6|drawLine: (10;610) - (410;378)|fillOval: (407;375) - w:6 h:6|drawLine: (410;378) - (810;10)|fillOval: (807;7) - w:6 h:6|";
      String result = g2d.getResult();
      assertEquals(expResult, result);
   }

   /**
    * Test of processPoint method, of class AbstractRowPlotter.
    */
   @Test
   public void testProcessPoint() {
      System.out.println("processPoint");
      Graphics2DEmul g2d = new Graphics2DEmul();
      int granulation = 100;
      AbstractRowPlotter instance = new AbstractRowPlotterImpl();
      instance.setBoundsValues(g2d.getBounds(), minXVal, maxXVal, minYVal, maxYVal);
      instance.processPoint(g2d, granulation);
   }

   /**
    * Test of postPaintRow method, of class AbstractRowPlotter.
    */
   @Test
   public void testPostPaintRow() {
      System.out.println("postPaintRow");
      Graphics2DEmul g2d = new Graphics2DEmul();
      AbstractRowPlotter instance = new AbstractRowPlotterImpl();
      instance.setBoundsValues(g2d.getBounds(), minXVal, maxXVal, minYVal, maxYVal);
      instance.postPaintRow(testRow, g2d);
   }

   public class AbstractRowPlotterImpl extends AbstractRowPlotter {

      public AbstractRowPlotterImpl() {
         super(new ChartSettings(), new NumberRenderer());
         allowMarkers = true;
      }

      @Override
      public void processPoint(Graphics2D g2d, int granulation) {
         if (prevX >= 0) {
            if (isChartPointValid(x, y)) {
               g2d.drawLine(prevX, prevY, x, y);
            }
         }
      }
   }

}