/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.reporters;

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
public class ConsoleStatusLoggerGuiTest {

    public ConsoleStatusLoggerGuiTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        TestJMeterUtils.createJmeterEnv();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getStaticLabel method, of class ConsoleStatusLoggerGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        ConsoleStatusLoggerGui instance = new ConsoleStatusLoggerGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of getLabelResource method, of class ConsoleStatusLoggerGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        ConsoleStatusLoggerGui instance = new ConsoleStatusLoggerGui();
        String result = instance.getLabelResource();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of createTestElement method, of class ConsoleStatusLoggerGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        ConsoleStatusLoggerGui instance = new ConsoleStatusLoggerGui();
        TestElement result = instance.createTestElement();
        assertTrue(result instanceof ConsoleStatusLogger);
    }

    /**
     * Test of modifyTestElement method, of class ConsoleStatusLoggerGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement te = new ConsoleStatusLogger();
        ConsoleStatusLoggerGui instance = new ConsoleStatusLoggerGui();
        instance.modifyTestElement(te);
    }
}
