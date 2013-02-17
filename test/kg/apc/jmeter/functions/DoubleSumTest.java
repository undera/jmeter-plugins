package kg.apc.jmeter.functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
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
public class DoubleSumTest {

    public DoubleSumTest() {
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
     * Test of execute method, of class DoubleSum.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        SampleResult previousResult = null;
        Sampler currentSampler = null;

        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("1.256"));
        parameters.add(new CompoundVariable("4.3346"));

        DoubleSum instance = new DoubleSum();
        instance.setParameters(parameters);
        String expResult = "5.5906";
        String result = instance.execute(previousResult, currentSampler);
        assertEquals(expResult, result);
    }

    /**
     * Test of setParameters method, of class DoubleSum.
     */
    @Test
    public void testSetParameters() throws Exception {
        System.out.println("setParameters");
        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("1.256"));
        parameters.add(new CompoundVariable("4.3346"));
        DoubleSum instance = new DoubleSum();
        instance.setParameters(parameters);
    }

    /**
     * Test of getReferenceKey method, of class DoubleSum.
     */
    @Test
    public void testGetReferenceKey() {
        System.out.println("getReferenceKey");
        DoubleSum instance = new DoubleSum();
        String expResult = "__doubleSum";
        String result = instance.getReferenceKey();
        assertEquals(expResult, result);
    }

    /**
     * Test of getArgumentDesc method, of class DoubleSum.
     */
    @Test
    public void testGetArgumentDesc() {
        System.out.println("getArgumentDesc");
        DoubleSum instance = new DoubleSum();
        List result = instance.getArgumentDesc();
        assertEquals(3, result.size());
    }

}