package com.blazemeter.jmeter.threads.arrivals;

import com.blazemeter.jmeter.threads.AbstractDynamicThreadGroup;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.sampler.TestAction;
import org.apache.jorphan.collections.ListedHashTree;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ArrivalsThreadGroupTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    public static ListedHashTree getListedHashTree(AbstractDynamicThreadGroup atg, boolean stopThread) {
        ListedHashTree tree = new ListedHashTree();
        TestAction pauser = new TestAction();
        if (stopThread) {
            pauser.setAction(TestAction.STOP);
        } else {
            pauser.setAction(TestAction.PAUSE);
        }
        pauser.setTarget(TestAction.THREAD);

        pauser.setDuration(String.valueOf(300));
        tree.add(atg, pauser);
        return tree;
    }

    @Test
    public void testFlow() throws IOException {
        File f = File.createTempFile("arrivalsLog-", ".jtl");
        f.deleteOnExit();
        ArrivalsThreadGroupEmul atg = new ArrivalsThreadGroupEmul();
        atg.setName("TEST");
        atg.setLogFilename(f.getAbsolutePath());
        atg.setTargetLevel("10");
        atg.setRampUp("5");
        atg.setHold("5");
        atg.setConcurrencyLimit("3");
        atg.setArrivalsLimit("20");
        atg.testStarted();
        atg.start(0, null, getListedHashTree(atg, false), null);
        atg.waitThreadsStopped();
        atg.testEnded();
        atg.tellThreadsToStop();
        atg.verifyThreadsStopped();
        assertTrue(2000 < f.length());
        assertEquals(20, atg.getArrivalsDone());
    }

    @Test
    public void testFlowStepping() throws IOException {
        File f = File.createTempFile("arrivalsLog-", ".jtl");
        f.deleteOnExit();
        ArrivalsThreadGroupEmul atg = new ArrivalsThreadGroupEmul();
        atg.setName("TEST");
        atg.setLogFilename(f.getAbsolutePath());
        atg.setTargetLevel("10");
        atg.setSteps("5");
        atg.setRampUp("5");
        atg.setHold("5");
        atg.setConcurrencyLimit("3");
        atg.setArrivalsLimit("20");
        atg.testStarted();
        atg.start(0, null, getListedHashTree(atg, false), null);
        atg.waitThreadsStopped();
        atg.testEnded();
        atg.tellThreadsToStop();
        atg.verifyThreadsStopped();
        assertEquals(20, atg.getArrivalsDone());
        assertTrue(2000 < f.length());
    }

    @Test
    public void testBlankUI() throws IOException {
        ArrivalsThreadGroup atg = new ArrivalsThreadGroupEmul();
        atg.setName("TEST");
        atg.setLogFilename("");
        atg.setConcurrencyLimit("");
        atg.setArrivalsLimit("10");
        atg.testStarted();
        atg.start(0, null, getListedHashTree(atg, false), null);
        atg.waitThreadsStopped();
        atg.testEnded();
        atg.tellThreadsToStop();
        atg.verifyThreadsStopped();
    }

    @Test
    public void testFlow_stopThread() throws IOException {
        ArrivalsThreadGroupEmul atg = new ArrivalsThreadGroupEmul();
        atg.setName("TEST");
        atg.setTargetLevel("2");
        atg.setHold("5");
        atg.testStarted();
        atg.start(0, null, getListedHashTree(atg, true), null);
        atg.waitThreadsStopped();
        atg.testEnded();
        atg.tellThreadsToStop();
        atg.verifyThreadsStopped();
        assertEquals(10, atg.getArrivalsDone());
    }


    @Test
    public void testFlowRefael() throws IOException {
        ArrivalsThreadGroup atg = new ArrivalsThreadGroupEmul();
        atg.setName("TEST");
        atg.setTargetLevel("1");
        atg.setRampUp("1");
        atg.setSteps("5");
        atg.setHold("180");
        atg.setConcurrencyLimit("1000");
        atg.setArrivalsLimit("20");
        atg.testStarted();
        atg.start(0, null, getListedHashTree(atg, true), null);
        atg.waitThreadsStopped();
        atg.testEnded();
        atg.tellThreadsToStop();
        atg.verifyThreadsStopped();
    }
}

