package kg.apc.jmeter.vizualizers;

import javax.swing.table.TableCellRenderer;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.AbstractGraphRowTest.AbstractGraphRowImpl;
import kg.apc.jmeter.charting.GraphRowAverages;
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
        ChartRowsTable instance = new ChartRowsTable(new JRowsSelectorPanel());
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
        ChartRowsTable instance = new ChartRowsTable(new JRowsSelectorPanel());
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
        ChartRowsTable instance = new ChartRowsTable(new JRowsSelectorPanel());
        instance.clear();
    }

}