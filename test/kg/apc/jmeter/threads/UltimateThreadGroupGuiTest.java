package kg.apc.jmeter.threads;

import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.testelement.property.CollectionProperty;
import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.testelement.TestElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apc
 */
public class UltimateThreadGroupGuiTest {

    /**
     *
     */
    public UltimateThreadGroupGuiTest() {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     *
     */
    @Test
    public void testInit() {
        System.out.println("init");
        UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
        instance.init();
    }

    /**
     *
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
        String expResult = "UltimateThreadGroupGui";
        String result = instance.getLabelResource();
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    /**
     *
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
        TestElement result = instance.createTestElement();
        assertTrue(result instanceof UltimateThreadGroup);
    }

    /**
     *
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        UltimateThreadGroup tg = new UltimateThreadGroup();
        UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
        instance.tableModel = UltimateThreadGroupTest.getTestModel();
        instance.modifyTestElement(tg);
        CollectionProperty data = (CollectionProperty) tg.getData();
        assertEquals(3, data.size());
        assertEquals("[[1, 2, 3, 4, 44], [5, 6, 7, 8, 88], [9, 10, 11, 12, 122]]", data.toString());
    }

    /**
     *
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        UltimateThreadGroup tg = new UltimateThreadGroup();
        tg.setData(getTestData());
        UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
        instance.configure(tg);
        assertEquals(3, instance.tableModel.getRowCount());
        assertEquals(3, instance.grid.getRowCount());
    }

    @Test
    public void testConfigure_upgrade() {
        System.out.println("configure_upgrade");
        UltimateThreadGroup tg = new UltimateThreadGroup();
        PowerTableModel model = UltimateThreadGroupTest.getTestModel();
        CollectionProperty data = JMeterPluginsUtils.tableModelColsToCollectionProperty(model, UltimateThreadGroup.DATA_PROPERTY);
        tg.setData(data);
        UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
        instance.configure(tg);
        assertEquals(3, instance.tableModel.getRowCount());
        assertEquals(3, instance.grid.getRowCount());
    }

    /**
     *
     */
    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
        instance.clearGui();
    }

    /**
     *
     */
    @Test
    public void testTableChanged() {
        System.out.println("tableChanged");
        TableModelEvent e = null;
        UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
        instance.tableChanged(e);
    }

    /**
     *
     */
    @Test
    public void testEditingStopped() {
        System.out.println("editingStopped");
        ChangeEvent e = null;
        UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
        instance.editingStopped(e);
    }

    /**
     *
     */
    @Test
    public void testEditingCanceled() {
        System.out.println("editingCanceled");
        ChangeEvent e = null;
        UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
        instance.editingCanceled(e);
    }

    private CollectionProperty getTestData() {
        PowerTableModel model = UltimateThreadGroupTest.getTestModel();
        CollectionProperty data = JMeterPluginsUtils.tableModelRowsToCollectionProperty(model, UltimateThreadGroup.DATA_PROPERTY);
        return data;
    }

    /**
     * Test of updateUI method, of class UltimateThreadGroupGui.
     */
    @Test
    public void testUpdateUI() {
        System.out.println("updateUI");
        UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
        instance.updateUI();
    }
}
