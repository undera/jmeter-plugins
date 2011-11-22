/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import kg.apc.ApcSuite;

/**
 *
 * @author undera
 */
public class KgSuite extends TestCase {
    
    public KgSuite(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite("KgSuite");
        suite.addTest(ApcSuite.suite());
        return suite;
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
