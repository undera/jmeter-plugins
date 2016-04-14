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
package kg.apc.jmeter.perfmon;

import java.io.IOException;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PerfMonCollectorTest {

    public PerfMonCollectorTest() {
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
     * Test of setData method, of class PerfMonCollector.
     */
    @Test
    public void testSetData() {
        System.out.println("setData");
        CollectionProperty rows = new CollectionProperty();
        PerfMonCollector instance = new PerfMonCollector();
        instance.setData(rows);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getMetricSettings method, of class PerfMonCollector.
     */
    @Test
    public void testGetMetricSettings() {
        System.out.println("getMetricSettings");
        PerfMonCollector instance = new PerfMonCollector();
        JMeterProperty expResult = null;
        JMeterProperty result = instance.getMetricSettings();
    }

    /**
     * Test of sampleOccurred method, of class PerfMonCollector.
     */
    @Test
    public void testSampleOccurred() {
        System.out.println("sampleOccurred");
        SampleEvent event = new SampleEvent(new PerfMonSampleResult(), "test");
        PerfMonCollector instance = new PerfMonCollector();
        instance.sampleOccurred(event);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of run method, of class PerfMonCollector.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        PerfMonCollector instance = new PerfMonCollector();
        //        instance.run(); don't do it, because it will loop eternally   
    }

    /**
     * Test of testStarted method, of class PerfMonCollector.
     */
    @Test
    public void testTestStarted() {
        System.out.println("testStarted");
        String host = "";
        PerfMonCollector instance = new PerfMonCollector();
        instance.testStarted(host);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of testEnded method, of class PerfMonCollector.
     */
    @Test
    public void testTestEnded() {
        System.out.println("testEnded");
        String host = "";
        PerfMonCollector instance = new PerfMonCollector();
        instance.testEnded(host);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getConnector method, of class PerfMonCollector.
     */
    @Test
    public void testGetConnector() throws Exception {
        System.out.println("getConnector");
        String host = "";
        int port = 0;
        PerfMonCollector instance = new PerfMonCollector();
        PerfMonAgentConnector expResult = null;
        try {
            PerfMonAgentConnector result = instance.getConnector(host, port);
            fail();
        } catch (IOException e) {
        }
    }

    /**
     * Test of generateSample method, of class PerfMonCollector.
     */
    @Test
    public void testGenerateSample() {
        System.out.println("generateSample");
        double value = 0.0;
        String label = "";
        PerfMonCollector instance = new PerfMonCollector();
        instance.generateSample(value, label);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of generateErrorSample method, of class PerfMonCollector.
     */
    @Test
    public void testGenerateErrorSample() {
        System.out.println("generateErrorSample");
        String label = "";
        String errorMsg = "";
        PerfMonCollector instance = new PerfMonCollector();
        instance.generateErrorSample(label, errorMsg);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of generate2Samples method, of class PerfMonCollector.
     */
    @Test
    public void testGenerate2Samples_3args() {
        System.out.println("generate2Samples");
        long[] values = {1L, 2L};
        String label1 = "";
        String label2 = "";
        PerfMonCollector instance = new PerfMonCollector();
        instance.generate2Samples(values, label1, label2);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of generate2Samples method, of class PerfMonCollector.
     */
    @Test
    public void testGenerate2Samples_4args() {
        System.out.println("generate2Samples");
        long[] values = {1L, 2L};
        String label1 = "";
        String label2 = "";
        double dividingFactor = 0.0;
        PerfMonCollector instance = new PerfMonCollector();
        instance.generate2Samples(values, label1, label2, dividingFactor);
        // TODO review the generated test code and remove the default call to fail.

    }
}
