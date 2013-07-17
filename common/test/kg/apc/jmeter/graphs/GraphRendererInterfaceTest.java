/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.graphs;

import kg.apc.jmeter.graphs.GraphRendererInterface;
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
public class GraphRendererInterfaceTest {

    public GraphRendererInterfaceTest() {
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
     * Test of getGraphDisplayPanel method, of class GraphRendererInterface.
     */
    @Test
    public void testGetGraphDisplayPanel()
    {
        System.out.println("getGraphDisplayPanel");
        GraphRendererInterface instance = new GraphRendererInterfaceImpl();
        JPanel expResult = null;
        JPanel result = instance.getGraphDisplayPanel();
        assertEquals(expResult, result);
    }

    /**
     * Test of isPreview method, of class GraphRendererInterface.
     */
    @Test
    public void testIsPreview()
    {
        System.out.println("isPreview");
        GraphRendererInterface instance = new GraphRendererInterfaceImpl();
        boolean expResult = false;
        boolean result = instance.isPreview();
        assertEquals(expResult, result);
    }

    public class GraphRendererInterfaceImpl implements GraphRendererInterface
    {

        public JPanel getGraphDisplayPanel()
        {
            return null;
        }

        public boolean isPreview()
        {
            return false;
        }
    }

}