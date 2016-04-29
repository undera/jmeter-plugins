package kg.apc.jmeter.jmxmon;

import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.testelement.property.JMeterProperty;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.util.Hashtable;

class TestJMXMonCollector extends JMXMonCollector {
    private final JMXMonTest jmxMonTest;

    public TestJMXMonCollector(JMXMonTest jmxMonTest) {
        this.jmxMonTest = jmxMonTest;
    }

    @Override
    public void run() {
        try {
            // Override run to controll the entire flow from the test
            Thread.sleep(24 * 60 * 60 * 1000);
        } catch (InterruptedException ex) {
            synchronized (jmxMonTest) {
                jmxMonTest.setThreadStoped(true);
                jmxMonTest.notifyAll();
            }
        }
    }
    
    @Override
    protected void initiateConnector(Hashtable attributes, JMeterProperty jmxUrl, String name, boolean delta, String objectName, String attribute, String key, boolean canRetry) throws IOException {
        MBeanServerConnection conn = new MBeanServerConnectionEmul(jmxMonTest.getQueryResults());
        jmxMonSamplers.add(new JMXMonSampler(conn, null, jmxUrl, name, objectName, attribute, key, delta));
    }

    @Override
    public void jmxMonSampleOccurred(SampleEvent event) {
        super.sampleOccurred(event);
        double value = JMXMonSampleResult.getValue(event.getResult());
        jmxMonTest.getLatestSamples().put(event.getResult().getSampleLabel(), value);
    }

    @Override
    public void testEnded() {
        super.testEnded(); //To change body of generated methods, choose Tools | Templates.
    }


}
