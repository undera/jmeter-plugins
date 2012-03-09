/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.perfmon.metrics.jmx;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class JmxSuite extends TestCase {
    
    public JmxSuite(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite("JmxSuite");
        suite.addTest(ClassesDataProviderTest.suite());
        suite.addTest(AbstractJMXDataProviderTest.suite());
        suite.addTest(MemoryDataProviderTest.suite());
        suite.addTest(CompilerDataProviderTest.suite());
        suite.addTest(JMXConnectorHelperTest.suite());
        suite.addTest(MemoryPoolDataProviderTest.suite());
        suite.addTest(GCDataProviderTest.suite());
        return suite;
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
