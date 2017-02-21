package com.blazemeter.jmeter;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.threads.*;
import org.apache.jorphan.collections.HashTree;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DirectoryListingConfigTest {

    private static final String VARIABLE_NAME = "fname";

    @Test
    public void testShareList() throws Exception {
        DirectoryListingConfig config = new DirectoryListingConfig();

        File rootDir = TestDirectoryListingConfigActionTest.createFileTree();

        setDirectoryConfig(config, rootDir.getAbsolutePath(), VARIABLE_NAME, true, false, true, false, false, false);

        config.testStarted();
        testFlow(config);
        config.testEnded();
    }

    @Test
    public void testNonShareList() throws Exception {
        DirectoryListingConfig config = new DirectoryListingConfig();

        File rootDir = TestDirectoryListingConfigActionTest.createFileTree();

        setDirectoryConfig(config, rootDir.getAbsolutePath(), VARIABLE_NAME, true, false, true, false, true, false);

        testFlow(config);
    }

    @Test
    public void testReRead() throws Exception {
        DirectoryListingConfig config = new DirectoryListingConfig();

        File rootDir = TestDirectoryListingConfigActionTest.createFileTree();

        setDirectoryConfig(config, rootDir.getAbsolutePath(), VARIABLE_NAME, true, false, true, true, true, true);

        testFlow(config);

        File.createTempFile("tmpFile3_", ".csv", rootDir);

        testFlow(config);
    }

    @Test
    public void testOnlyRewind() throws Exception {
        DirectoryListingConfig config = new DirectoryListingConfig();

        File rootDir = TestDirectoryListingConfigActionTest.createFileTree();

        setDirectoryConfig(config, rootDir.getAbsolutePath(), VARIABLE_NAME, true, false, true, true, true, false);

        testFlow(config);

        config.setRandomOrder(true);
        JMeterVariables variables = JMeterContextService.getContext().getVariables();
        assertNotNull(variables);

        List<File> etalonList = config.getDirectoryListing();
        assertNotNull(etalonList);

        List<String> filesNames = new ArrayList<>(etalonList.size());
        for (File f : etalonList) {
            filesNames.add(f.getAbsolutePath());
        }


        for (int i = 0; i <  etalonList.size(); i++) {
            config.iterationStart(null);
            String actualName = variables.get(VARIABLE_NAME);
            assertTrue(filesNames.contains(actualName));
            filesNames.remove(actualName);
        }

        assertEquals(0, filesNames.size());

    }

    @Test
    public void testThreadStopping() throws Exception {
        DirectoryListingConfig config = new DirectoryListingConfig();

        File rootDir = TestDirectoryListingConfigActionTest.createFileTree();

        setDirectoryConfig(config, rootDir.getAbsolutePath(), VARIABLE_NAME, true, false, false, false, true, false);

        final HashTree hashTree = new HashTree();
        hashTree.add(new LoopController());

        JMeterThread thread = new JMeterThread(hashTree, null, null);
        JMeterContextService.getContext().setThread(thread);

        testFlow(config);

        config.iterationStart(null);

        config.testEnded();
        config.setIndependentListPerThread(false);
        config.iterationStart(null);
        // TODO: assert something here
    }

    private void testFlow(DirectoryListingConfig config) {
        List<File> etalonList = config.getDirectoryListing();

        assertNotNull(etalonList);

        JMeterContext context = JMeterContextService.getContext();

        JMeterVariables variables = context.getVariables();
        if (variables == null) {
            context.setVariables(variables = new JMeterVariables());
        }

        for (File etalonFile : etalonList) {
            config.iterationStart(null);
            assertEquals(etalonFile.getAbsolutePath(), variables.get(VARIABLE_NAME));
        }
    }


    public static void setDirectoryConfig(
            DirectoryListingConfig config,
            String srcDir,
            String varName,
            boolean useFullPath,
            boolean isRandomOrder,
            boolean isRecursiveListing,
            boolean isRewindOnTheEnd,
            boolean isIndependentList,
            boolean isReReadScrDir
            ) {
        config.setSourceDirectory(srcDir);
        config.setDestinationVariableName(varName);
        config.setUseFullPath(useFullPath);
        config.setRandomOrder(isRandomOrder);
        config.setRecursiveListing(isRecursiveListing);
        config.setRewindOnTheEnd(isRewindOnTheEnd);
        config.setIndependentListPerThread(isIndependentList);
        config.setReReadDirectoryOnTheEndOfList(isReReadScrDir);
    }
}