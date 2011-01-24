/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.vizualizers;

import java.util.Iterator;
import javax.swing.JPanel;
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
public class JCompositeRowsSelectorPanelTest {

    public JCompositeRowsSelectorPanelTest() {
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
     * Test of updateGraph method, of class JCompositeRowsSelectorPanel.
     */
    @Test
    public void testUpdateGraph()
    {
        System.out.println("updateGraph");
        JCompositeRowsSelectorPanel instance = null;
        instance.updateGraph();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getItems method, of class JCompositeRowsSelectorPanel.
     */
    @Test
    public void testGetItems()
    {
        System.out.println("getItems");
        JCompositeRowsSelectorPanel instance = null;
        Iterator expResult = null;
        Iterator result = instance.getItems();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clearData method, of class JCompositeRowsSelectorPanel.
     */
    @Test
    public void testClearData()
    {
        System.out.println("clearData");
        JCompositeRowsSelectorPanel instance = null;
        instance.clearData();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGraphDisplayPanel method, of class JCompositeRowsSelectorPanel.
     */
    @Test
    public void testGetGraphDisplayPanel()
    {
        System.out.println("getGraphDisplayPanel");
        JCompositeRowsSelectorPanel instance = null;
        JPanel expResult = null;
        JPanel result = instance.getGraphDisplayPanel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isPreview method, of class JCompositeRowsSelectorPanel.
     */
    @Test
    public void testIsPreview()
    {
        System.out.println("isPreview");
        JCompositeRowsSelectorPanel instance = null;
        boolean expResult = false;
        boolean result = instance.isPreview();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of refresh method, of class JCompositeRowsSelectorPanel.
     */
    @Test
    public void testRefresh()
    {
        System.out.println("refresh");
        JCompositeRowsSelectorPanel instance = null;
        instance.refresh();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}