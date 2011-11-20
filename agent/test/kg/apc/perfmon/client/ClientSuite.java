/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.perfmon.client;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class ClientSuite extends TestCase {
    
    public ClientSuite(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite("ClientSuite");
        suite.addTest(NIOTransportTest.suite());
        suite.addTest(UDPInputStreamTest.suite());
        suite.addTest(UDPOutputStreamTest.suite());
        suite.addTest(StreamTransportTest.suite());
        suite.addTest(TransportFactoryTest.suite());
        suite.addTest(TransportTest.suite());
        suite.addTest(AbstractTransportTest.suite());
        return suite;
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
