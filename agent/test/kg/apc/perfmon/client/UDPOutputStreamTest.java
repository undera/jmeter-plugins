/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.perfmon.client;

import java.net.DatagramSocket;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class UDPOutputStreamTest extends TestCase {
    
    public UDPOutputStreamTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(UDPOutputStreamTest.class);
        return suite;
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of write method, of class UDPOutputStream.
     */
    public void testWrite() throws Exception {
        System.out.println("write");
        int i = 0;
        DatagramSocket sock = new DatagramSocket();
        UDPOutputStream instance = new UDPOutputStream(sock);
        instance.write(i);
    }
}
