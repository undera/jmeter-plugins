package kg.apc.jmeter.perfmon.client;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author undera
 */
public class AbstractTransportTest {

    public AbstractTransportTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of disconnect method, of class AbstractTransport.
     */
    @Test
    public void testDisconnect() {
        System.out.println("disconnect");
        AbstractTransport instance = new AbstractTransportImpl();
        instance.disconnect();
    }

    /**
     * Test of writeln method, of class AbstractTransport.
     */
    @Test
    public void testWriteln() {
        System.out.println("writeln");
        String string = "";
        AbstractTransport instance = new AbstractTransportImpl();
        instance.writeln(string);
    }

    /**
     * Test of readln method, of class AbstractTransport.
     */
    @Test
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
