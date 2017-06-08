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
package org.loadosophia.jmeter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StatusNotifierCallbackTest {
    
    public StatusNotifierCallbackTest() {
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
     * Test of notifyAbout method, of class StatusNotifierCallback.
     */
    @Test
    public void testNotifyAbout() {
        System.out.println("notifyAbout");
        String info = "";
        StatusNotifierCallback instance = new StatusNotifierCallbackImpl();
        instance.notifyAbout(info);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    public static class StatusNotifierCallbackImpl implements StatusNotifierCallback {
        private StringBuilder buffer = new StringBuilder();

        public void notifyAbout(String info) {
            buffer.append(info);
        }

        public StringBuilder getBuffer() {
            return buffer;
        }
    }
}
