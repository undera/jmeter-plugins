package com.blazemeter.jmeter;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DirectoryListing extends ConfigTestElement implements LoopIterationListener {

    public static final String SOURCE_DIRECTORY = "directory";
    public static final String DESTINATION_VARIABLE_NAME = "variableName";
    public static final String USE_FULL_PATH = "useFullPath";
    public static final String RANDOM_ORDER = "randomOrder";
    public static final String RECURSIVE_LISTING = "recursiveListing";
    public static final String REWIND_ON_THE_END = "rewindOnTheEndOfList";
    public static final String INDEPENDENT_LIST_PER_THREAD = "independentListPerThread";
    public static final String RE_READ_DIRECTORY_ON_THE_END_OF_LIST = "reReadDirectory";

    @Override
    public void iterationStart(LoopIterationEvent loopIterationEvent) {
        // TODO: independentListPerThread, rewindOnTheEndOfList, reReadDirectory

    }

    protected static List<File> getDirectoryListing(String dirPath, boolean isRandomOrder, boolean isRecursiveListing) throws FileNotFoundException {

        final List<File> list = getDirectoryListing(new File(dirPath), isRecursiveListing);

        if (isRandomOrder) {
            Collections.shuffle(list, new Random(System.currentTimeMillis()));
        }

        return list;
    }

    protected static List<File> getDirectoryListing(File baseDir, boolean isRecursiveListing) throws FileNotFoundException {
        final List<File> resultList = new ArrayList<>();


        if (!baseDir.exists()) {
            throw new FileNotFoundException("Directory does not exists: " + baseDir.getAbsolutePath());
        }

        File[] files = baseDir.listFiles();

        if (files == null) {
            return resultList;
        }

        for (File file : files) {
            boolean isDirectory = file.isDirectory();
            if (isRecursiveListing && isDirectory) {
                resultList.addAll(getDirectoryListing(file, true));
            } else if (!isDirectory) {
                resultList.add(file);
            }
        }

        return resultList;
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
}
