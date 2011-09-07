package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.graphs.CompositeNotifierInterface;
import java.util.Iterator;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.rows.GraphRowAverages;
import kg.apc.emulators.TestJMeterUtils;
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
public class CompositeModelTest {
    private CompositeNotifierInterface notifier;

    public CompositeModelTest() {
        notifier = new JCompositeRowsSelectorPanel(new CompositeModel(), new CompositeGraphGui());
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        TestJMeterUtils.createJmeterEnv();
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
     * Test of setNotifier method, of class CompositeModel.
     */
    @Test
    public void testSetNotifier()
    {
        System.out.println("setNotifier");
        CompositeModel instance = new CompositeModel();
        instance.setNotifier(notifier);
    }

    /**
     * Test of clear method, of class CompositeModel.
     */
    @Test
    public void testClear()
    {
        System.out.println("clear");
        CompositeModel instance = new CompositeModel();
        instance.clear();
    }

    /**
     * Test of addRow method, of class CompositeModel.
     */
    @Test
    public void testAddRow()
    {
        System.out.println("addRow");
        String vizualizerName = "Test";
        AbstractGraphRow row = new GraphRowAverages();
        CompositeModel instance = new CompositeModel();
        instance.setNotifier(notifier);
        instance.addRow(vizualizerName, row);
    }

    /**
     * Test of clearRows method, of class CompositeModel.
     */
    @Test
    public void testClearRows()
    {
        System.out.println("clearRows");
        String vizualizerName = "";
        CompositeModel instance = new CompositeModel();
        instance.setNotifier(notifier);
        instance.clearRows(vizualizerName);
    }

    /**
     * Test of containsVisualizer method, of class CompositeModel.
     */
    @Test
    public void testContainsVisualizer()
    {
        System.out.println("containsVisualizer");
        String vizualizerName = "";
        CompositeModel instance = new CompositeModel();
        boolean expResult = false;
        boolean result = instance.containsVisualizer(vizualizerName);
        assertEquals(expResult, result);
    }

    /**
     * Test of getVizualizerNamesIterator method, of class CompositeModel.
     */
    @Test
    public void testGetVizualizerNamesIterator()
    {
        System.out.println("getVizualizerNamesIterator");
        CompositeModel instance = new CompositeModel();
        Iterator result = instance.getVizualizerNamesIterator();
        assertNotNull(result);
    }

    /**
     * Test of getRowsIterator method, of class CompositeModel.
     */
    @Test
    public void testGetRowsIterator()
    {
        System.out.println("getRowsIterator");
        String vizualizerName = "";
        CompositeModel instance = new CompositeModel();
        Iterator result = instance.getRowsIterator(vizualizerName);
        assertNotNull(result);
    }

    /**
     * Test of getRow method, of class CompositeModel.
     */
    @Test
    public void testGetRow()
    {
        System.out.println("getRow");
        String testName = "";
        String rowName = "";
        CompositeModel instance = new CompositeModel();
        instance.setNotifier(notifier);
        AbstractGraphRow result = instance.getRow(testName, rowName);
        assertNull(result);

        instance.addRow(rowName, new GraphRowAverages());
        result = instance.getRow(testName, rowName);
        assertNotNull(result);
    }

}