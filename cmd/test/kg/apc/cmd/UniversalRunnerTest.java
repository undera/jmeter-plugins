package kg.apc.cmd;

import java.net.MalformedURLException;
import org.apache.jmeter.util.JMeterUtils;
import java.net.URL;
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
        JMeterUtils.setJMeterHome("");
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
    public void testMain() {
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
}