package kg.apc.jmeter.vizualizers;

import kg.apc.charting.AbstractGraphRow;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResponseTimesPercentilesOverTimeGuiTest {

    private ResponseTimesPercentilesOverTimeGui instance;

    /**
     *
     */
    @BeforeClass
    public static void setUpClass() {
        TestJMeterUtils.createJmeterEnv();
    }

    /**
     *
     */
    @Before
    public void setUp() {
        instance = new ResponseTimesPercentilesOverTimeGui();
    }

    /**
     *
     */
    @Test
    public void testConfigProperties() {
        JMeterUtils.setProperty("jmeterPlugin.percentilesOverTime", "50,60,70,80,90");
        instance = new ResponseTimesPercentilesOverTimeGui();
        assertEquals(5, instance.percentiles.size());
        assertEquals(instance.percentiles.size(), instance.shadingFactors.size());
        assertEquals(1.0d, instance.shadingFactors.get(50d), 0.0d);
        assertEquals(32/256d, instance.shadingFactors.get(90d), 0.0d);
    }

    /**
     *
     */
    @Test
    public void testGetNewRow() {
        AbstractGraphRow row = instance.getNewRow("label", true, 50d);
        assertNotNull(row);
        assertNotNull(row.getColor());
        assertEquals("label (p50)", row.getLabel());
        row = instance.getNewRow("label", true, 99d);
        assertNotNull(row.getColor());
        assertEquals(31, row.getColor().getAlpha());
    }

    /**
     *
     */
    @Test
    public void testGetLabelResource() {
        String result = instance.getLabelResource();
        assertEquals("ResponseTimesPercentilesOverTimeGui", result);
    }

    /**
     *
     */
    @Test
    public void testGetStaticLabel() {
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    /**
     *
     */
    @Test
    public void testAdd() {
        SampleResult res = new SampleResult();
        res.setSampleLabel("label");
        instance.add(res);
    }

    /**
     *
     */
    @Test
    public void testGetWikiPage() {
        String result = instance.getWikiPage();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of createSettingsPanel method, of class PercentilesOverTimeGui.
     */
    @Test
    public void testCreateSettingsPanel() {
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }
}
