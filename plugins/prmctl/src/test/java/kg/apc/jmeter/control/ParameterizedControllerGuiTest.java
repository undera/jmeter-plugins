/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.control;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.testelement.TestElement;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParameterizedControllerGuiTest {
    private ParameterizedControllerGui instance;

    @BeforeClass
    public static void setUpClass()
            throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Before
    public void setUp() {
        instance = new ParameterizedControllerGui();
    }

    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        TestElement expResult = new ParameterizedController();
        TestElement result = instance.createTestElement();
        assertEquals(expResult.getClass(), result.getClass());
    }

    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement te = new ParameterizedController();
        instance.modifyTestElement(te);
    }

    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        String expResult = instance.getClass().getName();
        String result = instance.getLabelResource();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        instance.clearGui();
    }

    @Test
    public void testConfigure() {
        System.out.println("configure");
        ParameterizedController te = new ParameterizedController();
        te.setUserDefinedVariables(new Arguments());
        te.setName("test");
        te.setComment("test");

        instance.configure(te);
    }
}
