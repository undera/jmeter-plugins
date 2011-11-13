/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.perfmon.agent;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class AgentSuite extends TestCase {
    
    public AgentSuite(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite("AgentSuite");
        suite.addTest(MetricsGetterTest.suite());
        suite.addTest(ConnectionThreadTest.suite());
        suite.addTest(ServerAgentTest.suite());
        suite.addTest(AgentCommandsInterfaceTest.suite());
        return suite;
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
