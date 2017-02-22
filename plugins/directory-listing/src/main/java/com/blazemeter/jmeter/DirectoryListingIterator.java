package com.blazemeter.jmeter;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DirectoryListingIterator implements Iterator<File>{

    private final String srcDir;
    private final boolean isRandomOrder;
    private final boolean isRecursiveListing;
    private final boolean isRewindOnEndOfList;
    private final boolean isReReadDirectory;


    public DirectoryListingIterator(String srcDir,
                                    boolean isRandomOrder,
                                    boolean isRecursiveListing,
                                    boolean isRewindOnEndOfList,
                                    boolean isReReadDirectory) {
        this.srcDir = srcDir;
        this.isRandomOrder = isRandomOrder;
        this.isRecursiveListing = isRecursiveListing;
        this.isRewindOnEndOfList = isRewindOnEndOfList;
        this.isReReadDirectory = isReReadDirectory;

        this.list = getDirectoryListing();
        if (isRandomOrder) {
            shuffleList(list);
        }
        this.iterator = this.list.iterator();
    }

    private Iterator<File> iterator;
    private List<File> list;

    public boolean hasNext() {
        if (!iterator.hasNext()) {
            if (isRewindOnEndOfList) {
                if (isReReadDirectory) {
                    list = getDirectoryListing();
                }

                if (isRandomOrder) {
                    shuffleList(list);
                }

                iterator = list.iterator();
            }
        }
        return iterator.hasNext();
    }

    public File next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Removing is not supported for this iterator");
    }

    protected List<File> getDirectoryListing() {
        try {
            return getDirectoryListing(new File(srcDir), isRecursiveListing);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void shuffleList(List<File> list) {
        Collections.shuffle(list, new Random(System.currentTimeMillis()));
    }

    public static List<File> getDirectoryListing(File baseDir, boolean isRecursiveListing) throws FileNotFoundException {
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
                List<File> nestedListing = getDirectoryListing(file, true);
                if (!nestedListing.isEmpty()) {
                    resultList.addAll(nestedListing);
                }
            } else if (!isDirectory) {
                resultList.add(file);
            }
        }

        return resultList;
    }
}
