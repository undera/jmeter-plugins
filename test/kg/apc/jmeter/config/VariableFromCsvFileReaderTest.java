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

}