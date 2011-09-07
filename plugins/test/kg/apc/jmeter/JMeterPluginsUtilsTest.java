package kg.apc.jmeter;

import javax.swing.BorderFactory;
import org.apache.jmeter.gui.util.VerticalPanel;
import java.awt.Component;
import java.nio.ByteBuffer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.testelement.property.CollectionProperty;
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
public class JMeterPluginsUtilsTest {

    public JMeterPluginsUtilsTest() {
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
     * Test of prefixLabel method, of class JMeterPluginsUtils.
     */
    @Test
    public void testPrefixLabel() {
        System.out.println("prefixLabel");
        String string = "TEST";
        String result = JMeterPluginsUtils.prefixLabel(string);
        assertTrue(result.indexOf(string) != -1);
    }

    /**
     * Test of getStackTrace method, of class JMeterPluginsUtils.
     */
    @Test
    public void testGetStackTrace() {
        System.out.println("getStackTrace");
        Exception ex = new Exception();
        String result = JMeterPluginsUtils.getStackTrace(ex);
        assertTrue(result.length() > 0);
    }

    /**
     * Test of byteBufferToString method, of class JMeterPluginsUtils.
     */
    @Test
    public void testByteBufferToString() {
        System.out.println("byteBufferToString");
        ByteBuffer buf = ByteBuffer.wrap("My Test".getBytes());
        String expResult = "My Test";
        String result = JMeterPluginsUtils.byteBufferToString(buf);
        assertEquals(expResult, result);
    }

    @Test
    public void testByteBufferToString2() {
        System.out.println("byteBufferToString2");
        
        ByteBuffer buf = ByteBuffer.allocateDirect(2014);
        buf.put("My Test".getBytes());
        buf.flip();
        String expResult = "My Test";
        String result = JMeterPluginsUtils.byteBufferToString(buf);
        assertEquals(expResult, result);
    }
    /**
     * Test of replaceRNT method, of class JMeterPluginsUtils.
     */
    @Test
    public void testReplaceRNT() {
        System.out.println("replaceRNT");
        assertEquals("\t", JMeterPluginsUtils.replaceRNT("\\t"));
        assertEquals("\t\t", JMeterPluginsUtils.replaceRNT("\\t\\t"));
        assertEquals("-\t-", JMeterPluginsUtils.replaceRNT("-\\t-"));
        System.out.println("\\\\t");
        assertEquals("\\t", JMeterPluginsUtils.replaceRNT("\\\\t"));
        assertEquals("\t\n\r", JMeterPluginsUtils.replaceRNT("\\t\\n\\r"));
        assertEquals("\t\n\n\r", JMeterPluginsUtils.replaceRNT("\\t\\n\\n\\r"));
    }

    /**
     * Test of getWikiLinkText method, of class JMeterPluginsUtils.
     */
    @Test
    public void testGetWikiLinkText() {
        System.out.println("getWikiLinkText");
        String wikiPage = "test";
        String result = JMeterPluginsUtils.getWikiLinkText(wikiPage);
        assertTrue(result.endsWith(wikiPage) || java.awt.Desktop.isDesktopSupported());
    }

    /**
     * Test of openInBrowser method, of class JMeterPluginsUtils.
     */
    @Test
    public void testOpenInBrowser() {
        System.out.println("openInBrowser");
        // don't do this, because of odd window popups
        // JMeterPluginsUtils.openInBrowser("http://code.google.com/p/jmeter-plugins/");
    }

    /**
     * Test of addHelpLinkToPanel method, of class JMeterPluginsUtils.
     */
    @Test
    public void testAddHelpLinkToPanel() {
        System.out.println("addHelpLinkToPanel");
        VerticalPanel titlePanel = new VerticalPanel();
        titlePanel.add(new JLabel("title"));
        VerticalPanel contentPanel = new VerticalPanel();
        contentPanel.setBorder(BorderFactory.createEtchedBorder());
        contentPanel.add(new JPanel());
        contentPanel.add(new JPanel());
        contentPanel.setName("THIS");
        titlePanel.add(contentPanel);
        String helpPage = "";
        Component result = JMeterPluginsUtils.addHelpLinkToPanel(titlePanel, helpPage);
        assertNotNull(result);
    }

    /**
     * Test of getSecondsForShort method, of class JMeterPluginsUtils.
     */
    @Test
    public void testGetSecondsForShortString() {
        System.out.println("getSecondsForShort");
        assertEquals(105, JMeterPluginsUtils.getSecondsForShortString("105"));
        assertEquals(105, JMeterPluginsUtils.getSecondsForShortString("105s"));
        assertEquals(60 * 15, JMeterPluginsUtils.getSecondsForShortString("15m"));
        assertEquals(60 * 60 * 4, JMeterPluginsUtils.getSecondsForShortString("4h"));
        assertEquals(104025, JMeterPluginsUtils.getSecondsForShortString("27h103m645s"));
    }

    /**
     * Test of byteBufferToByteArray method, of class JMeterPluginsUtils.
     */
    @Test
    public void testByteBufferToByteArray() {
        System.out.println("byteBufferToByteArray");
        ByteBuffer buf = ByteBuffer.wrap("test".getBytes());
        byte[] result = JMeterPluginsUtils.byteBufferToByteArray(buf);
        assertEquals(4, result.length);
    }

    private PowerTableModel getTestModel() {
        String[] headers = {"col1", "col2"};
        Class[] classes = {String.class, String.class};
        PowerTableModel model = new PowerTableModel(headers, classes);
        String[] row1 = {"1", "2"};
        String[] row2 = {"3", "4"};
        model.addRow(row1);
        model.addRow(row2);
        return model;
    }

    /**
     * Test of tableModelRowsToCollectionProperty method, of class JMeterPluginsUtils.
     */
    @Test
    public void testTableModelRowsToCollectionProperty() {
        System.out.println("tableModelRowsToCollectionProperty");
        PowerTableModel model = getTestModel();
        String propname = "prop";
        CollectionProperty result = JMeterPluginsUtils.tableModelRowsToCollectionProperty(model, propname);
        assertEquals(2, result.size());
        assertEquals("[[1, 2], [3, 4]]", result.toString());
    }

    /**
     * Test of collectionPropertyToTableModelRows method, of class JMeterPluginsUtils.
     */
    @Test
    public void testCollectionPropertyToTableModelRows() {
        System.out.println("collectionPropertyToTableModelRows");
        String propname = "prop";
        PowerTableModel modelSrc = getTestModel();
        CollectionProperty propExp = JMeterPluginsUtils.tableModelRowsToCollectionProperty(modelSrc, propname);
        PowerTableModel modelDst = getTestModel();
        modelDst.clearData();
        JMeterPluginsUtils.collectionPropertyToTableModelRows(propExp, modelDst);
        CollectionProperty propRes = JMeterPluginsUtils.tableModelRowsToCollectionProperty(modelDst, propname);
        assertEquals(propExp.toString(), propRes.toString());
    }

    /**
     * Test of tableModelRowsToCollectionPropertyEval method, of class JMeterPluginsUtils.
     */
    @Test
    public void testTableModelRowsToCollectionPropertyEval() {
        System.out.println("tableModelRowsToCollectionPropertyEval");
        PowerTableModel model = getTestModel();
        String propname = "prop";
        CollectionProperty result = JMeterPluginsUtils.tableModelRowsToCollectionPropertyEval(model, propname);
        assertEquals(2, result.size());
        assertEquals("[[1, 2], [3, 4]]", result.toString());
    }

    /**
     * Test of tableModelColsToCollectionProperty method, of class JMeterPluginsUtils.
     */
    @Test
    public void testTableModelColsToCollectionProperty() {
        System.out.println("tableModelColsToCollectionProperty");
        PowerTableModel model = getTestModel();
        String propname = "";
        CollectionProperty result = JMeterPluginsUtils.tableModelColsToCollectionProperty(model, propname);
        assertEquals("[[1, 3], [2, 4]]", result.toString());
    }

    /**
     * Test of collectionPropertyToTableModelCols method, of class JMeterPluginsUtils.
     */
    @Test
    public void testCollectionPropertyToTableModelCols() {
        System.out.println("collectionPropertyToTableModelCols");
        CollectionProperty prop = JMeterPluginsUtils.tableModelColsToCollectionProperty(getTestModel(), "");
        PowerTableModel model = getTestModel();
        JMeterPluginsUtils.collectionPropertyToTableModelCols(prop, model);
        assertEquals(prop.size(), model.getColumnCount());
    }
}
