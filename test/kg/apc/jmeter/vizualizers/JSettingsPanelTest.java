/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.vizualizers;

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
   }

}