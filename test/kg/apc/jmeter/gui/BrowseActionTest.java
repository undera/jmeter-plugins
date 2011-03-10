package kg.apc.jmeter.gui;

import kg.apc.emulators.FileChooserEmul;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import org.apache.jmeter.gui.GuiPackage;
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
public class BrowseActionTest {

    private static class BrowseActionEmul extends BrowseAction{

        public BrowseActionEmul(JTextField t) {
            super(t);
        }

        @Override
        protected JFileChooser getFileChooser() {
            return new FileChooserEmul();
        }
    }

    public BrowseActionTest() {
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
     * Test of actionPerformed method, of class BrowseAction.
     */
    @Test
    public void testActionPerformed() {
        System.out.println("actionPerformed");
        ActionEvent e = null;
        BrowseAction instance = new BrowseActionEmul(new JTextField());
        instance.actionPerformed(e);
    }

    /**
     * Test of getFileChooser method, of class BrowseAction.
     */
    @Test
    public void testGetFileChooser() {
        System.out.println("getFileChooser");
        BrowseAction instance = new BrowseActionEmul(new JTextField());
        JFileChooser result = instance.getFileChooser();
        assertNotNull(result);
    }

}