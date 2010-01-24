/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.vizualizers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author APC
 */
public class GraphPanelChartRowTest {

    public GraphPanelChartRowTest() {
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
    * Test of add method, of class GraphPanelChartRow.
    */
   @Test
   public void testAdd()
   {
      System.out.println("add");
      double xVal = 1.0;
      double yVal = 1.0;
      GraphPanelChartRow instance = new GraphPanelChartRow();
      instance.add(xVal, yVal);
   }
}