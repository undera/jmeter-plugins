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
public class LoadosophiaUploaderGuiTest {

    public LoadosophiaUploaderGuiTest() {
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
     * Test of getStaticLabel method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of getLabelResource method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        String result = instance.getLabelResource();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of createTestElement method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        TestElement result = instance.createTestElement();
        assertTrue(result instanceof LoadosophiaUploader);
    }

    /**
     * Test of modifyTestElement method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement te = new LoadosophiaUploader();
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        instance.modifyTestElement(te);
    }

    /**
     * Test of configure method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement element = new LoadosophiaUploader();
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        instance.configure(element);
    }
}
