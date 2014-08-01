package org.jmeterplugins.visualizers.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.vizualizers.CorrectedResultCollector;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AbstractFilterableVisualizerTest {

    public AbstractFilterableVisualizerTest() {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
    *
    */
    public class AbstractFilterableVisualizerImpl extends
            AbstractFilterableVisualizer {

        public String getLabelResource() {
            return "test";
        }

        public void add(SampleResult sample) {
            return;
        }

        @Override
        public String getStaticLabel() {
            return "";
        }

        @Override
        public void clearData() {
        }
    }

    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        AbstractFilterableVisualizer instance = new AbstractFilterableVisualizerImpl();
        TestElement result = instance.createTestElement();
        assertNotNull(result);
    }

    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement c = new ResultCollector();
        AbstractFilterableVisualizer instance = new AbstractFilterableVisualizerImpl();
        instance.modifyTestElement(c);
    }

    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement el = new ResultCollector();
        AbstractFilterableVisualizer instance = new AbstractFilterableVisualizerImpl();
        instance.configure(el);
    }

    private static class DebugVisualizer extends AbstractFilterableVisualizer {

        String lastLabel;

        public DebugVisualizer() {
        }

        public void add(SampleResult sr) {
            if (isSampleIncluded(sr)) {
                lastLabel = sr.getSampleLabel();
            } else {
                lastLabel = null;
            }
        }

        public boolean isStats() {
            return false;
        }

        @Override
        public String getStaticLabel() {
            return "debug";
        }

        public String getLabelResource() {
            return "debug";
        }

        public String getWikiPage() {
            return "debug";
        }

        @Override
        public void clearData() {
        }
    }

    @Test
    public void testIncludeExclude_none() {
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.testStarted();
        DebugVisualizer vis = new DebugVisualizer();
        vis.configure(instance);
        instance.setListener(vis);
        SampleResult res = new SampleResult();
        res.setSampleLabel("test");
        instance.sampleOccurred(new SampleEvent(res, "tg"));
        assertEquals("test", vis.lastLabel);
    }

    @Test
    public void testIncludeExclude_include_only() {
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.setProperty(CorrectedResultCollector.INCLUDE_SAMPLE_LABELS,
                "boom1,test,boom2");
        instance.testStarted();
        DebugVisualizer vis = new DebugVisualizer();
        instance.setListener(vis);
        vis.configure(instance);

        SampleResult res = new SampleResult();
        res.setSampleLabel("test");
        instance.sampleOccurred(new SampleEvent(res, "tg"));
        assertEquals("test", vis.lastLabel);

        vis.lastLabel = null;
        SampleResult res2 = new SampleResult();
        res2.setSampleLabel("test1");
        instance.sampleOccurred(new SampleEvent(res2, "tg"));
        assertNull(vis.lastLabel);
    }

    @Test
    public void testIncludeExclude_exclude_only() {
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.setProperty(CorrectedResultCollector.EXCLUDE_SAMPLE_LABELS,
                "boom1,test,boom2");
        instance.testStarted();
        DebugVisualizer vis = new DebugVisualizer();
        vis.configure(instance);
        instance.setListener(vis);

        vis.lastLabel = null;
        SampleResult res = new SampleResult();
        res.setSampleLabel("test");
        instance.sampleOccurred(new SampleEvent(res, "tg"));
        assertNull(vis.lastLabel);

        vis.lastLabel = null;
        SampleResult res2 = new SampleResult();
        res2.setSampleLabel("test1");
        instance.sampleOccurred(new SampleEvent(res2, "tg"));
        assertEquals("test1", vis.lastLabel);
    }

    @Test
    public void testIncludeExclude_exclude_include() {
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.setExcludeLabels("boom1,test,boom2");
        instance.setIncludeLabels("boom1,test1,boom2");
        instance.testStarted();
        DebugVisualizer vis = new DebugVisualizer();
        vis.configure(instance);
        instance.setListener(vis);

        vis.lastLabel = null;
        SampleResult res = new SampleResult();
        res.setSampleLabel("test");
        instance.sampleOccurred(new SampleEvent(res, "tg"));
        assertNull(vis.lastLabel);

        vis.lastLabel = null;
        SampleResult res2 = new SampleResult();
        res2.setSampleLabel("test1");
        instance.sampleOccurred(new SampleEvent(res2, "tg"));
        assertEquals("test1", vis.lastLabel);

        vis.lastLabel = null;
        SampleResult res3 = new SampleResult();
        res3.setSampleLabel("boom1");
        instance.sampleOccurred(new SampleEvent(res3, "tg"));
        assertNull(vis.lastLabel);
    }

    @Test
    public void testIncludeExcludeRegex_none() {
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.setProperty(CorrectedResultCollector.INCLUDE_SAMPLE_LABELS,
                "P[0-9].*");
        instance.testStarted();
        DebugVisualizer vis = new DebugVisualizer();
        instance.setListener(vis);
        vis.configure(instance);

        vis.lastLabel = null;
        SampleResult res2 = new SampleResult();
        res2.setSampleLabel("P1_TEST");
        instance.sampleOccurred(new SampleEvent(res2, "tg"));
        assertNull(vis.lastLabel);
    }

    @Test
    public void testIncludeExcludeRegex_include_only() {
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.setProperty(CorrectedResultCollector.INCLUDE_SAMPLE_LABELS,
                "P[0-9].*");
        instance.setProperty(
                CorrectedResultCollector.INCLUDE_REGEX_CHECKBOX_STATE, true);
        instance.testStarted();
        DebugVisualizer vis = new DebugVisualizer();
        instance.setListener(vis);
        vis.configure(instance);

        vis.lastLabel = null;
        SampleResult res = new SampleResult();
        res.setSampleLabel("P1_TEST");
        instance.sampleOccurred(new SampleEvent(res, "tg"));
        assertEquals("P1_TEST", vis.lastLabel);

        vis.lastLabel = null;
        SampleResult res1 = new SampleResult();
        res1.setSampleLabel("T1_TEST");
        instance.sampleOccurred(new SampleEvent(res1, "tg"));
        assertNull(vis.lastLabel);
    }

    @Test
    public void testIncludeExcludeRegex_exclude_only() {
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.setProperty(CorrectedResultCollector.EXCLUDE_SAMPLE_LABELS,
                "P[0-9].*");
        instance.setProperty(
                CorrectedResultCollector.EXCLUDE_REGEX_CHECKBOX_STATE, true);
        instance.testStarted();
        DebugVisualizer vis = new DebugVisualizer();
        instance.setListener(vis);
        vis.configure(instance);

        vis.lastLabel = null;
        SampleResult res = new SampleResult();
        res.setSampleLabel("P1_TEST");
        instance.sampleOccurred(new SampleEvent(res, "tg"));
        assertNull(vis.lastLabel);

        vis.lastLabel = null;
        SampleResult res1 = new SampleResult();
        res1.setSampleLabel("T1_TEST");
        instance.sampleOccurred(new SampleEvent(res1, "tg"));
        assertEquals("T1_TEST", vis.lastLabel);
    }

    @Test
    public void testIncludeExcludeRegex_exclude_include() {
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.setExcludeLabels("[T-Z][0-9].*");
        instance.setIncludeLabels("[P-T][0-9].*");
        instance.setProperty(
                CorrectedResultCollector.EXCLUDE_REGEX_CHECKBOX_STATE, true);
        instance.setProperty(
                CorrectedResultCollector.INCLUDE_REGEX_CHECKBOX_STATE, true);
        instance.testStarted();
        DebugVisualizer vis = new DebugVisualizer();
        vis.configure(instance);
        instance.setListener(vis);

        vis.lastLabel = null;
        SampleResult res = new SampleResult();
        res.setSampleLabel("Z1_TEST");
        instance.sampleOccurred(new SampleEvent(res, "tg"));
        assertNull(vis.lastLabel);

        vis.lastLabel = null;
        SampleResult res2 = new SampleResult();
        res2.setSampleLabel("P1_TEST");
        instance.sampleOccurred(new SampleEvent(res2, "tg"));
        assertEquals("P1_TEST", vis.lastLabel);

        vis.lastLabel = null;
        SampleResult res3 = new SampleResult();
        res3.setSampleLabel("T1_TEST");
        instance.sampleOccurred(new SampleEvent(res3, "tg"));
        assertNull(vis.lastLabel);
    }

    @Test
    public void testMinMax_none() {
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.testStarted();
        DebugVisualizer vis = new DebugVisualizer();
        vis.configure(instance);
        instance.setListener(vis);
        SampleResult res = SampleResult.createTestSample(21000, 30000);
        res.setSampleLabel("test");
        instance.sampleOccurred(new SampleEvent(res, "tg"));
        assertEquals("test", vis.lastLabel);
    }

    @Test
    public void testMinMax_min_only() {
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.setStartOffset("10");
        instance.testStarted();
        DebugVisualizer vis = new DebugVisualizer();
        vis.configure(instance);
        instance.setListener(vis);
        vis.startTimeRef = 10300;
        vis.startTimeInf = 10000;
        vis.startTimeSup = 11000;

        vis.lastLabel = null;
        SampleResult res = SampleResult.createTestSample(19000, 20000);
        res.setSampleLabel("test");
        instance.sampleOccurred(new SampleEvent(res, "tg"));
        assertNull(vis.lastLabel);

        vis.lastLabel = null;
        SampleResult res1 = SampleResult.createTestSample(21000, 22000);
        res1.setSampleLabel("test1");
        instance.sampleOccurred(new SampleEvent(res1, "tg"));
        assertEquals("test1", vis.lastLabel);

        instance.testEnded();
        instance.setStartOffset("10a");
        instance.testStarted();
        vis.configure(instance);
        instance.setListener(vis);

        vis.lastLabel = null;
        SampleResult res2 = SampleResult.createTestSample(19000, 20000);
        res2.setSampleLabel("test2");
        instance.sampleOccurred(new SampleEvent(res2, "tg"));
        assertEquals("test2", vis.lastLabel);
    }

    @Test
    public void testMinMax_max_only() {
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.setEndOffset("20");
        instance.testStarted();
        DebugVisualizer vis = new DebugVisualizer();
        vis.configure(instance);
        instance.setListener(vis);
        vis.startTimeRef = 10300;
        vis.startTimeInf = 10000;
        vis.startTimeSup = 11000;

        vis.lastLabel = null;
        SampleResult res = SampleResult.createTestSample(31500, 32000);
        res.setSampleLabel("test");
        instance.sampleOccurred(new SampleEvent(res, "tg"));
        assertNull(vis.lastLabel);

        vis.lastLabel = null;
        SampleResult res1 = SampleResult.createTestSample(29000, 30000);
        res1.setSampleLabel("test1");
        instance.sampleOccurred(new SampleEvent(res1, "tg"));
        assertEquals("test1", vis.lastLabel);

        instance.testEnded();
        instance.setEndOffset("20a");
        instance.testStarted();
        vis.configure(instance);
        instance.setListener(vis);

        vis.lastLabel = null;
        SampleResult res2 = SampleResult.createTestSample(31000, 32000);
        res2.setSampleLabel("test2");
        instance.sampleOccurred(new SampleEvent(res2, "tg"));
        assertEquals("test2", vis.lastLabel);
    }

    @Test
    public void testMinMax_min_max() {
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.setStartOffset("10");
        instance.setEndOffset("20");
        instance.testStarted();
        DebugVisualizer vis = new DebugVisualizer();
        vis.configure(instance);
        instance.setListener(vis);
        vis.startTimeRef = 10300;
        vis.startTimeInf = 10000;
        vis.startTimeSup = 11000;

        vis.lastLabel = null;
        SampleResult res = SampleResult.createTestSample(31500, 32000);
        res.setSampleLabel("test");
        instance.sampleOccurred(new SampleEvent(res, "tg"));
        assertNull(vis.lastLabel);

        vis.lastLabel = null;
        SampleResult res1 = SampleResult.createTestSample(29000, 30000);
        res1.setSampleLabel("test1");
        instance.sampleOccurred(new SampleEvent(res1, "tg"));
        assertEquals("test1", vis.lastLabel);

        vis.lastLabel = null;
        SampleResult res2 = SampleResult.createTestSample(19000, 20000);
        res2.setSampleLabel("test2");
        instance.sampleOccurred(new SampleEvent(res2, "tg"));
        assertNull(vis.lastLabel);
    }

    /**
     * Test of isSampleIncluded method, of class AbstractFilterableVisualizer.
     */
    @Test
    public void testIsSampleIncluded() {
        System.out.println("isSampleIncluded");
        SampleResult res = null;
        AbstractFilterableVisualizer instance = new AbstractFilterableVisualizerImpl();
        boolean expResult = true;
        boolean result = instance.isSampleIncluded(res);
        assertEquals(expResult, result);
    }

    /**
     * Test of setUpFiltering method, of class AbstractFilterableVisualizer.
     */
    @Test
    public void testSetUpFiltering() {
        System.out.println("setUpFiltering");
        CorrectedResultCollector rc = new CorrectedResultCollector();
        AbstractFilterableVisualizer instance = new AbstractFilterableVisualizerImpl();
        instance.setUpFiltering(rc);
    }

    /**
     * Test of isSampleIncluded method, of class AbstractFilterableVisualizer.
     */
    @Test
    public void testIsSampleIncluded_String() {
        System.out.println("isSampleIncluded");
        String sampleLabel = "test";
        AbstractFilterableVisualizer instance = new AbstractFilterableVisualizerImpl();
        boolean expResult = true;
        boolean result = instance.isSampleIncluded(sampleLabel);
        assertEquals(expResult, result);
    }

    /**
     * Test of isSampleIncluded method, of class AbstractFilterableVisualizer.
     */
    @Test
    public void testIsSampleIncluded_SampleResult() {
        System.out.println("isSampleIncluded");
        SampleResult res = null;
        AbstractFilterableVisualizer instance = new AbstractFilterableVisualizerImpl();
        boolean expResult = true;
        boolean result = instance.isSampleIncluded(res);
        assertEquals(expResult, result);
    }

}
