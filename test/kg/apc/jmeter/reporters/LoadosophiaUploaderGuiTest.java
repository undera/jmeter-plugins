/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.reporters;

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
public class LoadosophiaUploaderGuiTest {
    
    public LoadosophiaUploaderGuiTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getStaticLabel method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        String expResult = "";
        String result = instance.getStaticLabel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLabelResource method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        String expResult = "";
        String result = instance.getLabelResource();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createTestElement method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        TestElement expResult = null;
        TestElement result = instance.createTestElement();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of modifyTestElement method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement te = null;
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        instance.modifyTestElement(te);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of configure method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement element = null;
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        instance.configure(element);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
