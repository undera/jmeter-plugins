package kg.apc.jmeter.gui;

import java.awt.event.ActionEvent;
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

/**
 *
 * @author undera
 */
public class CopyRowActionTest {

    public CopyRowActionTest() {
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
     * Test of actionPerformed method, of class CopyRowAction.
     */
    @Test
    public void testActionPerformed() {
        System.out.println("actionPerformed");
        ActionEvent e = null;
        CopyRowAction instance = new CopyRowAction(new JPanel(),
                new JTable(),
                new PowerTableModel(UltimateThreadGroupGui.columnIdentifiers, UltimateThreadGroupGui.columnClasses),
                new JButton());

        instance.actionPerformed(e);
    }
}
