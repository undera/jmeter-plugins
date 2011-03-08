package kg.apc.jmeter.reporters;

import java.io.Serializable;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.reporters.AbstractListenerElement;
import org.apache.jmeter.samplers.Remoteable;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class AutoStop
        extends AbstractListenerElement
        implements SampleListener, Serializable,
        TestListener, Remoteable, NoThreadClone {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private final static String RESPONSE_TIME = "avg_response_time";
    private final static String ERROR_RATE = "error_rate";
    private final static String RESPONSE_TIME_SECS = "avg_response_time_length";
    private final static String ERROR_RATE_SECS = "error_rate_length";

    public AutoStop() {
        super();
        log.info("Create");
    }

    public void sampleOccurred(SampleEvent se) {
    }

    public void sampleStarted(SampleEvent se) {
    }

    public void sampleStopped(SampleEvent se) {
    }

    public void testStarted() {
    }

    public void testStarted(String string) {
    }

    public void testEnded() {
    }

    public void testEnded(String string) {
    }

    public void testIterationStart(LoopIterationEvent lie) {
    }

    void setResponseTime(String text) {
        setProperty(RESPONSE_TIME, text);
    }

    void setErrorRate(String text) {
        setProperty(ERROR_RATE, text);
    }

    void setResponseTimeSecs(String text) {
        setProperty(RESPONSE_TIME_SECS, text);
    }

    void setErrorRateSecs(String text) {
        setProperty(ERROR_RATE_SECS, text);
    }

    String getResponseTime() {
        return getPropertyAsString(RESPONSE_TIME);
    }

    String getResponseTimeSecs() {
        return getPropertyAsString(RESPONSE_TIME_SECS);
    }

    String getErrorRate() {
        return getPropertyAsString(ERROR_RATE);
    }

    String getErrorRateSecs() {
        return getPropertyAsString(ERROR_RATE_SECS);
    }
}
