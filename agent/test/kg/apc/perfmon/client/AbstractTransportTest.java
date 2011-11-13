package kg.apc.perfmon.client;

import junit.framework.TestCase;

/**
 *
 * @author undera
 */
public class AbstractTransportTest extends TestCase {

    public AbstractTransportTest() {
    }

    public static void setUpClass() throws Exception {
    }

    public static void tearDownClass() throws Exception {
    }

    public void setUp() {
    }

    public void tearDown() {
    }

    /**
     * Test of disconnect method, of class AbstractTransport.
     */
    public void testDisconnect() {
        System.out.println("disconnect");
        AbstractTransport instance = new AbstractTransportImpl();
        instance.disconnect();
    }

    /**
     * Test of writeln method, of class AbstractTransport.
     */
    public void testWriteln() {
        System.out.println("writeln");
        String string = "";
        AbstractTransport instance = new AbstractTransportImpl();
        instance.writeln(string);
    }

    /**
     * Test of readln method, of class AbstractTransport.
     */
    public void testReadln() {
        System.out.println("readln");
        AbstractTransport instance = new AbstractTransportImpl();
        String expResult = "";
        String result = instance.readln();
        assertEquals(expResult, result);
    }

    public class AbstractTransportImpl implements AbstractTransport {

        public void disconnect() {
        }

        public void writeln(String string) {
        }

        public String readln() {
            return "";
        }
    }
}
