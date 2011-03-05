package kg.apc.jmeter.vizualizers;

import javax.swing.JPanel;
import kg.apc.jmeter.charting.GraphPanelChart;
import kg.apc.jmeter.util.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stéphane Hoblingre
 */
public class JSettingsPanelTest {

    public JSettingsPanelTest() {
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
     * Test of setGranulationValue method, of class JSettingsPanel.
     */
    @Test
    public void testSetGranulationValue()
    {
        System.out.println("setGranulationValue");
        int value = 1000;
        JSettingsPanel instance = new JSettingsPanel(new AbstractGraphPanelVisualizerImpl(), true, true, true, true, true);
        instance.setGranulationValue(value);
    }

     public class AbstractGraphPanelVisualizerImpl
         extends AbstractGraphPanelVisualizer
   {
      public String getLabelResource()
      {
         return "test";
      }

      public void add(SampleResult sample)
      {
         return;
      }

        @Override
        protected JSettingsPanel getSettingsPanel()
        {
            return new JSettingsPanel(this, true, true, true, true, true);
        }

        @Override
        protected String getWikiPage() {
            return "";
        }
   }

    /**
     * Test of setAggregateMode method, of class JSettingsPanel.
     */
    @Test
    public void testSetAggregateMode()
    {
        System.out.println("setAggregateMode");
        boolean aggregate = false;
        JSettingsPanel instance = new JSettingsPanel(new AbstractGraphPanelVisualizerImpl(), true, true, true, true, true);
        instance.setAggregateMode(aggregate);
    }

    /**
     * Test of getGraphDisplayPanel method, of class JSettingsPanel.
     */
    @Test
    public void testGetGraphDisplayPanel()
    {
        System.out.println("getGraphDisplayPanel");
        SettingsInterfaceImpl parent = new SettingsInterfaceImpl();
        JSettingsPanel instance = new JSettingsPanel(parent, true, true, true, true, true);
        JPanel result = instance.getGraphDisplayPanel();
        assertNotNull(result);
    }

    /**
     * Test of isPreview method, of class JSettingsPanel.
     */
    @Test
    public void testIsPreview()
    {
        System.out.println("isPreview");
        JSettingsPanel instance = new JSettingsPanel(new SettingsInterfaceImpl(), true, true, true, true, true);
        boolean expResult = true;
        boolean result = instance.isPreview();
        assertEquals(expResult, result);
    }


    private class SettingsInterfaceImpl implements SettingsInterface{

        public int getGranulation()
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setGranulation(int granulation)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public GraphPanelChart getGraphPanelChart()
        {
            return new GraphPanelChart();
        }

        public void switchModel(boolean aggregate)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}