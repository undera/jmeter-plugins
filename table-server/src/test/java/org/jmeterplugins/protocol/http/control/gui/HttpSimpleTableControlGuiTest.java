package org.jmeterplugins.protocol.http.control.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JButton;

import kg.apc.emulators.TestJMeterUtils;

import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.testelement.TestElement;
import org.jmeterplugins.protocol.http.control.HttpSimpleTableControl;
import org.junit.BeforeClass;
import org.junit.Test;

public class HttpSimpleTableControlGuiTest {

    public HttpSimpleTableControlGuiTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        HttpSimpleTableControlGui instance = new HttpSimpleTableControlGui();
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement element = new HttpSimpleTableControl();
        HttpSimpleTableControlGui instance = new HttpSimpleTableControlGui();
        instance.configure(element);
    }

    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        HttpSimpleTableControlGui instance = new HttpSimpleTableControlGui();
        TestElement result = instance.createTestElement();
        assertNotNull(result);
    }

    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement sampler = new HttpSimpleTableControl();
        HttpSimpleTableControlGui instance = new HttpSimpleTableControlGui();
        instance.modifyTestElement(sampler);
    }

    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        HttpSimpleTableControlGui instance = new HttpSimpleTableControlGui();
        instance.clearGui();
    }

    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        HttpSimpleTableControlGui instance = new HttpSimpleTableControlGui();
        String expResult = "HttpSimpleTableControlGui";
        String result = instance.getLabelResource();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetMenuCategories() {
        System.out.println("getMenuCategories");
        HttpSimpleTableControlGui instance = new HttpSimpleTableControlGui();
        String expResult = MenuFactory.NON_TEST_ELEMENTS;
        Collection<String> result = instance.getMenuCategories();
        Iterator<String> it = result.iterator();
        assertEquals(expResult, it.next());
    }

    @Test
    public void testActionPerformed() {
        System.out.println("actionPerformed");
        HttpSimpleTableControlGui instance = new HttpSimpleTableControlGui();
        instance.createTestElement();
        ActionEvent actionStart = new ActionEvent(new JButton(), 1, "start");
        instance.actionPerformed(actionStart);

        ActionEvent actionStop = new ActionEvent(new JButton(), 2, "stop");
        instance.actionPerformed(actionStop);

        ActionEvent actionPause = new ActionEvent(new JButton(), 2, "pause");
        instance.actionPerformed(actionPause);
    }
}
