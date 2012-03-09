package kg.apc.perfmon.metrics.jmx;

import java.lang.management.GarbageCollectorMXBean;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class GCDataProviderTest extends TestCase {

    public GCDataProviderTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(GCDataProviderTest.class);
        return suite;
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getMXBeanType method, of class GCDataProvider.
     */
    public void testGetMXBeanType() throws Exception {
        System.out.println("getMXBeanType");
        GCDataProvider instance = new GCDataProvider(new EmulatorMBeanServerConnection(), false);
        String result = instance.getMXBeanType();
        assertNotNull(result);
    }

    /**
     * Test of getMXBeanClass method, of class GCDataProvider.
     */
    public void testGetMXBeanClass() throws Exception {
        System.out.println("getMXBeanClass");
        GCDataProvider instance = new GCDataProvider(new EmulatorMBeanServerConnection(), false);
        Class expResult = GarbageCollectorMXBean.class;
        Class result = instance.getMXBeanClass();
        assertEquals(expResult, result);
    }

    /**
     * Test of getValueFromBean method, of class GCDataProvider.
     */
    public void testGetValueFromBean() throws Exception {
        System.out.println("getValueFromBean");
        Object bean = new GarbageCollectorMXBeanImpl();
        GCDataProvider instance = new GCDataProvider(new EmulatorMBeanServerConnection(), false);
        long result = instance.getValueFromBean(bean);
        assertTrue(result>0);
    }

    private static class GarbageCollectorMXBeanImpl implements GarbageCollectorMXBean {

        public long getCollectionCount() {
            return System.currentTimeMillis();
        }

        public long getCollectionTime() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String getName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isValid() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String[] getMemoryPoolNames() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
