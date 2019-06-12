package kg.apc.jmeter.threads;

import org.apache.jmeter.threads.JMeterThread;

import java.util.Map;

public class AbstractSimpleThreadGroupImpl extends AbstractSimpleThreadGroup {
    public AbstractSimpleThreadGroupImpl() {
        super();
    }

    public void scheduleThread(JMeterThread thread, long now) {
    }

    public Map<JMeterThread, Thread> getAllThreads() {
        return this.allThreads;
    }
}
