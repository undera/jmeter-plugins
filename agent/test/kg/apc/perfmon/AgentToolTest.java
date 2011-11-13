package kg.apc.perfmon;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import junit.framework.TestCase;

/**
 *
 * @author undera
 */
public class AgentToolTest extends TestCase {

    private static class PerfMonAgentToolEmul extends AgentTool {

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

        public void processCommands() throws IOException {
            finished = true;
            rc = 0;
        }

        public boolean isFinished() {
            return finished;
        }

        public int getExitCode() {
            return rc;
        }
    }

    public AgentToolTest() {
    }

    private ListIterator argsArrayToListIterator(String[] args) {
        List arrayArgs = Arrays.asList(args);
        return new LinkedList(arrayArgs).listIterator();
    }

    /**
     * Test of processParams method, of class PerfMonAgentTool.
     */
    public void testProcessParams() {
        System.out.println("processParams");
        ListIterator args = argsArrayToListIterator("--tcp-port 4444 --udp-port 4444".split(" "));
        AgentTool instance = new PerfMonAgentToolEmul();

        int expResult = 0;
        int result = instance.processParams(args);
        assertEquals(expResult, result);
    }

    /**
     * Test of showHelp method, of class PerfMonAgentTool.
     */
    public void testShowHelp() {
        System.out.println("showHelp");
        PrintStream os = System.out;
        AgentTool instance = new AgentTool();
        instance.showHelp(os);
    }

    /**
     * Test of getWorker method, of class PerfMonAgentTool.
     */
    public void testGetWorker() throws Exception {
        System.out.println("getWorker");
        AgentTool instance = new AgentTool();
        PerfMonWorker result = instance.getWorker();
        assertNotNull(result);
    }
}
