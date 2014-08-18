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

import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class JMXMonSampleResultTest {
    
    public JMXMonSampleResultTest() {
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
     * Test of setValue method, of class JMXMonSampleResult.
     */
    @Test
    public void testSetValue() {
        System.out.println("setValue");
        double value = 0.0;
        JMXMonSampleResult instance = new JMXMonSampleResult();
        instance.setValue(value);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of setResponseMessage method, of class JMXMonSampleResult.
     */
    @Test
    public void testSetResponseMessage() {
        System.out.println("setResponseMessage");
        String msg = "";
        JMXMonSampleResult instance = new JMXMonSampleResult();
        instance.setResponseMessage(msg);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getValue method, of class JMXMonSampleResult.
     */
    @Test
    public void testGetValue_0args() {
        System.out.println("getValue");
        JMXMonSampleResult instance = new JMXMonSampleResult();
        instance.setResponseMessage("0");
        double expResult = 0.0;
        double result = instance.getValue();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getValue method, of class JMXMonSampleResult.
     */
    @Test
    public void testGetValue_SampleResult() {
        System.out.println("getValue");
        SampleResult res = new SampleResult();
        res.setResponseMessage("0");
        double expResult = 0.0;
        double result = JMXMonSampleResult.getValue(res);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        
    }
}
