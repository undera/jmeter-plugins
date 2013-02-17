package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.graphs.SettingsInterface;
import kg.apc.jmeter.graphs.AbstractGraphPanelVisualizer;
import javax.swing.JPanel;
import kg.apc.charting.GraphPanelChart;
import kg.apc.emulators.TestJMeterUtils;
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
        JSettingsPanel instance = new JSettingsPanel(new AbstractGraphPanelVisualizerImpl(), JSettingsPanel.GRADIENT_OPTION);
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
        protected JSettingsPanel createSettingsPanel()
        {
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

    /**
     * Test of setAggregateMode method, of class JSettingsPanel.
     */
    @Test
    public void testSetAggregateMode()
    {
        System.out.println("setAggregateMode");
        boolean aggregate = false;
        JSettingsPanel instance = new JSettingsPanel(new AbstractGraphPanelVisualizerImpl(), JSettingsPanel.GRADIENT_OPTION);
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
        JSettingsPanel instance = new JSettingsPanel(parent, JSettingsPanel.GRADIENT_OPTION);
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
        JSettingsPanel instance = new JSettingsPanel(new SettingsInterfaceImpl(), JSettingsPanel.GRADIENT_OPTION);
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

        public String getWikiPage() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}