package kg.apc.jmeter.functions;

import kg.apc.emulators.TestJMeterUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterContextService;
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
public class LowerCaseTest {

    public LowerCaseTest() {
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
     * Test of execute method, of class LowerCase.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("ABC"));
        parameters.add(new CompoundVariable("var"));

        SampleResult previousResult = null;
        Sampler currentSampler = null;
        LowerCase instance = new LowerCase();
        instance.setParameters(parameters);
        String expResult = "abc";
        String result = instance.execute(previousResult, currentSampler);
        assertEquals(expResult, result);
        assertEquals(expResult, JMeterContextService.getContext().getVariables().get("var"));
    }

    /**
     * Test of setParameters method, of class LowerCase.
     */
    @Test
    public void testSetParameters() throws Exception {
        System.out.println("setParameters");
        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("abc"));
        parameters.add(new CompoundVariable("var"));
        LowerCase instance = new LowerCase();
        instance.setParameters(parameters);
    }

    /**
     * Test of getReferenceKey method, of class LowerCase.
     */
    @Test
    public void testGetReferenceKey() {
        System.out.println("getReferenceKey");
        LowerCase instance = new LowerCase();
        String expResult = "__lowercase";
        String result = instance.getReferenceKey();
        assertEquals(expResult, result);
    }

    /**
     * Test of getArgumentDesc method, of class LowerCase.
     */
    @Test
    public void testGetArgumentDesc() {
        System.out.println("getArgumentDesc");
        LowerCase instance = new LowerCase();
        List result = instance.getArgumentDesc();
        assertEquals(2, result.size());
    }
}
