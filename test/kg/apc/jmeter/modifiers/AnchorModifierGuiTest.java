package kg.apc.jmeter.modifiers;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.testelement.TestElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author undera
 */
public class AnchorModifierGuiTest {

    public AnchorModifierGuiTest() {
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
     * Test of getStaticLabel method, of class AnchorModifierGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        AnchorModifierGui instance = new AnchorModifierGui();
        String result = instance.getStaticLabel();
        assertNotNull(result);
    }

    /**
     * Test of getLabelResource method, of class AnchorModifierGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        AnchorModifierGui instance = new AnchorModifierGui();
        String result = instance.getLabelResource();
        assertNotNull(result);
    }

    /**
     * Test of createTestElement method, of class AnchorModifierGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        AnchorModifierGui instance = new AnchorModifierGui();
        TestElement result = instance.createTestElement();
        assertNotNull(result);
    }

    /**
     * Test of modifyTestElement method, of class AnchorModifierGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement modifier = new AnchorModifier();
        AnchorModifierGui instance = new AnchorModifierGui();
        instance.modifyTestElement(modifier);
    }
}
