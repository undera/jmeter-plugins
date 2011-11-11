/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.perfmon;

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
public class PerfMonSampleGeneratorTest {
    
    public PerfMonSampleGeneratorTest() {
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
     * Test of generate2Samples method, of class PerfMonSampleGenerator.
     */
    @Test
    public void testGenerate2Samples_4args() {
        System.out.println("generate2Samples");
        long[] netIO = null;
        String string = "";
        String string0 = "";
        double d = 0.0;
        PerfMonSampleGenerator instance = new PerfMonSampleGeneratorImpl();
        instance.generate2Samples(netIO, string, string0, d);
    }

    /**
     * Test of generate2Samples method, of class PerfMonSampleGenerator.
     */
    @Test
    public void testGenerate2Samples_3args() {
        System.out.println("generate2Samples");
        long[] disksIO = null;
        String string = "";
        String string0 = "";
        PerfMonSampleGenerator instance = new PerfMonSampleGeneratorImpl();
        instance.generate2Samples(disksIO, string, string0);
    }

    /**
     * Test of generateSample method, of class PerfMonSampleGenerator.
     */
    @Test
    public void testGenerateSample() {
        System.out.println("generateSample");
        double d = 0.0;
        String string = "";
        PerfMonSampleGenerator instance = new PerfMonSampleGeneratorImpl();
        instance.generateSample(d, string);
    }

    /**
     * Test of generateErrorSample method, of class PerfMonSampleGenerator.
     */
    @Test
    public void testGenerateErrorSample() {
        System.out.println("generateErrorSample");
        String label = "";
        String errorMsg = "";
        PerfMonSampleGenerator instance = new PerfMonSampleGeneratorImpl();
        instance.generateErrorSample(label, errorMsg);
    }

    public class PerfMonSampleGeneratorImpl implements PerfMonSampleGenerator {

        public void generate2Samples(long[] netIO, String string, String string0, double d) {
        }

        public void generate2Samples(long[] disksIO, String string, String string0) {
        }

        public void generateSample(double d, String string) {
        }

        public void generateErrorSample(String label, String errorMsg) {
        }
    }
}
