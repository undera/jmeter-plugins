package kg.apc.jmeter.reporters;

import kg.apc.charting.elements.GraphPanelChartAverageElement;
import org.apache.jmeter.JMeter;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.reporters.AbstractListenerElement;
import org.apache.jmeter.samplers.Remoteable;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.SamplingStatCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.functions.AbstractFunction;




import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class AutoStop
        extends AbstractListenerElement
        implements SampleListener, Serializable,
        TestStateListener, Remoteable, NoThreadClone {

    private static final Logger log = LoggerFactory.getLogger(AutoStop.class);
    private final static String RESPONSE_TIME = "avg_response_time";
    private final static String ERROR_RATE = "error_rate";
    private final static String RESPONSE_TIME_SECS = "avg_response_time_length";
    private final static String ERROR_RATE_SECS = "error_rate_length";
    private final static String RESPONSE_LATENCY = "avg_response_latency";
    private final static String RESPONSE_LATENCY_SECS = "avg_response_latency_length";
    private final static String PERCENTILE_RESPONSE_TIME = "percentile_response_time";
    private final static String PERCENTILE_RESPONSE_TIME_SECS ="percentile_response_time_secs";
    private final static String PERCENTILE_VALUE = "percentile_value";
    private final static String CUSTOM_VALIDATION_DURATION = "custom_validation_duration";
    private long curSec = 0L;
    private GraphPanelChartAverageElement avgRespTime = new GraphPanelChartAverageElement();
    private GraphPanelChartAverageElement avgRespLatency = new GraphPanelChartAverageElement();
    private GraphPanelChartAverageElement errorRate = new GraphPanelChartAverageElement();
    SamplingStatCalculator statCalc = new SamplingStatCalculator();
    private long respTimeExceededStart = 0;
    private long errRateExceededStart = 0;
    private long respLatencyExceededStart = 0;
    private long percentileRespTimeExceededStart = 0;
    private long customValidationExceededStart = 0;
    private int stopTries = 0;
    //optimization: not convert String to number for each sample
    private int testValueRespTime = 0;
    private int testValueRespTimeSec = 0;
    private int testValueRespLatency = 0;
    private int testValueRespLatencySec = 0;
    private float testValueError = 0;
    private int testValueErrorSec = 0;
    private int testValuePercentileRespTime = 0;
    private int testValuePercentileRespTimeSec = 0;
    private float testPercentileValue = 0;
    private int percentileResponseTime = 0;
    private String testExpectedValue = "true";
    private String testActualValue = "false";
    private int testValueCustomSec = 180;
    private boolean skipIter = false;


    public AutoStop() {
        super();
    }

    @Override
    public void sampleOccurred(SampleEvent se) {
        SampleResult sr = se.getResult();
        statCalc.addSample(sr);
        long sec = System.currentTimeMillis() / 1000;

        if (curSec != sec) {
            if (testValueRespTime > 0) {
                //log.debug("Avg resp time: "+avgRespTime.getValue());
                if (avgRespTime.getValue() > testValueRespTime) {
                    //log.debug((sec - respTimeExceededStart)+" "+getResponseTimeSecsAsInt());
                    if (sec - respTimeExceededStart >= testValueRespTimeSec) {
                        log.info("Average Response Time is more than " + getResponseTime() + " for " + getResponseTimeSecs() + "s. Auto-shutdown test...");
                        System.out.println("AutoStop - Average Response Time is more than " + getResponseTime() + " for " + getResponseTimeSecs() + "s. Auto-shutdown test...");
                        stopTest();
                    }
                } else {
                    respTimeExceededStart = sec;
                }
            }

            if (testValueRespLatency > 0) {
                //log.debug("Avg resp time: "+avgRespTime.getValue());
                if (avgRespLatency.getValue() > testValueRespLatency) {
                    //log.debug((sec - respTimeExceededStart)+" "+getResponseLatencySecsAsInt());
                    if (sec - respLatencyExceededStart >= testValueRespLatencySec) {
                        log.info("Average Latency Time is more than " + getResponseLatency() + " for " + getResponseLatencySecs() + "s. Auto-shutdown test...");
                        System.out.println("AutoStop - Average Latency Time is more than " + getResponseLatency() + " for " + getResponseLatencySecs() + "s. Auto-shutdown test...");
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
                        System.out.println("AutoStop - Error rate more than " + getErrorRate() + " for " + getErrorRateSecs() + "s. Auto-shutdown test...");
                        stopTest();
                    }
                } else {
                    errRateExceededStart = sec;
                }
            }

            if(testValuePercentileRespTime > 0) {
                log.debug(testPercentileValue+"Percentile Response >"+testValuePercentileRespTime+"until"+testValuePercentileRespTimeSec+"currentValue"+percentileResponseTime);
                log.debug(testActualValue+">>"+testExpectedValue+">>"+skipIter+">>"+testValueCustomSec);
                if(percentileResponseTime > testValuePercentileRespTime) {
                    log.dubeg((sec - percentileRespTimeExceededStart)+" "+getPercentileResponseTimeSecsAsInt());
                    if (sec - percentileRespTimeExceededStart >= testValuePercentileRespTimeSec) {
                        log.info(testPercentileValue+"Percentile Response more than " + getPercentileResponseTime() + " for " + getPercentileResponseTimeSecs() + "s. Auto-shutdown test...");
                        stopTest();
                    }
                } else {
                    percentileRespTimeExceededStart = sec;
                }
            }

            curSec = sec;
            avgRespTime = new GraphPanelChartAverageElement();
            avgRespLatency = new GraphPanelChartAverageElement();
            errorRate = new GraphPanelChartAverageElement();
            percentileResponseTime = statCalc.getPercentPoint(testPercentileValue).intValue();
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
        percentileRespTimeExceededStart = 0;
        customValidationExceededStart = 0;

        //init test values
        testValueError = getErrorRateAsFloat();
        testValueErrorSec = getErrorRateSecsAsInt();
        testValueRespLatency = getResponseLatencyAsInt();
        testValueRespLatencySec = getResponseLatencySecsAsInt();
        testValueRespTime = getResponseTimeAsInt();
        testValueRespTimeSec = getResponseTimeSecsAsInt();
        testValuePercentileRespTime = getPercentileResponseTimeAsInt();
        testValuePercentileRespTimeSec = getPercentileResponseTimeSecsAsInt();
        testPercentileValue = getPercentileValueAsFloat();
        testValueCustomSec = getCustomValidationDurationAsInt();

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

    void setPercentileResponseTime(String text) { setProperty(PERCENTILE_RESPONSE_TIME, text); }

    void setPercentileResponseTimeSecs(String text) { setProperty(PERCENTILE_RESPONSE_TIME_SECS, text); }

    void setPercentileValue(String text) { setProperty(PERCENTILE_VALUE, text); }

    void setCustomValidationDuration(String text) { setProperty(CUSTOM_VALIDATION_DURATION, text); }

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

    String getPercentileResponseTime() { return getPropertyAsString(PERCENTILE_RESPONSE_TIME); }

    String getPercentileResponseTimeSecs() { return getPropertyAsString(PERCENTILE_RESPONSE_TIME_SECS); }

    String getPercentileValue() { return getPropertyAsString(PERCENTILE_VALUE); }

    String getCustomValidationDuration() { return getPropertyAsString(CUSTOM_VALIDATION_DURATION); }

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

    private int getPercentileResponseTimeAsInt() {
        int res = 0;
        try {
            res = Integer.parseInt(getPercentileResponseTime());
        } catch (NumberFormatException e) {
            log.error("Wrong response time: " + getPercentileResponseTime(), e);
            setPercentileResponseTime("0");
        }
        return res;
    }

    private int getPercentileResponseTimeSecsAsInt() {
        int res = 0;
        try {
            res = Integer.parseInt(getPercentileResponseTimeSecs());
        } catch (NumberFormatException e) {
            log.error("Wrong response time period: " + getPercentileResponseTimeSecs(), e);
            setPercentileResponseTimeSecs("1");
        }
        return res > 0 ? res : 1;
    }

    private int getCustomValidationDurationAsInt() {
        int res = 0;
        try {
            res = Integer.parseInt(getCustomValidationDuration());
        } catch (NumberFormatException e) {
            log.error("Wrong time period: " + getCustomValidationDuration(), e);
            setCustomValidationDuration("1");
        }
        return res > 0 ? res : 1;
    }

    private float getPercentileValueAsFloat() {
        float res = 0;
        try {
            res = Float.parseFloat(getPercentileValue()) / 100;
        } catch (NumberFormatException e) {
            log.error("Wrong Percentile Value: " + getPercentileValue(), e);
            setPercentileValue("1");
        }
        return res > 0 ? res : 1;
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

    private int getResponseLatencyAsInt() {
        int res = 0;
        try {
            res = Integer.parseInt(getResponseLatency());
        } catch (NumberFormatException e) {
            log.error("Wrong response time: " + getResponseLatency(), e);
            setResponseLatency("0");
        }
        return res;
    }

    private int getResponseLatencySecsAsInt() {
        int res = 0;
        try {
            res = Integer.parseInt(getResponseLatencySecs());
        } catch (NumberFormatException e) {
            log.error("Wrong response time period: " + getResponseLatencySecs(), e);
            setResponseLatencySecs("1");
        }
        return res > 0 ? res : 1;
    }

    private float getErrorRateAsFloat() {
        float res = 0;
        try {
            res = Float.parseFloat(getErrorRate()) / 100;
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
        stopTries++;
        System.setProperty("auto_stopped", "true");

        if (JMeter.isNonGUI()) {
            log.info("Stopping JMeter via UDP call");
            stopTestViaUDP("StopTestNow");
        } else {
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

    private void stopTestViaUDP(String command) {
        try {
            int port = JMeterUtils.getPropDefault("jmeterengine.nongui.port", JMeter.UDP_PORT_DEFAULT);
            log.info("Sending " + command + " request to port " + port);
            DatagramSocket socket = new DatagramSocket();
            byte[] buf = command.getBytes(StandardCharsets.US_ASCII);
            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
