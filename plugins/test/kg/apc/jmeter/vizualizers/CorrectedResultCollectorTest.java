package kg.apc.jmeter.vizualizers;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.Visualizer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.*;

/**
 *
 * @author undera
 */
public class CorrectedResultCollectorTest {

    public CorrectedResultCollectorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of testStarted method, of class CorrectedResultCollector.
     */
    @Test
    public void testTestStarted() {
        System.out.println("testStarted");
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.testStarted();
    }

    @Test
    public void testIncludeExclude_none() {
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.testStarted();
        DebugVisualizer vis = new DebugVisualizer();
        instance.setListener(vis);
        SampleResult res = new SampleResult();
        res.setSampleLabel("test");
        instance.sampleOccurred(new SampleEvent(res, "tg"));
        assertEquals("test", vis.lastLabel);
    }

    @Test
    public void testIncludeExclude_include_only() {
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.setProperty(CorrectedResultCollector.INCLUDE_SAMPLE_LABELS, "boom1,test,boom2");
        instance.testStarted();
        DebugVisualizer vis = new DebugVisualizer();
        instance.setListener(vis);

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
        instance.setProperty(CorrectedResultCollector.EXCLUDE_SAMPLE_LABELS, "boom1,test,boom2");
        instance.testStarted();
        DebugVisualizer vis = new DebugVisualizer();
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

    private static class DebugVisualizer implements Visualizer {

        String lastLabel;

        public DebugVisualizer() {
        }

        public void add(SampleResult sr) {
            lastLabel = sr.getSampleLabel();
        }

        public boolean isStats() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
