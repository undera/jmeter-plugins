package kg.apc.jmeter.graphs;

import kg.apc.jmeter.graphs.HeaderAsTextRenderer;
import kg.apc.jmeter.graphs.ChartRowsTable;
import java.awt.Component;
import javax.swing.JTable;
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
public class HeaderAsTextRendererTest
{
   /**
    *
    */
   public HeaderAsTextRendererTest()
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
    * Test of getTableCellRendererComponent method, of class HeaderAsTextRenderer.
    */
   @Test
   public void testGetTableCellRendererComponent()
   {
      System.out.println("getTableCellRendererComponent");
      JTable table = new ChartRowsTable(null);
      Object value = new String();
      boolean isSelected = false;
      boolean hasFocus = false;
      int row = 0;
      int column = 0;
      HeaderAsTextRenderer instance = new HeaderAsTextRenderer();
      Component result = instance.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      assertTrue(result instanceof HeaderAsTextRenderer);
   }

   /**
    * Test of getText method, of class HeaderAsTextRenderer.
    */
   @Test
   public void testGetText()
   {
      System.out.println("getText");
      Object value = null;
      int row = 0;
      int column = 0;
      HeaderAsTextRenderer instance = new HeaderAsTextRenderer();
      String expResult = "";
      String result = instance.getText(value, row, column);
      assertEquals(expResult, result);
   }
}
