package kg.apc.jmeter.modifiers;

import org.apache.jmeter.threads.JMeterContextService;
import kg.apc.jmeter.util.TestJMeterUtils;
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
public class RawRequestSourcePreProcessorTest {

    public RawRequestSourcePreProcessorTest() {
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
     * Test of process method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testProcess() {
        System.out.println("process");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        for (int n=1; n<1000; n++)
        {
            System.out.println(n);
            instance.process();
            assertTrue(JMeterContextService.getContext().getVariables().get(instance.getVarName()).length()>0);
        }
    }

}