package kg.apc.jmeter.config;

import java.util.Map;
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
}
