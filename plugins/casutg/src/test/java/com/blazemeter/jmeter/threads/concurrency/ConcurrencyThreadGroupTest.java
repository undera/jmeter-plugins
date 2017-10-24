package com.blazemeter.jmeter.threads.concurrency;

import com.blazemeter.jmeter.threads.arrivals.ArrivalsThreadGroupTest;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.sampler.DebugSampler;
import org.apache.jmeter.sampler.TestAction;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.*;
import org.apache.jorphan.collections.ListedHashTree;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ConcurrencyThreadGroupTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
        ArrivalsThreadGroupTest.setUpClass();
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testFlow() throws IOException {
        File f = File.createTempFile("arrivalsLog-", ".jtl");
        f.deleteOnExit();
        ConcurrencyThreadGroup ctg = new ConcurrencyThreadGroup();
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

    @Test
    public void testStartNextLoop() throws Exception {
        JMeterContextService.getContext().setVariables(new JMeterVariables());

        TestSampleListener listener = new TestSampleListener();

        DebugSampler beforeSampler = new DebugSamplerExt();
        beforeSampler.setName("Before Test Action sampler");

        TestAction testAction = new TestAction();
        testAction.setAction(TestAction.RESTART_NEXT_LOOP);

        DebugSampler afterSampler = new DebugSamplerExt();
        afterSampler.setName("After Test Action sampler");


        ConcurrencyThreadGroup ctg = new ConcurrencyThreadGroup();
        ctg.setProperty(new StringProperty(AbstractThreadGroup.ON_SAMPLE_ERROR, AbstractThreadGroup.ON_SAMPLE_ERROR_CONTINUE));
        ctg.setRampUp("0");
        ctg.setTargetLevel("1");
        ctg.setSteps("0");
        ctg.setHold("5"); // TODO: increase this value for debugging
        ctg.setIterationsLimit("10");
        ctg.setUnit("S");

        ListedHashTree hashTree = new ListedHashTree();
        hashTree.add(ctg);
        hashTree.add(ctg, beforeSampler);
        hashTree.add(ctg, testAction);
        hashTree.add(ctg, afterSampler);
        hashTree.add(ctg, listener);

        TestCompiler compiler = new TestCompiler(hashTree);
        hashTree.traverse(compiler);

        ListenerNotifier notifier = new ListenerNotifier();

        ctg.start(1, notifier, hashTree, new StandardJMeterEngine());

        ctg.waitThreadsStopped();
    }

    public static class DebugSamplerExt extends DebugSampler {
        @Override
        public SampleResult sample(Entry e) {
            System.out.println(this.getName());
            SampleResult result = super.sample(e);
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                throw new AssertionError(ex);
            }
            return result;
        }
    }

    public class TestSampleListener extends ResultCollector implements SampleListener {
        public List<SampleEvent> events = new ArrayList<>();

        @Override
        public void sampleOccurred(SampleEvent e) {
            events.add(e);
        }

        @Override
        public void sampleStarted(SampleEvent e) {
            events.add(e);
        }

        @Override
        public void sampleStopped(SampleEvent e) {
            events.add(e);
        }
    }
}