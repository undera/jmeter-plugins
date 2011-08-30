package kg.apc.jmeter.graphs;

import kg.apc.jmeter.graphs.AbstractVsThreadVisualizer;
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
 * @author Stephane Hoblingre
 */
public class AbstractVsThreadVisualizerTest {

    public AbstractVsThreadVisualizerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        TestJMeterUtils.createJmeterEnv();
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getCurrentThreadCount method, of class AbstractVsThreadVisualizer.
     */
    @Test
    public void testGetCurrentThreadCount()
    {
        System.out.println("getCurrentThreadCount");
        long now = System.currentTimeMillis();
        SampleResult sample = new SampleResult();
        sample.setAllThreads(3);
        sample.setThreadName("test_tg");
        sample.setStampAndTime(now, 300);
        AbstractVsThreadVisualizer instance = new AbstractVsThreadVisualizerImpl();
        instance.add(sample);
        int expResult = 3;
        int result = instance.getCurrentThreadCount(sample);
        assertEquals(expResult, result);
    }

    /**
     * Test of add method, of class AbstractVsThreadVisualizer.
     */
    @Test
    public void testAdd()
    {
        System.out.println("add");
        SampleResult sample = new SampleResult();
        AbstractVsThreadVisualizer instance = new AbstractVsThreadVisualizerImpl();
    }

    /**
     * Test of clearData method, of class AbstractVsThreadVisualizer.
     */
    @Test
    public void testClearData()
    {
        System.out.println("clearData");
        AbstractVsThreadVisualizer instance = new AbstractVsThreadVisualizerImpl();
        SampleResult sample = new SampleResult();
        instance.clearData();
    }

    public class AbstractVsThreadVisualizerImpl extends AbstractVsThreadVisualizer
    {
        @Override
        protected JSettingsPanel createSettingsPanel()
        {
            return new JSettingsPanel(this, JSettingsPanel.GRADIENT_OPTION);
        }

        @Override
        public String getLabelResource()
        {
            return "";
        }

        @Override
        public String getStaticLabel()
        {
            return "TEST";
        }

        @Override
        public String getWikiPage() {
           return "";
        }
    }

   /**
    * Test of setExtraChartSettings method, of class AbstractVsThreadVisualizer.
    */
   @Test
   public void testSetExtraChartSettings() {
      System.out.println("setExtraChartSettings");
      AbstractVsThreadVisualizer instance = new AbstractVsThreadVisualizerImpl();
      instance.setExtraChartSettings();
      assertTrue(instance.getGraphPanelChart().getChartSettings().isDrawCurrentX());
   }

}
