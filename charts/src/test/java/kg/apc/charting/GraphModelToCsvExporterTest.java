package kg.apc.charting;

import kg.apc.charting.rows.GraphRowAverages;
import kg.apc.emulators.FilesTestTools;
import org.junit.*;

import java.io.File;
import java.util.Calendar;
import java.util.concurrent.ConcurrentSkipListMap;

public class GraphModelToCsvExporterTest {

    public GraphModelToCsvExporterTest() {
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

    private ConcurrentSkipListMap<String, AbstractGraphRow> createTestModel() {
        ConcurrentSkipListMap<String, AbstractGraphRow> testModel = new ConcurrentSkipListMap<>();
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

        now.set(Calendar.SECOND, 10);

        row1.add(now.getTimeInMillis(), 20);
        row2.add(now.getTimeInMillis(), 30);

        now.set(Calendar.SECOND, 25);

        row3.add(now.getTimeInMillis(), 50);

        return testModel;
    }

    @Test
    public void testWriteCsvFile() throws Exception {
        File referenceFile = new File(GraphModelToCsvExporterTest.class.getResource("export.csv").toURI());
        File testFile = new File(referenceFile.getParent() + "/testExport.csv");

        System.out.println("writeCsvFile: " + testFile.getAbsolutePath());
        GraphModelToCsvExporter instance = new GraphModelToCsvExporter(createTestModel(), testFile, ";", '.', new DateTimeRenderer(), -1);
        instance.writeCsvFile();
        boolean success = FilesTestTools.compareFiles(testFile, referenceFile);
        testFile.delete();
        Assert.assertTrue(success);
    }
}