package kg.apc.jmeter.functions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author undera
 */
public class FifoPutTest {
    
    public FifoPutTest() {
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
     * Test of execute method, of class FifoPut.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        SampleResult previousResult = null;
        Sampler currentSampler = null;
        FifoPut instance = new FifoPut();
        String expResult = "test";
        LinkedList<CompoundVariable> list = new LinkedList<CompoundVariable>();
        list.add(new CompoundVariable("test"));
        list.add(new CompoundVariable("test"));
        instance.setParameters(list);
        String result = instance.execute(previousResult, currentSampler);
        assertEquals(expResult, result);
    }

    /**
     * Test of setParameters method, of class FifoPut.
     */
    @Test
    public void testSetParameters() throws Exception {
        System.out.println("setParameters");
         LinkedList<CompoundVariable> list = new LinkedList<CompoundVariable>();
        list.add(new CompoundVariable("test"));
        list.add(new CompoundVariable("test"));
        FifoPut instance = new FifoPut();
        instance.setParameters(list);
    }

    /**
     * Test of getReferenceKey method, of class FifoPut.
     */
    @Test
    public void testGetReferenceKey() {
        System.out.println("getReferenceKey");
        FifoPut instance = new FifoPut();
        String expResult = "__fifoPut";
        String result = instance.getReferenceKey();
        assertEquals(expResult, result);
    }

    /**
     * Test of getArgumentDesc method, of class FifoPut.
     */
    @Test
    public void testGetArgumentDesc() {
        System.out.println("getArgumentDesc");
        FifoPut instance = new FifoPut();
        List result = instance.getArgumentDesc();
        assertNotNull(result);
    }
}
