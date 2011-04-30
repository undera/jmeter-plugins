// TODO: cover all parameters
package kg.apc.jmeter;

import java.io.File;
import java.io.IOException;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.util.JMeterUtils;
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
public class PluginsCMDTest {
    private String basedir;

    public PluginsCMDTest() {
        String file = this.getClass().getResource("short.jtl").getPath();
        basedir = file.substring(0, file.lastIndexOf("/"));
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        JMeterUtils.setJMeterHome(basedir);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of processParams method, of class PluginsCMD.
     */
    @Test
    public void testProcessParams() {
        System.out.println("processParams");
        String[] args = null;
        PluginsCMD instance = new PluginsCMD();
        int expResult = 0;
        int result = instance.processParams(args);
        assertEquals(expResult, result);
    }

    @Test
    // issue 39
    public void testProcessParams_aggreg() throws IOException {
        System.out.println("processParams aggregate");
        File f = File.createTempFile("test", ".csv");
        String str=" --generate-csv "+f.getAbsolutePath()+" "
                + "--input-jtl "+basedir+"/few.jtl "
                + "--aggregate-rows yes --plugin-type ResponseTimesOverTime";
        String[] args = str.split(" +");
        PluginsCMD instance = new PluginsCMD();
        int expResult = 0;
        int result = instance.processParams(args);
        assertEquals(expResult, result);
        assertTrue(78==f.length() || 81==f.length()); // 78 at linux, 81 at windows because or \r\n
    }
}
