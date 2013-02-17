package kg.apc.jmeter.config;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
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
public class TestCsvFileActionTest {

    private final String fileName;

    public TestCsvFileActionTest() {
        fileName = TestCsvFileActionTest.class.getResource("csvFileTest.csv").getPath();
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
     * Test of actionPerformed method, of class TestCsvFileAction.
     */
    @Test
    public void testActionPerformed() {
        System.out.println("actionPerformed");
        JTextField file = new JTextField(fileName);
        JTextField prefix = new JTextField("");
        JTextField separator = new JTextField(",");
        JTextArea infoArea = new JTextArea();
        ActionEvent e = null;
        TestCsvFileAction instance = new TestCsvFileAction(file, prefix, separator, infoArea);
        instance.actionPerformed(e);
        assertTrue(infoArea.getText().startsWith("File successfuly parsed, 2"));
        file.setText(fileName + ".notFound");
        instance.actionPerformed(e);
        assertTrue(infoArea.getText().startsWith("Problem detected:"));
    }

    /**
     * @see https://groups.google.com/forum/#!topic/jmeter-plugins/gWn7MTgvTfE
     */
    @Test
    public void testActionPerformed_eternal_loop() {
        System.out.println("actionPerformed");
        String fileName1 = TestCsvFileActionTest.class.getResource("CSVSample_user.csv").getPath();
        JTextField file = new JTextField(fileName1);
        JTextField prefix = new JTextField("");
        JTextField separator = new JTextField("");
        JTextArea infoArea = new JTextArea();
        ActionEvent e = null;
        TestCsvFileAction instance = new TestCsvFileAction(file, prefix, separator, infoArea);
        instance.actionPerformed(e);
        assertTrue(infoArea.getText().startsWith("Problem detected:"));
    }
}