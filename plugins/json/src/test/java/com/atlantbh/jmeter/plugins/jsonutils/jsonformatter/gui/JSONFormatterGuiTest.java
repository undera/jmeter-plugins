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
package com.atlantbh.jmeter.plugins.jsonutils.jsonformatter.gui;

import com.atlantbh.jmeter.plugins.jsonutils.jsonformatter.JSONFormatter;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.testelement.TestElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JSONFormatterGuiTest {

    public JSONFormatterGuiTest() {
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
     * Test of init method, of class JSONFormatterGui.
     */
    @Test
    public void testInit() {
        System.out.println("init");
        JSONFormatterGui instance = new JSONFormatterGui();
        instance.init();
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of clearGui method, of class JSONFormatterGui.
     */
    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        JSONFormatterGui instance = new JSONFormatterGui();
        instance.clearGui();
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of createTestElement method, of class JSONFormatterGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        JSONFormatterGui instance = new JSONFormatterGui();
        TestElement expResult = null;
        TestElement result = instance.createTestElement();
    }

    /**
     * Test of getLabelResource method, of class JSONFormatterGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        JSONFormatterGui instance = new JSONFormatterGui();
        String expResult = "";
        String result = instance.getLabelResource();
    }

    /**
     * Test of getStaticLabel method, of class JSONFormatterGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        JSONFormatterGui instance = new JSONFormatterGui();
        String expResult = "";
        String result = instance.getStaticLabel();
    }

    /**
     * Test of modifyTestElement method, of class JSONFormatterGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement element = new JSONFormatter();
        JSONFormatterGui instance = new JSONFormatterGui();
        instance.modifyTestElement(element);
        // TODO review the generated test code and remove the default call to fail.

    }
}
