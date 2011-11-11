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
public class NewDriverTest {

    public NewDriverTest() {
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
        NewDriver.addURL(url);
    }

    /**
     * Test of addPath method, of class NewDriver.
     */
    @Test
    public void testAddPath() throws Exception {
        System.out.println("addPath");
        String path = "/";
        NewDriver.addPath(path);
    }

    /**
     * Test of getJMeterDir method, of class NewDriver.
     */
    @Test
    public void testGetJMeterDir() {
        System.out.println("getJMeterDir");
        String result = NewDriver.getJMeterDir();
        assertTrue(result.length()>0);
    }

    /**
     * Test of main method, of class NewDriver.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = "--help".split(" ");
        NewDriver.main(args);
    }

    /**
     * Test of addURL method, of class NewDriver.
     */
    @Test
    public void testAddURL() {
        System.out.println("addURL");
        URL url = null;
        NewDriver.addURL(url);
    }
}