/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.charting;

import java.util.Iterator;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author z000205
 */
public class RowsCollectorTest {

    public RowsCollectorTest() {
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
     * Test of getInstance method, of class RowsCollector.
     */
    @Test
    public void testGetInstance()
    {
        System.out.println("getInstance");
        RowsCollector expResult = null;
        RowsCollector result = RowsCollector.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addRow method, of class RowsCollector.
     */
    @Test
    public void testAddRow()
    {
        System.out.println("addRow");
        String vizualizerName = "";
        AbstractGraphRow row = null;
        RowsCollector instance = null;
        instance.addRow(vizualizerName, row);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clearRows method, of class RowsCollector.
     */
    @Test
    public void testClearRows()
    {
        System.out.println("clearRows");
        String vizualizerName = "";
        RowsCollector instance = null;
        instance.clearRows(vizualizerName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clear method, of class RowsCollector.
     */
    @Test
    public void testClear()
    {
        System.out.println("clear");
        RowsCollector instance = null;
        instance.clear();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getVizualizerNamesIterator method, of class RowsCollector.
     */
    @Test
    public void testGetVizualizerNamesIterator()
    {
        System.out.println("getVizualizerNamesIterator");
        RowsCollector instance = null;
        Iterator expResult = null;
        Iterator result = instance.getVizualizerNamesIterator();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRowsIterator method, of class RowsCollector.
     */
    @Test
    public void testGetRowsIterator()
    {
        System.out.println("getRowsIterator");
        String vizualizerNames = "";
        RowsCollector instance = null;
        Iterator expResult = null;
        Iterator result = instance.getRowsIterator(vizualizerNames);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRow method, of class RowsCollector.
     */
    @Test
    public void testGetRow()
    {
        System.out.println("getRow");
        String testName = "";
        String rowName = "";
        RowsCollector instance = null;
        AbstractGraphRow expResult = null;
        AbstractGraphRow result = instance.getRow(testName, rowName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of testStarted method, of class RowsCollector.
     */
    @Test
    public void testTestStarted_0args()
    {
        System.out.println("testStarted");
        RowsCollector instance = null;
        instance.testStarted();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of testStarted method, of class RowsCollector.
     */
    @Test
    public void testTestStarted_String()
    {
        System.out.println("testStarted");
        String string = "";
        RowsCollector instance = null;
        instance.testStarted(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of testEnded method, of class RowsCollector.
     */
    @Test
    public void testTestEnded_0args()
    {
        System.out.println("testEnded");
        RowsCollector instance = null;
        instance.testEnded();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of testEnded method, of class RowsCollector.
     */
    @Test
    public void testTestEnded_String()
    {
        System.out.println("testEnded");
        String string = "";
        RowsCollector instance = null;
        instance.testEnded(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of testIterationStart method, of class RowsCollector.
     */
    @Test
    public void testTestIterationStart()
    {
        System.out.println("testIterationStart");
        LoopIterationEvent lie = null;
        RowsCollector instance = null;
        instance.testIterationStart(lie);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

  

}