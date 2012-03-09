package kg.apc.perfmon.metrics.jmx;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class MemoryDataProviderTest extends TestCase {

    public MemoryDataProviderTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(MemoryDataProviderTest.class);
        return suite;
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getMXBeanType method, of class MemoryDataProvider.
     */
    public void testGetMXBeanType() throws Exception {
        System.out.println("getMXBeanType");
        MemoryDataProvider instance = new MemoryDataProvider(new EmulatorMBeanServerConnection(), false);
        String expResult = ManagementFactory.MEMORY_MXBEAN_NAME;
        String result = instance.getMXBeanType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMXBeanClass method, of class MemoryDataProvider.
     */
    public void testGetMXBeanClass() throws Exception {
        System.out.println("getMXBeanClass");
        MemoryDataProvider instance = new MemoryDataProvider(new EmulatorMBeanServerConnection(), false);
        Class expResult = MemoryMXBean.class;
        Class result = instance.getMXBeanClass();
        assertEquals(expResult, result);
    }

    /**
     * Test of getValueFromBean method, of class MemoryDataProvider.
     */
    public void testGetValueFromBean() throws Exception {
        System.out.println("getValueFromBean");
        Object bean = new MemoryMXBeanImpl();
        MemoryDataProvider instance = new MemoryDataProvider(new EmulatorMBeanServerConnection(), false);
        long expResult = 2L;
        long result = instance.getValueFromBean(bean);
        assertEquals(expResult, result);
    }

    private static class MemoryMXBeanImpl implements MemoryMXBean {

        public int getObjectPendingFinalizationCount() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public MemoryUsage getHeapMemoryUsage() {
            return new MemoryUsage(1, 2, 3, 4);
        }

        public MemoryUsage getNonHeapMemoryUsage() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isVerbose() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setVerbose(boolean bln) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void gc() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
