/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.vizualizers;

import java.util.Iterator;
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
public class CompositeModelTest {

    public CompositeModelTest() {
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
     * Test of setNotifier method, of class CompositeModel.
     */
    @Test
    public void testSetNotifier()
    {
        System.out.println("setNotifier");
        CompositeNotifierInterface notifier = null;
        CompositeModel instance = new CompositeModel();
        instance.setNotifier(notifier);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addRow method, of class CompositeModel.
     */
    @Test
    public void testAddRow()
    {
        System.out.println("addRow");
        String vizualizerName = "";
        AbstractGraphRow row = null;
        CompositeModel instance = new CompositeModel();
        instance.addRow(vizualizerName, row);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        instance.clearRows(vizualizerName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getVizualizerNamesIterator method, of class CompositeModel.
     */
    @Test
    public void testGetVizualizerNamesIterator()
    {
        System.out.println("getVizualizerNamesIterator");
        CompositeModel instance = new CompositeModel();
        Iterator expResult = null;
        Iterator result = instance.getVizualizerNamesIterator();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        Iterator expResult = null;
        Iterator result = instance.getRowsIterator(vizualizerName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        AbstractGraphRow expResult = null;
        AbstractGraphRow result = instance.getRow(testName, rowName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}