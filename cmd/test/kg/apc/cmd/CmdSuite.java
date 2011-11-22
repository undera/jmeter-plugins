/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.cmd;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class CmdSuite extends TestCase {
    
    public CmdSuite(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite("CmdSuite");
        suite.addTest(UniversalRunnerTest.suite());
        return suite;
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
