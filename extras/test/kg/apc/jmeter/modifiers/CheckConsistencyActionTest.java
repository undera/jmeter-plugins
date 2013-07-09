package kg.apc.jmeter.modifiers;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import kg.apc.emulators.TestJMeterUtils;
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
public class CheckConsistencyActionTest {

    private final String basedir;

    public CheckConsistencyActionTest() {
        String file = RawRequestSourcePreProcessorTest.class.getResource("rawdata_broken.txt").getPath();
        basedir = file.substring(0, file.lastIndexOf("/"));
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
     * Test of actionPerformed method, of class CheckConsistencyAction.
     */
    @Test
    public void testActionPerformed() {
        System.out.println("actionPerformed");
        ActionEvent e = null;
        JTextArea info = new JTextArea();
        JTextField fname = new JTextField(basedir + "/rawdata_crlf_metaline.txt");
        CheckConsistencyAction instance = new CheckConsistencyAction(fname, info);
        instance.actionPerformed(e);
        System.out.println(info.getText());
        assertEquals(Color.decode("0x00009900"), info.getForeground());
        assertTrue(info.getText().trim().startsWith("File seems to be OK.\nParsed 5 requests in"));
    }

    /**
     * Test of actionPerformed method, of class CheckConsistencyAction.
     */
    @Test
    public void testActionPerformed_err1() {
        System.out.println("actionPerformed err 1");
        ActionEvent e = null;
        JTextArea info = new JTextArea();
        JTextField fname = new JTextField(basedir + "/nofile");
        CheckConsistencyAction instance = new CheckConsistencyAction(fname, info);
        instance.actionPerformed(e);
        System.out.println(info.getText());
        assertEquals(Color.red, info.getForeground());
    }

    @Test
    public void testActionPerformed_err2() {
        System.out.println("actionPerformed err 2");
        ActionEvent e = null;
        JTextArea info = new JTextArea();
        JTextField fname = new JTextField(basedir + "/rawdata_broken.txt");
        CheckConsistencyAction instance = new CheckConsistencyAction(fname, info);
        instance.actionPerformed(e);
        System.out.println(info.getText());
        assertEquals(Color.red, info.getForeground());
    }
}
