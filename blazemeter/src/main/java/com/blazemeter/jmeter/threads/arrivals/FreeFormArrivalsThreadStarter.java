package com.blazemeter.jmeter.threads.arrivals;


import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jorphan.collections.ListedHashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class FreeFormArrivalsThreadStarter extends ArrivalsThreadStarter {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private final FreeFormArrivalsThreadGroup arrivalsTG;

    public FreeFormArrivalsThreadStarter(int groupIndex, ListenerNotifier listenerNotifier, ListedHashTree listedHashTree, StandardJMeterEngine standardJMeterEngine, FreeFormArrivalsThreadGroup owner) {
        super(groupIndex, listenerNotifier, listedHashTree, standardJMeterEngine, owner);
        this.arrivalsTG = owner;
    }

    @Override
    protected double getCurrentRate() {
        CollectionProperty data = arrivalsTG.getData();
        PropertyIterator it = data.iterator();
        int offset = 0;
        while (it.hasNext()) {
            CollectionProperty record = (CollectionProperty) it.next();
            double chunkLen = record.get(2).getDoubleValue() * arrivalsTG.getUnitFactor();
            double timeProgress = this.rollingTime / 1000.0 - startTime;
            double chunkProgress = (timeProgress - offset) / chunkLen;
            offset += chunkLen;
            if (timeProgress <= offset) {
                double chunkStart = record.get(0).getDoubleValue() / arrivalsTG.getUnitFactor();
                double chunkEnd = record.get(1).getDoubleValue() / arrivalsTG.getUnitFactor();
                double chunkHeight = chunkEnd - chunkStart;
                return chunkStart + chunkProgress * chunkHeight;
            }
        }
        log.info("Got no further schedule, can stop now");
        return -1;
    }
}
