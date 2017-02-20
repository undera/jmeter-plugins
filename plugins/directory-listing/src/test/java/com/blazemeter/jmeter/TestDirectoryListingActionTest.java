package com.blazemeter.jmeter;

import kg.apc.emulators.TestJMeterUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class TestDirectoryListingActionTest {

    @BeforeClass
    public static void setUpClass()
            throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testAction() throws Exception {
        DirectoryListingGui gui = new DirectoryListingGui();

        // create next file tree:
        // rootTmpDir :
        // --- tmpFile1_*****.csv
        // --- nestedTmpDir :
        // --- --- tmpFile2_*****.csv
        // --- nestedEmptyTmpDir :
        //
        File tmpDir = File.createTempFile("rootTmpDir", "");
        tmpDir.deleteOnExit();
        tmpDir.delete();
        tmpDir.mkdirs();

        File.createTempFile("tmpFile1_", ".csv", tmpDir);

        File nestedTmpDir = File.createTempFile("nestedTmpDir", "", tmpDir);
        nestedTmpDir.deleteOnExit();
        nestedTmpDir.delete();
        nestedTmpDir.mkdirs();

        File.createTempFile("tmpFile2_", ".csv", nestedTmpDir);

        File nestedEmptyTmpDir = File.createTempFile("nestedEmptyTmpDir", "", tmpDir);
        nestedEmptyTmpDir.deleteOnExit();
        nestedEmptyTmpDir.delete();
        nestedEmptyTmpDir.mkdirs();

        gui.getSourceDirectoryField().setText(tmpDir.getAbsolutePath());
        gui.getIsRecursiveListing().setSelected(true);
        gui.getIsRandomOrderCheckBox().setSelected(true);

        TestDirectoryListingAction action = new TestDirectoryListingAction(gui);

        action.actionPerformed(null);

        assertTrue(gui.getCheckArea().getText().startsWith("Listing of directory successfully finished, 2"));

        System.out.println(gui.getCheckArea().getText());
    }


    @Test
    public void testActionWithException() throws Exception {
        DirectoryListingGui gui = new DirectoryListingGui();

        File tmpDir = File.createTempFile("rootTmpDir", "1");
        tmpDir.delete();


        gui.getSourceDirectoryField().setText(tmpDir.getAbsolutePath());

        System.out.println(tmpDir.getAbsolutePath());

        TestDirectoryListingAction action = new TestDirectoryListingAction(gui);

        action.actionPerformed(null);

        assertEquals("Directory does not exists: " + tmpDir.getAbsolutePath(), gui.getCheckArea().getText());
    }



}