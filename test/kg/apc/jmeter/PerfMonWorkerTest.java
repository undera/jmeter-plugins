/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author undera
 */
public class PerfMonWorkerTest {

    public PerfMonWorkerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setTCPPort method, of class PerfMonWorker.
     */
    @Test
    public void testSetTCPPort() throws IOException {
        System.out.println("setTCPPort");
        int parseInt = 0;
        PerfMonWorker instance = new PerfMonWorker();
        instance.setTCPPort(parseInt);
    }

    /**
     * Test of setUDPPort method, of class PerfMonWorker.
     */
    @Test
    public void testSetUDPPort() throws IOException {
        System.out.println("setUDPPort");
        int parseInt = 0;
        PerfMonWorker instance = new PerfMonWorker();
        instance.setUDPPort(parseInt);
    }

    /**
     * Test of isFinished method, of class PerfMonWorker.
     */
    @Test
    public void testIsFinished() throws IOException {
        System.out.println("isFinished");
        PerfMonWorker instance = new PerfMonWorker();
        boolean expResult = true;
        boolean result = instance.isFinished();
        assertEquals(expResult, result);
    }

    /**
     * Test of processCommands method, of class PerfMonWorker.
     */
    @Test
    public void testProcessCommands() throws Exception {
        System.out.println("processCommands");
        PerfMonWorker instance = new PerfMonWorker();
        instance.processCommands();
    }

    /**
     * Test of getExitCode method, of class PerfMonWorker.
     */
    @Test
    public void testGetExitCode() throws IOException {
        System.out.println("getExitCode");
        PerfMonWorker instance = new PerfMonWorker();
        int expResult = 0;
        int result = instance.getExitCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of startAcceptingCommands method, of class PerfMonWorker.
     */
    @Test
    public void testStartAcceptingCommands() throws Exception {
        System.out.println("startAcceptingCommands");
        PerfMonWorker instance = new PerfMonWorker();
        instance.startAcceptingCommands();
    }
}
