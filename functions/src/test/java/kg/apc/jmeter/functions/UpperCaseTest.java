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

public class UpperCaseTest {

    public UpperCaseTest() {
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
     * Test of execute method, of class UpperCase.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        Collection<CompoundVariable> parameters = new ArrayList<>();
        parameters.add(new CompoundVariable("abc"));
        parameters.add(new CompoundVariable("var"));

        SampleResult previousResult = null;
        Sampler currentSampler = null;
        UpperCase instance = new UpperCase();
        instance.setParameters(parameters);
        String expResult = "ABC";
        String result = instance.execute(null, null);
        Assert.assertEquals(expResult, result);
        Assert.assertEquals(expResult, JMeterContextService.getContext().getVariables().get("var"));
    }

    /**
     * Test of setParameters method, of class UpperCase.
     */
    @Test
    public void testSetParameters() throws Exception {
        System.out.println("setParameters");
        Collection<CompoundVariable> parameters = new ArrayList<>();
        parameters.add(new CompoundVariable("abc"));
        parameters.add(new CompoundVariable("var"));
        UpperCase instance = new UpperCase();
        instance.setParameters(parameters);
    }

    /**
     * Test of getReferenceKey method, of class UpperCase.
     */
    @Test
    public void testGetReferenceKey() {
        System.out.println("getReferenceKey");
        UpperCase instance = new UpperCase();
        String expResult = "__uppercase";
        String result = instance.getReferenceKey();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of getArgumentDesc method, of class UpperCase.
     */
    @Test
    public void testGetArgumentDesc() {
        System.out.println("getArgumentDesc");
        UpperCase instance = new UpperCase();
        List result = instance.getArgumentDesc();
        Assert.assertEquals(2, result.size());
    }
}
