package kg.apc.perfmon.metrics;

import junit.framework.TestCase;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxyCache;

/**
 *
 * @author undera
 */
public class AbstractPerfMonMetricTest extends TestCase {

    public AbstractPerfMonMetricTest() {
    }

    /**
     * Test of getValue method, of class AbstractPerfMonMetric.
     */
    public void testGetValue() throws Exception {
        System.out.println("getValue");
        StringBuilder res = null;
        AbstractPerfMonMetric instance = new AbstractPerfMonMetricImpl();
        instance.getValue(res);
    }

    public class AbstractPerfMonMetricImpl extends AbstractPerfMonMetric {

        public AbstractPerfMonMetricImpl() {
            super(SigarProxyCache.newInstance(new Sigar(), 500));

        }

        public void getValue(StringBuilder res) throws SigarException {
        }
    }
}
