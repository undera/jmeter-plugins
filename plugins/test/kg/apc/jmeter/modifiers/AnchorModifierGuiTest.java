package kg.apc.jmeter.modifiers;

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
        String expResult = "";
        String result = instance.getStaticLabel();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLabelResource method, of class AnchorModifierGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        AnchorModifierGui instance = new AnchorModifierGui();
        String expResult = "";
        String result = instance.getLabelResource();
        assertEquals(expResult, result);
    }

    /**
     * Test of createTestElement method, of class AnchorModifierGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        AnchorModifierGui instance = new AnchorModifierGui();
        TestElement expResult = null;
        TestElement result = instance.createTestElement();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of modifyTestElement method, of class AnchorModifierGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement modifier = null;
        AnchorModifierGui instance = new AnchorModifierGui();
        instance.modifyTestElement(modifier);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
