package kg.apc.jmeter;

import java.io.PrintStream;
import java.util.ListIterator;

/**
 *
 * @author undera
 */
public class HelloWorldTool extends AbstractCMDTool {

    @Override
    protected int processParams(ListIterator<String> args) throws UnsupportedOperationException, IllegalArgumentException {
        System.out.println("Hello, World!");
        return 0;
    }

    @Override
    protected void showHelp(PrintStream os) {
        os.println("This tool just prints 'Hello, World!'");
    }
}
