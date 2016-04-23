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
package kg.apc.jmeter.dbmon;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.junit.*;

public class DbMonCollectorTest {

    public DbMonCollectorTest() {
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
     * Test of setData method, of class DbMonCollector.
     */
    @Test
    public void testSetData() {
        System.out.println("setData");
        CollectionProperty rows = new CollectionProperty();
        DbMonCollector instance = new DbMonCollector();
        instance.setData(rows);
    }

    /**
     * Test of getSamplerSettings method, of class DbMonCollector.
     */
    @Test
    public void testGetSamplerSettings() {
        System.out.println("getSamplerSettings");
        DbMonCollector instance = new DbMonCollector();
        JMeterProperty result = instance.getSamplerSettings();
    }

    /**
     * Test of run method, of class DbMonCollector.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        DbMonCollector instance = new DbMonCollector();
        // instance.run(); avoiding infinite loop
    }

    /**
     * Test of testStarted method, of class DbMonCollector.

    public void testTestStarted(String host) {
        System.out.println("Begin testStarted, host ="+ host);
        DbMonCollector instance = new DbMonCollector();
        instance.testStarted(host);
        // TODO review the generated test code and remove the default call to fail.

    }
*/
    public void testTestStarted() {
        System.out.println("testStarted");
        DbMonCollector instance = new DbMonCollector();
        String host = "";

        instance.testStarted(host);
        // TODO review the generated test code and remove the default call to fail.

    }
    /**
     * Test of testEnded method, of class DbMonCollector.
     */
    @Test
    public void testTestEnded() {
        System.out.println("testEnded");
        String host = "";
        DbMonCollector instance = new DbMonCollector();
        instance.testEnded(host);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of processConnectors method, of class DbMonCollector.
     */
    @Test
    public void testProcessConnectors() {
        System.out.println("processConnectors");
        DbMonCollector instance = new DbMonCollector();
        instance.processConnectors();
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of sampleOccurred method, of class DbMonCollector.
     */
    @Test
    public void testSampleOccurred() {
        System.out.println("sampleOccurred");
        SampleEvent event = null;
        DbMonCollector instance = new DbMonCollector();
        instance.sampleOccurred(event);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of dbMonSampleOccurred method, of class DbMonCollector.
     */
    @Test
    public void testDbMonSampleOccurred() {
        System.out.println("dbMonSampleOccurred");
        SampleEvent event = new SampleEvent(new SampleResult(), "test");
        DbMonCollector instance = new DbMonCollector();
        instance.dbMonSampleOccurred(event);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of generateSample method, of class DbMonCollector.
     */
    @Test
    public void testGenerateSample() {
        System.out.println("generateSample");
        double value = 0.0;
        String label = "";
        DbMonCollector instance = new DbMonCollector();
        instance.generateSample(value, label);
        // TODO review the generated test code and remove the default call to fail.

    }
}
