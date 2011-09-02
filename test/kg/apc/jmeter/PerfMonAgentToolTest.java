package kg.apc.jmeter;

import kg.apc.jmeter.perfmon.PerfMonWorker;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ListIterator;
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
public class PerfMonAgentToolTest {

    private static class PerfMonAgentToolEmul extends PerfMonAgentTool {

        @Override
        protected PerfMonWorker getWorker() throws IOException {
            return new PerfMonWorkerEmul();
        }
    }

    private static class PerfMonWorkerEmul extends PerfMonWorker {

        private boolean finished = false;
        private int rc = -1;

        public PerfMonWorkerEmul() throws IOException {
            super();
        }

        @Override
        public void processCommands() throws IOException {
            finished = true;
            rc = 0;
        }

        @Override
        public boolean isFinished() {
            return finished;
        }

        @Override
        public int getExitCode() {
            return rc;
        }
    }

    public PerfMonAgentToolTest() {
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
     * Test of processParams method, of class PerfMonAgentTool.
     */
    @Test
    public void testProcessParams() {
        System.out.println("processParams");
        ListIterator<String> args = PluginsCMD.argsArrayToListIterator("--tcp-port 4444 --udp-port 4444".split(" "));
        PerfMonAgentTool instance = new PerfMonAgentToolEmul();

        int expResult = 0;
        int result = instance.processParams(args);
        assertEquals(expResult, result);
    }

    /**
     * Test of showHelp method, of class PerfMonAgentTool.
     */
    @Test
    public void testShowHelp() {
        System.out.println("showHelp");
        PrintStream os = System.out;
        PerfMonAgentTool instance = new PerfMonAgentTool();
        instance.showHelp(os);
    }

    /**
     * Test of getWorker method, of class PerfMonAgentTool.
     */
    @Test
    public void testGetWorker() throws Exception {
        System.out.println("getWorker");
        PerfMonAgentTool instance = new PerfMonAgentTool();
        PerfMonWorker result = instance.getWorker();
        assertNotNull(result);
    }
}
