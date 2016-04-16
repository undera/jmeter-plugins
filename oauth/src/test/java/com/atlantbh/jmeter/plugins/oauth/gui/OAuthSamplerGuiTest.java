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
package com.atlantbh.jmeter.plugins.oauth.gui;

import com.atlantbh.jmeter.plugins.oauth.OAuthSampler;
import java.awt.Dimension;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.testelement.TestElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class OAuthSamplerGuiTest {

    public OAuthSamplerGuiTest() {
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
     * Test of getLabelResource method, of class OAuthSamplerGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        OAuthSamplerGui instance = new OAuthSamplerGui();
        String expResult = "";
        String result = instance.getLabelResource();
    }

    /**
     * Test of getStaticLabel method, of class OAuthSamplerGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        OAuthSamplerGui instance = new OAuthSamplerGui();
        String expResult = "";
        String result = instance.getStaticLabel();
    }

    /**
     * Test of createTestElement method, of class OAuthSamplerGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        OAuthSamplerGui instance = new OAuthSamplerGui();
        TestElement expResult = null;
        TestElement result = instance.createTestElement();
    }

    /**
     * Test of clear method, of class OAuthSamplerGui.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        OAuthSamplerGui instance = new OAuthSamplerGui();
        instance.clear();
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of modifyTestElement method, of class OAuthSamplerGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement s = new OAuthSampler();
        OAuthSamplerGui instance = new OAuthSamplerGui();
        instance.modifyTestElement(s);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of clearGui method, of class OAuthSamplerGui.
     */
    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        OAuthSamplerGui instance = new OAuthSamplerGui();
        instance.clearGui();
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of configure method, of class OAuthSamplerGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement el = new OAuthSampler();
        OAuthSamplerGui instance = new OAuthSamplerGui();
        instance.configure(el);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getPreferredSize method, of class OAuthSamplerGui.
     */
    @Test
    public void testGetPreferredSize() {
        System.out.println("getPreferredSize");
        OAuthSamplerGui instance = new OAuthSamplerGui();
        Dimension expResult = null;
        Dimension result = instance.getPreferredSize();
    }
}
