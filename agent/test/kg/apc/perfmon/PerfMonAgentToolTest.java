package kg.apc.perfmon;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ListIterator;
import junit.framework.TestCase;

/**
 *
 * @author undera
 */
public class PerfMonAgentToolTest extends TestCase{

    private static class PerfMonAgentToolEmul extends PerfMonAgentTool {

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

    public PerfMonAgentToolTest() {
    }


    /**
     * Test of processParams method, of class PerfMonAgentTool.
     */
    public void testProcessParams() {
        System.out.println("processParams");
        ListIterator args = null;//PluginsCMD.argsArrayToListIterator("--tcp-port 4444 --udp-port 4444".split(" "));
        PerfMonAgentTool instance = new PerfMonAgentToolEmul();

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
        PerfMonAgentTool instance = new PerfMonAgentTool();
        instance.showHelp(os);
    }

    /**
     * Test of getWorker method, of class PerfMonAgentTool.
     */
    public void testGetWorker() throws Exception {
        System.out.println("getWorker");
        PerfMonAgentTool instance = new PerfMonAgentTool();
        PerfMonWorker result = instance.getWorker();
        assertNotNull(result);
    }
}
