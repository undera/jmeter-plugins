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
        assertTrue(result.length()>0);
    }

   /**
    *
    */
   @Test
   public void testTableModelToCollectionProperty()
   {
      System.out.println("tableModelToCollectionProperty");
        PowerTableModel model = new PowerTableModel();
      CollectionProperty prop = JMeterPluginsUtils.tableModelToCollectionProperty(model, "");
      assertTrue(prop instanceof CollectionProperty);
   }

    /**
     * Test of tableModelToCollectionPropertyEval method, of class UltimateThreadGroup.
     */
    @Test
    public void testTableModelToCollectionPropertyEval() {
        System.out.println("tableModelToCollectionPropertyEval");
        PowerTableModel model = new PowerTableModel();
        CollectionProperty result = JMeterPluginsUtils.tableModelToCollectionPropertyEval(model, "");
        assertTrue(result instanceof CollectionProperty);
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
        String string = "";
        JMeterPluginsUtils.openInBrowser("");
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
        assertEquals(60*15, JMeterPluginsUtils.getSecondsForShortString("15m"));
        assertEquals(60*60*4, JMeterPluginsUtils.getSecondsForShortString("4h"));
        assertEquals(104025, JMeterPluginsUtils.getSecondsForShortString("27h103m645s"));
    }
}