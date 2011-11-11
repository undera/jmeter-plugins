package kg.apc.perfmon.metrics;

import kg.apc.perfmon.metrics.AbstractPerfMonMetric;
import junit.framework.TestCase;
import org.hyperic.sigar.SigarException;

/**
 *
 * @author undera
 */
public class AbstractPerfMonMetricTest extends TestCase{
    
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
            super(null);
        }

        public void getValue(StringBuilder res) throws SigarException {
        }
    }
}
