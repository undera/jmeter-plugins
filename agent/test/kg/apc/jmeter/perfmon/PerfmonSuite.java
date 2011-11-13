/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.perfmon;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import kg.apc.jmeter.perfmon.agent.AgentSuite;

/**
 *
 * @author undera
 */
public class PerfmonSuite extends TestCase {
    
    public PerfmonSuite(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite("PerfmonSuite");
        suite.addTest(AgentSuite.suite());
        return suite;
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
