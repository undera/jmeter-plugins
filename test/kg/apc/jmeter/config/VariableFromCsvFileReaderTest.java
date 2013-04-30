package kg.apc.jmeter.config;

import java.io.BufferedReader;
import java.io.StringReader;
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
public class VariableFromCsvFileReaderTest {

    private final String fileName;

    public VariableFromCsvFileReaderTest() {
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
    * Test of getDataAsMap method, of class VariableFromCsvFileReader.
    */
   @Test
   public void testGetDataAsMap() {
      System.out.println("getDataAsMap");
      String prefix = "";
      String separator = ",";
      VariableFromCsvFileReader instance = new VariableFromCsvFileReader(fileName);
      Map result = instance.getDataAsMap(prefix, separator);
      assertTrue(result.size() == 2);
   }

    /**
     * Test of getDataAsMap using a BufferedReader as input instead of a named file.
     */
    @Test
    public void testGetDataAsMapBufferedReaderInput() {
        String prefix = "";
        String separator = ",";
        String csvData = "var0,val0\nvar1,val1";
        BufferedReader input = new BufferedReader(new StringReader(csvData));
        VariableFromCsvFileReader instance = new VariableFromCsvFileReader(input);

        Map variables = instance.getDataAsMap(prefix, separator);

        assertEquals("incorrect value for var0", "val0", variables.get("var0"));
        assertEquals("incorrect value for var1", "val1", variables.get("var1"));
    }

}