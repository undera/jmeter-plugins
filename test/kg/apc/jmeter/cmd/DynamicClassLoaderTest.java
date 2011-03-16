package kg.apc.jmeter.cmd;

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
public class DynamicClassLoaderTest {

    public DynamicClassLoaderTest() {
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
     * Test of addURL method, of class DynamicClassLoader.
     */
    @Test
    public void testAddURL() {
        System.out.println("addURL");
        URL url = null;
        DynamicClassLoader instance = null;
        instance.addURL(url);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateLoader method, of class DynamicClassLoader.
     */
    @Test
    public void testUpdateLoader() {
        System.out.println("updateLoader");
        URL[] urls = null;
        DynamicClassLoader.updateLoader(urls);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}