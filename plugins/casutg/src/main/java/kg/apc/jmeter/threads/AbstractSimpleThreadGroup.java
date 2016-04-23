package kg.apc.jmeter.threads;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.engine.TreeCloner;
import org.apache.jmeter.threads.AbstractThreadGroup;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.ListedHashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public abstract class AbstractSimpleThreadGroup extends AbstractThreadGroup {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final long WAIT_TO_DIE = JMeterUtils.getPropDefault("jmeterengine.threadstop.wait", 5 * 1000); // 5 seconds

    // List of active threads
    private final Map<JMeterThread, Thread> allThreads = new ConcurrentHashMap<JMeterThread, Thread>();

    /**
     * Is test (still) running?
     */
    private volatile boolean running = false;

    //JMeter 2.7 Compatibility
    private long tgStartTime = -1;
    private static final long TOLERANCE = 1000;


    /**
     * No-arg constructor.
     */
    public AbstractSimpleThreadGroup() {
    }

    protected abstract void scheduleThread(JMeterThread thread, long now);

    //JMeter 2.7 compatibility
    public void scheduleThread(JMeterThread thread) {
       if(System.currentTimeMillis()-tgStartTime > TOLERANCE) {
           tgStartTime = System.currentTimeMillis();
       }
       scheduleThread(thread, tgStartTime);
    }

    
    @Override
    public void start(int groupCount, ListenerNotifier notifier, ListedHashTree threadGroupTree, StandardJMeterEngine engine) {
        running = true;

        int numThreads = getNumThreads();
        
        log.info("Starting thread group number " + groupCount
                + " threads " + numThreads);
       
            long now = System.currentTimeMillis(); // needs to be same time for all threads in the group
            final JMeterContext context = JMeterContextService.getContext();
            for (int i = 0; running && i < numThreads; i++) {
                JMeterThread jmThread = makeThread(groupCount, notifier, threadGroupTree, engine, i, context);
                scheduleThread(jmThread, now); // set start and end time
                Thread newThread = new Thread(jmThread, jmThread.getThreadName());
                registerStartedThread(jmThread, newThread);
                newThread.start();
            }

        log.info("Started thread group number "+groupCount);
    }

    private void registerStartedThread(JMeterThread jMeterThread, Thread newThread) {
        allThreads.put(jMeterThread, newThread);
    }

    private JMeterThread makeThread(int groupCount,
            ListenerNotifier notifier, ListedHashTree threadGroupTree,
            StandardJMeterEngine engine, int i,
            JMeterContext context) { // N.B. Context needs to be fetched in the correct thread
        boolean onErrorStopTest = getOnErrorStopTest();
        boolean onErrorStopTestNow = getOnErrorStopTestNow();
        boolean onErrorStopThread = getOnErrorStopThread();
        boolean onErrorStartNextLoop = getOnErrorStartNextLoop();
        String groupName = getName();
        final JMeterThread jmeterThread = new JMeterThread(cloneTree(threadGroupTree), this, notifier);
        jmeterThread.setThreadNum(i);
        jmeterThread.setThreadGroup(this);
        jmeterThread.setInitialContext(context);
        final String threadName = groupName + " " + (groupCount) + "-" + (i + 1);
        jmeterThread.setThreadName(threadName);
        jmeterThread.setEngine(engine);
        jmeterThread.setOnErrorStopTest(onErrorStopTest);
        jmeterThread.setOnErrorStopTestNow(onErrorStopTestNow);
        jmeterThread.setOnErrorStopThread(onErrorStopThread);
        jmeterThread.setOnErrorStartNextLoop(onErrorStartNextLoop);
        return jmeterThread;
    }

    @Override
    public boolean stopThread(String threadName, boolean now) {
        for(Entry<JMeterThread, Thread> entry : allThreads.entrySet()){
            JMeterThread thrd = entry.getKey();
            if (thrd.getThreadName().equals(threadName)){
                thrd.stop();
                thrd.interrupt();
                if (now) {
                    Thread t = entry.getValue();
                    if (t != null) {
                        t.interrupt();
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void threadFinished(JMeterThread thread) {
        log.debug("Ending thread " + thread.getThreadName());
        allThreads.remove(thread);
    }

    @Override
    public void tellThreadsToStop() {
        running = false;
        for (Entry<JMeterThread, Thread> entry : allThreads.entrySet()) {
            JMeterThread item = entry.getKey();
            item.stop(); // set stop flag
            item.interrupt(); // interrupt sampler if possible
            Thread t = entry.getValue();
            if (t != null ) { // Bug 49734
                t.interrupt(); // also interrupt JVM thread
            }
        }
    }

    @Override
    public void stop() {
        running = false;
        for (JMeterThread item : allThreads.keySet()) {
            item.stop();
        }
    }

    @Override
    public int numberOfActiveThreads() {
        return allThreads.size();
    }

    @Override
    public boolean verifyThreadsStopped() {
        boolean stoppedAll = true;
        for (Thread t : allThreads.values()) {
            stoppedAll = stoppedAll && verifyThreadStopped(t);
        }
        return stoppedAll;
    }

    private boolean verifyThreadStopped(Thread thread) {
        boolean stopped = true;
        if (thread != null) {
            if (thread.isAlive()) {
                try {
                    thread.join(WAIT_TO_DIE);
                } catch (InterruptedException e) {
                }
                if (thread.isAlive()) {
                    stopped = false;
                    log.warn("Thread won't exit: " + thread.getName());
                }
            }
        }
        return stopped;
    }

    @Override
    public void waitThreadsStopped() {
        for (Thread t : allThreads.values()) {
            waitThreadStopped(t);
        }
    }

    private void waitThreadStopped(Thread thread) {
        if (thread != null) {
            while (thread.isAlive()) {
                try {
                    thread.join(WAIT_TO_DIE);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private ListedHashTree cloneTree(ListedHashTree tree) {
        TreeCloner cloner = new TreeCloner(true);
        tree.traverse(cloner);
        return cloner.getClonedTree();
    }
}

