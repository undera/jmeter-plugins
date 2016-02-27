package com.blazemeter.jmeter.threads.concurrency;

import com.blazemeter.jmeter.control.VirtualUserController;
import com.blazemeter.jmeter.threads.arrivals.ArrivalsThreadGroupTest;
import org.apache.jmeter.sampler.DebugSampler;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class ConcurrencyThreadGroupTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
        ArrivalsThreadGroupTest.setUpClass();
    }

    @Test
    public void testFlow() throws IOException {
        File f = File.createTempFile("arrivalsLog-", ".jtl");
        f.deleteOnExit();
        ConcurrencyThreadGroup ctg = new ConcurrencyThreadGroup();
        VirtualUserController vuc = new VirtualUserController();
        ctg.addTestElement(new DebugSampler());
        ctg.setName("TEST");
        ctg.setLogFilename(f.getAbsolutePath());
        ctg.setTargetLevel("10");
        ctg.setRampUp("5");
        ctg.setHold("5");
        //ctg.setArrivalsLimit("20");
        ctg.testStarted();
        ctg.start(0, null, ArrivalsThreadGroupTest.getListedHashTree(ctg, false), null);
        ctg.waitThreadsStopped();
        ctg.testEnded();
        ctg.tellThreadsToStop();
        ctg.verifyThreadsStopped();
        assertTrue("length less than expected: " + f.length(), f.length() > 800);
    }
}