package kg.apc.jmeter.control.sampler;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.testelement.TestElement;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class SetVariablesActionGuiTest {
    private SetVariablesActionGui instance;

    @BeforeClass
    public static void setUpClass()
            throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Before
    public void setUp() {
        instance = new SetVariablesActionGui();
    }

    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        TestElement expResult = new SetVariablesAction();
        TestElement result = instance.createTestElement();
        assertEquals(expResult.getClass(), result.getClass());
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
    public void testGui() throws Exception {
        SetVariablesActionGui gui = new SetVariablesActionGui();

        SetVariablesAction element1 = (SetVariablesAction) gui.createTestElement();
        SetVariablesAction element2 = (SetVariablesAction) gui.createTestElement();

        Arguments args = new Arguments();
        args.addArgument("var1", "val0");
        element1.setUserDefinedVariables(args);

        gui.configure(element1);
        gui.modifyTestElement(element2);

        assertEquals(element1.getUserDefinedVariablesAsProperty().getObjectValue().toString(),
                element2.getUserDefinedVariablesAsProperty().getObjectValue().toString());

        gui.clearGui();
        gui.modifyTestElement(element2);

        assertEquals("", element2.getUserDefinedVariablesAsProperty().getObjectValue().toString());
    }
}