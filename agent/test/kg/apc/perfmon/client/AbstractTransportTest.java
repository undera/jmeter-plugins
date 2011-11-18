package kg.apc.perfmon.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import junit.framework.TestCase;

/**
 *
 * @author undera
 */
public class AbstractTransportTest extends TestCase {

    private AbstractTransportImpl instance;

    public AbstractTransportTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
        instance = new AbstractTransportImpl();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
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

    /**
     * Test of writeln method, of class AbstractTransport.
     */
    public void testWriteln() throws Exception {
        System.out.println("writeln");
        String line = "";
        instance.writeln(line);
    }

    /**
     * Test of readln method, of class AbstractTransport.
     */
    public void testReadln_0args() {
        System.out.println("readln");
        String expResult = "";
        String result = instance.readln();
        assertEquals(expResult, result);
    }

    /**
     * Test of readln method, of class AbstractTransport.
     */
    public void testReadln_ByteBuffer() throws Exception {
        System.out.println("readln");
        ByteBuffer buf1 = ByteBuffer.wrap("123".getBytes());
        instance.readln(buf1);
        ByteBuffer buf2 = ByteBuffer.wrap("456\n789\n".getBytes());
        String expResult = "789";
        String result = instance.readln(buf2);
        assertEquals(expResult, result);
    }

    public void testReadln_ByteBuffer_empty() throws Exception {
        System.out.println("readln");
        ByteBuffer buf1 = ByteBuffer.wrap("".getBytes());
        String result = instance.readln(buf1);
        assertEquals(null, result);

        ByteBuffer buf2 = ByteBuffer.wrap("\n".getBytes());
        String result2 = instance.readln(buf2);
        assertEquals("", result2);
    }

    public class AbstractTransportImpl extends AbstractTransport {

        public AbstractTransportImpl() throws Exception {
            super(null);
        }

        public void writeln(String line) throws IOException {
        }

        public String readln() {
            return "";
        }
    }
}
