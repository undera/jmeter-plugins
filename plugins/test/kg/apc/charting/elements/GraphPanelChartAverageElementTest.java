package kg.apc.charting.elements;

import kg.apc.charting.elements.GraphPanelChartAverageElement;
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
public class GraphPanelChartAverageElementTest
{
   /**
    *
    */
   public GraphPanelChartAverageElementTest()
   {
   }

   /**
    *
    * @throws Exception
    */
   @BeforeClass
   public static void setUpClass() throws Exception
   {
   }

   /**
    * 
    * @throws Exception
    */
   @AfterClass
   public static void tearDownClass() throws Exception
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
    * Test of add method, of class GraphPanelChartAverageElement.
    */
   @Test
   public void testAdd()
   {
      System.out.println("add");
      double yVal = 0.0;
      GraphPanelChartAverageElement instance = new GraphPanelChartAverageElement();
      instance.add(yVal);
   }

   /**
    * Test of getValue method, of class GraphPanelChartAverageElement.
    */
   @Test
   public void testGetValue()
   {
      System.out.println("getValue");
      GraphPanelChartAverageElement instance = new GraphPanelChartAverageElement();
      double expResult = 0.0;
      double result = instance.getValue();
      assertEquals(expResult, result, 0.0);
   }

   /**
    * Test of getCount method, of class GraphPanelChartAverageElement.
    */
   @Test
   public void testGetCount()
   {
      System.out.println("getCount");
      GraphPanelChartAverageElement instance = new GraphPanelChartAverageElement();
      int expResult = 0;
      int result = instance.getCount();
      assertEquals(expResult, result);
   }

    /**
     * Test of isPointRepresentative method, of class GraphPanelChartAverageElement.
     */
    @Test
    public void testIsPointRepresentative()
    {
        System.out.println("isPointRepresentative");
        int limit = 5;
        GraphPanelChartAverageElement instance = new GraphPanelChartAverageElement();
        for(int i=0; i<3; i++)
        {
            instance.add(10);
        }
        boolean expResult = false;
        boolean result = instance.isPointRepresentative(limit);
        assertEquals(expResult, result);

        for(int i=0; i<3; i++)
        {
            instance.add(10);
        }
        expResult = true;
        result = instance.isPointRepresentative(limit);
        assertEquals(expResult, result);
    }
}
