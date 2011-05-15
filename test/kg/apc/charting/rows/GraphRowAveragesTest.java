package kg.apc.charting.rows;

import kg.apc.charting.AbstractGraphPanelChartElement;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.elements.GraphPanelChartAverageElement;
import kg.apc.charting.rows.GraphRowAverages;
import java.util.Iterator;
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
public class GraphRowAveragesTest
{
   private GraphRowAverages instance;

   /**
    *
    */
   public GraphRowAveragesTest()
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
      instance = new GraphRowAverages();
      instance.setDrawLine(true);
      instance.setMarkerSize(AbstractGraphRow.MARKER_SIZE_BIG);
   }

   /**
    *
    */
   @After
   public void tearDown()
   {
   }

   /**
    * Test of add method, of class AbstractGraphRow.
    */
   @Test
   public void testAdd()
   {
      System.out.println("add");
      long xVal = 10;
      double yVal = 5.0;
      instance.add(xVal, yVal);

      double[] minMax = instance.getMinMaxY(-1);

      assertEquals(xVal, instance.getMinX());
      assertEquals(xVal, instance.getMaxX());
      assertEquals(yVal, minMax[0], 0.001);
      assertEquals(yVal, minMax[1], 0.001);
   }

   /**
    * Test of iterator method, of class GraphRowAverages.
    */
   @Test
   public void testIterator()
   {
      System.out.println("iterator");
      Iterator result = instance.iterator();
      assertNotNull(result);
   }

   @Test
   public void testSize()
   {
      System.out.println("size");
      int expResult = 2;
      instance.add(1000, 3);
      instance.add(1000, 2);
      instance.add(2000, 1);

      int result = instance.size();

      assertEquals(expResult, result);
   }

    /**
     * Test of getMaxY method, of class GraphRowAverages.
     */
    @Test
    public void testGetMaxY()
    {
        System.out.println("getMaxY");

        instance.add(1000, 3);
        instance.add(1000, 2);
        instance.add(1000, 1);

        double expResult = 2.0;
        double[] result = instance.getMinMaxY(-1);
        assertEquals(expResult, result[1], 0.0);
    }

    /**
     * Test of getElement method, of class GraphRowAverages.
     */
    @Test
    public void testGetElement()
    {
        System.out.println("getElement");
        long value = 100L;
        AbstractGraphPanelChartElement expResult = new GraphPanelChartAverageElement(10);
        instance.add(value, 10);
        AbstractGraphPanelChartElement result = instance.getElement(value);
        assertTrue(instance.getElement(value).getValue() == expResult.getValue());
    }

    /**
     * Test of getLowerElement method, of class GraphRowAverages.
     */
    @Test
    public void testGetLowerElement() {
        System.out.println("getLowerElement");
        long value = 0L;
        AbstractGraphPanelChartElement expResult = null;
        AbstractGraphPanelChartElement result = instance.getLowerElement(value);
        assertEquals(expResult, result);
    }
}
