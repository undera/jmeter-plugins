package kg.apc.perfmon.client;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class AbstractTransportTest extends TestCase {
    private AbstractTransportImpl instance;
    
    public AbstractTransportTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(AbstractTransportTest.class);
        return suite;
    }
    
    protected void setUp() throws Exception {
        super.setUp();
         instance = new AbstractTransportImpl();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getNextLine method, of class AbstractTransport.
     */
    public void testGetNextLine() throws Exception {
        System.out.println("getNextLine");
        int newlineCount = 0;
        String expResult = "";
        String result = instance.getNextLine(newlineCount);
        assertEquals(expResult, result);
    }

    /**
     * Test of disconnect method, of class AbstractTransport.
     */
    public void testDisconnect() {
        System.out.println("disconnect");
        instance.disconnect();
    }

    /**
     * Test of test method, of class AbstractTransport.
     */
    public void testTest() {
        System.out.println("test");
        boolean expResult = false;
        boolean result = instance.test();
        assertEquals(expResult, result);
    }

    /**
     * Test of startWithMetrics method, of class AbstractTransport.
     */
    public void testStartWithMetrics() throws Exception {
        System.out.println("startWithMetrics");
        String[] metricsArray = {"cpu"};
        instance.startWithMetrics(metricsArray);
    }

    /**
     * Test of readMetrics method, of class AbstractTransport.
     */
    public void testReadMetrics() {
        System.out.println("readMetrics");
        String[] result = instance.readMetrics();
        assertEquals("", result[0]);
    }

    public class AbstractTransportImpl extends AbstractTransport {

        public AbstractTransportImpl() throws Exception {
            super();
        }

        public String readln() {
            return "";
        }

        public void writeln(String line) throws IOException {
        }
    }
}
