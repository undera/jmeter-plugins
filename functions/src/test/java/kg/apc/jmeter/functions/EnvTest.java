package kg.apc.jmeter.functions;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterContextService;
import org.junit.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class EnvTest {

    private static String key, value;

    public EnvTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
        Map<String, String> env = System.getenv();
        if (!env.isEmpty()) {
            key = env.keySet().iterator().next();
            value = env.get(key);
        }
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
     * Test of execute method, of class Env.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute 1");
        SampleResult previousResult = null;
        Sampler currentSampler = null;
        Assert.assertNull(JMeterContextService.getContext().getVariables().get("toto"));
        Collection<CompoundVariable> parameters = new ArrayList<>();
        parameters.add(new CompoundVariable(key));
        Env instance = new Env();
        instance.setParameters(parameters);
        String result = instance.execute(null, null);
        Assert.assertEquals(value, result);
        Assert.assertNull(JMeterContextService.getContext().getVariables().get("toto"));
    }

    @Test
    public void testExecute_1() throws Exception {
        System.out.println("execute 1");
        SampleResult previousResult = null;
        Sampler currentSampler = null;

        Assert.assertNull(JMeterContextService.getContext().getVariables().get("toto"));
        Collection<CompoundVariable> parameters = new ArrayList<>();
        parameters.add(new CompoundVariable(key));
        parameters.add(new CompoundVariable("toto"));
        Env instance = new Env();
        instance.setParameters(parameters);
        String result = instance.execute(null, null);
        Assert.assertEquals(value, result);
        Assert.assertNotNull(JMeterContextService.getContext().getVariables().remove("toto"));
    }

    @Test
    public void testExecute_2() throws Exception {
        System.out.println("execute 1");
        SampleResult previousResult = null;
        Sampler currentSampler = null;
        Assert.assertNull(JMeterContextService.getContext().getVariables().get("toto"));
        Collection<CompoundVariable> parameters = new ArrayList<>();
        String overrideKey = key + "testExecute_2";
        parameters.add(new CompoundVariable(overrideKey));
        Env instance = new Env();
        instance.setParameters(parameters);
        String result = instance.execute(null, null);
        Assert.assertEquals(overrideKey, result);
        Assert.assertNull(JMeterContextService.getContext().getVariables().get("toto"));
    }

    @Test
    public void testExecute_3() throws Exception {
        System.out.println("execute 1");
        SampleResult previousResult = null;
        Sampler currentSampler = null;
        Assert.assertNull(JMeterContextService.getContext().getVariables().get("toto"));
        Collection<CompoundVariable> parameters = new ArrayList<>();
        String overrideKey = key + "testExecute_3";
        String defaultValue = "defaultValue";
        parameters.add(new CompoundVariable(overrideKey));
        parameters.add(new CompoundVariable(""));
        parameters.add(new CompoundVariable(defaultValue));
        Env instance = new Env();
        instance.setParameters(parameters);
        String result = instance.execute(null, null);
        Assert.assertEquals(defaultValue, result);
        Assert.assertNull(JMeterContextService.getContext().getVariables().get("toto"));
    }

    /**
     * Test of setParameters method, of class Env.
     */
    @Test
    public void testSetParameters() throws Exception {
        System.out.println("setParameters");
        Collection<CompoundVariable> parameters = new ArrayList<>();
        parameters.add(new CompoundVariable(key));
        Env instance = new Env();
        instance.setParameters(parameters);

        // second parameter
        parameters.add(new CompoundVariable("save_variable"));
        instance.setParameters(parameters);

        // third parameter
        parameters.add(new CompoundVariable("default_value"));
        instance.setParameters(parameters);
    }

    @Test(expected = InvalidVariableException.class)
    public void testSetParametersException() throws Exception {
        System.out.println("setParameters");
        Collection<CompoundVariable> parameters = new ArrayList<>();
        Env instance = new Env();
        instance.setParameters(parameters);
    }

    @Test(expected = InvalidVariableException.class)
    public void testSetParametersException2() throws Exception {
        System.out.println("setParameters");
        Collection<CompoundVariable> parameters = new ArrayList<>();
        parameters.add(new CompoundVariable(key));
        parameters.add(new CompoundVariable("save_variable"));
        parameters.add(new CompoundVariable("default_value"));
        parameters.add(new CompoundVariable("Error"));
        Env instance = new Env();
        instance.setParameters(parameters);
    }

    /**
     * Test of getReferenceKey method, of class Env.
     */
    @Test
    public void testGetReferenceKey() {
        System.out.println("getReferenceKey");
        Env instance = new Env();
        String expResult = "__env";
        String result = instance.getReferenceKey();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of getArgumentDesc method, of class Env.
     */
    @Test
    public void testGetArgumentDesc() {
        System.out.println("getArgumentDesc");
        Env instance = new Env();
        List<String> result = instance.getArgumentDesc();
        Assert.assertEquals(3, result.size());
    }
}