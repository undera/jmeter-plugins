/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.vizualizers;

import java.util.Iterator;
import javax.swing.JPanel;
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
public class JCompositeRowsSelectorPanelTest {

    public JCompositeRowsSelectorPanelTest() {
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
     * Test of updateGraph method, of class JCompositeRowsSelectorPanel.
     */
    @Test
    public void testUpdateGraph()
    {
        System.out.println("updateGraph");
        JCompositeRowsSelectorPanel instance = new JCompositeRowsSelectorPanel(new CompositeModel(), new CompositeGraphGui());
        instance.updateGraph();
    }

    /**
     * Test of getItems method, of class JCompositeRowsSelectorPanel.
     */
    @Test
    public void testGetItems()
    {
        System.out.println("getItems");
        JCompositeRowsSelectorPanel instance = new JCompositeRowsSelectorPanel(new CompositeModel(), new CompositeGraphGui());
        Iterator result = instance.getItems();
        assertNotNull(result);
    }

    /**
     * Test of clearData method, of class JCompositeRowsSelectorPanel.
     */
    @Test
    public void testClearData()
    {
        System.out.println("clearData");
        JCompositeRowsSelectorPanel instance = new JCompositeRowsSelectorPanel(new CompositeModel(), new CompositeGraphGui());
        instance.clearData();
    }

    /**
     * Test of getGraphDisplayPanel method, of class JCompositeRowsSelectorPanel.
     */
    @Test
    public void testGetGraphDisplayPanel()
    {
        System.out.println("getGraphDisplayPanel");
        JCompositeRowsSelectorPanel instance = new JCompositeRowsSelectorPanel(new CompositeModel(), new CompositeGraphGui());
        JPanel result = instance.getGraphDisplayPanel();
        assertNotNull(result);
    }

    /**
     * Test of isPreview method, of class JCompositeRowsSelectorPanel.
     */
    @Test
    public void testIsPreview()
    {
        System.out.println("isPreview");
        JCompositeRowsSelectorPanel instance = new JCompositeRowsSelectorPanel(new CompositeModel(), new CompositeGraphGui());
        boolean expResult = true;
        boolean result = instance.isPreview();
        assertEquals(expResult, result);
    }

    /**
     * Test of refresh method, of class JCompositeRowsSelectorPanel.
     */
    @Test
    public void testRefresh()
    {
        System.out.println("refresh");
        JCompositeRowsSelectorPanel instance = new JCompositeRowsSelectorPanel(new CompositeModel(), new CompositeGraphGui());
        instance.refresh();
    }

    /**
     * Test of addItemsToComposite method, of class JCompositeRowsSelectorPanel.
     */
    @Test
    public void testAddItemsToComposite()
    {
        System.out.println("addItemsToComposite");
        String testplan = "testplan";
        String row = "row";
        JCompositeRowsSelectorPanel instance = new JCompositeRowsSelectorPanel(new CompositeModel(), new CompositeGraphGui());;
        instance.addItemsToComposite(testplan, row);
    }

}