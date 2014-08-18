package kg.apc.jmeter.config;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.testelement.TestElement;
import org.junit.*;

public class LockFileGuiTest {

    public LockFileGuiTest() {
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
     * Test of getStaticLabel method, of class LockFileGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        LockFileGui instance = new LockFileGui();
        String result = instance.getStaticLabel();
        assert result.length() > 0;
    }

    /**
     * Test of getLabelResource method, of class LockFileGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        LockFileGui instance = new LockFileGui();
        String result = instance.getLabelResource();
        assert result.length() > 0;
    }

    /**
     * Test of configure method, of class LockFileGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        LockFile lf = new LockFile();
        lf.setFilename("testfilename");
        lf.setFilemask("testfilemask");
        LockFileGui instance = new LockFileGui();
        instance.configure(lf);
    }

    /**
     * Test of createTestElement method, of class LockFileGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        LockFileGui instance = new LockFileGui();
        TestElement result = instance.createTestElement();
        assert result instanceof LockFile;
    }

    /**
     * Test of modifyTestElement method, of class LockFileGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        LockFile lf = new LockFile();
        lf.setFilename("testfilename");
        lf.setFilemask("testfilemask");
        LockFileGui instance = new LockFileGui();
        instance.modifyTestElement(lf);
    }

    /**
     * Test of clearGui method, of class LockFileGui.
     */
    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        LockFileGui instance = new LockFileGui();
        instance.clearGui();
    }
}
