package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.GraphRowOverallAverages;
import kg.apc.jmeter.charting.AbstractGraphRow;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChartRowsTableTest
{
   public ChartRowsTableTest()
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
    * Test of addRow method, of class ChartRowsTable.
    */
   @Test
   public void testAddRow()
   {
      System.out.println("addRow");
      AbstractGraphRow row = new GraphRowOverallAverages();
      ChartRowsTable instance = new ChartRowsTable();
      instance.addRow(row);
   }

   /**
    * Test of getCellRenderer method, of class ChartRowsTable.
    */
   @Test
   public void testGetCellRenderer()
   {
      System.out.println("getCellRenderer");
      ChartRowsTable instance = new ChartRowsTable();
      assertTrue(instance.getCellRenderer(0, 1) instanceof ColorRenderer);
      assertTrue(instance.getCellRenderer(1, 1) instanceof ColorRenderer);
      assertFalse(instance.getCellRenderer(1, 0) instanceof ColorRenderer);
   }
}
