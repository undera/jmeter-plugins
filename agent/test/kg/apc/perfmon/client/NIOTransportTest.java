package kg.apc.perfmon.client;

import junit.framework.TestCase;
import kg.apc.emulators.DatagramChannelEmul;

/**
 *
 * @author undera
 */
public class NIOTransportTest extends TestCase {

    private NIOTransport instance;
    private DatagramChannelEmul channel;

    public NIOTransportTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
        instance = new NIOTransport();
        channel = (DatagramChannelEmul) DatagramChannelEmul.open();
        instance.setChannels(channel, channel);
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
}
