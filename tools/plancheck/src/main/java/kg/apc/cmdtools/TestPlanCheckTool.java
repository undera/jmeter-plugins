package kg.apc.cmdtools;

import kg.apc.cmd.UniversalRunner;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.JMeter;
import org.apache.jmeter.assertions.Assertion;
import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.control.Controller;
import org.apache.jmeter.engine.JMeterEngine;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.processor.PostProcessor;
import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.reporters.AbstractListenerElement;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.testelement.WorkBench;
import org.apache.jmeter.threads.AbstractThreadGroup;
import org.apache.jmeter.timers.Timer;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.HashTreeTraverser;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.apache.log.Priority;

import java.io.File;
import java.io.PrintStream;
import java.util.ListIterator;

public class TestPlanCheckTool extends AbstractCMDTool {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private String jmx = null;

    public TestPlanCheckTool() {
        super();
        JMeterPluginsUtils.prepareJMeterEnv(UniversalRunner.getJARLocation());
    }

    private boolean isStats = false;
    private boolean isDump = false;

    @Override
    protected void showHelp(PrintStream os) {
        os.println("Options for tool 'TestPlanCheck': --jmx <filename>"
                + " ["
                + " --stats"
                + " --tree-dump"
                + " ]");
    }

    @Override
    protected int processParams(ListIterator args) throws UnsupportedOperationException, IllegalArgumentException {
        // TODO: extract this duplicated code
        LoggingManager.setPriority(Priority.INFO);
        // first process params without worker created
        while (args.hasNext()) {
            String nextArg = (String) args.next();
            if (nextArg.equals("--loglevel")) {
                args.remove();
                String loglevelStr = (String) args.next();
                args.remove();
                LoggingManager.setPriority(loglevelStr);
            }
        }

        // rewind it
        while (args.hasPrevious()) {
            args.previous();
        }


        while (args.hasNext()) {
            String nextArg = (String) args.next();
            log.debug("Arg: " + nextArg);
            if (nextArg.equalsIgnoreCase("--jmx")) {
                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing JMX file name");
                }

                jmx = (String) args.next();
            } else if (nextArg.equalsIgnoreCase("--stats")) {
                isStats = true;
            } else if (nextArg.equalsIgnoreCase("--tree-dump")) {
                isDump = true;
            } else {
                throw new IllegalArgumentException("Unknown option: " + nextArg);
            }
        }

        if (jmx == null) {
            throw new IllegalArgumentException("Cannot proceed without --jmx option");
        }

        return doJob();
    }

    private int doJob() {
        HashTree testTree;
        try {
            testTree = loadJMX(new File(jmx));
            log.info("JMX is fine");
        } catch (Exception e) {
            log.error("Failed to load JMX", e);
            return 1;
        }

        if (isStats) {
            showStats(testTree);
        }

        if (isDump) {
            dumpTree(testTree);
        }
        return 0;
    }

    private void dumpTree(HashTree testTree) {
        log.info("Dumping tree structure:");
        testTree.traverse(new TreeDumper());

    }

    private void showStats(HashTree testTree) {
        log.info("Element stats goes below:");
        StatsCollector stats = new StatsCollector();
        testTree.traverse(stats);
        stats.logStats();
    }

    private HashTree loadJMX(File file) throws Exception {
        HashTree tree = SaveService.loadTree(file);

        // unfortunately core JMeter code does not throw exception, we may only guess...
        if (tree == null) {
            throw new TestPlanBrokenException("There was problems loading test plan. Please investigate error messages above.");
        }

        JMeter.convertSubTree(tree); // Remove the disabled items

        JMeterEngine engine = new StandardJMeterEngine();
        engine.configure(tree);

        return tree;
    }

    public class TestPlanBrokenException extends RuntimeException {
        public TestPlanBrokenException(String s) {
            super(s);
        }
    }

    private class TreeDumper implements HashTreeTraverser {
        private int indent = 0;

        @Override
        public void addNode(Object o, HashTree hashTree) {
            if (o instanceof TestElement) {
                TestElement el = (TestElement) o;
                log.info(StringUtils.repeat(" ", indent) + "[" + el.getClass().getSimpleName() + "] " + el.getName());
            } else {
                log.info(StringUtils.repeat(" ", indent) + o);
            }
            indent++;
        }

        @Override
        public void subtractNode() {
            indent--;
        }

        @Override
        public void processPath() {
        }
    }

    private class StatsCollector implements HashTreeTraverser {
        private int tGroups = 0;
        private int controllers = 0;
        private int samplers = 0;
        private int listeners = 0;
        private int others = 0;
        private int preProc = 0;
        private int postProc = 0;
        private int assertions = 0;
        private int timers = 0;
        private int configs = 0;

        @Override
        public void addNode(Object node, HashTree subTree) {
            if (node instanceof AbstractThreadGroup) {
                tGroups++;
            } else if (node instanceof Controller) {
                controllers++;
            } else if (node instanceof Sampler) {
                samplers++;
            } else if (node instanceof AbstractListenerElement) {
                listeners++;
            } else if (node instanceof PreProcessor) {
                preProc++;
            } else if (node instanceof PostProcessor) {
                postProc++;
            } else if (node instanceof Assertion) {
                assertions++;
            } else if (node instanceof Timer) {
                timers++;
            } else if (node instanceof ConfigElement) {
                configs++;
            } else if (node instanceof TestPlan) {
                log.debug("Ok, we got the root of test plan");
            } else if (node instanceof WorkBench) {
                log.debug("Ok, we got the root of test plan");
            } else {
                log.warn("Strange object in tree: " + node);
                others++;
            }
        }

        @Override
        public void subtractNode() {

        }

        @Override
        public void processPath() {

        }

        public void logStats() {
            log.info("Thread Groups:\t" + tGroups);
            log.info("Controllers:\t" + controllers);
            log.info("Config Items:\t" + configs);
            log.info("Samplers:\t" + samplers);
            log.info("Listeners:\t" + listeners);
            log.info("Timers:\t" + timers);
            log.info("Assertions:\t" + assertions);
            log.info("Pre-Processors:\t" + preProc);
            log.info("Post-Processors:\t" + postProc);
            if (others > 0) {
                log.info("Unknown Elements:\t" + others);
            }
        }
    }
}
