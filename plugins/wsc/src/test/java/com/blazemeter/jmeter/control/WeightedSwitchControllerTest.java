package com.blazemeter.jmeter.control;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.sampler.DebugSampler;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jmeter.threads.TestCompiler;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.ListedHashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WeightedSwitchControllerTest {

    private static final Logger log = LoggingManager.getLoggerForClass();

    @Test
    public void testGeneric() {
        WeightedSwitchController obj = new WeightedSwitchController();
        obj.addTestElement(getSampler("0"));
        obj.addTestElement(getSampler("1"));
        obj.addTestElement(getSampler("2"));
        obj.addTestElement(getSampler("5"));
        PowerTableModel mdl = new PowerTableModel(new String[]{"name", WeightedSwitchController.WEIGHTS}, new Class[]{String.class, String.class});
        mdl.addRow(new String[]{"0", "0"});
        mdl.addRow(new String[]{"1", "1"});
        mdl.addRow(new String[]{"2", "2"});
        mdl.addRow(new String[]{"5", "5"});
        obj.setData(mdl);

        for (int n = 0; n < 800; n++) {
            Sampler s = obj.next();
            log.info("Sampler: " + s.getName());
            Assert.assertNotNull(s);
            Assert.assertNull(obj.next());
            Assert.assertNotEquals(s.getName(), "0");
        }
    }

    private DebugSampler getSampler(String s) {
        DebugSampler debugSampler = new DebugSampler();
        debugSampler.setName(s);
        return debugSampler;
    }

    @Test
    public void testComb() {
        long[] sums = new long[]{0L, 0L};
        long[] prio = new long[]{2, 3};
        long[] choices = new long[]{0, 0};
        for (int i = 0; i < 100; i++) {
            // inc counts
            for (int k = 0; k < sums.length; k++) {
                sums[k] += prio[k];
            }

            if (sums[0] >= sums[1]) {
                // 2
                sums[0] -= prio[0];
                choices[0]++;
            } else {
                // 3
                sums[1] -= prio[1];
                choices[1]++;
            }

            // minimize nums
            long min;
            if (sums[0] >= sums[1]) {
                min = sums[1];
            } else {
                min = sums[0];
            }
            for (int z = 0; z < sums.length; z++) {
                sums[z] -= min;
            }

        }
        System.out.print(Arrays.toString(choices));
    }


    @Test
    public void testSampleCount() throws Exception {
        JMeterUtils.loadJMeterProperties(getClass().getResource("jmeter.properties").getFile());
        JMeterContextService.getContext().setVariables(new JMeterVariables());


        TestSampleListener listener = new TestSampleListener();

        // top WSC
        WeightedSwitchController topWSC = new WeightedSwitchController();
        PowerTableModel topPTM = new PowerTableModel(new String[]{"name", WeightedSwitchController.WEIGHTS}, new Class[]{String.class, String.class});
        topPTM.addRow(new String[]{"wsc1", "2"});
        topPTM.addRow(new String[]{"wsc2", "2"});
        topPTM.addRow(new String[]{"D_#1", "1"});
        topPTM.addRow(new String[]{"D_#2", "1"});
        topWSC.setData(topPTM);

        DebugSampler d1 = new DebugSampler();
        DebugSampler d2 = new DebugSampler();

        // first child WSC of top WSC
        WeightedSwitchController childWSC1 = new WeightedSwitchController();
        PowerTableModel childPTM1 = new PowerTableModel(new String[]{"name", WeightedSwitchController.WEIGHTS}, new Class[]{String.class, String.class});
        childPTM1.addRow(new String[]{"D1_#1", "1"});
        childPTM1.addRow(new String[]{"D1_#2", "1"});
        childWSC1.setData(childPTM1);

        DebugSampler d1_1 = new DebugSampler();
        DebugSampler d1_2 = new DebugSampler();

        // second child WSC of top WSC
        WeightedSwitchController childWSC2 = new WeightedSwitchController();
        PowerTableModel childPTM2 = new PowerTableModel(new String[]{"name", WeightedSwitchController.WEIGHTS}, new Class[]{String.class, String.class});
        childPTM2.addRow(new String[]{"D2_#1", "1"});
        childPTM2.addRow(new String[]{"D2_#2", "1"});
        childWSC2.setData(childPTM2);

        DebugSampler d2_1 = new DebugSampler();
        DebugSampler d2_2 = new DebugSampler();

        // main loop
        LoopController loop = new LoopController();
        loop.setLoops(6);
        loop.setContinueForever(false);

        // test tree
        ListedHashTree hashTree = new ListedHashTree();
        hashTree.add(loop);
        hashTree.add(loop, topWSC);
        hashTree.add(topWSC, listener);
        hashTree.add(topWSC, d1);
        hashTree.add(topWSC, d2);
        hashTree.add(topWSC, childWSC1);
        hashTree.add(topWSC, childWSC2);
        hashTree.add(childWSC1, d1_1);
        hashTree.add(childWSC1, d1_2);
        hashTree.add(childWSC2, d2_1);
        hashTree.add(childWSC2, d2_2);

        TestCompiler compiler = new TestCompiler(hashTree);
        hashTree.traverse(compiler);

        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setNumThreads(1);

        ListenerNotifier notifier = new ListenerNotifier();

        JMeterThread thread = new JMeterThread(hashTree, threadGroup, notifier);
        thread.setThreadGroup(threadGroup);
        thread.setOnErrorStopThread(true);
        thread.run();

        assertEquals(6, listener.events.size());
        System.out.println();
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