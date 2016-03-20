package com.blazemeter.jmeter.threads;

import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.JMeterThreadMonitor;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jorphan.collections.HashTree;

public class DynamicThread extends JMeterThread {
    private Thread osThread;
    private boolean stopping = false;

    public DynamicThread(HashTree test, JMeterThreadMonitor monitor, ListenerNotifier note) {
        super(test, monitor, note);
    }

    public void setOSThread(Thread OSThread) {
        this.osThread = OSThread;
    }

    public Thread getOSThread() {
        return osThread;
    }

    @Override
    public String toString() {
        return getThreadName();
    }

    @Override
    public void stop() {
        stopping = true;
        super.stop();
    }

    public void interruptOSThread() {
        if (osThread != null) {
            osThread.interrupt();
        }
    }

    public boolean isStopping() {
        return stopping;
    }
}
