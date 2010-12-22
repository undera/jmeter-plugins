/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.charting;

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
public class AbstractGraphPanelChartElementTest
{
   /**
    * 
    */
   public AbstractGraphPanelChartElementTest()
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
    * Test of getValue method, of class AbstractGraphPanelChartElement.
    */
   @Test
   public void testGetValue()
   {
      System.out.println("getValue");
      AbstractGraphPanelChartElement instance = new AbstractGraphPanelChartElementImpl();
      double expResult = 0.0;
      double result = instance.getValue();
      assertEquals(expResult, result, 0.0);
   }

   /** {@inheritDoc} */
   public class AbstractGraphPanelChartElementImpl
         extends AbstractGraphPanelChartElement
   {
      /** {@inheritDoc} */
      public double getValue()
      {
         return 0.0;
      }
   }

    /**
     * Test of isPointRepresentative method, of class AbstractGraphPanelChartElement.
     */
    @Test
    public void testIsPointRepresentative()
    {
        System.out.println("isPointRepresentative");
        int limit = 0;
        AbstractGraphPanelChartElement instance = new AbstractGraphPanelChartElementImpl();
        boolean expResult = false;
        boolean result = instance.isPointRepresentative(limit);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
