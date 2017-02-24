package com.blazemeter.jmeter;


import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.util.JMeterStopThreadException;

import java.io.File;

public class DirectoryListingConfig extends ConfigTestElement implements NoThreadClone, LoopIterationListener, TestStateListener {
    public static final String DEFAULT_DESTINATION_VARIABLE_NAME = "filename";


    private final ThreadLocal<DirectoryListingIterator> threadLocalIterator = new ThreadLocal<DirectoryListingIterator>(){
        @Override
        protected DirectoryListingIterator initialValue()
        {
            return createDirectoryListingIterator();
        }
    };

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
        boolean isIndependentListPerThread = getIndependentListPerThread();

        if (!isIndependentListPerThread && directoryListingIterator == null) {
            JMeterContextService.getContext().getThread().stop();
            return;
        }

        if (getIterator().hasNext()) {
            JMeterVariables variables = JMeterContextService.getContext().getVariables();
            variables.put(getDestinationVariableName(), getFilePath(getIterator().next()));
        } else {
            // TODO: interrupt iteration
            directoryListingIterator = null;
            throw new JMeterStopThreadException("All files in the directory have been passed.");
        }
    }

    private DirectoryListingIterator getIterator() {
        return getIndependentListPerThread() ? threadLocalIterator.get() : directoryListingIterator;
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
                getRandomOrder(),
                getRecursiveListing(),
                getRewindOnTheEnd(),
                getReReadDirectoryOnTheEndOfList()
        );
    }

    public String getSourceDirectory() {
        return getPropertyAsString(SOURCE_DIRECTORY);
    }

    public String getDestinationVariableName() {
        return getPropertyAsString(DESTINATION_VARIABLE_NAME, DEFAULT_DESTINATION_VARIABLE_NAME);
    }

    @Override
    public String getPropertyAsString(String key, String defaultValue) {
        String str = super.getPropertyAsString(key, defaultValue);
        return (str == null || str.isEmpty()) ? defaultValue : str;
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
        directoryListingIterator = createDirectoryListingIterator();
    }

    @Override
    public void testEnded() {
        testEnded("*local*");
    }

    @Override
    public void testEnded(String s) {
        directoryListingIterator = null;
    }
}
