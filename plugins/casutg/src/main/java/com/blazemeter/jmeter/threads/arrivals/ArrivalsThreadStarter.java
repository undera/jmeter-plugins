package com.blazemeter.jmeter.threads.arrivals;

import com.blazemeter.jmeter.threads.AbstractThreadStarter;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jorphan.collections.ListedHashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class ArrivalsThreadStarter extends AbstractThreadStarter {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private ArrivalsThreadGroup arrivalsTG;
    protected long scheduledCount = 0;
    protected double rollingTime = 0;

    public ArrivalsThreadStarter(int groupIndex, ListenerNotifier listenerNotifier, ListedHashTree listedHashTree, StandardJMeterEngine standardJMeterEngine, ArrivalsThreadGroup owner) {
        super(groupIndex, owner, listedHashTree, listenerNotifier, standardJMeterEngine);
        arrivalsTG = owner;
    }

    protected void supplyActiveThreads() throws InterruptedException {
        while (needMoreArrivals()) {
            if (!arrivalsTG.releasedPoolThread()) {
                if (arrivalsTG.canCreateMoreThreads()) {
                    addActiveThread();
                } else {
                    log.debug("Not creating thread because of concurrency limit");
                }
            }
        }
    }

    public synchronized boolean needMoreArrivals() throws InterruptedException {
        if (rollingTime > 0) { // a case for very first thread
            while (rollingTime >= System.currentTimeMillis()) {
                long maxWait = (long) (rollingTime - System.currentTimeMillis());
                if (maxWait > 0) {
                    log.debug("Waiting " + maxWait);
                    wait(maxWait);
                }
            }
        } else {
            rollingTime = System.currentTimeMillis();
            startTime = rollingTime / 1000.0;
        }

        double currentRate;
        do {
            currentRate = getCurrentRate();
            if (currentRate == 0) {
                log.debug("Zero arrivals rate, waiting a bit");
                rollingTime += 200; // FIXME: magic constant
                Thread.sleep(200);
            }
        } while (currentRate == 0);

        if (currentRate < 0) {
            log.info("Duration limit reached, no more arrivals needed, had arrivals: " + scheduledCount);
            ((ArrivalsThreadGroup) owner).setArrivalsLimit(String.valueOf(scheduledCount));
            return false;
        }
        tickRollingTime(currentRate);
        return !owner.isLimitReached();
    }


    // ported from Taurus PBench module
    protected double getCurrentRate() {
        long rampUp = owner.getRampUpSeconds();
        long hold = owner.getHoldSeconds();
        long steps = owner.getStepsAsLong();
        double throughput = owner.getTargetLevelFactored();
        double timeOffset = rollingTime / 1000.0 - startTime;

        if (timeOffset >= rampUp + hold) {
            return -1; // means no more requests
        }

        if (rampUp == 0 || timeOffset > rampUp) {
            return throughput;
        } else if (steps > 0) {
            double stepSize = throughput / (double) steps;
            double stepLen = rampUp / (double) steps;
            return stepSize * (Math.floor(timeOffset / stepLen) + 1);
        } else {
            double slope = throughput / rampUp;
            return slope * Math.sqrt(2 * scheduledCount / slope);
        }
    }

    protected void tickRollingTime(double currentRate) {
        if (currentRate > 0) {
            double delay = currentRate > 0 ? (1000.0 / currentRate) : 0;
            rollingTime += delay;
            scheduledCount++;
        } else {
            log.debug("Negative arrivals rate, ignoring");
        }
    }
}
