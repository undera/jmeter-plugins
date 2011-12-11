package kg.apc.cmdtools;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * This class used to handle all command-line stuff
 * like parameter processing etc. All real work
 * made by PluginsCMDWorker
 * @author undera
 * @see PluginsCMDWorker
 */
public class PluginsCMD extends AbstractCMDTool {

    public int processParams(String[] args) {
        if (args == null) {
            args = new String[]{"--help"};
        }

        return processParams(argsArrayToListIterator(args));
    }

    public static ListIterator argsArrayToListIterator(String[] args) {
        List arrayArgs = Arrays.asList(args);
        return new LinkedList(arrayArgs).listIterator();
    }

    protected int processParams(ListIterator args) throws UnsupportedOperationException, IllegalArgumentException {
        AbstractCMDTool tool = null;

        while (args.hasNext()) {
            String arg = (String) args.next();
            if (arg.equals("-?") || arg.equals("--help")) {
                showHelp(System.out);
                // FIXME: how to show help for the tool?
                return 0;
            } else if (arg.equals("--version")) {
                showVersion(System.out);
                return 0;
            } else if (arg.equals("")) {
                args.remove();
            } else if (arg.equals("--tool")) {
                args.remove();
                if (!args.hasNext()) {
                    throw new IllegalArgumentException("No tool name passed");
                }
                arg = (String) args.next();
                tool = getToolInstance(arg);
                args.remove();
            }
        }

        if (tool == null) {
            throw new IllegalArgumentException("No suitable tool class provided in params");
        }

        while (args.hasPrevious()) {
            args.previous();
        }
        return tool.processParams(args);
    }

    private void showVersion(PrintStream os) {
        os.println("JP@GC Tools v. 0.4.3");
    }

    protected void showHelp(PrintStream os) {
        os.println("JMeter Plugins at Google Code Command-Line Tools");
        os.println("For help and support please visit http://code.google.com/p/jmeter-plugins/wiki/JMeterPluginsCMD");
        os.println("Usage:\n JMeterPluginsCMD "
                + "--tool < Reporter | PerfMonAgent > [--help]");

        AbstractCMDTool tool;
        try {
            tool = getToolInstance("Reporter");
            os.println();
            tool.showHelp(os);
        } catch (RuntimeException e) {
            os.println(e.getMessage());
        }

        try {
            tool = getToolInstance("PerfMonAgent");
            os.println();
            tool.showHelp(os);
        } catch (RuntimeException e) {
            os.println(e.getMessage());
        }
    }

    private AbstractCMDTool getToolInstance(String arg) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class toolClass;

        try {
            toolClass = loader.loadClass("kg.apc.jmeter." + arg + "Tool");
        } catch (ClassNotFoundException e) {
            try {
                toolClass = loader.loadClass(arg);
            } catch (ClassNotFoundException ex) {
                throw new IllegalArgumentException("Tool class " + arg + " not found");
            }
        }

        if (!AbstractCMDTool.class.isAssignableFrom(toolClass)) {
            throw new IllegalArgumentException("Tool class " + arg + " not extends AbstractCMDTool");
        }

        try {
            return (AbstractCMDTool) toolClass.newInstance();
        } catch (InstantiationException ex) {
            throw new RuntimeException("Cannot instantiate tool class: " + arg, ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Cannot instantiate tool class: " + arg, ex);
        }
    }
}
