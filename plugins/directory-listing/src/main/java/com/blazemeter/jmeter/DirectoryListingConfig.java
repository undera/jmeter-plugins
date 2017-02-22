package com.blazemeter.jmeter;


import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;

import java.io.File;

public class DirectoryListingConfig extends ConfigTestElement implements LoopIterationListener, TestStateListener {
//    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryListingConfig.class);

    public static final String SOURCE_DIRECTORY = "directory";
    public static final String DESTINATION_VARIABLE_NAME = "variableName";
    public static final String USE_FULL_PATH = "useFullPath";
    public static final String RANDOM_ORDER = "randomOrder";
    public static final String RECURSIVE_LISTING = "recursiveListing";
    public static final String REWIND_ON_THE_END = "rewindOnTheEndOfList";
    public static final String INDEPENDENT_LIST_PER_THREAD = "independentListPerThread";
    public static final String RE_READ_DIRECTORY_ON_THE_END_OF_LIST = "reReadDirectory";

    private DirectoryListingIterator directoryListingIterator;

    @Override
    public void iterationStart(LoopIterationEvent loopIterationEvent) {

        // TODO: isIndependentListPerThread
        boolean isIndependentListPerThread = getIndependentListPerThread();

        if (directoryListingIterator == null) {
            directoryListingIterator = createDirectoryListingIterator();
        }


        if (!isIndependentListPerThread) {
            synchronized (directoryListingIterator) {
                setVariable();
            }
        } else {
            setVariable();
        }

    }

    private void setVariable() {
        if (directoryListingIterator.hasNext()) {
            JMeterVariables variables = JMeterContextService.getContext().getVariables();
            variables.put(getDestinationVariableName(), getFilePath(directoryListingIterator.next()));
        } else {
            // TODO: interrupt iteration
            JMeterContextService.getContext().getThread().stop();
            directoryListingIterator = null;
        }
    }

    protected String getFilePath(File file) {
        return getUseFullPath() ?
                file.getAbsolutePath() :
                getSubPath(file.getAbsolutePath());
    }

    private String getSubPath(String absolutePath) {
        String rootDir = getSourceDirectory();
        return absolutePath.substring(rootDir.length());
    }

    public DirectoryListingIterator createDirectoryListingIterator() {
        return new DirectoryListingIterator(
                getSourceDirectory(),
                getUseFullPath(),
                getRandomOrder(),
                getRecursiveListing(),
                getRewindOnTheEnd(),
                getIndependentListPerThread(),
                getReReadDirectoryOnTheEndOfList()
        );
    }

    public String getSourceDirectory() {
        return getPropertyAsString(SOURCE_DIRECTORY);
    }

    public String getDestinationVariableName() {
        return getPropertyAsString(DESTINATION_VARIABLE_NAME);
    }

    public boolean getUseFullPath() {
        return getPropertyAsBoolean(USE_FULL_PATH);
    }

    public boolean getRandomOrder() {
        return getPropertyAsBoolean(RANDOM_ORDER);
    }

    public boolean getRecursiveListing() {
        return getPropertyAsBoolean(RECURSIVE_LISTING);
    }

    public boolean getRewindOnTheEnd() {
        return getPropertyAsBoolean(REWIND_ON_THE_END);
    }

    public boolean getIndependentListPerThread() {
        return getPropertyAsBoolean(INDEPENDENT_LIST_PER_THREAD);
    }

    public boolean getReReadDirectoryOnTheEndOfList() {
        return getPropertyAsBoolean(RE_READ_DIRECTORY_ON_THE_END_OF_LIST);
    }

    public void setSourceDirectory(String sourceDirectory) {
        setProperty(SOURCE_DIRECTORY, sourceDirectory);
    }

    public void setDestinationVariableName(String destinationVariableName) {
        setProperty(DESTINATION_VARIABLE_NAME, destinationVariableName);
    }

    public void setUseFullPath(boolean useFullPath) {
        setProperty(USE_FULL_PATH, useFullPath);
    }

    public void setRandomOrder(boolean randomOrder) {
        setProperty(RANDOM_ORDER, randomOrder);
    }

    public void setRecursiveListing(boolean recursiveListing) {
        setProperty(RECURSIVE_LISTING, recursiveListing);
    }

    public void setRewindOnTheEnd(boolean rewindOnTheEnd) {
        setProperty(REWIND_ON_THE_END, rewindOnTheEnd);
    }

    public void setIndependentListPerThread(boolean independentListPerThread) {
        setProperty(INDEPENDENT_LIST_PER_THREAD, independentListPerThread);
    }

    public void setReReadDirectoryOnTheEndOfList(boolean reReadDirectoryOnTheEndOfList) {
        setProperty(RE_READ_DIRECTORY_ON_THE_END_OF_LIST, reReadDirectoryOnTheEndOfList);
    }

    @Override
    public void testStarted() {
        testStarted("*local*");
    }

    @Override
    public void testStarted(String s) {
//        if (!getIndependentListPerThread()) {
//            pool.init(this);
//        }
    }


    @Override
    public void testEnded() {
        testEnded("*local*");
    }

    @Override
    public void testEnded(String s) {
//        if (!getIndependentListPerThread()) {
//            pool.nullify();
//        }
    }
}
