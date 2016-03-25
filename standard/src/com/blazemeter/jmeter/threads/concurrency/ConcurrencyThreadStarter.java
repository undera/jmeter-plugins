package com.blazemeter.jmeter.threads.concurrency;

import com.blazemeter.jmeter.threads.AbstractThreadStarter;
import com.blazemeter.jmeter.threads.DynamicThread;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.ListedHashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class ConcurrencyThreadStarter extends AbstractThreadStarter {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private final ConcurrencyThreadGroup concurrTG;

    public ConcurrencyThreadStarter(int groupIndex, ListenerNotifier listenerNotifier, ListedHashTree testTree, StandardJMeterEngine engine, ConcurrencyThreadGroup concurrencyThreadGroup) {
        super(groupIndex, concurrencyThreadGroup, testTree, listenerNotifier, engine);
        concurrTG = concurrencyThreadGroup;
    }

    @Override
    protected void supplyActiveThreads() throws InterruptedException {
        log.info("Start supplying threads");
        startTime = System.currentTimeMillis();
        while (!owner.isLimitReached() && getPlannedConcurrency() >= 0) {
            log.debug("Concurrency factual/expected: " + concurrTG.getConcurrency() + "/" + getPlannedConcurrency());
            while (concurrTG.getConcurrency() < getPlannedConcurrency()) {
                DynamicThread thread = addActiveThread();
                concurrTG.threadStarted(thread);
            }
            concurrTG.waitThreadStopped();
        }
        log.info("Done supplying threads");
    }

    private long getPlannedConcurrency() {
        long rampUp = owner.getRampUpSeconds();
        long hold = owner.getHoldSeconds();
        long steps = owner.getStepsAsLong();
        double maxConcurr = owner.getTargetLevelAsDouble();
        double timeOffset = (System.currentTimeMillis() - startTime) / 1000.0;
        log.debug("Time progress: " + timeOffset + "/" + (rampUp + hold));

        long shift = JMeterUtils.getPropDefault("dynamic_tg.shift_rampup_start", 0L);
        timeOffset -= shift;
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
}
