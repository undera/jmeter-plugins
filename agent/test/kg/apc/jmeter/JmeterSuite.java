/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import kg.apc.jmeter.perfmon.PerfmonSuite;

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
        suite.addTest(PerfmonSuite.suite());
        return suite;
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
