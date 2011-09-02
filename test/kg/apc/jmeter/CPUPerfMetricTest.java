package kg.apc.jmeter;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxyCache;
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
public class CPUPerfMetricTest {

    public CPUPerfMetricTest() {
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
     * Test of getValue method, of class CPUPerfMetric.
     */
    @Test
    public void testGetValue() throws Exception {
        System.out.println("getValue");
        StringBuilder res = new StringBuilder();
        CPUTotalMetric instance = new CPUTotalMetric(SigarProxyCache.newInstance(new Sigar(), 500));
        instance.getValue(res);
        assertTrue(Double.parseDouble(res.toString()) > 0);
    }
}
