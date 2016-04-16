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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JMXMonSampleGeneratorTest {
    
    public JMXMonSampleGeneratorTest() {
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
     * Test of generateSample method, of class JMXMonSampleGenerator.
     */
    @Test
    public void testGenerateSample() {
        System.out.println("generateSample");
        double d = 0.0;
        String string = "";
        JMXMonSampleGenerator instance = new JMXMonSampleGeneratorImpl();
        instance.generateSample(d, string);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    public class JMXMonSampleGeneratorImpl implements JMXMonSampleGenerator {

        public void generateSample(double d, String string) {
        }
    }
}
