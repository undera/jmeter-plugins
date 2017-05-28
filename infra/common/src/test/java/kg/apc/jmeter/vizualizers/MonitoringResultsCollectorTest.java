package kg.apc.jmeter.vizualizers;

import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;

import static org.junit.Assert.*;
import org.junit.Test;

public class MonitoringResultsCollectorTest {
    
    /**
     * Test of setData method, of class MonitoringResultsCollector.
     */
    @Test
    public void testSetData() {
        System.out.println("setData");
        CollectionProperty rows = new CollectionProperty();
        MonitoringResultsCollector instance = new MonitoringResultsCollector();
        instance.setData(rows);
        JMeterProperty result = instance.getProperty(MonitoringResultsCollector.DATA_PROPERTY);
        assertNotNull(result);
    }

    /**
     * Test of getSamplerSettings method, of class MonitoringResultsCollector.
     */
    @Test
    public void testGetSamplerSettings() {
        System.out.println("getSamplerSettings");
        MonitoringResultsCollector instance = new MonitoringResultsCollector();
        JMeterProperty result = instance.getSamplerSettings();
        assertNotNull(result);
    }

    /**
     * Test of run method, of class MonitoringResultsCollector.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        MonitoringResultsCollector instance = new MonitoringResultsCollector();
        Thread t = new Thread(instance);
        t.start(); 
        assertTrue(t.isAlive());
        instance.testEnded(); // stop thread
    }

    /**
     * Test of testStarted method, of class MonitoringResultsCollector.
     */
    @Test
    public void testTestStarted() {
        System.out.println("testStarted");
        String host = "";
        MonitoringResultsCollector instance = new MonitoringResultsCollector();
        instance.testStarted(host);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of testEnded method, of class MonitoringResultsCollector.
     */
    @Test
    public void testTestEnded() {
        System.out.println("testEnded");
        String host = "";
        MonitoringResultsCollector instance = new MonitoringResultsCollector();
        instance.testEnded(host);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of processConnectors method, of class MonitoringResultsCollector.
     */
    @Test
    public void testProcessConnectors() {
        System.out.println("processConnectors");
        MonitoringResultsCollector instance = new MonitoringResultsCollector();
        instance.processConnectors();
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of sampleOccurred method, of class MonitoringResultsCollector.
     */
    @Test
    public void testSampleOccurred() {
        System.out.println("sampleOccurred");
        SampleEvent event = null;
        MonitoringResultsCollector instance = new MonitoringResultsCollector();
        instance.sampleOccurred(event);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of monitoringSampleOccurred method, of class MonitoringResultsCollector.
     */
    @Test
    public void testMonitoringSampleOccurred() {
        System.out.println("monitoringSampleOccurred");
        SampleEvent event = new SampleEvent(new SampleResult(), "test");
        MonitoringResultsCollector instance = new MonitoringResultsCollector();
        instance.monitoringSampleOccurred(event);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of generateSample method, of class MonitoringResultsCollector.
     */
    @Test
    public void testGenerateSample() {
        System.out.println("generateSample");
        double value = 0.0;
        String label = "";
        MonitoringResultsCollector instance = new MonitoringResultsCollector();
        instance.generateSample(value, label);
        // TODO review the generated test code and remove the default call to fail.

    }
}
