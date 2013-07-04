package kg.apc.charting.elements;

import kg.apc.charting.elements.GraphPanelChartSumElement;
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
public class GraphPanelChartSumElementTest
{
   /**
    * 
    */
   public GraphPanelChartSumElementTest()
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
      GraphPanelChartSumElement instance = new GraphPanelChartSumElement();
      instance.add(yVal);
   }

   /**
    * Test of getValue method, of class GraphPanelChartAverageElement.
    */
   @Test
   public void testGetValue()
   {
      System.out.println("getValue");
      GraphPanelChartSumElement instance = new GraphPanelChartSumElement();
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
      GraphPanelChartSumElement instance = new GraphPanelChartSumElement();
      int expResult = 0;
      int result = instance.getCount();
      assertEquals(expResult, result);
   }
}
