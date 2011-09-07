package kg.apc.jmeter.functions;

import org.apache.jmeter.threads.JMeterContextService;
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
public class StrLenTest {

    public StrLenTest() {
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
     * Test of execute method, of class StrLen.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute 1");
        SampleResult previousResult = null;
        Sampler currentSampler = null;
        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("test string"));
        StrLen instance = new StrLen();
        instance.setParameters(parameters);
        String expResult = "11";
        String result = instance.execute(previousResult, currentSampler);
        assertEquals(expResult, result);
    }

    @Test
    public void testExecute_fromVar() throws Exception {
        System.out.println("execute 1");
        SampleResult previousResult = null;
        Sampler currentSampler = null;
        JMeterContextService.getContext().getVariables().put("varName", "test string 123");
        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("${varName}"));
        StrLen instance = new StrLen();
        instance.setParameters(parameters);
        String expResult = "15";
        String result = instance.execute(previousResult, currentSampler);
        assertEquals(expResult, result);
    }

    @Test
    public void testExecute_toVar() throws Exception {
        System.out.println("execute 2");
        SampleResult previousResult = null;
        Sampler currentSampler = null;
        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("test string"));
        parameters.add(new CompoundVariable("varName"));
        StrLen instance = new StrLen();
        instance.setParameters(parameters);
        String expResult = "11";
        String result = instance.execute(previousResult, currentSampler);
        assertEquals(expResult, result);
        assertEquals(expResult, JMeterContextService.getContext().getVariables().get("varName"));
    }

    /**
     * Test of setParameters method, of class StrLen.
     */
    @Test
    public void testSetParameters() throws Exception {
        System.out.println("setParameters");
        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("test string"));
        StrLen instance = new StrLen();
        instance.setParameters(parameters);
    }

    /**
     * Test of getReferenceKey method, of class StrLen.
     */
    @Test
    public void testGetReferenceKey() {
        System.out.println("getReferenceKey");
        StrLen instance = new StrLen();
        String expResult = "__strLen";
        String result = instance.getReferenceKey();
        assertEquals(expResult, result);
    }

    /**
     * Test of getArgumentDesc method, of class StrLen.
     */
    @Test
    public void testGetArgumentDesc() {
        System.out.println("getArgumentDesc");
        StrLen instance = new StrLen();
        List result = instance.getArgumentDesc();
        assertEquals(2, result.size());
    }

}