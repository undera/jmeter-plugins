/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.perfmon.client;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class TransportTest extends TestCase {

    public TransportTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TransportTest.class);
        return suite;
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of disconnect method, of class Transport.
     */
    public void testDisconnect() {
        System.out.println("disconnect");
        Transport instance = new TransportImpl();
        instance.disconnect();
    }

    /**
     * Test of readMetrics method, of class Transport.
     */
    public void testReadMetrics() {
        System.out.println("readMetrics");
        Transport instance = new TransportImpl();
        String[] expResult = null;
        String[] result = instance.readMetrics();
        assertEquals(expResult, result);
    }

    /**
     * Test of readln method, of class Transport.
     */
    public void testReadln() {
        System.out.println("readln");
        Transport instance = new TransportImpl();
        String expResult = "";
        String result = instance.readln();
        assertEquals(expResult, result);
    }

    /**
     * Test of setInterval method, of class Transport.
     */
    public void testSetInterval() {
        System.out.println("setInterval");
        long interval = 0L;
        Transport instance = new TransportImpl();
        instance.setInterval(interval);
    }

    /**
     * Test of shutdownAgent method, of class Transport.
     */
    public void testShutdownAgent() {
        System.out.println("shutdownAgent");
        Transport instance = new TransportImpl();
        instance.shutdownAgent();
    }

    /**
     * Test of startWithMetrics method, of class Transport.
     */
    public void testStartWithMetrics() throws Exception {
        System.out.println("startWithMetrics");
        String[] metricsArray = null;
        Transport instance = new TransportImpl();
        instance.startWithMetrics(metricsArray);
    }

    /**
     * Test of test method, of class Transport.
     */
    public void testTest() {
        System.out.println("test");
        Transport instance = new TransportImpl();
        boolean expResult = false;
        boolean result = instance.test();
        assertEquals(expResult, result);
    }

    /**
     * Test of writeln method, of class Transport.
     */
    public void testWriteln() throws Exception {
        System.out.println("writeln");
        String line = "";
        Transport instance = new TransportImpl();
        instance.writeln(line);
    }

    public class TransportImpl implements Transport {

        public void disconnect() {
        }

        public String[] readMetrics() {
            return null;
        }

        public String readln() {
            return "";
        }

        public void setInterval(long interval) {
        }

        public void shutdownAgent() {
        }

        public void startWithMetrics(String[] metricsArray) throws IOException {
        }

        public boolean test() {
            return false;
        }

        public void writeln(String line) throws IOException {
        }
    }
}
