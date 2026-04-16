package com.adaptive.jmeter.plugins;

import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.LongProperty;
import org.apache.jmeter.timers.Timer;

/**
 * Adaptive Throughput Timer - Adjusts delay based on current throughput
 */
public class AdaptiveTimer extends AbstractTestElement implements Timer {

    private static final long serialVersionUID = 1L;

    public static final String TARGET_THROUGHPUT = "targetThroughput";
    public static final String MIN_DELAY = "minDelay";
    public static final String MAX_DELAY = "maxDelay";

    @Override
    public long delay() {
        // Calculate adaptive delay based on throughput
        long minDelay = getMinDelay();
        long maxDelay = getMaxDelay();
        
        // Default implementation - returns minimum delay
        return minDelay;
    }

    public void setTargetThroughput(long throughput) {
        setProperty(new LongProperty(TARGET_THROUGHPUT, throughput));
    }

    public long getTargetThroughput() {
        return getPropertyAsLong(TARGET_THROUGHPUT);
    }

    public void setMinDelay(long delay) {
        setProperty(new LongProperty(MIN_DELAY, delay));
    }

    public long getMinDelay() {
        return getPropertyAsLong(MIN_DELAY);
    }

    public void setMaxDelay(long delay) {
        setProperty(new LongProperty(MAX_DELAY, delay));
    }

    public long getMaxDelay() {
        return getPropertyAsLong(MAX_DELAY);
    }
}
