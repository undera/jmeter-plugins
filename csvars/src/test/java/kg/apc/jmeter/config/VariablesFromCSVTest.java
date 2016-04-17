package kg.apc.jmeter.config;

import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class VariablesFromCSVTest {

   private final String fileName;

   public VariablesFromCSVTest() {
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
    * Test of getArgumentsAsMap method, of class VariablesFromCSV.
    */
   @Test
   public void testGetArgumentsAsMap() {
      System.out.println("getArgumentsAsMap");
      VariablesFromCSV instance = new VariablesFromCSV();
      instance.setFileName(fileName);
      instance.setSeparator(",");
      Map result = instance.getArgumentsAsMap();
      assertEquals(result.size(), 2);
   }

   /**
    * Test of getVariablePrefix method, of class VariablesFromCSV.
    */
   @Test
   public void testGetVariablePrefix() {
      System.out.println("getVariablePrefix");
      VariablesFromCSV instance = new VariablesFromCSV();
      String expResult = "";
      String result = instance.getVariablePrefix();
      assertEquals(expResult, result);
   }

   /**
    * Test of setVariablePrefix method, of class VariablesFromCSV.
    */
   @Test
   public void testSetVariablePrefix() {
      System.out.println("setVariablePrefix");
      String prefix = "";
      VariablesFromCSV instance = new VariablesFromCSV();
      instance.setVariablePrefix(prefix);
   }

   /**
    * Test of getFileName method, of class VariablesFromCSV.
    */
   @Test
   public void testGetFileName() {
      System.out.println("getFileName");
      VariablesFromCSV instance = new VariablesFromCSV();
      String expResult = "";
      String result = instance.getFileName();
      assertEquals(expResult, result);
   }

   /**
    * Test of setFileName method, of class VariablesFromCSV.
    */
   @Test
   public void testSetFileName() {
      System.out.println("setFileName");
      String filename = "";
      VariablesFromCSV instance = new VariablesFromCSV();
      instance.setFileName(filename);
   }

   /**
    * Test of getSeparator method, of class VariablesFromCSV.
    */
   @Test
   public void testGetSeparator() {
      System.out.println("getSeparator");
      VariablesFromCSV instance = new VariablesFromCSV();
      String expResult = "";
      String result = instance.getSeparator();
      assertEquals(expResult, result);
   }

   /**
    * Test of setSeparator method, of class VariablesFromCSV.
    */
   @Test
   public void testSetSeparator() {
      System.out.println("setSeparator");
      String separator = "";
      VariablesFromCSV instance = new VariablesFromCSV();
      instance.setSeparator(separator);
   }

    /**
     * Test of isStoreAsSystemProperty method, of class VariablesFromCSV.
     */
    @Test
    public void testIsStoreAsSystemProperty() {
        System.out.println("isStoreAsSystemProperty");
        VariablesFromCSV instance = new VariablesFromCSV();
        boolean expResult = false;
        boolean result = instance.isStoreAsSystemProperty();
        assertEquals(expResult, result);
    }

    /**
     * Test of setStoreAsSystemProperty method, of class VariablesFromCSV.
     */
    @Test
    public void testSetStoreAsSystemProperty() {
        System.out.println("setStoreAsSystemProperty");
        boolean storeAsSysProp = false;
        VariablesFromCSV instance = new VariablesFromCSV();
        instance.setStoreAsSystemProperty(storeAsSysProp);
    }

    /**
     * Test skipLines property default.
     */
    @Test
    public void skipLinesDefault() {
        VariablesFromCSV element = new VariablesFromCSV();
        assertEquals("getSkipLines() did not return SKIP_LINES_DEFAULT",
                VariablesFromCSV.SKIP_LINES_DEFAULT, element.getSkipLines());
    }

    /**
     * Test skipLines round trip.
     */
    @Test
    public void skipLinesRoundTrip() {
        VariablesFromCSV element = new VariablesFromCSV();
        int skipLines = 2;
        element.setSkipLines(skipLines);
        assertEquals("value returned by getter does not match", skipLines, element.getSkipLines());
    }

    /**
     * Test skipLines with underlying property explicitly set to non-integer. This should only occur if a caller
     * explicitly sets underlying property. When an element is created from a saved test plan, JMeter SaveService
     * appears to call the setSkipLines(int) method, so this test is merely to document expected behavior in abnormal
     * situations.
     */
    @Test
    public void skipLinesNonInteger() {
        VariablesFromCSV element = new VariablesFromCSV();
        element.setProperty(VariablesFromCSV.SKIP_LINES, "a");
        assertEquals("getSkipLines() did not return SKIP_LINES_DEFAULT",
                VariablesFromCSV.SKIP_LINES_DEFAULT, element.getSkipLines());
    }

    /**
     * Test skipLines with underlying property explicitly set to empty string. This should only occur if a caller
     * explicitly sets underlying property. When an element is created from a saved test plan, JMeter SaveService
     * appears to call the setSkipLines(int) method, so this test is merely to document expected behavior in abnormal
     * situations.
     */
    @Test
    public void skipLinesEmptyString() {
        VariablesFromCSV element = new VariablesFromCSV();
        element.setProperty(VariablesFromCSV.SKIP_LINES, "");
        assertEquals("getSkipLines() did not return SKIP_LINES_DEFAULT",
                VariablesFromCSV.SKIP_LINES_DEFAULT, element.getSkipLines());
    }
}
