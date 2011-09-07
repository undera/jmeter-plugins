package kg.apc.jmeter.reporters;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
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
public class FlexibleFileWriterGuiTest {

    public FlexibleFileWriterGuiTest() {
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
     * Test of getStaticLabel method, of class FlexibleFileWriterGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        FlexibleFileWriterGui instance = new FlexibleFileWriterGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length()>0);
    }

    /**
     * Test of getLabelResource method, of class FlexibleFileWriterGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        FlexibleFileWriterGui instance = new FlexibleFileWriterGui();
        String result = instance.getLabelResource();
        assertTrue(result.length()>0);
    }

    /**
     * Test of createTestElement method, of class FlexibleFileWriterGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        FlexibleFileWriterGui instance = new FlexibleFileWriterGui();
        TestElement result = instance.createTestElement();
        assertTrue(result instanceof FlexibleFileWriter);
    }

    /**
     * Test of modifyTestElement method, of class FlexibleFileWriterGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement te = new FlexibleFileWriter();
        FlexibleFileWriterGui instance = new FlexibleFileWriterGui();
        instance.modifyTestElement(te);
    }

    /**
     * Test of clearGui method, of class FlexibleFileWriterGui.
     */
    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        FlexibleFileWriterGui instance = new FlexibleFileWriterGui();
        instance.clearGui();
    }

    /**
     * Test of configure method, of class FlexibleFileWriterGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement element = new FlexibleFileWriter();
        FlexibleFileWriterGui instance = new FlexibleFileWriterGui();
        instance.configure(element);
    }

    /**
     * Test of lostOwnership method, of class FlexibleFileWriterGui.
     */
    @Test
    public void testLostOwnership() {
        System.out.println("lostOwnership");
        Clipboard clipboard = null;
        Transferable contents = null;
        FlexibleFileWriterGui instance = new FlexibleFileWriterGui();
        instance.lostOwnership(clipboard, contents);
    }

}
