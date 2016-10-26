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
package com.atlantbh.jmeter.plugins.jsonutils.jsonpathassertion.gui;

import com.atlantbh.jmeter.plugins.jsonutils.jsonpathassertion.JSONPathAssertion;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.testelement.TestElement;
import org.junit.*;

import javax.swing.*;
import java.awt.*;

public class JSONPathAssertionGuiTest {

    @BeforeClass
    public static void setUpClass() {
        TestJMeterUtils.createJmeterEnv();
    }

    public void displayGUI() throws InterruptedException {
        AbstractJMeterGuiComponent obj = new JSONPathAssertionGui();
        JSONPathAssertion te = (JSONPathAssertion) obj.createTestElement();
        obj.configure(te);
        obj.clearGui();
        obj.modifyTestElement(te);

        JFrame frame = new JFrame("FrameDemo");
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(obj, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        Thread.sleep(60000);
    }

    @Test
    public void testInit() {
        System.out.println("init");
        JSONPathAssertionGui instance = new JSONPathAssertionGui();
        instance.init();
        instance.stateChanged(null);
    }

    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        JSONPathAssertionGui instance = new JSONPathAssertionGui();
        instance.clearGui();
    }

    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        JSONPathAssertionGui instance = new JSONPathAssertionGui();
        instance.createTestElement();
    }

    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        JSONPathAssertionGui instance = new JSONPathAssertionGui();
        instance.getLabelResource();
    }

    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        JSONPathAssertionGui instance = new JSONPathAssertionGui();
        instance.getStaticLabel();
    }

    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement element = new JSONPathAssertion();
        JSONPathAssertionGui instance = new JSONPathAssertionGui();
        instance.modifyTestElement(element);
    }

    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement element = new JSONPathAssertion();
        JSONPathAssertionGui instance = new JSONPathAssertionGui();
        instance.configure(element);
    }
}
