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
package kg.apc.jmeter.vizualizers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MonitoringSamplerTest {
    
    public MonitoringSamplerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
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
    
    @Test
    public void testGenerateSamples() {
        System.out.println("generateSamples");
        MonitoringSampleGenerator collector = new MonitoringResultsCollector();

        MonitoringSampler instance = new MonitoringSamplerImpl("TestSample", 1.234);
        instance.generateSamples(collector);
    }
}
