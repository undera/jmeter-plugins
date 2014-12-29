package kg.apc.jmeter.config;

import javax.swing.JTextArea;
import java.awt.event.ActionEvent;

import static org.mockito.Mockito.*;
import org.junit.*;

import static org.junit.Assert.*;

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
        VariablesFromCSV csvVarsTestElem = new VariablesFromCSV();
        csvVarsTestElem.setFileName(fileName);
        csvVarsTestElem.setVariablePrefix("");
        csvVarsTestElem.setSeparator(",");
        csvVarsTestElem.setSkipLines(0);

        JTextArea infoArea = new JTextArea();

        VariablesFromCSVGui ui = mock(VariablesFromCSVGui.class);
        when(ui.createTestElement()).thenReturn(csvVarsTestElem);
        when(ui.getCheckInfoTextArea()).thenReturn(infoArea);

        TestCsvFileAction instance = new TestCsvFileAction(ui);
        ActionEvent e = null;
        instance.actionPerformed(e);

        assertTrue(infoArea.getText().startsWith("File successfuly parsed, 2"));
    }

    /**
     * Verify that exceptions are reported when test action is performed.
     *
     * see https://groups.google.com/forum/#!topic/jmeter-plugins/gWn7MTgvTfE
     */
    @Test
    public void testActionPerformed_exception() {
        String fileName = TestCsvFileActionTest.class.getResource("CSVSample_user.csv").getPath();
        VariablesFromCSV csvVarsTestElem = new VariablesFromCSV();
        csvVarsTestElem.setFileName(fileName);
        csvVarsTestElem.setVariablePrefix("");
        csvVarsTestElem.setSeparator("");
        csvVarsTestElem.setSkipLines(0);

        JTextArea infoArea = new JTextArea();

        VariablesFromCSVGui ui = mock(VariablesFromCSVGui.class);
        when(ui.createTestElement()).thenReturn(csvVarsTestElem);
        when(ui.getCheckInfoTextArea()).thenReturn(infoArea);

        TestCsvFileAction instance = new TestCsvFileAction(ui);
        ActionEvent e = null;
        instance.actionPerformed(e);

        assertTrue(infoArea.getText().startsWith("Problem detected:"));
    }
}