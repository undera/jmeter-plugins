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

public class MonitoringSampleGeneratorTest {
    
    /**
     * Test of generateSample method, of class MonitoringSampleGenerator.
     */
    @Test
    public void testGenerateSample() {
        System.out.println("generateSample");
        double d = 0.0;
        String string = "";
        MonitoringSampleGenerator instance = new MonitoringSampleGeneratorImpl();
        instance.generateSample(d, string);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    public class MonitoringSampleGeneratorImpl implements MonitoringSampleGenerator {

        /**
         * No-op implemenation for testing.
         */
        public void generateSample(double d, String string) {
        }
    }
}
