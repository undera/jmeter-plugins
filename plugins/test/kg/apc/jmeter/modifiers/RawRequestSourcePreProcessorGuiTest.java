/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.modifiers;

import kg.apc.emulators.TestJMeterUtils;
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
public class RawRequestSourcePreProcessorGuiTest {

    public RawRequestSourcePreProcessorGuiTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
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
     * Test of getStaticLabel method, of class RawRequestSourcePreProcessorGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        RawRequestSourcePreProcessorGui instance = new RawRequestSourcePreProcessorGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length()>0);
    }

    /**
     * Test of getLabelResource method, of class RawRequestSourcePreProcessorGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        RawRequestSourcePreProcessorGui instance = new RawRequestSourcePreProcessorGui();
        String result = instance.getLabelResource();
        assertTrue(result.length()>0);
    }

    /**
     * Test of configure method, of class RawRequestSourcePreProcessorGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement element = new RawRequestSourcePreProcessor();
        RawRequestSourcePreProcessorGui instance = new RawRequestSourcePreProcessorGui();
        instance.configure(element);
    }

    /**
     * Test of createTestElement method, of class RawRequestSourcePreProcessorGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        RawRequestSourcePreProcessorGui instance = new RawRequestSourcePreProcessorGui();
        TestElement result = instance.createTestElement();
        assertTrue(result instanceof RawRequestSourcePreProcessor);
    }

    /**
     * Test of modifyTestElement method, of class RawRequestSourcePreProcessorGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement te = new RawRequestSourcePreProcessor();
        RawRequestSourcePreProcessorGui instance = new RawRequestSourcePreProcessorGui();
        instance.modifyTestElement(te);
    }

    /**
     * Test of clearGui method, of class RawRequestSourcePreProcessorGui.
     */
    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        RawRequestSourcePreProcessorGui instance = new RawRequestSourcePreProcessorGui();
        instance.clearGui();
    }

}