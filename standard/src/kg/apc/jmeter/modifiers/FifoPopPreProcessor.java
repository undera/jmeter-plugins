package kg.apc.jmeter.modifiers;

import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class FifoPopPreProcessor extends AbstractTestElement
        implements PreProcessor, TestStateListener {

    private static final Logger log = LoggingManager.getLoggerForClass();

    public static final String queueName = "FifoName";
    public static final String variableName = "Variable";
    public static final String TIMEOUT = "Timeout";

    public FifoPopPreProcessor() {
        setTimeout(Long.toString(JMeterUtils.getPropDefault(FifoMap.TIMEOUT_PROP, Long.MAX_VALUE)));
    }

    public void testStarted() {
        FifoMap.getInstance().clear();
    }

    public void testStarted(String host) {
        testStarted();
    }

    public void testEnded() {
        FifoMap.getInstance().clear();
    }

    public void testEnded(String host) {
        testEnded();
    }

    public void process() {
        String value = null;
        try {
            Object valueObj = FifoMap.getInstance().pop(getQueueName(), getTimeoutAsLong());
            if (valueObj != null) {
                value = valueObj.toString();
            }
        } catch (InterruptedException ex) {
            log.warn("Interrupted pop from queue " + getQueueName());
            value = "INTERRUPTED";
        }
        final JMeterVariables vars = JMeterContextService.getContext().getVariables();
        if (vars != null) {
            vars.put(getVarName(), value);
        }
    }

    public String getVarName() {
        return getPropertyAsString(variableName);
    }

    private long getTimeoutAsLong() {
        String timeout = getTimeout();
        if (timeout.isEmpty()) {
            return Long.MAX_VALUE;
        } else {
            return Long.parseLong(timeout);
        }
    }

    public String getTimeout() {
        return getPropertyAsString(TIMEOUT);
    }

    public String getQueueName() {
        return getPropertyAsString(queueName);
    }

    public final void setTimeout(String atimeout) {
        setProperty(TIMEOUT, atimeout);
    }

    public void setVarName(String text) {
        setProperty(variableName, text);
    }

    public void setQueueName(String text) {
        setProperty(queueName, text);
    }
}
