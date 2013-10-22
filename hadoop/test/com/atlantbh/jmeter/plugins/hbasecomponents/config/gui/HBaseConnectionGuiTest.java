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
package com.atlantbh.jmeter.plugins.hbasecomponents.config.gui;

import com.atlantbh.jmeter.plugins.hbasecomponents.config.HBaseConnection;
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
public class HBaseConnectionGuiTest {

    public HBaseConnectionGuiTest() {
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
     * Test of getStaticLabel method, of class HBaseConnectionGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        HBaseConnectionGui instance = new HBaseConnectionGui();
        String expResult = "";
        String result = instance.getStaticLabel();
    }

    /**
     * Test of getLabelResource method, of class HBaseConnectionGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        HBaseConnectionGui instance = new HBaseConnectionGui();
        String expResult = "";
        String result = instance.getLabelResource();
    }

    /**
     * Test of createTestElement method, of class HBaseConnectionGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        HBaseConnectionGui instance = new HBaseConnectionGui();
        TestElement expResult = null;
        TestElement result = instance.createTestElement();
    }

    /**
     * Test of modifyTestElement method, of class HBaseConnectionGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement te = new HBaseConnection();
        HBaseConnectionGui instance = new HBaseConnectionGui();
        instance.modifyTestElement(te);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of configure method, of class HBaseConnectionGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement el = new HBaseConnection();
        HBaseConnectionGui instance = new HBaseConnectionGui();
        instance.configure(el);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of clearGui method, of class HBaseConnectionGui.
     */
    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        HBaseConnectionGui instance = new HBaseConnectionGui();
        instance.clearGui();
        // TODO review the generated test code and remove the default call to fail.

    }
}
