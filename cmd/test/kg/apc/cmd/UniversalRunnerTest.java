package kg.apc.cmd;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author undera
 */
public class UniversalRunnerTest {

    public UniversalRunnerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
       // JMeterUtils.setJMeterHome("");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addURL method, of class NewDriver.
     */
    @Test
    public void testAddURL_URL() throws MalformedURLException {
        System.out.println("addURL");
        URL url = new URL("http://localhost/");
        UniversalRunner.addURL(url);
    }

    /**
     * Test of addPath method, of class NewDriver.
     */
    @Test
    public void testAddPath() throws Exception {
        System.out.println("addPath");
        String path = "/";
        UniversalRunner.addPath(path);
    }

    /**
     * Test of getJMeterDir method, of class NewDriver.
     */
    @Test
    public void testGetJMeterDir() {
        System.out.println("getJMeterDir");
        String result = UniversalRunner.getJMeterDir();
        assertTrue(result.length()>0);
    }

    /**
     * Test of main method, of class NewDriver.
     */
    @Test
    public void testMain() throws Throwable {
        System.out.println("main");
        String[] args = "--help".split(" ");
        UniversalRunner.main(args);
    }

    /**
     * Test of addURL method, of class NewDriver.
     */
    @Test
    public void testAddURL() {
        System.out.println("addURL");
        URL url = null;
        UniversalRunner.addURL(url);
    }

    /**
     * Test of getJarDirectory method, of class UniversalRunner.
     */
    @Test
    public void testGetJarDirectory() {
        System.out.println("getJarDirectory");
        String initial_classpath = "";
        String expResult = "";
        String result = UniversalRunner.getJarDirectory(initial_classpath);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of buildUpdatedClassPath method, of class UniversalRunner.
     */
    @Test
    public void testBuildUpdatedClassPath() {
        System.out.println("buildUpdatedClassPath");
        List<URL> jars = null;
        StringBuilder expResult = null;
        StringBuilder result = UniversalRunner.buildUpdatedClassPath(jars);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}