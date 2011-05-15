/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.graphs;

import kg.apc.jmeter.graphs.CompositeNotifierInterface;
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
public class CompositeNotifierInterfaceTest {

    public CompositeNotifierInterfaceTest() {
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
     * Test of refresh method, of class CompositeNotifierInterface.
     */
    @Test
    public void testRefresh()
    {
        System.out.println("refresh");
        CompositeNotifierInterface instance = new CompositeNotifierInterfaceImpl();
        instance.refresh();
    }

    public class CompositeNotifierInterfaceImpl implements CompositeNotifierInterface
    {

        public void refresh()
        {
        }
    }

}