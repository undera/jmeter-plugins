package kg.apc.jmeter.gui;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import kg.apc.jmeter.threads.UltimateThreadGroupGui;
import org.apache.jmeter.gui.util.PowerTableModel;
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
public class AddRowActionTest {

    public AddRowActionTest() {
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
     * Test of actionPerformed method, of class AddRowAction.
     */
    @Test
    public void testActionPerformed() {
        System.out.println("actionPerformed");
        AddRowAction instance = new AddRowAction(
                new JPanel(),
                new JTable(),
                new PowerTableModel(UltimateThreadGroupGui.columnIdentifiers, UltimateThreadGroupGui.columnClasses),
                new JButton(),
                UltimateThreadGroupGui.defaultValues);
        instance.actionPerformed(null);
    }
}