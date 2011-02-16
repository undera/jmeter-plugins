package kg.apc.jmeter.timers;

import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import kg.apc.jmeter.util.TestJMeterUtils;
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
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of init method, of class VariableThroughputTimerGui.
     */
    @Test
    public void testInit() {
        System.out.println("init");
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        instance.init();
    }

    /**
     * Test of getLabelResource method, of class VariableThroughputTimerGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        String result = instance.getLabelResource();
        assertTrue(result.length()>0);
    }

    /**
     * Test of getStaticLabel method, of class VariableThroughputTimerGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length()>0);
    }

    /**
     * Test of createTestElement method, of class VariableThroughputTimerGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        TestElement result = instance.createTestElement();
        assertTrue(result instanceof VariableThroughputTimer);
    }

    /**
     * Test of modifyTestElement method, of class VariableThroughputTimerGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement tg = new VariableThroughputTimer();
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        instance.modifyTestElement(tg);
    }

    /**
     * Test of configure method, of class VariableThroughputTimerGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement tg = new VariableThroughputTimer();
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        instance.configure(tg);
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
     * Test of editingStopped method, of class VariableThroughputTimerGui.
     */
    @Test
    public void testEditingStopped() {
        System.out.println("editingStopped");
        ChangeEvent e = null;
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        instance.editingStopped(e);
    }

    /**
     * Test of editingCanceled method, of class VariableThroughputTimerGui.
     */
    @Test
    public void testEditingCanceled() {
        System.out.println("editingCanceled");
        ChangeEvent e = null;
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        instance.editingCanceled(e);
    }

    /**
     * Test of clearGui method, of class VariableThroughputTimerGui.
     */
    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        VariableThroughputTimerGui instance = new VariableThroughputTimerGui();
        instance.clearGui();
    }

}