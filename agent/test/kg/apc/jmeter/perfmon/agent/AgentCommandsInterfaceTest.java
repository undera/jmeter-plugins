package kg.apc.jmeter.perfmon.agent;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class AgentCommandsInterfaceTest extends TestCase {

    public AgentCommandsInterfaceTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(AgentCommandsInterfaceTest.class);
        return suite;
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSomeMethod() {
        // nothing to test
    }
}
