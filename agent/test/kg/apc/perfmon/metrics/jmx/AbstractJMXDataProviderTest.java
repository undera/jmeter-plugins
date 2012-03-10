package kg.apc.perfmon.metrics.jmx;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServerConnection;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class AbstractJMXDataProviderTest extends TestCase {

    public AbstractJMXDataProviderTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(AbstractJMXDataProviderTest.class);
        return suite;
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getProvider method, of class AbstractJMXDataProvider.
     */
    public void testGetProvider() throws Exception {
        System.out.println("getProvider");
        MBeanServerConnection mBeanServerConn = new EmulatorMBeanServerConnection();
        String params = "gc-time";
        Class expResult = GCDataProvider.class;
        AbstractJMXDataProvider result = AbstractJMXDataProvider.getProvider(mBeanServerConn, params);
        assertEquals(expResult, result.getClass());
    }

    /**
     * Test of getMXBeanType method, of class AbstractJMXDataProvider.
     */
    public void testGetMXBeanType() throws Exception {
        System.out.println("getMXBeanType");
        AbstractJMXDataProvider instance = new AbstractJMXDataProviderImpl();
        String result = instance.getMXBeanType();
        assertNotNull(result);
    }

    /**
     * Test of getMXBeanClass method, of class AbstractJMXDataProvider.
     */
    public void testGetMXBeanClass() throws Exception {
        System.out.println("getMXBeanClass");
        AbstractJMXDataProvider instance = new AbstractJMXDataProviderImpl();
        Class result = instance.getMXBeanClass();
        assertNotNull(result);
    }

    /**
     * Test of getValueFromBean method, of class AbstractJMXDataProvider.
     */
    public void testGetValueFromBean() throws Exception {
        System.out.println("getValueFromBean");
        Object bean = null;
        AbstractJMXDataProvider instance = new AbstractJMXDataProviderImpl();
        long expResult = 0L;
        long result = instance.getValueFromBean(bean);
        assertEquals(expResult, result);
    }

    /**
     * Test of getValue method, of class AbstractJMXDataProvider.
     */
    public void testGetValue() throws Exception {
        System.out.println("getValue");
        StringBuffer res = new StringBuffer();
        AbstractJMXDataProvider instance = new AbstractJMXDataProviderImpl();
        instance.getValue(res);
    }

    public class AbstractJMXDataProviderImpl extends AbstractJMXDataProvider {

        public AbstractJMXDataProviderImpl() throws Exception {
            super(new EmulatorMBeanServerConnection(), false);
        }

        public String getMXBeanType() {
            return ManagementFactory.CLASS_LOADING_MXBEAN_NAME;
        }

        public Class getMXBeanClass() {
            return GarbageCollectorMXBean.class;
        }

        public long getValueFromBean(Object bean) {
            return 0L;
        }
    }
}
