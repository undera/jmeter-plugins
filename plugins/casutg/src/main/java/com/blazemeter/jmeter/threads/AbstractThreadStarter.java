package com.blazemeter.jmeter.threads;

import com.blazemeter.jmeter.control.VirtualUserController;
import org.apache.jmeter.control.Controller;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.engine.TreeCloner;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.ListedHashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public abstract class AbstractThreadStarter extends Thread {
    private static final Logger log = LoggingManager.getLoggerForClass();
    protected final ListenerNotifier notifier;
    protected final ListedHashTree threadGroupTree;
    protected final StandardJMeterEngine engine;
    protected final JMeterContext context;
    protected final AbstractDynamicThreadGroup owner;
    protected final int groupIndex;
    protected long threadIndex = 0;
    protected HashTree treeClone;
    protected double startTime = 0;

    public AbstractThreadStarter(int groupIndex, AbstractDynamicThreadGroup owner, ListedHashTree listedHashTree, ListenerNotifier listenerNotifier, StandardJMeterEngine standardJMeterEngine) {
        super();
        this.owner = owner;
        this.treeClone = cloneTree(listedHashTree); // it needs owner inside
        this.engine = standardJMeterEngine;
        this.groupIndex = groupIndex;
        this.threadGroupTree = listedHashTree;
        this.notifier = listenerNotifier;
        this.context = JMeterContextService.getContext();
        setDaemon(true);
    }

    @Override
    public void run() {
        try {
            // Copy in ThreadStarter thread context from calling Thread
            JMeterContextService.getContext().setVariables(this.context.getVariables());
            supplyActiveThreads();
        } catch (InterruptedException e) {
            log.debug("Interrupted", e);
        }
        log.debug("Thread starter has done its job");
    }

    abstract protected void supplyActiveThreads() throws InterruptedException;

    protected DynamicThread makeThread(long threadIndex) {
        boolean onErrorStopTest = owner.getOnErrorStopTest();
        boolean onErrorStopTestNow = owner.getOnErrorStopTestNow();
        boolean onErrorStopThread = owner.getOnErrorStopThread();
        boolean onErrorStartNextLoop = owner.getOnErrorStartNextLoop();
        final DynamicThread jmeterThread = new DynamicThread(treeClone, this.owner, notifier);
        jmeterThread.setThreadNum((int) threadIndex);
        jmeterThread.setThreadGroup(this.owner);
        jmeterThread.setInitialContext(context);
        final String threadName = owner.getName() + " " + groupIndex + "-" + (threadIndex + 1);
        jmeterThread.setThreadName(threadName);
        jmeterThread.setEngine(engine);
        jmeterThread.setOnErrorStopTest(onErrorStopTest);
        jmeterThread.setOnErrorStopTestNow(onErrorStopTestNow);
        jmeterThread.setOnErrorStopThread(onErrorStopThread);
        jmeterThread.setOnErrorStartNextLoop(onErrorStartNextLoop);
        return jmeterThread;
    }

    // had to copy it from ThreadGroup
    protected ListedHashTree cloneTree(ListedHashTree tree) {
        TreeCloner cloner = new TreeCloner(true);
        tree.traverse(cloner);
        ListedHashTree clonedTree = cloner.getClonedTree();
        if (!clonedTree.isEmpty()) {
            Object firstElement = clonedTree.getArray()[0];
            Controller samplerController = ((AbstractDynamicThreadGroup) firstElement).getSamplerController();
            if (samplerController instanceof VirtualUserController) {
                assert owner != null;
                ((VirtualUserController) samplerController).setOwner(owner);
            }
        }
        return clonedTree;
    }

    protected DynamicThread addActiveThread() {
        DynamicThread threadWorker = makeThread(threadIndex++);
        owner.addThread(threadWorker);
        Thread thread = new Thread(threadWorker, threadWorker.getThreadName());
        threadWorker.setOSThread(thread);
        thread.setDaemon(false); // we can't have it daemon, since it will stay and eat RAM in UI mode
        thread.start();
        treeClone = cloneTree(threadGroupTree); // use background time to clone tree
        return threadWorker;
    }

}
