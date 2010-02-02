package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ColorRendererTest {

    public ColorRendererTest() {
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
    * Test of getTableCellRendererComponent method, of class ColorRenderer.
    */
   @Test
   public void testGetTableCellRendererComponent()
   {
      System.out.println("getTableCellRendererComponent");
      JTable table = new JTable();
      Object color = Color.red;
      boolean isSelected = false;
      boolean hasFocus = false;
      int row = 0;
      int column = 0;
      ColorRenderer instance = new ColorRenderer(isSelected);
      Component result = instance.getTableCellRendererComponent(table, color, isSelected, hasFocus, row, column);
      assertNotNull(result);
   }

}