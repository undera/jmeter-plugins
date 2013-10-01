package kg.apc.jmeter.config;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class DistributedTestControl extends ConfigTestElement
        implements TestStateListener {

    public static final String DATA_PROP = "SERVERS";
    public static Logger log = LoggingManager.getLoggerForClass();

    public void testStarted() {
        testStarted(null);
    }

    public void testStarted(String string) {
    }

    public void testEnded() {
        testEnded(null);
    }

    public void testEnded(String string) {
        // TODO: don't run in slave mode, only in master
    }

    public CollectionProperty getData() {
        return (CollectionProperty) getProperty(DATA_PROP);
    }

    public void setData(CollectionProperty data) {
        setProperty(data);
    }
}
