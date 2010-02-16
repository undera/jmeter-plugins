package kg.apc.jmeter.vizualizers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class GraphPanelChartElementTest
{
   public GraphPanelChartElementTest()
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
    * Test of add method, of class GraphPanelChartAverageElement.
    */
   @Test
   public void testAdd()
   {
      System.out.println("add");
      double yVal = 0.0;
      GraphPanelChartAverageElement instance = new GraphPanelChartAverageElement(yVal);
      instance.add(yVal);
   }

   /**
    * Test of getAvgValue method, of class GraphPanelChartAverageElement.
    */
   @Test
   public void testGetAvgValue()
   {
      System.out.println("getAvgValue");
      double expResult = 0.0;
      GraphPanelChartAverageElement instance = new GraphPanelChartAverageElement(expResult);
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
}
