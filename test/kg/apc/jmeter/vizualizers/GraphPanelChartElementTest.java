package kg.apc.jmeter.vizualizers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class GraphPanelChartElementTest {

    public GraphPanelChartElementTest() {
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
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

   /**
    * Test of add method, of class GraphPanelChartElement.
    */
   @Test
   public void testAdd()
   {
      System.out.println("add");
      double yVal = 0.0;
      GraphPanelChartElement instance = new GraphPanelChartElement(yVal);
      instance.add(yVal);
   }

}