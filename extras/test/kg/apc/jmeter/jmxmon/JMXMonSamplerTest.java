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

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author cyberw
 */
public class JMXMonSamplerTest {

    public JMXMonSamplerTest() {
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

    /**
     * Test of generateSamples method, of class JMXMonSampler.
     */
    @Test
    public void testGenerateSamples() {
        System.out.println("generateSamples");
        JMXMonSampleGenerator collector = new JMXMonSampleGeneratorEmul();

        // empty query results
        Map<String, Double> queryResults = new HashMap<String, Double>();
        queryResults.put("attribute",1.0);
        JMXMonSampler instance = new JMXMonSampler(new MBeanServerConnectionEmul(queryResults), null, "service:jmx:rmi:///jndi/rmi://localhost:6969/jmxrmi", "name", "Something:name=objectName", "attribute", "", true);
        instance.generateSamples(collector);
    }
}
