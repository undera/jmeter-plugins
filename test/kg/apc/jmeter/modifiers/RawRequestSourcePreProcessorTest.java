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
        System.out.println("process zeroterm looped");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setFileName("/home/undera/bs0.ammo");
        instance.setRewindOnEOF(true);
        for (int n=1; n<100; n++)
        {
            instance.process();
            String result = JMeterContextService.getContext().getVariables().get(instance.getVarName());
            //System.out.println(n+" "+result);
            assertTrue(result.length()>0);
        }
    }

    /**
     * Test of process method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testProcess_zeroterminated() {
        System.out.println("zeroterm");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setFileName("/home/undera/bs0.ammo");
        for (int n=1; n<200000; n++)
        {
            instance.process();
            String result = JMeterContextService.getContext().getVariables().get(instance.getVarName());
            //System.out.println(n);
            assertTrue(result.length()>0 || n==154286);
        }
    }

    /**
     * Test of process method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testProcess_nonzeroterminated() {
        System.out.println("nonzeroterm");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setFileName("/home/undera/bs.ammo");
        for (int n=1; n<200000; n++)
        {
            instance.process();
            String result = JMeterContextService.getContext().getVariables().get(instance.getVarName());
            //System.out.println(n);
            assertTrue(result.length()>0);
        }
    }

    /**
     * Test of process method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testProcess_broken() {
        System.out.println("broken");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setFileName("/home/undera/0-bs.ammo");
        boolean ok=false;
        for (int n=1; n<200000; n++)
        {
            instance.process();
            String result = JMeterContextService.getContext().getVariables().get(instance.getVarName());
            if(result.length()<1)
            {
               assertEquals(154286, n);
               ok=true;
               break;
            }
        }
        assertTrue(ok);
    }

   @Test
    public void testProcess_file_not_found() {
        System.out.println("file_not_found");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setFileName("/nofilefortest");
            instance.process();
            String result = JMeterContextService.getContext().getVariables().get(instance.getVarName());
            assertTrue(result.length()==0);
    }

 }