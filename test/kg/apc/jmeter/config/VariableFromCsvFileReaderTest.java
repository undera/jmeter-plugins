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
     * Test getDataAsMap() using a BufferedReader as input instead of a file.
     */
    @Test
    public void testBufferedReaderInput() {
        String prefix = "";
        String separator = ",";
        String csvData = "var0,val0\nvar1,val1";
        BufferedReader input = new BufferedReader(new StringReader(csvData));
        VariableFromCsvFileReader instance = new VariableFromCsvFileReader(input);

        Map variables = instance.getDataAsMap(prefix, separator);

        assertEquals("incorrect value for var0", "val0", variables.get("var0"));
        assertEquals("incorrect value for var1", "val1", variables.get("var1"));
    }

    /**
     * Test getDataAsMap() for input with more than two columns.
     */
    @Test
    public void testExtraColumnsInput() {
        String prefix = "";
        String separator = ",";
        String csvData = "var0,val0,a comment\nvar1,val1";
        BufferedReader input = new BufferedReader(new StringReader(csvData));
        VariableFromCsvFileReader instance = new VariableFromCsvFileReader(input);

        Map variables = instance.getDataAsMap(prefix, separator);

        assertEquals("incorrect value for var0", "val0", variables.get("var0"));
        assertEquals("incorrect value for var1", "val1", variables.get("var1"));
    }

    /**
     * Test getDataAsMap() with input that contains blank lines.
     */
    @Test
    public void testBlankLineInput() {
        String prefix = "";
        String separator = ",";
        String csvData = "var0,val0\n\nvar1,val1\n";
        BufferedReader input = new BufferedReader(new StringReader(csvData));
        VariableFromCsvFileReader instance = new VariableFromCsvFileReader(input);

        Map variables = instance.getDataAsMap(prefix, separator);

        assertEquals("incorrect number of variables parsed from input", 2, variables.size());
        assertEquals("incorrect value for var0", "val0", variables.get("var0"));
        assertEquals("incorrect value for var1", "val1", variables.get("var1"));
    }

    /**
     * Tests getDataAsMap() with input that contains only a single column.
     */
    @Test
    public void testSingleColumn() {
        String prefix = "";
        String separator = ",";
        String csvData = "var0\n\nvar1,val1\n";
        BufferedReader input = new BufferedReader(new StringReader(csvData));
        VariableFromCsvFileReader instance = new VariableFromCsvFileReader(input);

        Map variables = instance.getDataAsMap(prefix, separator);

        assertEquals("incorrect value for var0", "", variables.get("var0"));
        assertEquals("incorrect value for var1", "val1", variables.get("var1"));
    }

    /**
     * Test getDataAsMap() with a non-blank variable prefix.
     */
    @Test
    public void testVariablePrefix() {
        String prefix = "test";
        String separator = ",";
        String csvData = "var0,val0,a comment\nvar1,val1";
        BufferedReader input = new BufferedReader(new StringReader(csvData));
        VariableFromCsvFileReader instance = new VariableFromCsvFileReader(input);

        Map variables = instance.getDataAsMap(prefix, separator);

        assertEquals("incorrect value for testvar0", "val0", variables.get("testvar0"));
        assertEquals("incorrect value for testvar1", "val1", variables.get("testvar1"));
        assertNull("var0 should not be mapped", variables.get("var0"));
        assertNull("var1 should not be mapped", variables.get("var1"));
    }

}