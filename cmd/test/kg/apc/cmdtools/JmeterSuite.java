/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.cmdtools;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class JmeterSuite extends TestCase {
    
    public JmeterSuite(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite("JmeterSuite");
        suite.addTest(PluginsCMDTest.suite());
        suite.addTest(HelloWorldToolTest.suite());
        return suite;
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
