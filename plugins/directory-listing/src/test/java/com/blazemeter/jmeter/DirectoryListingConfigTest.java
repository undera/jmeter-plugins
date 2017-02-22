package com.blazemeter.jmeter;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.threads.*;
import org.apache.jorphan.collections.HashTree;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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

        // TODO: ??????????????????????/
        Class<?> jmeterThreadCls = JMeterThread.class;
        Field isRunning = jmeterThreadCls.getDeclaredField("running");
        isRunning.setAccessible(true);

        assertFalse(isRunning.getBoolean(thread));
    }

    private void testFlow(DirectoryListingConfig config) {
        List<File> etalonList = config.createDirectoryListingIterator().getDirectoryListing();

        if (config.getRandomOrder()) {
            DirectoryListingIterator.shuffleList(etalonList);
        }

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