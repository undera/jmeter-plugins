package com.blazemeter.jmeter.threads.arrivals;

import com.blazemeter.jmeter.control.VirtualUserController;
import com.blazemeter.jmeter.threads.AbstractDynamicThreadGroup;
import com.blazemeter.jmeter.threads.DynamicThread;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jmeter.threads.ThreadCountsAccessor;
import org.apache.jorphan.collections.ListedHashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ArrivalsThreadGroup extends AbstractDynamicThreadGroup {
    public static final String CONCURRENCY_LIMIT = "ConcurrencyLimit";
    public static final String ARRIVALS_LIMIT = "ArrivalsLimit";
    private static final Logger log = LoggingManager.getLoggerForClass();
    protected final AtomicLong arrivalsCount = new AtomicLong();
    protected final AtomicLong completionsCount = new AtomicLong();
    protected AtomicLong abandonsCount = new AtomicLong();
    protected final Set<DynamicThread> poolThreads = Collections.newSetFromMap(new ConcurrentHashMap<DynamicThread, Boolean>());

    @Override
    public void start(int groupIndex, ListenerNotifier listenerNotifier, ListedHashTree testTree, StandardJMeterEngine engine) {
        super.start(groupIndex, listenerNotifier, testTree, engine);
        synchronized (this) {
            try {
                wait();
                log.info("Got first arrival");
            } catch (InterruptedException e) {
                log.warn("Interrupted start", e);
            }
        }
    }

    public int getNumThreads() {
        return threads.size();
    }

    @Override
    public void addThread(DynamicThread threadWorker) {
        super.addThread(threadWorker);
        JMeterContextService.addTotalThreads(1);
    }

    @Override
    protected Thread getThreadStarter(int groupIndex, ListenerNotifier listenerNotifier, ListedHashTree testTree, StandardJMeterEngine engine) {
        return new ArrivalsThreadStarter(groupIndex, listenerNotifier, testTree, engine, this);
    }

    /**
     * Forceful stop of test calls this
     */
    @Override
    public void stop() {
        super.stop();

        for (DynamicThread thread : poolThreads) {
            thread.interruptOSThread();
        }
    }

    /**
     * Graceful shutdown of test calls this, then #verifyThreadsStopped
     */
    @Override
    public void tellThreadsToStop() {
        super.tellThreadsToStop();
        for (DynamicThread thread : poolThreads) {
            stopThread(thread.getThreadName(), true);
        }
    }

    @Override
    public boolean verifyThreadsStopped() {
        boolean parent = super.verifyThreadsStopped();
        log.info("Verify shutdown thread counts: " + threads.size() + "/" + poolThreads.size());
        return parent && poolThreads.isEmpty();
    }

    public boolean movedToPool(DynamicThread thread) {
        threads.remove(thread);
        if (thread.isStopping()) {
            log.debug("Did not move into pool, because thread is stopping: " + thread);
            return false;
        }

        poolThreads.add(thread);
        log.debug("Moved thread to pool: " + thread + ", pool size: " + poolThreads.size());

        ThreadCountsAccessor.decrNumberOfThreads();
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (thread) {
            try {
                thread.wait();
            } catch (InterruptedException e) {
                log.debug("Interrupted", e);
            }
        }
        ThreadCountsAccessor.incrNumberOfThreads();
        return running;
    }

    public synchronized boolean releasedPoolThread() {
        if (poolThreads.isEmpty()) {
            return false;
        }

        DynamicThread thread = poolThreads.toArray(new DynamicThread[poolThreads.size()])[0];
        poolThreads.remove(thread);
        threads.add(thread);
        log.debug("Releasing pool thread: " + thread + ", pool size: " + poolThreads.size());
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (thread) {
            thread.notify();
        }
        return true;
    }

    public boolean isLimitReached() {
        long limit;
        try {
            limit = Long.parseLong(getArrivalsLimit());
        } catch (NumberFormatException e) {
            log.error("Invalid arrivals limit, defaulting to 0");
            limit = 0;
        }
        return !(limit <= 0 || arrivalsCount.longValue() < limit);
    }

    public synchronized void arrivalFact(JMeterThread thread, long arrivalID) {
        arrivalsCount.incrementAndGet();
        notifyAll();
        saveLogRecord("ARRIVAL", thread.getThreadName(), thread.getThreadNum() + "." + arrivalID);
    }

    public void completionFact(JMeterThread thread, long arrivalID) {
        completionsCount.incrementAndGet();
        saveLogRecord("COMPLETION", thread.getThreadName(), thread.getThreadNum() + "." + arrivalID);
    }

    public void abandonFact(JMeterThread thread, long arrivalID) {
        abandonsCount.incrementAndGet();
        saveLogRecord("ABANDONMENT", thread.getThreadName(), thread.getThreadNum() + "." + arrivalID);
    }

    public boolean canCreateMoreThreads() {
        try {
            long limit = Long.parseLong(getConcurrencyLimit());
            return limit <= 0 || threads.size() < limit;
        } catch (NumberFormatException e) {
            log.debug("Invalid concurrency limit, defaulting to 0");
            return true;
        }
    }

    public void setConcurrencyLimit(String value) {
        setProperty(CONCURRENCY_LIMIT, value);
    }

    public String getConcurrencyLimit() {
        return getPropertyAsString(CONCURRENCY_LIMIT, "");
    }

    public void setArrivalsLimit(String value) {
        setProperty(ARRIVALS_LIMIT, value);
    }

    public String getArrivalsLimit() {
        return getPropertyAsString(ARRIVALS_LIMIT, "0");
    }


    @Override
    public void testEnded(String s) {
        releaseAllPoolThreads();
        super.testEnded(s);
        log.info("Done " + arrivalsCount.longValue() + " arrivals, " + completionsCount.longValue() + " completions, " + abandonsCount.longValue() + " abandonments");
        log.debug("Pool size: " + poolThreads.size());
    }

    public void releaseAllPoolThreads() {
        for (DynamicThread thread : poolThreads) {
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (thread) {
                thread.interrupt();
                thread.interruptOSThread();
                thread.notify();
            }
        }
    }
}