package kg.apc.cmd;

import kg.apc.cmd.DynamicClassLoader;
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
        URL url = getClass().getResource(".");
        DynamicClassLoader instance = new DynamicClassLoader(new URL[0]);
        instance.addURL(url);
    }

    /**
     * Test of updateLoader method, of class DynamicClassLoader.
     */
    @Test
    public void testUpdateLoader() {
        System.out.println("updateLoader");
        URL[] urls = new URL[0];
        DynamicClassLoader.updateLoader(urls);
    }
}