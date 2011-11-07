package kg.apc.jmeter.graphs;

import kg.apc.charting.rows.GraphRowOverallAverages;
import javax.swing.table.TableCellRenderer;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.rows.GraphRowAverages;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author undera
 */
public class ChartRowsTableTest {

    public ChartRowsTableTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addRow method, of class ChartRowsTable.
     */
    @Test
    public void testAddRow()
    {
        System.out.println("addRow");
        AbstractGraphRow row = new GraphRowAverages();
        ChartRowsTable instance = new ChartRowsTable(new JRowsSelectorPanel(new GraphPanel()));
        instance.addRow(row);
    }

    /**
     * Test of getCellRenderer method, of class ChartRowsTable.
     */
    @Test
    public void testGetCellRenderer()
    {
        System.out.println("getCellRenderer");
        int row = 0;
        int column = 0;
        ChartRowsTable instance = new ChartRowsTable(new JRowsSelectorPanel(new GraphPanel()));
        TableCellRenderer result = instance.getCellRenderer(row, column);
        assertNotNull(result);
    }

    /**
     * Test of clear method, of class ChartRowsTable.
     */
    @Test
    public void testClear()
    {
        System.out.println("clear");
        ChartRowsTable instance = new ChartRowsTable(new JRowsSelectorPanel(new GraphPanel()));
        instance.clear();
    }

   /**
    * Test of addRow method, of class ChartRowsTable.
    */
   @Test
   public void testAddRow2()
   {
      System.out.println("addRow");
      AbstractGraphRow row = new GraphRowOverallAverages();
      ChartRowsTable instance = new ChartRowsTable(null);
      instance.addRow(row);
   }

   /**
    * Test of getCellRenderer method, of class ChartRowsTable.
    */
   @Test
   public void testGetCellRenderer2()
   {
      System.out.println("getCellRenderer");
      ChartRowsTable instance = new ChartRowsTable(null);
      assertTrue(instance.getCellRenderer(0, 1) instanceof ColorRenderer);
      assertTrue(instance.getCellRenderer(1, 1) instanceof ColorRenderer);
      assertFalse(instance.getCellRenderer(1, 0) instanceof ColorRenderer);
   }

}