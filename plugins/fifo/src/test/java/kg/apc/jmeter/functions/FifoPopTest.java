package kg.apc.jmeter.functions;

import kg.apc.jmeter.modifiers.FifoMap;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.junit.*;

import java.util.LinkedList;
import java.util.List;

public class FifoPopTest {

    public FifoPopTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
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
     * Test of execute method, of class FifoPop.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        SampleResult previousResult = null;
        Sampler currentSampler = null;
        FifoPop instance = new FifoPop();
        LinkedList<CompoundVariable> list = new LinkedList<>();
        list.add(new CompoundVariable("FifoPoptest"));
        list.add(new CompoundVariable("FifoPoptest"));
        instance.setParameters(list);
        FifoMap.getInstance().put("FifoPoptest", "FifoPoptest");
        String expResult = "FifoPoptest";
        String result = instance.execute(null, null);
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of setParameters method, of class FifoPop.
     */
    @Test
    public void testSetParameters() throws Exception {
        System.out.println("setParameters");
        LinkedList<CompoundVariable> list = new LinkedList<>();
        list.add(new CompoundVariable("FifoPoptest"));
        list.add(new CompoundVariable("FifoPoptest"));
        FifoPop instance = new FifoPop();
        instance.setParameters(list);
    }

    /**
     * Test of getReferenceKey method, of class FifoPop.
     */
    @Test
    public void testGetReferenceKey() {
        System.out.println("getReferenceKey");
        FifoPop instance = new FifoPop();
        String expResult = "__fifoPop";
        String result = instance.getReferenceKey();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of getArgumentDesc method, of class FifoPop.
     */
    @Test
    public void testGetArgumentDesc() {
        System.out.println("getArgumentDesc");
        FifoPop instance = new FifoPop();
        List result = instance.getArgumentDesc();
        Assert.assertNotNull(result);
    }
}
