package kg.apc.jmeter;

import java.nio.ByteBuffer;
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
}