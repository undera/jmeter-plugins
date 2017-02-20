package com.blazemeter.jmeter;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DirectoryListingConfig extends ConfigTestElement implements LoopIterationListener, TestStateListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryListingConfig.class);

    public static final String SOURCE_DIRECTORY = "directory";
    public static final String DESTINATION_VARIABLE_NAME = "variableName";
    public static final String USE_FULL_PATH = "useFullPath";
    public static final String RANDOM_ORDER = "randomOrder";
    public static final String RECURSIVE_LISTING = "recursiveListing";
    public static final String REWIND_ON_THE_END = "rewindOnTheEndOfList";
    public static final String INDEPENDENT_LIST_PER_THREAD = "independentListPerThread";
    public static final String RE_READ_DIRECTORY_ON_THE_END_OF_LIST = "reReadDirectory";

    private Iterator<File> iterator;
    private List<File> list;

    private static List<File> globalList;
    private static Iterator<File> globalIterator;

    // TODO: how to switch isIndependentListPerThread flag in RunningTest mode????

    @Override
    public void iterationStart(LoopIterationEvent loopIterationEvent) {
        boolean isIndependentListPerThread = getIndependentListPerThread();

        checkNext(isIndependentListPerThread, isIndependentListPerThread ? iterator : globalIterator);

        JMeterVariables variables = JMeterContextService.getContext().getVariables();
        variables.put(getDestinationVariableName(),
                (isIndependentListPerThread ? iterator : globalIterator).next().getAbsolutePath());
    }

    private void checkNext(boolean isIndependentListPerThread, Iterator<File> iterator) {
        if (!iterator.hasNext()) {
            boolean isReRead = getReReadDirectoryOnTheEndOfList();
            boolean isRewindOnTheEnd = getRewindOnTheEnd();

            if (isReRead && isRewindOnTheEnd) {
                initList(isIndependentListPerThread);
            } else if (isRewindOnTheEnd) {
                if (getRandomOrder()) {
                    shuffleList(isIndependentListPerThread ? list : globalList);
                }
                initIterator(isIndependentListPerThread);
            } else {
                // if the end of list && !isRewindOnTheEnd
                nullifyAll();
                JMeterContextService.getContext().getThread().stop();
            }
        }
    }

    private void initList(boolean isIndependentPerThreadList) {
        if (isIndependentPerThreadList) {
            list = getDirectoryListing();
        } else {
            globalList = getDirectoryListing();
        }
    }

    private void initIterator(boolean isIndependentPerThreadList) {
        if (isIndependentPerThreadList) {
            iterator = list.iterator();
        } else {
            globalIterator = globalList.iterator();
        }
    }

    private void nullifyAll() {
        list = globalList = null;
        iterator = globalIterator = null;
    }

    private List<File> getDirectoryListing() {
        try {
            return getDirectoryListing(getSourceDirectory(), getRandomOrder(), getRecursiveListing());
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
//            LOGGER.error(ex.getMessage(), ex);
        }
    }

    protected static List<File> getDirectoryListing(String dirPath, boolean isRandomOrder, boolean isRecursiveListing) throws FileNotFoundException {

        final List<File> list = getDirectoryListing(new File(dirPath), isRecursiveListing);

        if (isRandomOrder) {
            shuffleList(list);
        }
//        TODO: remove it
        else {
            Collections.sort(list, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }

        return list;
    }

    private static List<File> shuffleList(List<File> list) {
        if (list != null) {
            Collections.shuffle(list, new Random(System.currentTimeMillis()));
        }
        return list;
    }

    protected static List<File> getDirectoryListing(File baseDir, boolean isRecursiveListing) throws FileNotFoundException {

        if (!baseDir.exists()) {
            throw new FileNotFoundException("Directory does not exists: " + baseDir.getAbsolutePath());
        }

        File[] files = baseDir.listFiles();

        if (files == null || files.length == 0) {
            return null;
        }

        final List<File> resultList = new ArrayList<>();

        for (File file : files) {
            boolean isDirectory = file.isDirectory();
            if (isRecursiveListing && isDirectory) {
                List<File> nestedListing = getDirectoryListing(file, true);
                if (nestedListing != null) {
                    resultList.addAll(nestedListing);
                }
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

    @Override
    public void testStarted() {
        testStarted("*local*");
    }

    @Override
    public void testStarted(String s) {
        boolean independentListPerThread = getIndependentListPerThread();
        initList(independentListPerThread);
        initIterator(independentListPerThread);
    }


    @Override
    public void testEnded() {
        testEnded("*local*");
    }

    @Override
    public void testEnded(String s) {
        nullifyAll();
    }
}
