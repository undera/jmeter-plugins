package com.blazemeter.jmeter;

import kg.apc.emulators.TestJMeterUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class TestDirectoryListingConfigActionTest {

    @BeforeClass
    public static void setUpClass()
            throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    // create next file tree:
    // rootTmpDir :
    // --- tmpFile1_*****.csv
    // --- nestedTmpDir :
    // --- --- tmpFile2_*****.csv
    // --- nestedEmptyTmpDir :
    //
    public static File createFileTree() throws Exception {
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

        return tmpDir;
    }

    @Test
    public void testAction() throws Exception {
        File tmpDir = createFileTree();

        DirectoryListingConfig config = new DirectoryListingConfig();
        config.setSourceDirectory(tmpDir.getAbsolutePath());
        config.setRecursiveListing(true);

        DirectoryListingConfigGui gui = new DirectoryListingConfigGui();

        gui.configure(config);

        TestDirectoryListingAction action = new TestDirectoryListingAction(gui);

        action.actionPerformed(null);

        assertTrue(gui.getCheckArea().getText().startsWith("Listing of directory successfully finished, 2"));
    }


    @Test
    public void testActionWithException() throws Exception {
        File tmpDir = File.createTempFile("rootTmpDir", "1");
        tmpDir.delete();

        DirectoryListingConfig config = new DirectoryListingConfig();
        config.setSourceDirectory(tmpDir.getAbsolutePath());

        DirectoryListingConfigGui gui = new DirectoryListingConfigGui();

        gui.configure(config);

        TestDirectoryListingAction action = new TestDirectoryListingAction(gui);

        action.actionPerformed(null);

        assertEquals("java.io.FileNotFoundException: Directory does not exists: " + tmpDir.getAbsolutePath(), gui.getCheckArea().getText());
    }

}