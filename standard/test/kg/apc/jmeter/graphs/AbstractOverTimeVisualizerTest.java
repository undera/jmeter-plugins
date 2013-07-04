package kg.apc.jmeter.graphs;

import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.vizualizers.JSettingsPanel;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author undera
 */
public class AbstractOverTimeVisualizerTest {

    public AbstractOverTimeVisualizerTest() {
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
     * Test of add method, of class AbstractOverTimeVisualizer.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        SampleResult sample = new SampleResult();
        AbstractOverTimeVisualizer instance = new AbstractOverTimeVisualizerImpl();
        instance.add(sample);
    }

    public class AbstractOverTimeVisualizerImpl extends AbstractOverTimeVisualizer {

        @Override
        protected JSettingsPanel createSettingsPanel() {
            return new JSettingsPanel(this, JSettingsPanel.GRADIENT_OPTION);
        }

        public String getLabelResource() {
            return "";
        }

        @Override
        public String getStaticLabel() {
            return "TEST";
        }

        @Override
        public String getWikiPage() {
            return "";
        }
    }

    @Test
    public void testClearData() {
        System.out.println("clearData");
        AbstractOverTimeVisualizer instance = new AbstractOverTimeVisualizerImpl();
        instance.clearData();
    }

    /**
     * Test of normalizeTime method, of class AbstractOverTimeVisualizer.
     */
    @Test
    public void testNormalizeTime() {
        System.out.println("normalizeTime");
        long time = 0L;
        AbstractOverTimeVisualizer instance = new AbstractOverTimeVisualizerImpl();
        long expResult = 0L;
        long result = instance.normalizeTime(time);
        assertEquals(expResult, result);
    }

    /**
     * Test of handleRelativeStartTime method, of class AbstractOverTimeVisualizer.
     */
    @Test
    public void testHandleRelativeStartTime() {
        System.out.println("handleRelativeStartTime");
        AbstractOverTimeVisualizer instance = new AbstractOverTimeVisualizerImpl();
        instance.handleRelativeStartTime();
    }
}