package kg.apc.jmeter.modifiers;

import org.apache.jmeter.processor.PostProcessor;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class FifoPutPostProcessor extends AbstractTestElement
        implements PostProcessor, TestStateListener {
    private static final Logger log = LoggerFactory.getLogger(FifoPutPostProcessor.class);

    public static final String QUEUE = "FifoName";
    public static final String VALUE = "Value";

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
        try {
            FifoMap.getInstance().put(getQueueName(), getValue());
        } catch (InterruptedException ex) {
            log.warn("Interrupted put into queue " + getQueueName());
        }
    }

    public String getValue() {
        return getPropertyAsString(VALUE);
    }

    public String getQueueName() {
        return getPropertyAsString(QUEUE);
    }

    public void setQueueName(String text) {
        setProperty(QUEUE, text);
    }

    public void setValue(String text) {
        setProperty(VALUE, text);
    }
}
