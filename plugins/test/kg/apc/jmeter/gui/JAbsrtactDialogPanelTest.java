package kg.apc.jmeter.gui;

import javax.swing.JDialog;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stephane Hoblingre
 */
public class JAbsrtactDialogPanelTest {

    public JAbsrtactDialogPanelTest() {
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
     * Test of getMinWidth method, of class JAbsrtactDialogPanel.
     */
    @Test
    public void testGetMinWidth() {
        System.out.println("getMinWidth");
        JAbsrtactDialogPanel instance = new JAbsrtactDialogPanelImpl();
        int expResult = 0;
        int result = instance.getMinWidth();
        assertEquals(expResult, result);
    }

    /**
     * Test of setMinWidth method, of class JAbsrtactDialogPanel.
     */
    @Test
    public void testSetMinWidth() {
        System.out.println("setMinWidth");
        int minWidth = 100;
        JAbsrtactDialogPanel instance = new JAbsrtactDialogPanelImpl();
        instance.setMinWidth(minWidth);
    }

    /**
     * Test of repack method, of class JAbsrtactDialogPanel.
     */
    @Test
    public void testRepack() {
        System.out.println("repack");
        JAbsrtactDialogPanel instance = new JAbsrtactDialogPanelImpl();
        instance.repack();
    }

    /**
     * Test of getAssociatedDialog method, of class JAbsrtactDialogPanel.
     */
    @Test
    public void testGetAssociatedDialog() {
        System.out.println("getAssociatedDialog");
        JAbsrtactDialogPanel instance = new JAbsrtactDialogPanelImpl();
        JDialog expResult = null;
        JDialog result = instance.getAssociatedDialog();
        assertEquals(expResult, result);
    }

    public class JAbsrtactDialogPanelImpl extends JAbsrtactDialogPanel {
    }

}