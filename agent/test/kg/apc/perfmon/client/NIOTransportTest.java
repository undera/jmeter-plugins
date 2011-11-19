package kg.apc.perfmon.client;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
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

    public static Test suite() {
        TestSuite suite = new TestSuite(NIOTransportTest.class);
        return suite;
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
     * Test of setChannels method, of class NIOTransport.
     */
    public void testSetChannels() throws IOException {
        System.out.println("setChannels");
        ReadableByteChannel reader = null;
        WritableByteChannel writer = null;
        instance.setChannels(reader, writer);
    }

    /**
     * Test of readln method, of class NIOTransport.
     */
    public void testReadln() {
        System.out.println("readln");
        String expResult = "";
        String result = instance.readln();
        assertEquals(expResult, result);
    }

    /**
     * Test of disconnect method, of class NIOTransport.
     */
    public void testDisconnect() {
        System.out.println("disconnect");
        instance.disconnect();
    }
}
