package kg.apc.perfmon.metrics.jmx;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class MemoryPoolDataProviderTest extends TestCase {

    public MemoryPoolDataProviderTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(MemoryPoolDataProviderTest.class);
        return suite;
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getMXBeanType method, of class MemoryPoolDataProvider.
     */
    public void testGetMXBeanType() throws Exception {
        System.out.println("getMXBeanType");
        MemoryPoolDataProvider instance = new MemoryPoolDataProvider(new EmulatorMBeanServerConnection(), false, MemoryPoolDataProvider.TYPE_COMMITTED);
        String expResult = ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE;
        String result = instance.getMXBeanType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMXBeanClass method, of class MemoryPoolDataProvider.
     */
    public void testGetMXBeanClass() throws Exception {
        System.out.println("getMXBeanClass");
        MemoryPoolDataProvider instance = new MemoryPoolDataProvider(new EmulatorMBeanServerConnection(), false, MemoryPoolDataProvider.TYPE_COMMITTED);
        Class expResult = MemoryPoolMXBean.class;
        Class result = instance.getMXBeanClass();
        assertEquals(expResult, result);
    }

    /**
     * Test of getValueFromBean method, of class MemoryPoolDataProvider.
     */
    public void testGetValueFromBean() throws Exception {
        System.out.println("getValueFromBean");
        Object bean = new MemoryPoolMXBeanImpl();
        MemoryPoolDataProvider instance = new MemoryPoolDataProvider(new EmulatorMBeanServerConnection(), false, MemoryPoolDataProvider.TYPE_COMMITTED);
        long expResult = 3L;
        long result = instance.getValueFromBean(bean);
        assertEquals(expResult, result);
    }

    private static class MemoryPoolMXBeanImpl implements MemoryPoolMXBean {

        public String getName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public MemoryType getType() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public MemoryUsage getUsage() {
            MemoryUsage res = new MemoryUsage(1, 2, 3, 4);
            return res;
        }

        public MemoryUsage getPeakUsage() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void resetPeakUsage() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isValid() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String[] getMemoryManagerNames() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public long getUsageThreshold() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setUsageThreshold(long l) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isUsageThresholdExceeded() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public long getUsageThresholdCount() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isUsageThresholdSupported() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public long getCollectionUsageThreshold() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setCollectionUsageThreshold(long l) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isCollectionUsageThresholdExceeded() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public long getCollectionUsageThresholdCount() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public MemoryUsage getCollectionUsage() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isCollectionUsageThresholdSupported() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
