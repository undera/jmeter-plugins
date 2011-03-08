package kg.apc.jmeter.reporters;

import java.io.Serializable;
import kg.apc.jmeter.charting.GraphPanelChartAverageElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.reporters.AbstractListenerElement;
import org.apache.jmeter.samplers.Remoteable;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.threads.JMeterContextService;
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
    private long curSec = 0L;
    private GraphPanelChartAverageElement avgRespTime = new GraphPanelChartAverageElement();
    private GraphPanelChartAverageElement errorRate = new GraphPanelChartAverageElement();
    private long respTimeExceededStart = 0;
    private long errRateExceededStart=0;

    public AutoStop() {
        super();
    }

    public void sampleOccurred(SampleEvent se) {
        long sec = System.currentTimeMillis() / 1000;

        if (curSec != sec) {
            if (getResponseTimeAsInt() > 0) {
                //log.debug("Avg resp time: "+avgRespTime.getValue());
                if (avgRespTime.getValue() > getResponseTimeAsInt()) {
                    //log.debug((sec - respTimeExceededStart)+" "+getResponseTimeSecsAsInt());
                    if (sec - respTimeExceededStart >= getResponseTimeSecsAsInt()) {
                        log.info("Average latency more than " + getResponseTime() + " for " + getResponseTimeSecs() + "s. Auto-shutdown test...");
                        stopTest();
                    }
                } else {
                    respTimeExceededStart = sec;
                }
            }

            if (getErrorRateAsFloat() > 0) {
                //log.debug("Error rate: "+errorRate.getValue());
                if (errorRate.getValue() > getErrorRateAsFloat()) {
                    //log.debug((sec - errRateExceededStart)+" "+getErrorRateSecsAsInt());
                    if (sec - errRateExceededStart >= getErrorRateSecsAsInt()) {
                        log.info("Error rate more than " + getErrorRate() + " for " + getErrorRateSecs() + "s. Auto-shutdown test...");
                        stopTest();
                    }
                } else {
                    errRateExceededStart = sec;
                }
            }

            curSec = sec;
            avgRespTime = new GraphPanelChartAverageElement();
            errorRate = new GraphPanelChartAverageElement();
        }

        avgRespTime.add(se.getResult().getLatency());
        errorRate.add(se.getResult().isSuccessful()?0:1);
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

    private int getResponseTimeAsInt() {
        int res = 0;
        try {
            res = Integer.parseInt(getResponseTime());
        } catch (NumberFormatException e) {
            log.error("Wrong response time: " + getResponseTime(), e);
            setResponseTime("0");
        }
        return res;
    }

    private int getResponseTimeSecsAsInt() {
        int res = 0;
        try {
            res = Integer.parseInt(getResponseTimeSecs());
        } catch (NumberFormatException e) {
            log.error("Wrong response time period: " + getResponseTime(), e);
            setResponseTimeSecs("1");
        }
        return res > 0 ? res : 1;
    }

    private float getErrorRateAsFloat() {
        float res = 0;
        try {
            res = Float.parseFloat(getErrorRate())/100;
        } catch (NumberFormatException e) {
            log.error("Wrong error rate: " + getErrorRate(), e);
            setErrorRate("0");
        }
        return res;
    }

    private int getErrorRateSecsAsInt() {
        int res = 0;
        try {
            res = Integer.parseInt(getErrorRateSecs());
        } catch (NumberFormatException e) {
            log.error("Wrong error rate period: " + getResponseTime(), e);
            setErrorRateSecs("1");
        }
        return res > 0 ? res : 1;
    }

    private void stopTest() {
        // TODO: provide option for stop/stop now
        JMeterContextService.getContext().getEngine().askThreadsToStop();
    }
}
