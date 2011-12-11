package kg.apc.cmdtools;

import java.io.PrintStream;
import java.util.ListIterator;

/**
 *
 * @author undera
 */
public abstract class AbstractCMDTool {

    public AbstractCMDTool() {
    }

    protected int getLogicValue(String string) {
        if (string.equalsIgnoreCase("on")) {
            return 1;
        }
        if (string.equalsIgnoreCase("1")) {
            return 1;
        }
        if (string.equalsIgnoreCase("yes")) {
            return 1;
        }
        return 0;
    }

    protected abstract int processParams(ListIterator args) throws UnsupportedOperationException, IllegalArgumentException;

    protected abstract void showHelp(PrintStream os);
}
