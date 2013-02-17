package kg.apc.jmeter.functions;

import java.util.LinkedList;
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
public class FifoSizeTest {
    
    public FifoSizeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of execute method, of class FifoSize.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        SampleResult previousResult = null;
        Sampler currentSampler = null;
        LinkedList<CompoundVariable> list = new LinkedList<CompoundVariable>();
        list.add(new CompoundVariable("test"));
        list.add(new CompoundVariable("test"));
        FifoSize instance = new FifoSize();
        instance.setParameters(list);
        String expResult = "0";
        String result = instance.execute(previousResult, currentSampler);
        assertEquals(expResult, result);
    }

    /**
     * Test of setParameters method, of class FifoSize.
     */
    @Test
    public void testSetParameters() throws Exception {
        System.out.println("setParameters");
        LinkedList<CompoundVariable> list = new LinkedList<CompoundVariable>();
        list.add(new CompoundVariable("test"));
        list.add(new CompoundVariable("test"));
        FifoSize instance = new FifoSize();
        instance.setParameters(list);
    }

    /**
     * Test of getReferenceKey method, of class FifoSize.
     */
    @Test
    public void testGetReferenceKey() {
        System.out.println("getReferenceKey");
        FifoSize instance = new FifoSize();
        String expResult = "__fifoSize";
        String result = instance.getReferenceKey();
        assertEquals(expResult, result);
    }

    /**
     * Test of getArgumentDesc method, of class FifoSize.
     */
    @Test
    public void testGetArgumentDesc() {
        System.out.println("getArgumentDesc");
        FifoSize instance = new FifoSize();
        List result = instance.getArgumentDesc();
        assertEquals(2, result.size());
    }
}
