package kg.apc.jmeter;

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
}