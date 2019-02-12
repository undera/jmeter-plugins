package com.blazemeter.jmeter.threads.concurrency;

import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.ListedHashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import com.blazemeter.jmeter.threads.AbstractThreadStarter;
import com.blazemeter.jmeter.threads.DynamicThread;

public class ConcurrencyThreadStarter extends AbstractThreadStarter {
    private static final Logger log = LoggingManager.getLoggerForClass();
    static final long CACHING_VALIDITY_MS = JMeterUtils.getPropDefault("dynamic_tg.properties_caching_validity", 20L);

    private final ConcurrencyThreadGroup concurrTG;
    // We cache values for performance
    private long rampUp;
    private long hold;
    private long steps;
    private double maxConcurr;
    private long lastCachedTime;
    private long defaultShiftRampup;

    public ConcurrencyThreadStarter(int groupIndex, ListenerNotifier listenerNotifier, ListedHashTree testTree, StandardJMeterEngine engine, ConcurrencyThreadGroup concurrencyThreadGroup) {
        super(groupIndex, concurrencyThreadGroup, testTree, listenerNotifier, engine);
        concurrTG = concurrencyThreadGroup;
        // We cache values
        this.rampUp = owner.getRampUpSeconds();
        this.hold = owner.getHoldSeconds();
        this.steps = owner.getStepsAsLong();
        this.maxConcurr = owner.getTargetLevelAsDouble();
        this.defaultShiftRampup = JMeterUtils.getPropDefault("dynamic_tg.shift_rampup_start", 0L);
        this.lastCachedTime = System.currentTimeMillis();
    }

    @Override
    protected void supplyActiveThreads() throws InterruptedException {
        log.info("Start supplying threads");
        startTime = System.currentTimeMillis();
        boolean isDebugEnabled = log.isDebugEnabled();
        while (!owner.isLimitReached() && getPlannedConcurrency(isDebugEnabled) >= 0) {
            if(isDebugEnabled) {
                log.debug("Concurrency factual/expected: " + concurrTG.getConcurrency() + "/" + getPlannedConcurrency(isDebugEnabled));
            }
            while (concurrTG.getConcurrency() < getPlannedConcurrency(isDebugEnabled)) {
                DynamicThread thread = addActiveThread();
                concurrTG.threadStarted(thread);
            }
            concurrTG.waitThreadStopped();
        }
        log.info("Done supplying threads");
    }

    private long getPlannedConcurrency(boolean isDebugEnabled) {
        long now = System.currentTimeMillis();
        checkNeedsPropertiesReloading(now);
        double timeOffset = (now - startTime) / 1000.0;
        if(isDebugEnabled) {
            log.debug("Time progress: " + timeOffset + "/" + (rampUp + hold));
        }

        timeOffset -= defaultShiftRampup;
        if (timeOffset < 0) {
            timeOffset = 0;
        }

        if (timeOffset >= rampUp + hold) {
            return -1; // means no more concurrency needed
        }

        if (rampUp == 0 || timeOffset > rampUp) {
            return Math.round(maxConcurr);
        } else if (steps > 0) {
            double stepSize = maxConcurr / (double) steps;
            double stepLen = rampUp / (double) steps;
            return Math.round(stepSize * (Math.floor(timeOffset / stepLen) + 1));
        } else {
            double slope = maxConcurr / rampUp;
            return Math.round(slope * timeOffset);
        }
    }

    /**
     * Check if we need to reload properties
     * @param now Now as Millis
     */
    void checkNeedsPropertiesReloading(long now) {
        if (CACHING_VALIDITY_MS > 0 && now - lastCachedTime > CACHING_VALIDITY_MS) {
            this.rampUp = owner.getRampUpSeconds();
            this.hold = owner.getHoldSeconds();
            this.steps = owner.getStepsAsLong();
            this.maxConcurr =owner.getTargetLevelAsDouble();
            this.defaultShiftRampup = JMeterUtils.getPropDefault("dynamic_tg.shift_rampup_start", 0L);
            this.lastCachedTime = System.currentTimeMillis();            
        }
    }

    /**
     * @return the lastCachedTime
     */
    long getLastCachedTime() {
        return lastCachedTime;
    }

}
