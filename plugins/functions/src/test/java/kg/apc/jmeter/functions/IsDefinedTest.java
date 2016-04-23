package kg.apc.jmeter.functions;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterContextService;
import org.junit.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    @Test
    public void testExecute() throws Exception {
        System.out.println("execute 1");
        SampleResult previousResult = null;
        Sampler currentSampler = null;
        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("var_to_be_defined1"));
        IsDefined instance = new IsDefined();
        instance.setParameters(parameters);
        String expResult = "0";
        String result = instance.execute(previousResult, currentSampler);
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testExecute_1() throws Exception {
        System.out.println("execute 1");
        SampleResult previousResult = null;
        Sampler currentSampler = null;

        JMeterContextService.getContext().getVariables().put("var_to_be_defined2", "");
        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("var_to_be_defined2"));
        IsDefined instance = new IsDefined();
        instance.setParameters(parameters);
        String expResult = "1";
        String result = instance.execute(previousResult, currentSampler);
        Assert.assertEquals(expResult, result);
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
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of getArgumentDesc method, of class IsDefined.
     */
    @Test
    public void testGetArgumentDesc() {
        System.out.println("getArgumentDesc");
        IsDefined instance = new IsDefined();
        List result = instance.getArgumentDesc();
        Assert.assertEquals(1, result.size());
    }
}