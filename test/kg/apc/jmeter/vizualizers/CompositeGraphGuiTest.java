/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.vizualizers;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
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
public class CompositeGraphGuiTest {

    public CompositeGraphGuiTest() {
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
     * Test of getSettingsPanel method, of class CompositeGraphGui.
     */
    @Test
    public void testGetSettingsPanel()
    {
        System.out.println("getSettingsPanel");
        CompositeGraphGui instance = new CompositeGraphGui();
        JSettingsPanel expResult = null;
        JSettingsPanel result = instance.getSettingsPanel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLabelResource method, of class CompositeGraphGui.
     */
    @Test
    public void testGetLabelResource()
    {
        System.out.println("getLabelResource");
        CompositeGraphGui instance = new CompositeGraphGui();
        String expResult = "";
        String result = instance.getLabelResource();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStaticLabel method, of class CompositeGraphGui.
     */
    @Test
    public void testGetStaticLabel()
    {
        System.out.println("getStaticLabel");
        CompositeGraphGui instance = new CompositeGraphGui();
        String expResult = "";
        String result = instance.getStaticLabel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createTestElement method, of class CompositeGraphGui.
     */
    @Test
    public void testCreateTestElement()
    {
        System.out.println("createTestElement");
        CompositeGraphGui instance = new CompositeGraphGui();
        TestElement expResult = null;
        TestElement result = instance.createTestElement();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of configure method, of class CompositeGraphGui.
     */
    @Test
    public void testConfigure()
    {
        System.out.println("configure");
        TestElement te = null;
        CompositeGraphGui instance = new CompositeGraphGui();
        instance.configure(te);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateGui method, of class CompositeGraphGui.
     */
    @Test
    public void testUpdateGui()
    {
        System.out.println("updateGui");
        CompositeGraphGui instance = new CompositeGraphGui();
        instance.updateGui();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class CompositeGraphGui.
     */
    @Test
    public void testAdd()
    {
        System.out.println("add");
        SampleResult sr = null;
        CompositeGraphGui instance = new CompositeGraphGui();
        instance.add(sr);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}