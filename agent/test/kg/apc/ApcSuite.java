/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import kg.apc.perfmon.PerfmonSuite;

/**
 *
 * @author undera
 */
public class ApcSuite extends TestCase {
    
    public ApcSuite(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite("ApcSuite");
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
