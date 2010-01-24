package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GraphPanelChartRowTest
{
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
      double xVal = 1.0;
      double yVal = 1.0;
      GraphPanelChartRow instance = new GraphPanelChartRow("test", Color.black);
      instance.add(xVal, yVal);
   }
}
