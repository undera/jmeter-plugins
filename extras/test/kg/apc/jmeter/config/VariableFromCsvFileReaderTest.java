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
     * Test getDataAsMap() for input with # at the beginning of the line = comments.
     */
    @Test
    public void testComments() {
        String prefix = "";
        String separator = ",";
        String csvData = "var0,val0,a comment\n#var1,val1\nvar2,val2";
        BufferedReader input = new BufferedReader(new StringReader(csvData));
        VariableFromCsvFileReader instance = new VariableFromCsvFileReader(input);

        Map variables = instance.getDataAsMap(prefix, separator);

        assertEquals("incorrect value for var0", "val0", variables.get("var0"));
        assertNull("no value for var1", variables.get("var1"));
        assertEquals("incorrect value for var1", "val2", variables.get("var2"));
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
     * Tests getDataAsMap() with input that contains multi lines variables.
     */
    @Test
    public void testMutiLine() {
        String prefix = "";
        String separator = ",";
        String csvData = "var0,\"line1\nline2\nline3\"\n\nvar1,val1\nvar2,\"lineA\nlineB\nlineC\nlineD\nlineE\"";
        BufferedReader input = new BufferedReader(new StringReader(csvData));
        VariableFromCsvFileReader instance = new VariableFromCsvFileReader(input);

        Map variables = instance.getDataAsMap(prefix, separator);
        assertEquals("incorrect value for var0", "line1\nline2\nline3", variables.get("var0"));
        assertEquals("incorrect value for var1", "val1", variables.get("var1"));
        assertEquals("incorrect value for var2", "lineA\nlineB\nlineC\nlineD\nlineE", variables.get("var2"));
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

    /**
     * Test getDataAsMap() skipping the first input line which is a header.
     */
    @Test
    public void testSkipHeaderLine() {
        String prefix = "";
        String separator = ",";
        String csvData = "name,value,description\nvar0,val0,a comment\nvar1,val1";
        BufferedReader input = new BufferedReader(new StringReader(csvData));
        VariableFromCsvFileReader instance = new VariableFromCsvFileReader(input);

        Map variables = instance.getDataAsMap(prefix, separator, 1);

        assertNull("header line was interpreted as variable data", variables.get("name"));
        assertEquals("incorrect value for var0", "val0", variables.get("var0"));
        assertEquals("incorrect value for var1", "val1", variables.get("var1"));
    }

    /**
     * Test getDataAsMap() with negative integer for skipLines.
     */
    @Test
    public void testSkipHeaderNegative() {
        String prefix = "";
        String separator = ",";
        String csvData = "var0,val0,a comment\nvar1,val1";
        BufferedReader input = new BufferedReader(new StringReader(csvData));
        VariableFromCsvFileReader instance = new VariableFromCsvFileReader(input);

        Map variables = instance.getDataAsMap(prefix, separator, -1);

        assertEquals("incorrect value for var0", "val0", variables.get("var0"));
        assertEquals("incorrect value for var1", "val1", variables.get("var1"));
    }

    /**
     * Test getDataAsMap() with empty separator. see https://groups.google.com/forum/#!topic/jmeter-plugins/gWn7MTgvTfE
     */
    @Test(expected=IllegalArgumentException.class)
    public void testEmptySeparator() {
        String prefix = "";
        String separator = "";
        String csvData = "name,value,description\nvar0,val0,a comment\nvar1,val1";
        BufferedReader input = new BufferedReader(new StringReader(csvData));
        VariableFromCsvFileReader instance = new VariableFromCsvFileReader(input);

        Map variables = instance.getDataAsMap(prefix, separator, 1);
    }
}