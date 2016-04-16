/*
 * Copyright 2013 undera.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kg.apc.jmeter.jmxmon;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JMXMonCollectorTest {

    public JMXMonCollectorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        TestJMeterUtils.createJmeterEnv();
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
     * Test of setData method, of class JMXMonCollector.
     */
    @Test
    public void testSetData() {
        System.out.println("setData");
        CollectionProperty rows = new CollectionProperty();
        JMXMonCollector instance = new JMXMonCollector();
        instance.setData(rows);
    }

    /**
     * Test of getSamplerSettings method, of class JMXMonCollector.
     */
    @Test
    public void testGetSamplerSettings() {
        System.out.println("getSamplerSettings");
        JMXMonCollector instance = new JMXMonCollector();
        JMeterProperty result = instance.getSamplerSettings();
    }

    /**
     * Test of run method, of class JMXMonCollector.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        JMXMonCollector instance = new JMXMonCollector();
        // instance.run(); avoiding infinite loop
    }

    /**
     * Test of testStarted method, of class JMXMonCollector.
     */
    @Test
    public void testTestStarted() {
        System.out.println("testStarted");
        String host = "";
        JMXMonCollector instance = new JMXMonCollector();
        instance.testStarted(host);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of testEnded method, of class JMXMonCollector.
     */
    @Test
    public void testTestEnded() {
        System.out.println("testEnded");
        String host = "";
        JMXMonCollector instance = new JMXMonCollector();
        instance.testEnded(host);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of processConnectors method, of class JMXMonCollector.
     */
    @Test
    public void testProcessConnectors() {
        System.out.println("processConnectors");
        JMXMonCollector instance = new JMXMonCollector();
        instance.processConnectors();
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of sampleOccurred method, of class JMXMonCollector.
     */
    @Test
    public void testSampleOccurred() {
        System.out.println("sampleOccurred");
        SampleEvent event = null;
        JMXMonCollector instance = new JMXMonCollector();
        instance.sampleOccurred(event);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of JMXMonSampleOccurred method, of class JMXMonCollector.
     */
    @Test
    public void testJMXMonSampleOccurred() {
        System.out.println("JMXMonSampleOccurred");
        SampleEvent event = new SampleEvent(new SampleResult(), "test");
        JMXMonCollector instance = new JMXMonCollector();
        instance.jmxMonSampleOccurred(event);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of generateSample method, of class JMXMonCollector.
     */
    @Test
    public void testGenerateSample() {
        System.out.println("generateSample");
        double value = 0.0;
        String label = "";
        JMXMonCollector instance = new JMXMonCollector();
        instance.generateSample(value, label);
        // TODO review the generated test code and remove the default call to fail.

    }
}
