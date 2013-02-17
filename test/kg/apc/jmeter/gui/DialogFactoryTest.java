package kg.apc.jmeter.gui;

import java.awt.GraphicsEnvironment;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
public class DialogFactoryTest {

    public DialogFactoryTest() {
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
     * Test of getJDialogInstance method, of class DialogFactory.
     */
    @Test
    public void testGetJDialogInstance() {
        System.out.println("getJDialogInstance");
        Frame owner = null;
        String title = "test";
        boolean modal = false;
        JAbsrtactDialogPanel content = new JAbsrtactDialogPanelImpl();
        JDialog result = DialogFactory.getJDialogInstance(owner, title, modal, content, "/kg/apc/jmeter/vizualizers/wand.png");
        assertEquals(result == null, GraphicsEnvironment.isHeadless());
    }

    /**
     * Test of centerDialog method, of class DialogFactory.
     */
    @Test
    public void testCenterDialog() {
        System.out.println("centerDialog");
        Frame parent = null;
        JDialog dialog = null;

        if(!GraphicsEnvironment.isHeadless()) {
            parent = new JFrame("Frame");
            parent.setBounds(10, 10, 640, 480);

            dialog = new JDialog(parent, "Dialog");
            dialog.setSize(200, 50);
        }

        DialogFactory.centerDialog(parent, dialog);

    }

    public class JAbsrtactDialogPanelImpl extends JAbsrtactDialogPanel {
    }
}