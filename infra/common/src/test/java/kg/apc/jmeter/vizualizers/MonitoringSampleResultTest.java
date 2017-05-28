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

import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MonitoringSampleResultTest {
    /**
     * Test of setValue method, of class MonitoringSampleResult.
     */
    @Test
    public void testSetValue() {
        System.out.println("setValue");
        double value = 0.0;
        MonitoringSampleResult instance = new MonitoringSampleResult();
        instance.setValue(value);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of setResponseMessage method, of class MonitoringSampleResult.
     */
    @Test
    public void testSetResponseMessage() {
        System.out.println("setResponseMessage");
        String msg = "";
        MonitoringSampleResult instance = new MonitoringSampleResult();
        instance.setResponseMessage(msg);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getValue method, of class MonitoringSampleResult.
     */
    @Test
    public void testGetValue_0args() {
        System.out.println("getValue");
        MonitoringSampleResult instance = new MonitoringSampleResult();
        instance.setResponseMessage("0");
        double expResult = 0.0;
        double result = instance.getValue();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getValue method, of class MonitoringSampleResult.
     */
    @Test
    public void testGetValue_SampleResult() {
        System.out.println("getValue");
        SampleResult res = new SampleResult();
        res.setResponseMessage("0");
        double expResult = 0.0;
        double result = MonitoringSampleResult.getValue(res);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        
    }
}
