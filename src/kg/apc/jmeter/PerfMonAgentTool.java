package kg.apc.jmeter;

import java.io.PrintStream;
import java.util.ListIterator;

/**
 *
 * @author undera
 */
public class PerfMonAgentTool extends AbstractCMDTool {

    @Override
    protected int processParams(ListIterator<String> args) throws UnsupportedOperationException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void showHelp(PrintStream os) {
        // tcp-port
        // udp-port
    }
}
