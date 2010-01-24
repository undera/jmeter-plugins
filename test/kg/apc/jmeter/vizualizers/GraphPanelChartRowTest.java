package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class GraphPanelChartRowTest
{
   private GraphPanelChartRow instance;

   public GraphPanelChartRowTest()
   {
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
   public void setUp()
   {
      instance = new GraphPanelChartRow("test", Color.black, true, 5);
   }

   @After
   public void tearDown()
   {
   }

   /**
    * Test of add method, of class GraphPanelChartRow.
    */
   @Test
   public void testAdd()
   {
      System.out.println("add");
      long xVal = 10;
      double yVal = 5.0;
      instance.add(xVal, yVal);
      assertEquals(xVal, instance.getMinX());
      assertEquals(xVal, instance.getMaxX());
      assertEquals(yVal, instance.getMinY(), 0.001);
      assertEquals(yVal, instance.getMaxY(), 0.001);
   }

   /**
    * Test of setDrawLine method, of class GraphPanelChartRow.
    */
   @Test
   public void testSetDrawLine()
   {
      System.out.println("setDrawLine");
      boolean b = false;
      instance.setDrawLine(b);
   }

   /**
    * Test of setMarkerSize method, of class GraphPanelChartRow.
    */
   @Test
   public void testSetMarkerSize()
   {
      System.out.println("setMarkerSize");
      int aMarkerSize = 0;
      instance.setMarkerSize(aMarkerSize);
   }

   /**
    * Test of isDrawLine method, of class GraphPanelChartRow.
    */
   @Test
   public void testIsDrawLine()
   {
      System.out.println("isDrawLine");
      boolean expResult = true;
      boolean result = instance.isDrawLine();
      assertEquals(expResult, result);
   }

   /**
    * Test of getMarkerSize method, of class GraphPanelChartRow.
    */
   @Test
   public void testGetMarkerSize()
   {
      System.out.println("getMarkerSize");
      int expResult = 5;
      int result = instance.getMarkerSize();
      assertEquals(expResult, result);
   }

   /**
    * Test of getMaxX method, of class GraphPanelChartRow.
    */
   @Test
   public void testGetMaxX()
   {
      System.out.println("getMaxX");
      double expResult = 0.0;
      double result = instance.getMaxX();
      assertEquals(expResult, result, 0.0);
   }

   /**
    * Test of getMaxY method, of class GraphPanelChartRow.
    */
   @Test
   public void testGetMaxY()
   {
      System.out.println("getMaxY");
      double expResult = 0.0;
      double result = instance.getMaxY();
      assertEquals(expResult, result, 0.0);
   }

   /**
    * Test of getMinX method, of class GraphPanelChartRow.
    */
   @Test
   public void testGetMinX()
   {
      System.out.println("getMinX");
      double expResult = 0.0;
      instance.add(0, 0);
      double result = instance.getMinX();
      assertEquals(expResult, result, 0.0);
   }

   /**
    * Test of getMinY method, of class GraphPanelChartRow.
    */
   @Test
   public void testGetMinY()
   {
      System.out.println("getMinY");
      double expResult = 0.0;
      instance.add(0, 0);
      double result = instance.getMinY();
      assertEquals(expResult, result, 0.0);
   }

   /**
    * Test of getColor method, of class GraphPanelChartRow.
    */
   @Test
   public void testGetColor()
   {
      System.out.println("getColor");
      Color expResult = Color.black;
      Color result = instance.getColor();
      assertEquals(expResult, result);
   }

   /**
    * Test of setColor method, of class GraphPanelChartRow.
    */
   @Test
   public void testSetColor()
   {
      System.out.println("setColor");
      Color nextColor = null;
      instance.setColor(nextColor);
   }
}
