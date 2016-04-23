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

public class MD5Test {

    public MD5Test() {
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
     * Test of execute method, of class MD5.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("public void testExecute() throws Exception {}"));
        parameters.add(new CompoundVariable("var"));

        SampleResult previousResult = null;
        Sampler currentSampler = null;
        MD5 instance = new MD5();
        instance.setParameters(parameters);
        String expResult = "cb5b1ca8504cb5a7772f219109e05ccf";
        String result = instance.execute(previousResult, currentSampler);
        Assert.assertEquals(expResult, result);
        Assert.assertEquals(expResult, JMeterContextService.getContext().getVariables().get("var"));
    }

    /**
     * Test of setParameters method, of class MD5.
     */
    @Test
    public void testSetParameters() throws Exception {
        System.out.println("setParameters");
        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("abc"));
        parameters.add(new CompoundVariable("var"));
        MD5 instance = new MD5();
        instance.setParameters(parameters);
    }

    /**
     * Test of getReferenceKey method, of class MD5.
     */
    @Test
    public void testGetReferenceKey() {
        System.out.println("getReferenceKey");
        MD5 instance = new MD5();
        String expResult = "__MD5";
        String result = instance.getReferenceKey();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of getArgumentDesc method, of class MD5.
     */
    @Test
    public void testGetArgumentDesc() {
        System.out.println("getArgumentDesc");
        MD5 instance = new MD5();
        List result = instance.getArgumentDesc();
        Assert.assertEquals(2, result.size());
    }
}
