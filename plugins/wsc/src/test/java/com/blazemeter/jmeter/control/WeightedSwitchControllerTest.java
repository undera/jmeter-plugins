package com.blazemeter.jmeter.control;

import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.TransactionController;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    public void testNestedWSC() throws Exception {
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
        d1.setName("D1");
        DebugSampler d2 = new DebugSampler();
        d2.setName("D2");

        // first child WSC of top WSC
        WeightedSwitchController childWSC1 = new WeightedSwitchController();
        PowerTableModel childPTM1 = new PowerTableModel(new String[]{"name", WeightedSwitchController.WEIGHTS}, new Class[]{String.class, String.class});
        childPTM1.addRow(new String[]{"D1_#1", "1"});
        childPTM1.addRow(new String[]{"D1_#2", "1"});
        childWSC1.setData(childPTM1);

        DebugSampler d1_1 = new DebugSampler();
        d1_1.setName("D1_1");
        DebugSampler d1_2 = new DebugSampler();
        d1_2.setName("D1_2");

        // second child WSC of top WSC
        WeightedSwitchController childWSC2 = new WeightedSwitchController();
        PowerTableModel childPTM2 = new PowerTableModel(new String[]{"name", WeightedSwitchController.WEIGHTS}, new Class[]{String.class, String.class});
        childPTM2.addRow(new String[]{"D2_#1", "1"});
        childPTM2.addRow(new String[]{"D2_#2", "1"});
        childWSC2.setData(childPTM2);

        DebugSampler d2_1 = new DebugSampler();
        d2_1.setName("D2_1");
        DebugSampler d2_2 = new DebugSampler();
        d2_2.setName("D2_2");

        // main loop
        LoopController loop = new LoopController();
        loop.setLoops(6);
        loop.setContinueForever(false);

        // test tree
        ListedHashTree hashTree = new ListedHashTree();
        hashTree.add(loop);
        hashTree.add(loop, topWSC);
        hashTree.add(topWSC, listener);
        hashTree.add(topWSC, childWSC1);
        hashTree.add(childWSC1, d1_1);
        hashTree.add(childWSC1, d1_2);
        hashTree.add(childWSC1, listener);
        hashTree.add(topWSC, childWSC2);
        hashTree.add(childWSC2, d2_1);
        hashTree.add(childWSC2, d2_2);
        hashTree.add(childWSC2, listener);
        hashTree.add(topWSC, d1);
        hashTree.add(topWSC, d2);

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
        List<String> labels = new ArrayList<>();
        labels.add("D1");
        labels.add("D2");
        labels.add("D1_1");
        labels.add("D1_2");
        labels.add("D2_1");
        labels.add("D2_2");
        for (SampleEvent event : listener.events) {
            assertTrue(labels.contains(event.getResult().getSampleLabel()));
        }
    }

    // https://groups.google.com/forum/?hl=ru#!searchin/jmeter-plugins/Weighted$20Switch$20Controller|sort:relevance/jmeter-plugins/P9Nx9OqgWj4/aeC-RaNgCAAJ
    @Test
    public void testNestedSimpleControllers() throws Exception {
        JMeterUtils.loadJMeterProperties(getClass().getResource("jmeter.properties").getFile());
        JMeterContextService.getContext().setVariables(new JMeterVariables());


        TestSampleListener listener = new TestSampleListener();

        // top WSC
        WeightedSwitchController topWSC = new WeightedSwitchController();
        PowerTableModel topPTM = new PowerTableModel(new String[]{"name", WeightedSwitchController.WEIGHTS}, new Class[]{String.class, String.class});
        topPTM.addRow(new String[]{"ex1", "10"});
        topPTM.addRow(new String[]{"ex2", "20"});
        topWSC.setData(topPTM);


        // first child: simple controller
        GenericController ex1 = new GenericController();

        DebugSampler example1_1 = new DebugSampler();
        example1_1.setName("example1_1");
        DebugSampler example1_2 = new DebugSampler();
        example1_2.setName("example1_2");

        // second child: simple controller
        GenericController ex2 = new GenericController();

        DebugSampler example2_1 = new DebugSampler();
        example2_1.setName("example2_1");
        DebugSampler example2_2 = new DebugSampler();
        example2_2.setName("example2_2");

        // main loop
        LoopController loop = new LoopController();
        loop.setLoops(60);
        loop.setContinueForever(false);

        // test tree
        ListedHashTree hashTree = new ListedHashTree();
        hashTree.add(loop);
        hashTree.add(loop, topWSC);
        hashTree.add(topWSC, listener);
        hashTree.add(topWSC, ex1);
        hashTree.add(ex1, example1_1);
        hashTree.add(ex1, example1_2);
        hashTree.add(ex1, listener);
        hashTree.add(topWSC, ex2);
        hashTree.add(ex2, example2_1);
        hashTree.add(ex2, example2_2);
        hashTree.add(ex2, listener);

        TestCompiler compiler = new TestCompiler(hashTree);
        hashTree.traverse(compiler);

        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setNumThreads(1);

        ListenerNotifier notifier = new ListenerNotifier();

        JMeterThread thread = new JMeterThread(hashTree, threadGroup, notifier);
        thread.setThreadGroup(threadGroup);
        thread.setOnErrorStopThread(true);
        thread.run();

        Map<String, Integer> totalResults = new HashMap<>();
        for (SampleEvent event : listener.events) {
            String label = event.getResult().getSampleLabel();
            if (totalResults.containsKey(label)) {
                totalResults.put(label, totalResults.get(label) + 1);
            } else {
                totalResults.put(label, 1);
            }
        }

        assertEquals(120, listener.events.size());
        assertEquals(20, (int) totalResults.get("example1_1"));
        assertEquals(20, (int) totalResults.get("example1_2"));
        assertEquals(40, (int) totalResults.get("example2_1"));
        assertEquals(40, (int) totalResults.get("example2_2"));
    }

    @Test
    public void testNestedTransactionControllers() throws Exception {
        JMeterUtils.loadJMeterProperties(getClass().getResource("jmeter.properties").getFile());
        JMeterContextService.getContext().setVariables(new JMeterVariables());


        TestSampleListener listener = new TestSampleListener();

        // top WSC
        WeightedSwitchController topWSC = new WeightedSwitchController();
        PowerTableModel topPTM = new PowerTableModel(new String[]{"name", WeightedSwitchController.WEIGHTS}, new Class[]{String.class, String.class});
        topPTM.addRow(new String[]{"ex1", "10"});
        topPTM.addRow(new String[]{"ex2", "20"});
        topWSC.setData(topPTM);


        // first child: transaction controller
        TransactionController ex1 = new TransactionController();

        DebugSampler example1_1 = new DebugSampler();
        example1_1.setName("example1_1");
        DebugSampler example1_2 = new DebugSampler();
        example1_2.setName("example1_2");

        // second child: transaction controller
        TransactionController ex2 = new TransactionController();

        DebugSampler example2_1 = new DebugSampler();
        example2_1.setName("example2_1");
        DebugSampler example2_2 = new DebugSampler();
        example2_2.setName("example2_2");

        // main loop
        LoopController loop = new LoopController();
        loop.setLoops(60);
        loop.setContinueForever(false);

        // test tree
        ListedHashTree hashTree = new ListedHashTree();
        hashTree.add(loop);
        hashTree.add(loop, topWSC);
        hashTree.add(topWSC, listener);
        hashTree.add(topWSC, ex1);
        hashTree.add(ex1, example1_1);
        hashTree.add(ex1, example1_2);
        hashTree.add(ex1, listener);
        hashTree.add(topWSC, ex2);
        hashTree.add(ex2, example2_1);
        hashTree.add(ex2, example2_2);
        hashTree.add(ex2, listener);

        TestCompiler compiler = new TestCompiler(hashTree);
        hashTree.traverse(compiler);

        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setNumThreads(1);

        ListenerNotifier notifier = new ListenerNotifier();

        JMeterThread thread = new JMeterThread(hashTree, threadGroup, notifier);
        thread.setThreadGroup(threadGroup);
        thread.setOnErrorStopThread(true);
        thread.run();

        Map<String, Integer> totalResults = new HashMap<>();
        for (SampleEvent event : listener.events) {
            String label = event.getResult().getSampleLabel();
            if (totalResults.containsKey(label)) {
                totalResults.put(label, totalResults.get(label) + 1);
            } else {
                totalResults.put(label, 1);
            }
        }

        assertEquals(120, listener.events.size());
        assertEquals(20, (int) totalResults.get("example1_1"));
        assertEquals(40, (int) totalResults.get("example2_1"));
        assertEquals(60, (int) totalResults.get("")); // transaction result
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