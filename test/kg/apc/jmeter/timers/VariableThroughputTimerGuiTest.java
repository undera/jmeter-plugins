package kg.apc.jmeter.timers;

import javax.swing.event.TableModelEvent;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.testelement.property.CollectionProperty;
import javax.swing.event.ChangeEvent;
import kg.apc.jmeter.JMeterPluginsUtils;
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
public class VariableThroughputTimerGuiTest {

    private PowerTableModel dataModel;

    public VariableThroughputTimerGuiTest() {
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
        dataModel = new PowerTableModel(VariableThroughputTimer.columnIdentifiers, VariableThroughputTimer.columnClasses);
        dataModel.addRow(new Integer[]{
                    1, 10, 3
                });
        dataModel.addRow(new Integer[]{
                    15, 15, 3
                });
        dataModel.addRow(new Integer[]{
                    15, 1, 3
                });
        dataModel.addRow(new Integer[]{
                    1, 1, 1
                });
    }

    @After
    public void tearDown() {
    }

    /**
     *
     */
    @Test
    public void testInit() {
        System.out.println("init");
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        instance.init();
    }

    /**
     *
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        String expResult = "VariableThroughputTimerGui";
        String result = instance.getLabelResource();
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    /**
     *
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        TestElement result = instance.createTestElement();
        assertTrue(result instanceof VariableThroughputTimer);
    }

    /**
     *
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        VariableThroughputTimer tg = new VariableThroughputTimer();
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        //instance.addRowButton.doClick();
        instance.modifyTestElement(tg);
        CollectionProperty data = (CollectionProperty) tg.getData();
        assertEquals(instance.grid.getModel().getRowCount(), data.size());
    }

    /**
     *
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        VariableThroughputTimer tg = new VariableThroughputTimer();
        CollectionProperty rows = JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, VariableThroughputTimer.DATA_PROPERTY);
        System.err.println(rows);
        tg.setData(rows);
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        //tg.setProperty(new ObjectProperty(AbstractThreadGroup.MAIN_CONTROLLER, tg));
        instance.configure(tg);
        assertEquals(4, instance.tableModel.getRowCount());
        assertEquals(4, instance.grid.getRowCount());
    }

    @Test
    public void testConfigure_upgrade() {
        System.out.println("configure upgrade");
        VariableThroughputTimer tg = new VariableThroughputTimer();
        CollectionProperty rows = JMeterPluginsUtils.tableModelColsToCollectionProperty(dataModel, VariableThroughputTimer.DATA_PROPERTY);
        System.err.println(rows);
        tg.setData(rows);
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        //tg.setProperty(new ObjectProperty(AbstractThreadGroup.MAIN_CONTROLLER, tg));
        instance.configure(tg);
        assertEquals(4, instance.tableModel.getRowCount());
        assertEquals(4, instance.grid.getRowCount());
    }

    /**
     *
     */
    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        instance.clearGui();
    }

    /**
     *
     */
    @Test
    public void testEditingStopped() {
        System.out.println("editingStopped");
        ChangeEvent e = null;
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        instance.editingStopped(e);
    }

    /**
     *
     */
    @Test
    public void testEditingCanceled() {
        System.out.println("editingCanceled");
        ChangeEvent e = null;
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        instance.editingCanceled(e);
    }

    /**
     * Test of tableChanged method, of class VariableThroughputTimerGui.
     */
    @Test
    public void testTableChanged() {
        System.out.println("tableChanged");
        TableModelEvent e = null;
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        instance.tableChanged(e);
    }

    /**
     * Test of updateUI method, of class VariableThroughputTimerGui.
     */
    @Test
    public void testUpdateUI() {
        System.out.println("updateUI");
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        instance.updateUI();
    }
}
