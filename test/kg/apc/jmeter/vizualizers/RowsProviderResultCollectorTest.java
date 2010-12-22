/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.vizualizers;

import java.util.Collection;
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
public class RowsProviderResultCollectorTest {

    public RowsProviderResultCollectorTest() {
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
     * Test of getRowNames method, of class RowsProviderResultCollector.
     */
    @Test
    public void testGetRowNames()
    {
        System.out.println("getRowNames");
        RowsProviderResultCollector instance = null;
        Collection expResult = null;
        Collection result = instance.getRowNames();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRowByName method, of class RowsProviderResultCollector.
     */
    @Test
    public void testGetRowByName()
    {
        System.out.println("getRowByName");
        String name = "";
        RowsProviderResultCollector instance = null;
        AbstractGraphRow expResult = null;
        AbstractGraphRow result = instance.getRowByName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}