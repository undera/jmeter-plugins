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
public class IsDefinedTest {

    public IsDefinedTest() {
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
     * Test of execute method, of class IsDefined.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute 1");
        SampleResult previousResult = null;
        Sampler currentSampler = null;
        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("var"));
        IsDefined instance = new IsDefined();
        instance.setParameters(parameters);
        String expResult = "0";
        String result = instance.execute(previousResult, currentSampler);
        assertEquals(expResult, result);
    }

    @Test
    public void testExecute_1() throws Exception {
        System.out.println("execute 1");
        SampleResult previousResult = null;
        Sampler currentSampler = null;

        JMeterContextService.getContext().getVariables().put("var", "");
        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("var"));
        IsDefined instance = new IsDefined();
        instance.setParameters(parameters);
        String expResult = "1";
        String result = instance.execute(previousResult, currentSampler);
        assertEquals(expResult, result);
    }

    /**
     * Test of setParameters method, of class IsDefined.
     */
    @Test
    public void testSetParameters() throws Exception {
        System.out.println("setParameters");
        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("test string"));
        IsDefined instance = new IsDefined();
        instance.setParameters(parameters);
    }

    /**
     * Test of getReferenceKey method, of class IsDefined.
     */
    @Test
    public void testGetReferenceKey() {
        System.out.println("getReferenceKey");
        IsDefined instance = new IsDefined();
        String expResult = "__isDefined";
        String result = instance.getReferenceKey();
        assertEquals(expResult, result);
    }

    /**
     * Test of getArgumentDesc method, of class IsDefined.
     */
    @Test
    public void testGetArgumentDesc() {
        System.out.println("getArgumentDesc");
        IsDefined instance = new IsDefined();
        List result = instance.getArgumentDesc();
        assertEquals(1, result.size());
    }
}