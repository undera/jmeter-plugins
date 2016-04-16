package kg.apc.jmeter.functions;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.junit.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("test string"));
        parameters.add(new CompoundVariable("5"));
        parameters.add(new CompoundVariable("8"));
        parameters.add(new CompoundVariable("var"));
        Substring instance = new Substring();
        instance.setParameters(parameters);
        String expResult = "str";
        String result = instance.execute(null, null);
        Assert.assertEquals(expResult, result);
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
        instance.execute(null, null);
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
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of getArgumentDesc method, of class Substring.
     */
    @Test
    public void testGetArgumentDesc() {
        System.out.println("getArgumentDesc");
        Substring instance = new Substring();
        List result = instance.getArgumentDesc();
        Assert.assertEquals(4, result.size());
    }
}