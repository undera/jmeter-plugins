package kg.apc.perfmon.metrics.jmx;

import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import sun.management.CompilerThreadStat;

/**
 *
 * @author undera
 */
public class CompilerDataProviderTest extends TestCase {

    public CompilerDataProviderTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CompilerDataProviderTest.class);
        return suite;
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getMXBeanType method, of class CompilerDataProvider.
     */
    public void testGetMXBeanType() throws Exception {
        System.out.println("getMXBeanType");
        CompilerDataProvider instance = new CompilerDataProvider(new EmulatorMBeanServerConnection(), false);
        String expResult = ManagementFactory.COMPILATION_MXBEAN_NAME;
        String result = instance.getMXBeanType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMXBeanClass method, of class CompilerDataProvider.
     */
    public void testGetMXBeanClass() throws Exception {
        System.out.println("getMXBeanClass");
        CompilerDataProvider instance = new CompilerDataProvider(new EmulatorMBeanServerConnection(), false);
        Class expResult = CompilationMXBean.class;
        Class result = instance.getMXBeanClass();
        assertEquals(expResult, result);
    }

    /**
     * Test of getValueFromBean method, of class CompilerDataProvider.
     */
    public void testGetValueFromBean() throws Exception {
        System.out.println("getValueFromBean");
        Object bean = new CompilationMXBeanImpl();
        CompilerDataProvider instance = new CompilerDataProvider(new EmulatorMBeanServerConnection(), false);
        long expResult = 123L;
        long result = instance.getValueFromBean(bean);
        assertEquals(expResult, result);
    }

    private static class CompilationMXBeanImpl implements CompilationMXBean {

        public String getName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isCompilationTimeMonitoringSupported() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public long getTotalCompilationTime() {
            return 123;
        }
    }
}
