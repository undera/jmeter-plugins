/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.perfmon;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * For this JUnit test to work, the Agent must be started locally on port 4444!!!
 * Orders of test is important
 * A better test class should be written which simulate the agent...
 * @author Stephane Hoblingre
 */
public class AgentConnectorTest {

    public AgentConnectorTest() {
    }

    private static AgentConnector instance = null;

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
     * Test of connect method, of class AgentConnector.
     */
    @Test
    public void testConnect() throws Exception
    {
        System.out.println("connect");
        instance = new AgentConnector("localhost", 4444);
        instance.connect();
    }

    /**
     * Test of getMem method, of class AgentConnector.
     */
    @Test
    public void testGetMem()
    {
        System.out.println("getMem");
        String result = instance.getMem();
        System.out.println(result);
        assertNotNull(result);
    }

    /**
     * Test of getCpu method, of class AgentConnector.
     */
    @Test
    public void testGetCpu()
    {
        System.out.println("getCpu");
        String result = instance.getCpu();
        System.out.println(result);
        assertNotNull(result);
    }

    /**
     * Test of disconnect method, of class AgentConnector.
     */
    @Test
    public void testDisconnect() throws Exception
    {
        System.out.println("disconnect");
        instance.disconnect();
    }

}