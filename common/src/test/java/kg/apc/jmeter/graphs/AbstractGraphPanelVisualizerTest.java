package kg.apc.jmeter.graphs;

import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.GraphPanelChart;
import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.vizualizers.CorrectedResultCollector;
import kg.apc.jmeter.vizualizers.JSettingsPanel;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.LongProperty;
import org.apache.jmeter.visualizers.Sample;
import org.junit.*;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentSkipListMap;

import static org.junit.Assert.*;

public class AbstractGraphPanelVisualizerTest {

    /**
     *
     */
    public AbstractGraphPanelVisualizerTest() {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass()
            throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass()
            throws Exception {
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
     * Test of createGraphPanel method, of class AbstractGraphPanelVisualizer.
     */
    @Test
    public void testCreateGraphPanel() {
        System.out.println("createGraphPanel");
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        GraphPanel result = instance.createGraphPanel();
        assertNotNull(result);
    }

    /**
     * Test of updateGui method, of class AbstractGraphPanelVisualizer.
     */
    @Test
    public void testUpdateGui_Sample() {
        System.out.println("updateGui");
        Sample sample = null;
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        instance.updateGui(sample);
    }

    /**
     * Test of updateGui method, of class AbstractGraphPanelVisualizer.
     */
    @Test
    public void testUpdateGui_0args() {
        System.out.println("updateGui");
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        instance.updateGui();
    }

    /**
     * Test of clearData method, of class AbstractGraphPanelVisualizer.
     */
    @Test
    public void testClearData() {
        System.out.println("clearData");
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        instance.clearData();
    }

    /**
     * Test of getImage method, of class AbstractGraphPanelVisualizer.
     */
    @Test
    public void testGetImage() {
        System.out.println("getImage");
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        Image result = instance.getImage();
        assertNull(result); // in unit test we'll have null here
    }

    /**
     *
     */
    public class AbstractGraphPanelVisualizerImpl
            extends AbstractGraphPanelVisualizer {

        public String getLabelResource() {
            return "test";
        }

        public void add(SampleResult sample) {
        }

        @Override
        protected JSettingsPanel createSettingsPanel() {
            return new JSettingsPanel(this, JSettingsPanel.GRADIENT_OPTION);
        }

        @Override
        public String getWikiPage() {
            return "";
        }

        @Override
        public String getStaticLabel() {
            return "";
        }
    }

    @Test
    public void testGetGranulation() {
        System.out.println("getGranulation");
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        long result = instance.getGranulation();
        assertTrue(result > 0);
    }

    @Test
    public void testGetGranulation_2() {
        System.out.println("getGranulation");
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        try {
            instance.setGranulation(0);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            //
        }
    }

    @Test
    public void testSetGranulation() {
        System.out.println("setGranulation");
        int i = 100;
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        instance.setGranulation(i);
    }

    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        TestElement result = instance.createTestElement();
        assertNotNull(result);
    }

    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement c = new ResultCollector();
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        instance.modifyTestElement(c);
    }

    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement el = new ResultCollector();
        el.setProperty(new LongProperty(AbstractGraphPanelVisualizer.INTERVAL_PROPERTY, 10000));
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        instance.configure(el);
    }

    @Test
    public void testGetSettingsPanel() {
        System.out.println("getSettingsPanel");
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

    @Test
    public void testGetGraphPanelChart() {
        System.out.println("getGraphPanelChart");
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        GraphPanelChart result = instance.getGraphPanelChart();
        assertNotNull(result);
    }

    /**
     * Test of switchModel method, of class AbstractGraphPanelVisualizer.
     */
    @Test
    public void testSwitchModel() {
        System.out.println("switchModel");
        boolean aggregate = true;
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        instance.switchModel(true);
    }

    /**
     * Test of getNewRow method, of class AbstractGraphPanelVisualizer.
     */
    @Test
    public void testGetNewRow_10args() {
        System.out.println("getNewRow");
        ConcurrentSkipListMap<String, AbstractGraphRow> model = new ConcurrentSkipListMap<>();
        int rowType = 0;
        String label = "";
        int markerSize = 0;
        boolean isBarRow = false;
        boolean displayLabel = false;
        boolean thickLines = false;
        boolean showInLegend = false;
        Color color = null;
        boolean canCompose = false;
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        AbstractGraphRow result = instance.getNewRow(model, rowType, label, markerSize, false, false, thickLines, showInLegend, color, canCompose);
        assertNotNull(result);
    }

    /**
     * Test of getNewRow method, of class AbstractGraphPanelVisualizer.
     */
    @Test
    public void testGetNewRow_9args() {
        System.out.println("getNewRow");
        ConcurrentSkipListMap<String, AbstractGraphRow> model = new ConcurrentSkipListMap<String, AbstractGraphRow>();
        int rowType = 0;
        String label = "";
        int markerSize = 0;
        boolean isBarRow = false;
        boolean displayLabel = false;
        boolean thickLines = false;
        boolean showInLegend = false;
        boolean canCompose = false;
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        AbstractGraphRow result = instance.getNewRow(model, rowType, label, markerSize, isBarRow, displayLabel, thickLines, showInLegend, canCompose);
        assertNotNull(result);
    }

    /**
     * Test of updateGui method, of class AbstractGraphPanelVisualizer.
     */
    @Test
    public void testUpdateGui_ErrorType() {
        System.out.println("updateGui");
        Sample sample = null;
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        instance.updateGui(sample);
    }

    /**
     * Test of getWikiPage method, of class AbstractGraphPanelVisualizer.
     */
    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        String expResult = "";
        String result = instance.getWikiPage();
        assertEquals(expResult, result);
    }

    /**
     * Test of isFromTransactionControler method, of class
     * AbstractGraphPanelVisualizer.
     */
    @Test
    public void testIsFromTransactionControler() {
        System.out.println("isFromTransactionControler");
        SampleResult res = new SampleResult();
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        boolean expResult = false;
        boolean result = instance.isFromTransactionControler(res);
        assertEquals(expResult, result);
    }

    /**
     * Test of createSettingsPanel method, of class
     * AbstractGraphPanelVisualizer.
     */
    @Test
    public void testCreateSettingsPanel() {
        System.out.println("createSettingsPanel");
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        String expResult = "";
        String result = instance.getStaticLabel();
        assertEquals(expResult, result);
    }

    /**
     * Test of getGraphPanelContainer method, of class
     * AbstractGraphPanelVisualizer.
     */
    @Test
    public void testGetGraphPanelContainer() {
        System.out.println("getGraphPanelContainer");
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        JPanel result = instance.getGraphPanelContainer();
        assertNotNull(result);
    }

    /**
     * Test of setExtraChartSettings method, of class
     * AbstractGraphPanelVisualizer.
     */
    @Test
    public void testSetExtraChartSettings() {
        System.out.println("setExtraChartSettings");
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        instance.setExtraChartSettings();
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
        instance.setProperty(CorrectedResultCollector.INCLUDE_SAMPLE_LABELS, "boom1,test,boom2");
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
        instance.setProperty(CorrectedResultCollector.EXCLUDE_SAMPLE_LABELS, "boom1,test,boom2");
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

    private static class DebugVisualizer extends AbstractGraphPanelVisualizer {

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
        protected JSettingsPanel createSettingsPanel() {
            return new JSettingsPanel(this, 0);
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
    }

    /**
     * Test of isSampleIncluded method, of class AbstractGraphPanelVisualizer.
     */
    @Test
    public void testIsSampleIncluded() {
        System.out.println("isSampleIncluded");
        SampleResult res = null;
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        boolean expResult = true;
        boolean result = instance.isSampleIncluded(res);
        assertEquals(expResult, result);
    }

    /**
     * Test of setUpFiltering method, of class AbstractGraphPanelVisualizer.
     */
    @Test
    public void testSetUpFiltering() {
        System.out.println("setUpFiltering");
        CorrectedResultCollector rc = new CorrectedResultCollector();
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        instance.setUpFiltering(rc);
    }

    /**
     * Test of isSampleIncluded method, of class AbstractGraphPanelVisualizer.
     */
    @Test
    public void testIsSampleIncluded_String() {
        System.out.println("isSampleIncluded");
        String sampleLabel = "test";
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        boolean expResult = true;
        boolean result = instance.isSampleIncluded(sampleLabel);
        assertEquals(expResult, result);
    }

    /**
     * Test of createTitleLabel method, of class AbstractGraphPanelVisualizer.
     */
    @Test
    public void testCreateTitleLabel() {
        System.out.println("createTitleLabel");
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        Component result = instance.createTitleLabel();
    }

    /**
     * Test of enableMaximize method, of class AbstractGraphPanelVisualizer.
     */
    @Test
    public void testEnableMaximize() {
        System.out.println("enableMaximize");
        boolean enable = false;
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        instance.enableMaximize(enable);
    }

    /**
     * Test of hideFilePanel method, of class AbstractGraphPanelVisualizer.
     */
    @Test
    public void testHideFilePanel() {
        System.out.println("hideFilePanel");
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        instance.hideFilePanel();
    }

    /**
     * Test of isSampleIncluded method, of class AbstractGraphPanelVisualizer.
     */
    @Test
    public void testIsSampleIncluded_SampleResult() {
        System.out.println("isSampleIncluded");
        SampleResult res = null;
        AbstractGraphPanelVisualizer instance = new AbstractGraphPanelVisualizerImpl();
        boolean expResult = true;
        boolean result = instance.isSampleIncluded(res);
        assertEquals(expResult, result);
    }
}
