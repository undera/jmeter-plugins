/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.vizualizers;

import org.apache.jmeter.samplers.SampleResult;
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
public class CompositeGraphTest {

    public CompositeGraphTest() {
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
     * Test of getSettingsPanel method, of class CompositeGraph.
     */
    @Test
    public void testGetSettingsPanel()
    {
        System.out.println("getSettingsPanel");
        CompositeGraph instance = new CompositeGraph();
        JSettingsPanel expResult = null;
        JSettingsPanel result = instance.getSettingsPanel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLabelResource method, of class CompositeGraph.
     */
    @Test
    public void testGetLabelResource()
    {
        System.out.println("getLabelResource");
        CompositeGraph instance = new CompositeGraph();
        String expResult = "";
        String result = instance.getLabelResource();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStaticLabel method, of class CompositeGraph.
     */
    @Test
    public void testGetStaticLabel()
    {
        System.out.println("getStaticLabel");
        CompositeGraph instance = new CompositeGraph();
        String expResult = "";
        String result = instance.getStaticLabel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class CompositeGraph.
     */
    @Test
    public void testAdd()
    {
        System.out.println("add");
        SampleResult sr = null;
        CompositeGraph instance = new CompositeGraph();
        instance.add(sr);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}