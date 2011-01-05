package kg.apc.jmeter.utils;

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
public class TestPlanMigratorTest {

    public TestPlanMigratorTest() {
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

    /**
     * Test of processFile method, of class TestPlanMigrator.
     */
    @Test
    public void testProcessFile() throws Exception
    {
        System.out.println("processFile");
        String fileName = "";
        TestPlanMigrator instance = new TestPlanMigrator();
        instance.processFile(fileName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of processDirectory method, of class TestPlanMigrator.
     */
    @Test
    public void testProcessDirectory()
    {
        System.out.println("processDirectory");
        String dir = "";
        TestPlanMigrator instance = new TestPlanMigrator();
        instance.processDirectory(dir);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class TestPlanMigrator.
     */
    @Test
    public void testMain()
    {
        System.out.println("main");
        String[] args = null;
        TestPlanMigrator.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}