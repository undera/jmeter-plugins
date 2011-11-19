package kg.apc.perfmon.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class StreamTransportTest extends TestCase {

    public StreamTransportTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(StreamTransportTest.class);
        return suite;
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of setStreams method, of class StreamTransport.
     */
    public void testSetStreams() throws IOException {
        System.out.println("setStreams");
        InputStream i = new ByteArrayInputStream(new byte[0]);
        OutputStream o = new ByteArrayOutputStream();
        StreamTransport instance = new StreamTransport();
        instance.setStreams(i, o);
    }

    /**
     * Test of readln method, of class StreamTransport.
     */
    public void testReadln() throws IOException {
        System.out.println("readln");
        StreamTransport instance = new StreamTransport();
        InputStream i = new ByteArrayInputStream(new byte[0]);
        OutputStream o = new ByteArrayOutputStream();
        instance.setStreams(i, o);
        String expResult = "";
        String result = instance.readln();
        assertEquals(expResult, result);
    }

    /**
     * Test of writeln method, of class StreamTransport.
     */
    public void testWriteln() throws Exception {
        System.out.println("writeln");
        String line = "";
        StreamTransport instance = new StreamTransport();
        InputStream i = new ByteArrayInputStream(new byte[0]);
        OutputStream o = new ByteArrayOutputStream();
        instance.setStreams(i, o);
        instance.writeln(line);
    }
}
