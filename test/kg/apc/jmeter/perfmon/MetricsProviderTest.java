/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.perfmon;

import javax.net.SocketFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author z000205
 */
public class MetricsProviderTest {

    public MetricsProviderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of run method, of class MetricsProvider.
     */
    @Test
    public void testRun()
    {
        System.out.println("run");
        MetricsProvider instance = null;
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of testStarted method, of class MetricsProvider.
     */
    @Test
    public void testTestStarted()
    {
        System.out.println("testStarted");
        MetricsProvider instance = null;
        instance.testStarted();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of testEnded method, of class MetricsProvider.
     */
    @Test
    public void testTestEnded()
    {
        System.out.println("testEnded");
        MetricsProvider instance = null;
        instance.testEnded();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSocketFactory method, of class MetricsProvider.
     */
    @Test
    public void testSetSocketFactory()
    {
        System.out.println("setSocketFactory");
        SocketFactory sf = null;
        MetricsProvider instance = null;
        instance.setSocketFactory(sf);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}