package kg.apc.perfmon.metrics.jmx;

import java.lang.management.ClassLoadingMXBean;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class ClassesDataProviderTest extends TestCase {

    public ClassesDataProviderTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ClassesDataProviderTest.class);
        return suite;
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getMXBeanType method, of class ClassesDataProvider.
     */
    public void testGetMXBeanType() throws Exception {
        System.out.println("getMXBeanType");
        ClassesDataProvider instance = new ClassesDataProvider(new EmulatorMBeanServerConnection(), false);
        String result = instance.getMXBeanType();
        assertNotNull(result);
    }

    /**
     * Test of getMXBeanClass method, of class ClassesDataProvider.
     */
    public void testGetMXBeanClass() throws Exception {
        System.out.println("getMXBeanClass");
        ClassesDataProvider instance = new ClassesDataProvider(new EmulatorMBeanServerConnection(), false);
        Class expResult = ClassLoadingMXBean.class;
        Class result = instance.getMXBeanClass();
        assertEquals(expResult, result);
    }

    /**
     * Test of getValueFromBean method, of class ClassesDataProvider.
     */
    public void testGetValueFromBean() throws Exception {
        System.out.println("getValueFromBean");
        Object bean = new ClassLoadingMXBeanImpl();
        ClassesDataProvider instance = new ClassesDataProvider(new EmulatorMBeanServerConnection(), false);
        long result = instance.getValueFromBean(bean);
        assertTrue(result > 0);
    }

    private static class ClassLoadingMXBeanImpl implements ClassLoadingMXBean {

        public long getTotalLoadedClassCount() {
            return System.currentTimeMillis();
        }

        public int getLoadedClassCount() {
            return 100;
        }

        public long getUnloadedClassCount() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isVerbose() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setVerbose(boolean bln) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
