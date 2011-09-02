package kg.apc.jmeter.perfmon;

import kg.apc.jmeter.perfmon.AbstractPerfMonMetric;
import org.hyperic.sigar.SigarException;
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
public class AbstractPerfMonMetricTest {
    
    public AbstractPerfMonMetricTest() {
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
     * Test of getValue method, of class AbstractPerfMonMetric.
     */
    @Test
    public void testGetValue() throws Exception {
        System.out.println("getValue");
        StringBuilder res = null;
        AbstractPerfMonMetric instance = new AbstractPerfMonMetricImpl();
        instance.getValue(res);
    }

    public class AbstractPerfMonMetricImpl extends AbstractPerfMonMetric {

        public AbstractPerfMonMetricImpl() {
            super(null);
        }

        @Override
        public void getValue(StringBuilder res) throws SigarException {
        }
    }
}
