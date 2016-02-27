package com.blazemeter.jmeter.threads;

import com.blazemeter.jmeter.reporters.FlushingResultCollector;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.AbstractThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// reason to have this class as separate is will to keep Model responsibility separate
public abstract class AbstractDynamicThreadGroupModel extends AbstractThreadGroup implements TestStateListener {
    private static final Logger log = LoggingManager.getLoggerForClass();
    protected static final long WAIT_TO_DIE = JMeterUtils.getPropDefault("jmeterengine.threadstop.wait", 5 * 1000);
    public static final String LOG_FILENAME = "LogFilename";
    public static final String TARGET_LEVEL = "TargetLevel";
    public static final String RAMP_UP = "RampUp";
    public static final String STEPS = "Steps";
    public static final String ITERATIONS = "Iterations";
    public static final String HOLD = "Hold";
    protected final Set<DynamicThread> threads = Collections.newSetFromMap(new ConcurrentHashMap<DynamicThread, Boolean>());
    protected final ResultCollector logFile = new FlushingResultCollector();
    protected volatile boolean running = false;

    public void setLogFilename(String value) {
        setProperty(LOG_FILENAME, value);
    }

    public String getLogFilename() {
        return getPropertyAsString(LOG_FILENAME);
    }

    protected void saveLogRecord(String marker, String threadName, String arrivalID) {
        SampleResult res = new SampleResult();
        res.sampleStart();
        res.setSampleLabel(arrivalID);
        res.setResponseMessage(marker);
        res.setThreadName(threadName);
        res.sampleEnd();
        SampleEvent evt = new SampleEvent(res, getName());
        logFile.sampleOccurred(evt);
    }

    @Override
    public void testStarted() {
        testStarted("");
    }

    @Override
    public void testStarted(String s) {
        logFile.setFilename(getLogFilename());
        logFile.testStarted(s);
    }

    @Override
    public void testEnded() {
        testEnded("");
    }

    @Override
    public void testEnded(String s) {
        logFile.testEnded(s);
    }

    @Override
    public int numberOfActiveThreads() {
        return threads.size();
    }

    @Override
    public int getNumberOfThreads() {
        return threads.size();
    }

    public void addThread(DynamicThread threadWorker) {
        threads.add(threadWorker);
    }

    public void setTargetLevel(String value) {
        setProperty(TARGET_LEVEL, value.trim());
    }

    public String getTargetLevel() {
        return getPropertyAsString(TARGET_LEVEL, "1");
    }

    public int getTargetLevelAsInt() {
        return getPropertyAsInt(TARGET_LEVEL, 1);
    }

    public void setRampUp(String value) {
        setProperty(RAMP_UP, value.trim());
    }

    public String getRampUp() {
        return getPropertyAsString(RAMP_UP, "");
    }

    public long getRampUpSeconds() {
        String val = getRampUp();
        if (val.isEmpty()) {
            return 0;
        } else {
            return Math.round(Double.parseDouble(val) * getUnitFactor());
        }
    }

    public double getUnitFactor() {
        return 1;
    }

    public void setSteps(String value) {
        setProperty(STEPS, value.trim());
    }

    public String getSteps() {
        return getPropertyAsString(STEPS, "");
    }

    public long getStepsAsLong() {
        String val = getSteps();
        if (val.isEmpty()) {
            return 0;
        } else {
            return Long.parseLong(val);
        }
    }

    public void setHold(String value) {
        setProperty(HOLD, value.trim());
    }

    public String getHold() {
        return getPropertyAsString(HOLD, "1");
    }

    public long getHoldSeconds() {
        String val = getHold();
        if (val.isEmpty()) {
            return 0;
        } else {
            return Math.round(Double.parseDouble(val) * getUnitFactor());
        }
    }

    public double getTargetLevelAsDouble() {
        return getTargetLevelAsInt();
    }

    public double getTargetLevelFactored() {
        return getTargetLevelAsDouble() / getUnitFactor();
    }

    public long getIterationsLimitAsLong() {
        String val = getIterationsLimit();
        if (val.isEmpty()) {
            return 0;
        } else {
            return Long.parseLong(val);
        }
    }

    public String getIterationsLimit() {
        return getPropertyAsString(ITERATIONS);
    }

    public void setIterationsLimit(String val) {
        setProperty(ITERATIONS, val);
    }
}
