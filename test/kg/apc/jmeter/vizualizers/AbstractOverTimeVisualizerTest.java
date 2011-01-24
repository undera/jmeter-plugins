package kg.apc.jmeter.vizualizers;

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
    public static void setUpClass() throws Exception
    {
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
     * Test of setUseRelativeTime method, of class AbstractOverTimeVisualizer.
     */
    @Test
    public void testSetUseRelativeTime()
    {
        System.out.println("setUseRelativeTime");
        boolean selected = false;
        AbstractOverTimeVisualizer instance = new AbstractOverTimeVisualizerImpl();
        instance.setUseRelativeTime(selected);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class AbstractOverTimeVisualizer.
     */
    @Test
    public void testAdd()
    {
        System.out.println("add");
        SampleResult sample = null;
        AbstractOverTimeVisualizer instance = new AbstractOverTimeVisualizerImpl();
        instance.add(sample);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class AbstractOverTimeVisualizerImpl extends AbstractOverTimeVisualizer
    {

        @Override
        protected JSettingsPanel getSettingsPanel()
        {
            return null;
        }

        public String getLabelResource()
        {
            return "test";
        }
    }

}