package kg.apc.jmeter.vizualizers;

import org.junit.Test;
import static org.junit.Assert.*;

public class MonitoringSamplerTest {

    public class MonitoringSamplerImpl implements MonitoringSampler {
        
        private String metric;
        private double value;
        
        public MonitoringSamplerImpl(String metric, double value) {
            this.metric = metric;
            this.value = value;
        }
        
        public void generateSamples(MonitoringSampleGenerator collector) {
            collector.generateSample(value, metric);
        }
    }

    public class MonitoringSampleGeneratorImpl implements MonitoringSampleGenerator {
        protected String metric;
        protected double value;

        public void generateSample(double d, String string) {
            this.metric = string;
            this.value = d;
        }
    }    
    @Test
    public void testGenerateSamples() {
        System.out.println("generateSamples");
        MonitoringSampleGenerator collector = new MonitoringSampleGeneratorImpl();

        String expResult1 = "TestSample";
        double expResult2 = 1.234;
        MonitoringSampler instance = new MonitoringSamplerImpl(expResult1, expResult2);
        instance.generateSamples(collector);
        
        String result1 = ((MonitoringSampleGeneratorImpl)collector).metric;
        double result2 = ((MonitoringSampleGeneratorImpl)collector).value;
        assertEquals(expResult1, result1);
        assertEquals(expResult2, result2, 0.0);
    }
}
