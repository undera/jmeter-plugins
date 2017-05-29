package kg.apc.jmeter.vizualizers;

import org.junit.Test;
import static org.junit.Assert.*;

public class MonitoringSampleGeneratorTest {

    @Test
    public void testGenerateSample() {
        System.out.println("generateSample");
        double d = 0.0;
        String string = "Test";
        MonitoringSampleGenerator instance = new MonitoringSampleGeneratorImpl();
        instance.generateSample(d, string);
        
        String result1 = ((MonitoringSampleGeneratorImpl)instance).metric;
        double result2 = ((MonitoringSampleGeneratorImpl)instance).value;
        assertEquals(string, result1);
        assertEquals(d, result2, 0.0);
    }

    public class MonitoringSampleGeneratorImpl implements MonitoringSampleGenerator {
        protected String metric;
        protected double value;

        public void generateSample(double d, String string) {
            this.metric = string;
            this.value = d;
        }
    }
}
