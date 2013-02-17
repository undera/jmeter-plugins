/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.perfmon;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author undera
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({kg.apc.jmeter.perfmon.OldAgentConnectorTest.class, kg.apc.jmeter.perfmon.UnavailableAgentConnectorTest.class, kg.apc.jmeter.perfmon.AgentConnectorTest.class, kg.apc.jmeter.perfmon.NewAgentConnectorTest.class, kg.apc.jmeter.perfmon.PerfMonExceptionTest.class, kg.apc.jmeter.perfmon.PerfMonAgentConnectorTest.class, kg.apc.jmeter.perfmon.PerfMonSampleGeneratorTest.class, kg.apc.jmeter.perfmon.PerfMonSampleResultTest.class, kg.apc.jmeter.perfmon.PerfMonCollectorTest.class})
public class PerfmonSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
