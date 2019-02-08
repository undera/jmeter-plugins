package com.blazemeter.jmeter.threads.concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.ListedHashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import com.blazemeter.jmeter.threads.AbstractDynamicThreadGroup;

public class ConcurrencyThreadGroup extends AbstractDynamicThreadGroup {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final long DEFAULT_TEMPORISATION = JMeterUtils.getPropDefault("dynamic_tg.temporisation", 10L);

    public static final long MIN_CHECK_TIME = 1000L;
    private final transient Lock lock = new ReentrantLock();
    private final transient Condition condition = lock.newCondition(); 
    
    @Override
    protected Thread getThreadStarter(int groupIndex, ListenerNotifier listenerNotifier, ListedHashTree testTree, StandardJMeterEngine engine) {
        return new ConcurrencyThreadStarter(groupIndex, listenerNotifier, testTree, engine, this);
    }

    /**
     * If threads is empty we wait on condition for 1s max to be notified,
     *  otherwise 
     */
    public void waitThreadStopped() { // FIXME: method named in confusing way
        long sleepTime = threads.isEmpty() ? DEFAULT_TEMPORISATION : MIN_CHECK_TIME;
        lock.lock();
        try {
            condition.await(sleepTime, TimeUnit.MILLISECONDS); 
        } catch (InterruptedException e) {
            log.debug("Interrupted", e);
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getNumThreads() {
        return (int) Math.round(getTargetLevelAsDouble());
    }

    @Override
    public boolean isLimitReached() {
        return !running || !threadStarter.isAlive();
    }

    public void threadStarted(JMeterThread thread) {
        saveLogRecord("START", thread.getThreadName(), "");
    }

    public ConcurrencyThreadGroup() {
        super();
        
    }

    @Override
    public void threadFinished(JMeterThread thread) {
        super.threadFinished(thread);
        saveLogRecord("FINISH", thread.getThreadName(), "");
        lock.lock();
        try {
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public long getConcurrency() {
        return threads.size();
    }

    public boolean tooMuchConcurrency() {
        return threads.size() > getTargetLevelAsDouble();
    }
}
