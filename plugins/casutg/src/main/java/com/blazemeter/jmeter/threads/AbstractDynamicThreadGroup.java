package com.blazemeter.jmeter.threads;

import com.blazemeter.jmeter.control.VirtualUserController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jorphan.collections.ListedHashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.util.Set;

// adding some basic methods to our Model
public abstract class AbstractDynamicThreadGroup extends AbstractDynamicThreadGroupModel {
    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String UNIT = "Unit";
    public static final String UNIT_MINUTES = "M";
    public static final String UNIT_SECONDS = "S";
    protected transient Thread threadStarter;

    public AbstractDynamicThreadGroup() {
        super();
        setProperty(new TestElementProperty(MAIN_CONTROLLER, new VirtualUserController()));
    }

    @Override
    public void start(int groupIndex, ListenerNotifier listenerNotifier, ListedHashTree testTree, StandardJMeterEngine engine) {
        running = true;
        threadStarter = getThreadStarter(groupIndex, listenerNotifier, testTree, engine);
        threadStarter.setName(getName() + "-ThreadStarter");
        threadStarter.start();
    }

    protected abstract Thread getThreadStarter(int groupIndex, ListenerNotifier listenerNotifier, ListedHashTree testTree, StandardJMeterEngine engine);

    @Override
    public void threadFinished(JMeterThread jMeterThread) {
        log.debug("threadFinished: " + jMeterThread.getThreadName());
        if (jMeterThread instanceof DynamicThread) {
            threads.remove(jMeterThread);
        }
    }

    @Override
    public void waitThreadsStopped() {
        while (running) {
            if (!threads.isEmpty()) {
                joinThreadFrom(threads);
            } else if (isLimitReached()) {
                log.debug("Don't need more load, running=false");
                running = false;
            } else if (!threadStarter.isAlive()) {
                log.debug("Thread Starter is done and we have no active threads, let's finish with this");
                running = false;
            } else {
                log.debug("Nothing to do, let's have some sleep");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    log.warn("Interrupted", e);
                }
            }
        }
        log.debug("Done waiting for threads stopped");
    }

    public abstract boolean isLimitReached();

    @Override
    public boolean verifyThreadsStopped() {
        for (DynamicThread thread : threads) {
            if (thread.getOSThread() != null) {
                try {
                    thread.getOSThread().join(WAIT_TO_DIE);
                } catch (InterruptedException e) {
                    log.warn("Interrupted", e);
                }
            }
            stopThread(thread.getThreadName(), true);
        }
        return threads.isEmpty();
    }

    @Override
    public void tellThreadsToStop() {
        running = false;
        threadStarter.interrupt();

        for (DynamicThread thread : threads) {
            stopThread(thread.getThreadName(), false);
        }
    }

    /**
     * Forceful stop of test calls this
     */
    @Override
    public void stop() {
        running = false;
        threadStarter.interrupt();
        for (DynamicThread thread : threads) {
            thread.interrupt();
            thread.interruptOSThread();
        }
    }

    @Override
    public boolean stopThread(String threadName, boolean forced) {
        for (DynamicThread thrd : threads) {
            if (thrd.getThreadName().equals(threadName)) {
                thrd.stop();
                thrd.interrupt();
                if (forced) {
                    if (thrd.getOSThread() != null) {
                        thrd.getOSThread().interrupt();
                    }
                }
                return true;
            }
        }
        return false;
    }

    protected void joinThreadFrom(Set<DynamicThread> threadSet) {
        DynamicThread[] threads = threadSet.toArray(new DynamicThread[threadSet.size()]);
        if (threads.length > 0 && threads[0] != null) {
            DynamicThread thread = threads[0];
            log.debug("Joining thread " + thread.getThreadName());
            if (thread.getOSThread() != null) {
                try {
                    thread.getOSThread().join(WAIT_TO_DIE);
                } catch (InterruptedException e) {
                    log.warn("Interrupted", e);
                }
            }
            log.debug("Done joining thread " + thread.getThreadName());
        }
    }

    public boolean isRunning() {
        return running;
    }

    public static String getUnitStr(String unit) {
        if (unit.equals(UNIT_MINUTES)) {
            return "min";
        } else {
            return "sec";
        }
    }

    public void setUnit(String value) {
        setProperty(UNIT, value);
    }

    public String getUnit() {
        return getPropertyAsString(UNIT);
    }

    public double getUnitFactor() {
        if (getUnit().equals(UNIT_MINUTES)) {
            return 60.0;
        } else {
            return 1;
        }

    }

    public String getUnitStr() {
        String unit = getUnit();
        return getUnitStr(unit);
    }

    @Override
    public void startNextLoop() {
        ((VirtualUserController) getSamplerController()).startNextLoop();
    }
}
