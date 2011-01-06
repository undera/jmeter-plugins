package kg.apc.jmeter.utils;

import java.io.File;
import java.net.URL;
import kg.apc.jmeter.util.FilesTestTools;
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
public class TestPlanMigratorTest
{

    public TestPlanMigratorTest()
    {
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
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of processFile method, of class TestPlanMigrator.
     */
    @Test
    public void testProcessFile() throws Exception
    {
        URL inputFileUrl = TestPlanMigratorTest.class.getResource("TestPlan030.jmx");
        File inputFile = new File(inputFileUrl.toURI());

        System.out.println("processFile: " + inputFile.getAbsolutePath());
        String fileName = inputFile.getAbsolutePath();
        TestPlanMigrator instance = new TestPlanMigrator();
        instance.processFile(fileName);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");

        File outputFile = new File(TestPlanMigratorTest.class.getResource("TestPlan030-FIXED.jmx").toURI());
        File referenceFile = new File(TestPlanMigratorTest.class.getResource("TestPlan040.jmx").toURI());

        boolean isFilesIdentical = FilesTestTools.compareFiles(outputFile, referenceFile);

        if(outputFile != null)
        {
            outputFile.delete();
        }
        
        assertNotNull(outputFile);
        assertTrue(isFilesIdentical);
    }

    /**
     * Test of processDirectory method, of class TestPlanMigrator.
     */
    @Test
    public void testProcessDirectory() throws Exception
    {
        URL inputFileUrl = TestPlanMigratorTest.class.getResource("TestPlan030.jmx");
        File inputFile = new File(inputFileUrl.toURI());

        System.out.println(inputFile.getParent());

        System.out.println("processDirectory: " + inputFile.getParent());
        String dir = inputFile.getParent();
        TestPlanMigrator instance = new TestPlanMigrator();
        instance.processDirectory(dir);

        URL testResult1 = TestPlanMigratorTest.class.getResource("TestPlan030-FIXED.jmx");
        URL testResult2 = TestPlanMigratorTest.class.getResource("TestPlan040-FIXED.jmx");
        URL testResult3 = TestPlanMigratorTest.class.getResource("TestPlanMigratorTest-FIXED.java");

        new File(testResult1.toURI()).delete();
        new File(testResult2.toURI()).delete();
        if(testResult3 != null)
        {
            new File(testResult3.toURI()).delete();
        }

        assertNotNull(testResult1);
        assertNotNull(testResult2);
        assertNull(testResult3);
    }

    /**
     * Test of main method, of class TestPlanMigrator.
     */
    @Test
    public void testMain()
    {
        System.out.println("main - test bad parameters are handled");

        String[] args = new String[0];
        TestPlanMigrator.main(args);

        args = new String[1];
        args[0] = "fake";

        TestPlanMigrator.main(args);

        args = new String[2];
        args[0] = "fake";
        args[1] = "fake";

        TestPlanMigrator.main(args);

        args[0] = "-d";
        args[1] = "fake";

        TestPlanMigrator.main(args);
    }
}
