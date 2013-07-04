package kg.apc.charting;

import kg.apc.charting.elements.GraphPanelChartExactElement;
import java.awt.Color;
import java.util.Iterator;
import java.util.Map.Entry;
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
public class AbstractGraphRowTest
{
   /**
    *
    */
   public AbstractGraphRowTest()
   {
   }

   /**
    *
    * @throws Exception
    */
   @BeforeClass
   public static void setUpClass()
        throws Exception
   {
   }

   /**
    *
    * @throws Exception
    */
   @AfterClass
   public static void tearDownClass()
        throws Exception
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
      Iterator result = instance.iterator();
      assertNotNull(result);
   }

   /**
    *
    */
   public class AbstractGraphRowImpl
        extends AbstractGraphRow implements Iterator<Entry<Long, AbstractGraphPanelChartElement>>
   {
      /**
       *
       * @return
       */
      public Iterator<Entry<Long, AbstractGraphPanelChartElement>> iterator()
      {
         return this;
      }

        @Override
        public int size()
        {
            return 1;
        }

        @Override
        public AbstractGraphPanelChartElement getElement(long value)
        {
            return new GraphPanelChartExactElement(0, 0);
        }

        public boolean hasNext()
        {
            return false;
        }

        public Entry<Long, AbstractGraphPanelChartElement> next()
        {
            return null;
        }

        public void remove()
        {
            throw new UnsupportedOperationException("Not supported yet.");
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

    /**
     * Test of setDrawThickLines method, of class AbstractGraphRow.
     */
    @Test
    public void testSetDrawThickLines()
    {
        System.out.println("setDrawThickLines");
        boolean isThickLine = false;
        AbstractGraphRow instance = new AbstractGraphRowImpl();
        instance.setDrawThickLines(isThickLine);
    }

    /**
     * Test of isDrawThickLines method, of class AbstractGraphRow.
     */
    @Test
    public void testIsDrawThickLines()
    {
        System.out.println("isDrawThickLines");
        AbstractGraphRow instance = new AbstractGraphRowImpl();
        boolean expResult = false;
        boolean result = instance.isDrawThickLines();
        assertEquals(expResult, result);
    }

   @Test
   public void testSetDrawBar()
   {
      System.out.println("setDrawBar");
      boolean b = false;
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      instance.setDrawBar(b);
   }

   @Test
   public void testIsDrawBar()
   {
      System.out.println("isDrawBar");
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      boolean expResult = false;
      boolean result = instance.isDrawBar();
      assertEquals(expResult, result);
   }

   @Test
   public void testSize()
   {
      System.out.println("size");
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      int expResult = 1;
      int result = instance.size();
      assertEquals(expResult, result);
   }

    /**
     * Test of getGranulationValue method, of class AbstractGraphRow.
     */
    @Test
    public void testGetGranulationValue()
    {
        System.out.println("getGranulationValue");
        AbstractGraphRow instance = new AbstractGraphRowImpl();
        int expResult = 0;
        int result = instance.getGranulationValue();
        assertEquals(expResult, result);
    }

    /**
     * Test of setGranulationValue method, of class AbstractGraphRow.
     */
    @Test
    public void testSetGranulationValue()
    {
        System.out.println("setGranulationValue");
        int value = 500;
        AbstractGraphRow instance = new AbstractGraphRowImpl();
        instance.setGranulationValue(value);
    }

    /**
     * Test of setExcludeOutOfRangeValues method, of class AbstractGraphRow.
     */
    @Test
    public void testSetExcludeOutOfRangeValues()
    {
        System.out.println("setExcludeOutOfRangeValues");
        boolean excludeOutOfRangeValues = false;
        AbstractGraphRow instance = new AbstractGraphRowImpl();
        instance.setExcludeOutOfRangeValues(excludeOutOfRangeValues);
    }

    /**
     * Test of getElement method, of class AbstractGraphRow.
     */
    @Test
    public void testGetElement()
    {
        System.out.println("getElement");
        long value = 0L;
        AbstractGraphRow instance = new AbstractGraphRowImpl();
        AbstractGraphPanelChartElement result = instance.getElement(value);
        assertNotNull(result);
    }

    /**
     * Test of getMinMaxY method, of class AbstractGraphRow.
     */
    @Test
    public void testGetMinMaxY()
    {
        System.out.println("getMinMaxY");
        int maxPoints = 0;
        AbstractGraphRow instance = new AbstractGraphRowImpl();
        double[] result = instance.getMinMaxY(maxPoints);
        assertNotNull(result);
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
     * Test of instantiateNewRow method, of class AbstractGraphRow.
     */
    @Test
    public void testInstantiateNewRow()
    {
        System.out.println("instantiateNewRow");
        int rowType = 0;
        AbstractGraphRow result = AbstractGraphRow.instantiateNewRow(rowType);
        assertNotNull(result);
    }

    /**
     * Test of getFirstTime method, of class AbstractGraphRow.
     */
    @Test
    public void testGetFirstTime()
    {
        System.out.println("getFirstTime");
        AbstractGraphRow instance = new AbstractGraphRowImpl();
        long expResult = Long.MIN_VALUE;
        long result = instance.getFirstTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLowerElement method, of class AbstractGraphRow.
     */
    @Test
    public void testGetLowerElement() {
        System.out.println("getLowerElement");
        long value = 0L;
        AbstractGraphRow instance = new AbstractGraphRowImpl();
        try {
        AbstractGraphPanelChartElement result = instance.getLowerElement(value);
        fail("Exception expected");
        }
        catch(UnsupportedOperationException e)
        {}
    }

    /**
     * Test of getHigherKey method, of class AbstractGraphRow.
     */
    @Test
    public void testGetHigherKey() {
        System.out.println("getHigherKey");
        long value = 0L;
        AbstractGraphRow instance = new AbstractGraphRowImpl();
        Long expResult = null;
        try{
        Long result = instance.getHigherKey(value);
        fail("Exception expected");
        }
        catch(UnsupportedOperationException e)
        {}
    }

   /**
    * Test of isDrawSpline method, of class AbstractGraphRow.
    */
   @Test
   public void testIsDrawSpline() {
      System.out.println("isDrawSpline");
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      boolean expResult = false;
      boolean result = instance.isDrawSpline();
      assertEquals(expResult, result);
   }

   /**
    * Test of setDrawSpline method, of class AbstractGraphRow.
    */
   @Test
   public void testSetDrawSpline() {
      System.out.println("setDrawSpline");
      boolean drawSpline = true;
      AbstractGraphRow instance = new AbstractGraphRowImpl();
      instance.setDrawSpline(drawSpline);
   }
}
