package com.blazemeter.jmeter.threads.arrivals;

import kg.apc.emulators.EmulatorJmeterEngine;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.ListedHashTree;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


public class FreeFormArrivalsThreadStarterTest {
    private static final Logger log = LoggerFactory.getLogger(FreeFormArrivalsThreadStarterTest.class);

    @BeforeClass
    public static void setUp() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }


    @Test
    public void testCustomProperties() {
        CollectionProperty sched = new CollectionProperty();
        sched.addProperty(getRow(1, 10, 30));
        sched.addProperty(getRow(10, 10, 5));
        sched.addProperty(getRow(10, 1, 15));
        JMeterUtils.getJMeterProperties().put("arrivals.schedule.1", "spawn(1,10,30s) spawn(10,10,5s) spawn(10,1,15s)");
        FreeFormArrivalsThreadGroup atg = new FreeFormArrivalsThreadGroup();
        atg.setUseIdentifier(true);
        atg.setIdentifier("arrivals.schedule.1");
        atg.setProperty(sched);
        FreeFormArrivalsThreadStarterEmul obj = new FreeFormArrivalsThreadStarterEmul(atg);
        List<Double> resultList = new ArrayList<>();


        for (int n = 0; n < 60; n++) {
            resultList.add(obj.getCurrentRate());
            log.error("Rate " + n + ": " + obj.getCurrentRate());
            obj.addRollingTime(1000);
        }

    }

    @Test
    public void testSchedule() throws Exception {
        CollectionProperty sched = new CollectionProperty(FreeFormArrivalsThreadGroup.SCHEDULE, new LinkedHashSet<>());
        sched.addProperty(getRow(1, 10, 30));
        sched.addProperty(getRow(0, 0, 5));
        sched.addProperty(getRow(10, 1, 15));

        FreeFormArrivalsThreadGroup atg = new FreeFormArrivalsThreadGroup();
        atg.setProperty(sched);
        FreeFormArrivalsThreadStarterEmul obj = new FreeFormArrivalsThreadStarterEmul(atg);
        for (int n = 0; n < 60; n++) {
            log.info("Rate " + n + ": " + obj.getCurrentRate());
            obj.addRollingTime(1000);
        }
    }

    private CollectionProperty getRow(int i, int i1, int i2) {
        CollectionProperty row = new CollectionProperty();
        row.addProperty(new StringProperty("", String.valueOf(i)));
        row.addProperty(new StringProperty("", String.valueOf(i1)));
        row.addProperty(new StringProperty("", String.valueOf(i2)));
        return row;
    }

    private class FreeFormArrivalsThreadStarterEmul extends FreeFormArrivalsThreadStarter {
        public FreeFormArrivalsThreadStarterEmul(FreeFormArrivalsThreadGroup atg) {
            super(0, new ListenerNotifier(), new ListedHashTree(), new EmulatorJmeterEngine(), atg);
            startTime = System.currentTimeMillis() / 1000;
            rollingTime = System.currentTimeMillis();
        }

        public void addRollingTime(int i) {
            rollingTime += i;
        }
    }
}