package kg.apc.jmeter.threads;

import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.threads.JMeterThread;

import java.util.Map;

public class AbstractSimpleThreadGroupImpl extends AbstractSimpleThreadGroup {

    public void scheduleThread(JMeterThread thread, long now) {
        // just dummy impl
    }

    public Map<JMeterThread, Thread> getAllThreads() {
        return this.allThreads;
    }

    @Override
    public JMeterThread addNewThread(int i, StandardJMeterEngine standardJMeterEngine) {
        return null;
    }
}
