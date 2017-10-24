package com.blazemeter.jmeter.threads.concurrency;

import com.blazemeter.jmeter.threads.AbstractDynamicThreadGroup;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jorphan.collections.ListedHashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class ConcurrencyThreadGroup extends AbstractDynamicThreadGroup {
    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final int MIN_CHECK_TIME = 1000;

    @Override
    protected Thread getThreadStarter(int groupIndex, ListenerNotifier listenerNotifier, ListedHashTree testTree, StandardJMeterEngine engine) {
        return new ConcurrencyThreadStarter(groupIndex, listenerNotifier, testTree, engine, this);
    }

    public synchronized void waitThreadStopped() { // FIXME: method named in confusing way
        if (!threads.isEmpty()) {
            try {
                wait(MIN_CHECK_TIME);
            } catch (InterruptedException e) {
                log.debug("Interrupted", e);
            }
        }
    }

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

    @Override
    public void threadFinished(JMeterThread thread) {
        super.threadFinished(thread);
        saveLogRecord("FINISH", thread.getThreadName(), "");
        synchronized (this) {
            notifyAll();
        }
    }

    public long getConcurrency() {
        return threads.size();
    }

    public boolean tooMuchConcurrency() {
        return threads.size() > getTargetLevelAsDouble();
    }
}
