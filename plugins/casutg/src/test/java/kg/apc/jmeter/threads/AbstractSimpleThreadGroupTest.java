package kg.apc.jmeter.threads;

import com.blazemeter.jmeter.threads.arrivals.ArrivalsThreadGroupTest;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jorphan.collections.ListedHashTree;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class AbstractSimpleThreadGroupTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testFlow() {
        JMeterUtils.setProperty(AbstractSimpleThreadGroup.THREAD_GROUP_DISTRIBUTED_PREFIX_PROPERTY_NAME, "distprefix");
        final AbstractSimpleThreadGroupImpl tg = new AbstractSimpleThreadGroupImpl();
        tg.setName("TGName");
        LoopController looper = new LoopController();
        looper.setLoops(-1);
        tg.setSamplerController(looper);
        tg.setNumThreads(1);
        ListedHashTree listedHashTree = ArrivalsThreadGroupTest.getListedHashTree(tg, false);
        tg.start(0, null, listedHashTree, null);

        for (Map.Entry<JMeterThread, Thread> entry : tg.getAllThreads().entrySet()) {
            assertEquals("distprefix-TGName 0-1", entry.getValue().getName());
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    tg.tellThreadsToStop();
                    tg.waitThreadsStopped();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        tg.verifyThreadsStopped();
    }

    @Test
    public void testScheduleThread_JMeterThread_long() {
        System.out.println("scheduleThread");
        JMeterThread thread = null;
        long now = 0L;
        AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
        instance.scheduleThread(thread, now);
    }

    @Test
    public void testScheduleThread_JMeterThread() {
        System.out.println("scheduleThread");
        JMeterThread thread = null;
        AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
        instance.scheduleThread(thread);
    }

    @Test
    public void testStart() {
        System.out.println("start");
        int groupCount = 0;
        ListenerNotifier notifier = null;
        ListedHashTree threadGroupTree = null;
        StandardJMeterEngine engine = null;
        AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
        instance.start(groupCount, notifier, threadGroupTree, engine);
    }

    @Test
    public void testStopThread() {
        System.out.println("stopThread");
        String threadName = "";
        boolean now = false;
        AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
        boolean expResult = false;
        boolean result = instance.stopThread(threadName, now);
        assertEquals(expResult, result);
    }

    @Test
    public void testThreadFinished() {
        System.out.println("threadFinished");
        HashTree hashtree = new HashTree();
        hashtree.add(new LoopController());
        JMeterThread thread = new JMeterThread(hashtree, null, null);
        AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
        instance.threadFinished(thread);
    }

    @Test
    public void testTellThreadsToStop() {
        System.out.println("tellThreadsToStop");
        AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
        instance.tellThreadsToStop();
    }

    @Test
    public void testStop() {
        System.out.println("stop");
        AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
        instance.stop();
    }

    @Test
    public void testNumberOfActiveThreads() {
        System.out.println("numberOfActiveThreads");
        AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
        int expResult = 0;
        int result = instance.numberOfActiveThreads();
        assertEquals(expResult, result);
    }

    @Test
    public void testVerifyThreadsStopped() {
        System.out.println("verifyThreadsStopped");
        AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
        boolean expResult = true;
        boolean result = instance.verifyThreadsStopped();
        assertEquals(expResult, result);
    }

    @Test
    public void testWaitThreadsStopped() {
        System.out.println("waitThreadsStopped");
        AbstractSimpleThreadGroup instance = new AbstractSimpleThreadGroupImpl();
        instance.waitThreadsStopped();
    }

}