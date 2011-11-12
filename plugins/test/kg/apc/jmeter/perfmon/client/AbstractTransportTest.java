/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
