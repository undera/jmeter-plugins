package kg.apc.jmeter.reporters;

import java.io.Serializable;
import kg.apc.charting.elements.GraphPanelChartAverageElement;
import org.apache.jmeter.engine.StandardJMeterEngine;
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
    private final static String RESPONSE_LATENCY = "avg_response_latency";
    private final static String RESPONSE_LATENCY_SECS = "avg_response_latency_length";
    private long curSec = 0L;
    private GraphPanelChartAverageElement avgRespTime = new GraphPanelChartAverageElement();
    private GraphPanelChartAverageElement avgRespLatency = new GraphPanelChartAverageElement();
    private GraphPanelChartAverageElement errorRate = new GraphPanelChartAverageElement();
    private long respTimeExceededStart = 0;
    private long errRateExceededStart = 0;
    private long respLatencyExceededStart = 0;
    private int stopTries = 0;

    //optimization: not convert String to number for each sample
    private int testValueRespTime = 0;
    private int testValueRespTimeSec = 0;
    private int testValueRespLatency = 0;
    private int testValueRespLatencySec = 0;
    private float testValueError = 0;
    private int testValueErrorSec = 0;

    public AutoStop() {
        super();
    }

    @Override
    public void sampleOccurred(SampleEvent se) {
        long sec = System.currentTimeMillis() / 1000;

        if (curSec != sec) {
            if (testValueRespTime > 0) {
                //log.debug("Avg resp time: "+avgRespTime.getValue());
                if (avgRespTime.getValue() > testValueRespTime) {
                    //log.debug((sec - respTimeExceededStart)+" "+getResponseTimeSecsAsInt());
                    if (sec - respTimeExceededStart >= testValueRespTimeSec) {
                        log.info("Average Response Time is more than " + getResponseTime() + " for " + getResponseTimeSecs() + "s. Auto-shutdown test...");
                        stopTest();
                    }
                } else {
                    respTimeExceededStart = sec;
                }
            }

            if (testValueRespLatency > 0) {
                //log.debug("Avg resp time: "+avgRespTime.getValue());
                if (avgRespLatency.getValue() > testValueRespLatency) {
                    //log.debug((sec - respTimeExceededStart)+" "+getResponseTimeSecsAsInt());
                    if (sec - respLatencyExceededStart >= testValueRespLatencySec) {
                        log.info("Average Latency Time is more than " + getResponseLatency() + " for " + getResponseLatencySecs() + "s. Auto-shutdown test...");
                        stopTest();
                    }
                } else {
                    respLatencyExceededStart = sec;
                }
            }

            if (testValueError > 0) {
                //log.debug("Error rate: "+errorRate.getValue()+"/"+getErrorRateAsFloat());
                if (errorRate.getValue() > testValueError) {
                    //log.debug((sec - errRateExceededStart)+" "+getErrorRateSecsAsInt());
                    if (sec - errRateExceededStart >= testValueErrorSec) {
                        log.info("Error rate more than " + getErrorRate() + " for " + getErrorRateSecs() + "s. Auto-shutdown test...");
                        stopTest();
                    }
                } else {
                    errRateExceededStart = sec;
                }
            }

            curSec = sec;
            avgRespTime = new GraphPanelChartAverageElement();
            avgRespLatency = new GraphPanelChartAverageElement();
            errorRate = new GraphPanelChartAverageElement();
        }

        avgRespTime.add(se.getResult().getTime());
        avgRespLatency.add(se.getResult().getLatency());
        errorRate.add(se.getResult().isSuccessful() ? 0 : 1);
    }

    @Override
    public void sampleStarted(SampleEvent se) {
    }

    @Override
    public void sampleStopped(SampleEvent se) {
    }

    @Override
    public void testStarted() {
        curSec = 0;
        stopTries = 0;

        avgRespTime = new GraphPanelChartAverageElement();
        errorRate = new GraphPanelChartAverageElement();
        avgRespLatency = new GraphPanelChartAverageElement();
        
        errRateExceededStart = 0;
        respTimeExceededStart = 0;
        respLatencyExceededStart = 0;
        
        //init test values
        testValueError = getErrorRateAsFloat();
        testValueErrorSec = getErrorRateSecsAsInt();
        testValueRespLatency = getResponseLatencyAsInt();
        testValueRespLatencySec = getResponseLatencySecsAsInt();
        testValueRespTime = getResponseTimeAsInt();
        testValueRespTimeSec = getResponseTimeSecsAsInt();
    }

    @Override
    public void testStarted(String string) {
        testStarted();
    }

    @Override
    public void testEnded() {
    }

    @Override
    public void testEnded(String string) {
    }

    @Override
    public void testIterationStart(LoopIterationEvent lie) {
    }

    void setResponseTime(String text) {
        setProperty(RESPONSE_TIME, text);
    }

    void setResponseLatency(String text) {
        setProperty(RESPONSE_LATENCY, text);
    }

    void setErrorRate(String text) {
        setProperty(ERROR_RATE, text);
    }

    void setResponseTimeSecs(String text) {
        setProperty(RESPONSE_TIME_SECS, text);
    }

    void setResponseLatencySecs(String text) {
        setProperty(RESPONSE_LATENCY_SECS, text);
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

    String getResponseLatency() {
        return getPropertyAsString(RESPONSE_LATENCY);
    }

    String getResponseLatencySecs() {
        return getPropertyAsString(RESPONSE_LATENCY_SECS);
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
            res = Integer.valueOf(getResponseTime());
        } catch (NumberFormatException e) {
            log.error("Wrong response time: " + getResponseTime(), e);
            setResponseTime("0");
        }
        return res;
    }

    private int getResponseTimeSecsAsInt() {
        int res = 0;
        try {
            res = Integer.valueOf(getResponseTimeSecs());
        } catch (NumberFormatException e) {
            log.error("Wrong response time period: " + getResponseTime(), e);
            setResponseTimeSecs("1");
        }
        return res > 0 ? res : 1;
    }

    private int getResponseLatencyAsInt() {
        int res = 0;
        try {
            res = Integer.valueOf(getResponseLatency());
        } catch (NumberFormatException e) {
            log.error("Wrong response time: " + getResponseLatency(), e);
            setResponseLatency("0");
        }
        return res;
    }

    private int getResponseLatencySecsAsInt() {
        int res = 0;
        try {
            res = Integer.valueOf(getResponseLatencySecs());
        } catch (NumberFormatException e) {
            log.error("Wrong response time period: " + getResponseLatencySecs(), e);
            setResponseLatencySecs("1");
        }
        return res > 0 ? res : 1;
    }

    private float getErrorRateAsFloat() {
        float res = 0;
        try {
            res = Float.valueOf(getErrorRate()) / 100;
        } catch (NumberFormatException e) {
            log.error("Wrong error rate: " + getErrorRate(), e);
            setErrorRate("0");
        }
        return res;
    }

    private int getErrorRateSecsAsInt() {
        int res = 0;
        try {
            res = Integer.valueOf(getErrorRateSecs());
        } catch (NumberFormatException e) {
            log.error("Wrong error rate period: " + getResponseTime(), e);
            setErrorRateSecs("1");
        }
        return res > 0 ? res : 1;
    }

    private void stopTest() {
        stopTries++;
        if (stopTries > 10) {
            log.info("Tries more than 10, stop it NOW!");
            StandardJMeterEngine.stopEngineNow();
        } else if (stopTries > 5) {
            log.info("Tries more than 5, stop it!");
            StandardJMeterEngine.stopEngine();
        } else {
            JMeterContextService.getContext().getEngine().askThreadsToStop();
        }
    }
}
