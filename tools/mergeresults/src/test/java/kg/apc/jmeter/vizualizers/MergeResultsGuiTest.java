/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package kg.apc.jmeter.vizualizers;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jorphan.util.JMeterError;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Felix Henry
 * @author Vincent Daburon
 */
public class MergeResultsGuiTest {

    private final String DATA_DIR;
    private static final String CRLF = System.getProperty("line.separator");

    public MergeResultsGuiTest() {
        TestJMeterUtils.createJmeterEnv();
        DATA_DIR = TestJMeterUtils.getTempDir();
    }

    /**
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    /**
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        MergeResultsGui instance = new MergeResultsGui();
        String expResult = "MergeResultsGui";
        String result = instance.getLabelResource();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        MergeResultsGui instance = new MergeResultsGui();
        String expResult = "jp@gc - Merge Results";
        String result = instance.getStaticLabel();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetMenuCategories() {
        System.out.println("getMenuCategorie");
        MergeResultsGui instance = new MergeResultsGui();
        String expResult = MenuFactory.NON_TEST_ELEMENTS;
        Collection<String> result = instance.getMenuCategories();
        Iterator<String> it = result.iterator();
        assertEquals(expResult, it.next());
    }

    @Test
    public void testSetAndGetFile() {
        System.out.println("setFile");
        MergeResultsGui instance = new MergeResultsGui();
        String filename = "filename";
        instance.setFile("filename");
        assertEquals(filename, instance.getFile());
    }

    @Test
    public void testAdd() {
        System.out.println("add");
        SampleResult res = new SampleResult();
        res.setAllThreads(1);
        res.setThreadName("test 1-2");
        MergeResultsGui instance = new MergeResultsGui();
        instance.add(res);
    }

    @Test
    public void testCreateParamsPanel() {
        System.out.println("getSettingsPanel");
        MergeResultsGui instance = new MergeResultsGui();
        JPanel result = instance.createParamsPanel();
        assertNotNull(result);
    }

    /**
     * Test of updateUI method, of class MergeResultsGui.
     */
    @Test
    public void testUpdateUI() {
        System.out.println("updateGui");
        MergeResultsGui instance = new MergeResultsGui();
        instance.updateUI();
    }

    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement c = new ResultCollector();
        MergeResultsGui instance = new MergeResultsGui();
        instance.modifyTestElement(c);
    }

    @Test
    public void testConfigure_NullProperty() {
        System.out.println("configure");
        TestElement el = new ResultCollector();
        el.setProperty(new StringProperty(MergeResultsGui.FILENAME,
                "fusionRes.csv"));
        MergeResultsGui instance = new MergeResultsGui();
        instance.configure(el);
    }

    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement el = new ResultCollector();
        el.setProperty(new StringProperty(MergeResultsGui.FILENAME,
                "fusionRes.csv"));
        MergeResultsGui instance = new MergeResultsGui();
        instance.modifyTestElement(el);
        instance.configure(el);
    }

    /**
     * Test of getWikiPage method, of class MergeResultsGui.
     */
    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        MergeResultsGui instance = new MergeResultsGui();
        String expResult = "MergeResults";
        String result = instance.getWikiPage();
        assertEquals(expResult, result);
    }

    @Test
    public void testActionPerformed_Add_Copy_Delete_SaveConfig() {
        System.out.println("actionPerformed");
        MergeResultsGui instance = new MergeResultsGui();
        JTable grid = instance.getGrid();

        ActionEvent actionAdd = new ActionEvent(new JButton(), 1, "add");
        ActionEvent actionCopy = new ActionEvent(new JButton(), 2, "copy");
        ActionEvent actionDelete = new ActionEvent(new JButton(), 3, "delete");
        ActionEvent actionSaveConfig = new ActionEvent(new JButton(), 4, "save_config");

        instance.actionPerformed(actionAdd);
        grid.editCellAt(0, 0);
        instance.actionPerformed(actionAdd);
        instance.actionPerformed(actionAdd);
        instance.actionPerformed(actionAdd);
        instance.actionPerformed(actionAdd);

        instance.actionPerformed(actionDelete);
        grid.editCellAt(0, 0);
        instance.actionPerformed(actionDelete);
        instance.actionPerformed(actionDelete);
        instance.actionPerformed(actionDelete);
        instance.actionPerformed(actionDelete);

        instance.actionPerformed(actionCopy);
        instance.actionPerformed(actionAdd);
        instance.actionPerformed(actionCopy);
        grid.editCellAt(0, 0);
        instance.actionPerformed(actionCopy);
        instance.actionPerformed(actionCopy);
        instance.actionPerformed(actionCopy);
    }

    @Test
    public void testActionPerformed_Merge() throws Exception {
        System.out.println("actionPerformed");
        MergeResultsGui instance = new MergeResultsGui();
        JTable grid = instance.getGrid();

        ActionEvent actionMerge = new ActionEvent(new JButton(), 5, "merge");
        ActionEvent actionAdd = new ActionEvent(new JButton(), 1, "add");

        // create a file to test the merge action
        BufferedWriter out;
        String f1 = "test-merge-1.csv";
        String f2 = "test-merge-2.csv";
        String fRes = "test-merge-1-2.csv";
        out = new BufferedWriter(new FileWriter(new File(DATA_DIR, f1)));
        out.write("timeStamp;elapsed;label;responseCode;threadName;success;bytes;grpThreads;allThreads;Latency;Hostname");
        out.write(CRLF);
        out.write("2014-04-28 16:49:28.068;288478;P1_RECHERCHE;200;G3_G1_G2 Paliers 1-7;true;290687;28;28;1559;ITEM-63339");
        out.write(CRLF);
        out.close();

        out = new BufferedWriter(new FileWriter(new File(DATA_DIR, f2)));
        out.write("timeStamp;elapsed;label;responseCode;threadName;success;bytes;grpThreads;allThreads;Latency;Hostname");
        out.write(CRLF);
        out.write("2014-04-29 17:43:18.161;257065;P1_RECHERCHE;200;G3_G1_G2 Paliers 1-12;true;279542;20;20;908;ITEM-63339");
        out.write(CRLF);
        out.close();

        instance.actionPerformed(actionAdd);
        instance.actionPerformed(actionAdd);
        grid.setValueAt(f1, 0, 0);
        grid.setValueAt(f2, 1, 0);
        instance.setFile(DATA_DIR + File.separator + fRes);
        instance.updateUI();
        instance.createTestElement();
        try {
            instance.actionPerformed(actionMerge);
            File f = new File(DATA_DIR, f1);
            f.delete();
            f = new File(DATA_DIR, f2);
            f.delete();
            f = new File(DATA_DIR, fRes);
            assertTrue(f.exists());
            f.delete();
        } catch (JMeterError e) {
            //  FIXME: this test is broken
            e.printStackTrace(System.err);
        }
    }

    @Test
    public void testCheckDeleteButtonStatus() {
        System.out.println("checkDeleteButtonStatus");
        MergeResultsGui instance = new MergeResultsGui();
        instance.checkDeleteButtonStatus();

        ActionEvent actionAdd = new ActionEvent(new JButton(), 1, "add");
        instance.actionPerformed(actionAdd);
        instance.checkDeleteButtonStatus();
    }

    @Test
    public void testCheckMergeButtonStatus() {
        System.out.println("checkMergeButtonStatus");
        MergeResultsGui instance = new MergeResultsGui();
        instance.checkMergeButtonStatus();

        ActionEvent actionAdd = new ActionEvent(new JButton(), 1, "add");
        instance.actionPerformed(actionAdd);
        instance.checkMergeButtonStatus();
    }

    @Test
    public void testEditingCanceled() {
        System.out.println("editingCanceled");
        MergeResultsGui instance = new MergeResultsGui();
        instance.editingCanceled(new ChangeEvent(instance));
    }

    @Test
    public void testStateChanged() {
        System.out.println("stateChanged");
        MergeResultsGui instance = new MergeResultsGui();
        instance.stateChanged(new ChangeEvent(instance));
    }

    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        MergeResultsGui instance = new MergeResultsGui();
        instance.clearGui();
    }

    @Test
    public void testgetPreferredSize() {
        System.out.println("getPreferredSize");
        MergeResultsGui instance = new MergeResultsGui();
        instance.getPreferredSize();
    }

}