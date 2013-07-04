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
package com.atlantbh.jmeter.plugins.rest.gui;

import com.atlantbh.jmeter.plugins.rest.RestSampler;
import java.awt.Dimension;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.testelement.TestElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author undera
 */
public class RestGuiTest {

    public RestGuiTest() {
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
     * Test of getLabelResource method, of class RestGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        RestGui instance = new RestGui();
        String expResult = "";
        String result = instance.getLabelResource();
    }

    /**
     * Test of getStaticLabel method, of class RestGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        RestGui instance = new RestGui();
        String expResult = "";
        String result = instance.getStaticLabel();
    }

    /**
     * Test of createTestElement method, of class RestGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        RestGui instance = new RestGui();
        TestElement expResult = null;
        TestElement result = instance.createTestElement();
    }

    /**
     * Test of clear method, of class RestGui.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        RestGui instance = new RestGui();
        instance.clear();
    }

    /**
     * Test of modifyTestElement method, of class RestGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement s = new RestSampler();
        RestGui instance = new RestGui();
        instance.modifyTestElement(s);
    }

    /**
     * Test of clearGui method, of class RestGui.
     */
    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        RestGui instance = new RestGui();
        instance.clearGui();
    }

    /**
     * Test of configure method, of class RestGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement el = new RestSampler();
        RestGui instance = new RestGui();
        instance.configure(el);
    }

    /**
     * Test of getPreferredSize method, of class RestGui.
     */
    @Test
    public void testGetPreferredSize() {
        System.out.println("getPreferredSize");
        RestGui instance = new RestGui();
        Dimension expResult = null;
        Dimension result = instance.getPreferredSize();
    }
}
