package com.blazemeter.jmeter.threads.concurrency;

import com.blazemeter.jmeter.threads.arrivals.ArrivalsThreadGroupTest;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.ListedHashTree;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConcurrencyThreadStarterTest {
    @BeforeClass
    public static void setUpClass() {
        TestJMeterUtils.createJmeterEnv();
        // Workaround for new JMeters
        JMeterUtils.setProperty("saveservice_properties", JMeterUtils.getJMeterHome() + "/ss.props");
        JMeterUtils.setProperty("upgrade_properties", JMeterUtils.getJMeterHome() + "/ss.props");
    }

    @Test(timeout = 1000)
    public void testZeroThreads() {
        ConcurrencyThreadGroupExt ctg = new ConcurrencyThreadGroupExt();
        ctg.setTargetLevel("0");
        ctg.setHold("400000");
        ListedHashTree tree = ArrivalsThreadGroupTest.getListedHashTree(ctg, false);
        ConcurrencyThreadStarter starter = new ConcurrencyThreadStarter(0, null, tree, null, ctg);
        starter.supplyActiveThreads();
    }

    public static class ConcurrencyThreadGroupExt extends ConcurrencyThreadGroup {
        @Override
        public boolean isLimitReached() {
            return false;
        }
    }
}