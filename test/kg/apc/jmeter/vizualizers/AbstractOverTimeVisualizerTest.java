package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.util.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author undera
 */
public class AbstractOverTimeVisualizerTest {

    public AbstractOverTimeVisualizerTest() {
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
     * Test of add method, of class AbstractOverTimeVisualizer.
     */
    @Test
    public void testAdd()
    {
        System.out.println("add");
        SampleResult sample = new SampleResult();
        AbstractOverTimeVisualizer instance = new AbstractOverTimeVisualizerImpl();
        instance.add(sample);
    }

    public class AbstractOverTimeVisualizerImpl extends AbstractOverTimeVisualizer
    {

        @Override
        protected JSettingsPanel getSettingsPanel()
        {
            return new JSettingsPanel(this, isStats, isStats, isStats, isStats, isStats);
        }

        public String getLabelResource()
        {
            return "";
        }

        @Override
        public String getStaticLabel()
        {
            return "TEST";
        }
   }

   @Test
   public void testClearData()
   {
      System.out.println("clearData");
      AbstractOverTimeVisualizer instance = new AbstractOverTimeVisualizerImpl();
      instance.clearData();
   }

}