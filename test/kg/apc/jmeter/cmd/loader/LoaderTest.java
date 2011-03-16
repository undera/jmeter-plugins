package kg.apc.jmeter.cmd.loader;

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
public class LoaderTest {

    public LoaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        String homeDir = "/home/undera/NetBeansProjects/jmeter/trunk";
        System.setProperty("jmeter.home", homeDir);
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
     * Test of main method, of class PluginsCMD.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = "--help".split(" ");
        Loader.main(args);
    }

}