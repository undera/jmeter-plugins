/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.charting;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.AbstractMap;
import java.util.Calendar;
import kg.apc.jmeter.util.FilesTestTools;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author z000205
 */
public class GraphModelToCsvExporterTest {

    public GraphModelToCsvExporterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private ConcurrentSkipListMap<String, AbstractGraphRow> createTestModel()
    {
        ConcurrentSkipListMap<String, AbstractGraphRow> testModel = new ConcurrentSkipListMap<String, AbstractGraphRow>();
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 10);
        now.set(Calendar.MINUTE, 30);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 500);

        GraphRowAverages row1 = new GraphRowAverages();
        GraphRowAverages row2 = new GraphRowAverages();
        GraphRowAverages row3 = new GraphRowAverages();

        testModel.put("row1", row1);
        testModel.put("row2", row2);
        testModel.put("row3", row3);

        row1.add(now.getTimeInMillis(), 10);
        row2.add(now.getTimeInMillis(), 20);
        row3.add(1, 50);

        now.set(Calendar.SECOND, 10);

        row1.add(now.getTimeInMillis(), 20);
        row2.add(now.getTimeInMillis(), 30);
        row3.add(2, 100);

        now.set(Calendar.SECOND, 25);

        row1.add(now.getTimeInMillis(), 50);
        row3.add(10, 1000);

        return testModel;
    }

    /**
     * Test of writeCsvFile method, of class GraphModelToCsvExporter.
     */
    @Test
    public void testWriteCsvFile() throws Exception
    {
        File referenceFile = new File(GraphModelToCsvExporterTest.class.getResource("export.csv").toURI());
        File testFile = new File(referenceFile.getParent() + "/testExport.csv");

        System.out.println("writeCsvFile: " + testFile.getAbsolutePath());
        GraphModelToCsvExporter instance = new GraphModelToCsvExporter(createTestModel(), testFile, ";", '.');
        instance.writeCsvFile();
        boolean success = FilesTestTools.compareFiles(testFile, referenceFile);
        testFile.delete();
        assertTrue(success);
    }

}