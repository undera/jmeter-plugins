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
package com.atlantbh.jmeter.plugins.hadooputilities.jobstatistics.gui;

import com.atlantbh.jmeter.plugins.hadooputilities.jobstatistics.JobStatistics;
import javax.swing.JCheckBox;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.testelement.TestElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class JobStatisticsGuiTest {

    public JobStatisticsGuiTest() {
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
     * Test of init method, of class JobStatisticsGui.
     */
    @Test
    public void testInit() {
        System.out.println("init");
        JobStatisticsGui instance = new JobStatisticsGui();
        instance.init();
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of registerCheckBoxForItemListener method, of class
     * JobStatisticsGui.
     */
    @Test
    public void testRegisterCheckBoxForItemListener() {
        System.out.println("registerCheckBoxForItemListener");
        JCheckBox checkBox = new JCheckBox();
        JobStatisticsGui instance = new JobStatisticsGui();
        instance.registerCheckBoxForItemListener(checkBox);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of clearGui method, of class JobStatisticsGui.
     */
    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        JobStatisticsGui instance = new JobStatisticsGui();
        instance.clearGui();
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of createTestElement method, of class JobStatisticsGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        JobStatisticsGui instance = new JobStatisticsGui();
        TestElement result = instance.createTestElement();
    }

    /**
     * Test of modifyTestElement method, of class JobStatisticsGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement element = new JobStatistics();
        JobStatisticsGui instance = new JobStatisticsGui();
        instance.modifyTestElement(element);
    }

    /**
     * Test of configure method, of class JobStatisticsGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement element = new JobStatistics();
        JobStatisticsGui instance = new JobStatisticsGui();
        instance.configure(element);
    }

    /**
     * Test of getLabelResource method, of class JobStatisticsGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        JobStatisticsGui instance = new JobStatisticsGui();
        String expResult = "";
        String result = instance.getLabelResource();
    }

    /**
     * Test of getStaticLabel method, of class JobStatisticsGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        JobStatisticsGui instance = new JobStatisticsGui();
        String expResult = "";
        String result = instance.getStaticLabel();
    }
}
