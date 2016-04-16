package kg.apc.jmeter.functions;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.junit.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IfTest {

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
        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("test string"));
        parameters.add(new CompoundVariable("5"));
        parameters.add(new CompoundVariable("8"));
        parameters.add(new CompoundVariable("var"));
        If instance = new If();
        instance.setParameters(parameters);
        String expResult = "var";
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
        parameters.add(new CompoundVariable("2"));
        If instance = new If();
        instance.setParameters(parameters);
        instance.execute(null, null);
    }

    /**
     * Test of getReferenceKey method, of class Substring.
     */
    @Test
    public void testGetReferenceKey() {
        System.out.println("getReferenceKey");
        If instance = new If();
        String expResult = "__if";
        String result = instance.getReferenceKey();
        Assert.assertEquals(expResult, result);

    }

    /**
     * Test of getArgumentDesc method, of class Substring.
     */
    @Test
    public void testGetArgumentDesc() {
        System.out.println("getArgumentDesc");
        If instance = new If();
        List result = instance.getArgumentDesc();
        Assert.assertEquals(5, result.size());
    }
}
