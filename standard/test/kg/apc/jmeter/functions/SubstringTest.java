package kg.apc.jmeter.functions;

import kg.apc.emulators.TestJMeterUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
public class SubstringTest {

    public SubstringTest() {
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
     * Test of execute method, of class Substring.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute 1");
        SampleResult previousResult = null;
        Sampler currentSampler = null;
        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("test string"));
        parameters.add(new CompoundVariable("5"));
        parameters.add(new CompoundVariable("8"));
        parameters.add(new CompoundVariable("var"));
        Substring instance = new Substring();
        instance.setParameters(parameters);
        String expResult = "str";
        String result = instance.execute(previousResult, currentSampler);
        assertEquals(expResult, result);
    }

    /**
     * Test of setParameters method, of class Substring.
     */
    @Test
    public void testSetParameters() throws Exception {
        System.out.println("setParameters");
        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("test string"));
        parameters.add(new CompoundVariable("0"));
        parameters.add(new CompoundVariable("1"));
        Substring instance = new Substring();
        instance.setParameters(parameters);
    }

    /**
     * Test of getReferenceKey method, of class Substring.
     */
    @Test
    public void testGetReferenceKey() {
        System.out.println("getReferenceKey");
        Substring instance = new Substring();
        String expResult = "__substring";
        String result = instance.getReferenceKey();
        assertEquals(expResult, result);
    }

    /**
     * Test of getArgumentDesc method, of class Substring.
     */
    @Test
    public void testGetArgumentDesc() {
        System.out.println("getArgumentDesc");
        Substring instance = new Substring();
        List result = instance.getArgumentDesc();
        assertEquals(4, result.size());
    }
}