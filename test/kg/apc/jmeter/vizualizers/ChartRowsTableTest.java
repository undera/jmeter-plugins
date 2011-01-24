package kg.apc.jmeter.vizualizers;

import javax.swing.table.TableCellRenderer;
import kg.apc.jmeter.charting.AbstractGraphRow;
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
        AbstractGraphRow row = null;
        ChartRowsTable instance = null;
        instance.addRow(row);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        ChartRowsTable instance = null;
        TableCellRenderer expResult = null;
        TableCellRenderer result = instance.getCellRenderer(row, column);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clear method, of class ChartRowsTable.
     */
    @Test
    public void testClear()
    {
        System.out.println("clear");
        ChartRowsTable instance = null;
        instance.clear();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}