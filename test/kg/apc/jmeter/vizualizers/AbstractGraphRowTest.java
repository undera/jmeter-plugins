package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import java.util.Iterator;
import java.util.Map.Entry;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractGraphRowTest
{
   public AbstractGraphRowTest()
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
   }

   @After
   public void tearDown()
   {
   }

   /**
    * Test of setDrawLine method, of class AbstractGraphRow.
    */
   @Test
   public void testSetDrawLine()
   {
      System.out.println("setDrawLine");
      boolean b = false;
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      instance.setDrawLine(b);
   }

   /**
    * Test of setMarkerSize method, of class AbstractGraphRow.
    */
   @Test
   public void testSetMarkerSize()
   {
      System.out.println("setMarkerSize");
      int aMarkerSize = 0;
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      instance.setMarkerSize(aMarkerSize);
   }

   /**
    * Test of isDrawLine method, of class AbstractGraphRow.
    */
   @Test
   public void testIsDrawLine()
   {
      System.out.println("isDrawLine");
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      boolean expResult = false;
      boolean result = instance.isDrawLine();
      assertEquals(expResult, result);
   }

   /**
    * Test of getMarkerSize method, of class AbstractGraphRow.
    */
   @Test
   public void testGetMarkerSize()
   {
      System.out.println("getMarkerSize");
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      int expResult = 0;
      int result = instance.getMarkerSize();
      assertEquals(expResult, result);
   }

   /**
    * Test of getColor method, of class AbstractGraphRow.
    */
   @Test
   public void testGetColor()
   {
      System.out.println("getColor");
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      Color expResult = Color.BLACK;
      Color result = instance.getColor();
      assertEquals(expResult, result);
   }

   /**
    * Test of setColor method, of class AbstractGraphRow.
    */
   @Test
   public void testSetColor()
   {
      System.out.println("setColor");
      Color nextColor = null;
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      instance.setColor(nextColor);
   }

   /**
    * Test of getLabel method, of class AbstractGraphRow.
    */
   @Test
   public void testGetLabel()
   {
      System.out.println("getLabel");
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      String expResult = "";
      String result = instance.getLabel();
      assertEquals(expResult, result);
   }

   /**
    * Test of setLabel method, of class AbstractGraphRow.
    */
   @Test
   public void testSetLabel()
   {
      System.out.println("setLabel");
      String label = "";
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      instance.setLabel(label);
   }

   /**
    * Test of getMaxX method, of class AbstractGraphRow.
    */
   @Test
   public void testGetMaxX()
   {
      System.out.println("getMaxX");
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      long expResult = Long.MIN_VALUE;
      long result = instance.getMaxX();
      assertEquals(expResult, result);
   }

   /**
    * Test of getMaxY method, of class AbstractGraphRow.
    */
   @Test
   public void testGetMaxY()
   {
      System.out.println("getMaxY");
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      double expResult = Double.MIN_VALUE;
      double result = instance.getMaxY();
      assertEquals(expResult, result, 0.0);
   }

   /**
    * Test of getMinX method, of class AbstractGraphRow.
    */
   @Test
   public void testGetMinX()
   {
      System.out.println("getMinX");
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      long expResult = Long.MAX_VALUE;
      long result = instance.getMinX();
      assertEquals(expResult, result);
   }

   /**
    * Test of getMinY method, of class AbstractGraphRow.
    */
   @Test
   public void testGetMinY()
   {
      System.out.println("getMinY");
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      double expResult = Double.MAX_VALUE;
      double result = instance.getMinY();
      assertEquals(expResult, result, 0.0);
   }

   /**
    * Test of add method, of class AbstractGraphRow.
    */
   @Test
   public void testAdd()
   {
      System.out.println("add");
      long X = 0L;
      double Y = 0.0;
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      instance.add(X, Y);
   }

   /**
    * Test of iterator method, of class AbstractGraphRow.
    */
   @Test
   public void testIterator()
   {
      System.out.println("iterator");
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      Iterator expResult = null;
      Iterator result = instance.iterator();
      assertEquals(expResult, result);
   }

   public class AbstractGraphRowImpl
        extends AbstractGraphRow
   {
      public Iterator<Entry<Long, Object>> iterator()
      {
         return null;
      }
   }

   /**
    * Test of isDrawValueLabel method, of class AbstractGraphRow.
    */
   @Test
   public void testIsDrawValueLabel()
   {
      System.out.println("isDrawValueLabel");
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      boolean expResult = false;
      boolean result = instance.isDrawValueLabel();
      assertEquals(expResult, result);
   }

   /**
    * Test of setDrawValueLabel method, of class AbstractGraphRow.
    */
   @Test
   public void testSetDrawValueLabel()
   {
      System.out.println("setDrawValueLabel");
      boolean drawValueLabel = false;
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      instance.setDrawValueLabel(drawValueLabel);
   }

   /**
    * Test of isShowInLegend method, of class AbstractGraphRow.
    */
   @Test
   public void testIsShowInLegend()
   {
      System.out.println("isShowInLegend");
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      boolean expResult = true;
      boolean result = instance.isShowInLegend();
      assertEquals(expResult, result);
   }

   /**
    * Test of setShowInLegend method, of class AbstractGraphRow.
    */
   @Test
   public void testSetShowInLegend()
   {
      System.out.println("setShowInLegend");
      boolean showInLegend = false;
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      instance.setShowInLegend(showInLegend);
   }

   /**
    * Test of isDrawOnChart method, of class AbstractGraphRow.
    */
   @Test
   public void testIsDrawOnChart()
   {
      System.out.println("isDrawOnChart");
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      boolean expResult = true;
      boolean result = instance.isDrawOnChart();
      assertEquals(expResult, result);
   }

   /**
    * Test of setDrawOnChart method, of class AbstractGraphRow.
    */
   @Test
   public void testSetDrawOnChart()
   {
      System.out.println("setDrawOnChart");
      boolean drawOnChart = false;
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      instance.setDrawOnChart(drawOnChart);
   }
}
