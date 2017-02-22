package com.blazemeter.jmeter;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DirectoryListingIterator implements Iterator<File>{

    private final String srcDir;
//    private final String destVar;
//    private final boolean useFullPath;
    private final boolean isRandomOrder;
    private final boolean isRecursiveListing;
    private final boolean isRewindOnEndOfList;
    private final boolean isIndependentList;
    private final boolean isReReadDirectory;


    public DirectoryListingIterator(String srcDir,
//                                    String destVar,
//                                    boolean useFullPath,
                                    boolean isRandomOrder,
                                    boolean isRecursiveListing,
                                    boolean isRewindOnEndOfList,
                                    boolean isIndependentList,
                                    boolean isReReadDirectory) {
        this.srcDir = srcDir;
//        this.destVar = destVar;
//        this.useFullPath = useFullPath;
        this.isRandomOrder = isRandomOrder;
        this.isRecursiveListing = isRecursiveListing;
        this.isRewindOnEndOfList = isRewindOnEndOfList;
        this.isIndependentList = isIndependentList;
        this.isReReadDirectory = isReReadDirectory;

        this.list = getDirectoryListing(isRandomOrder);
        this.iterator = this.list.iterator();
    }

    private Iterator<File> iterator;
    private List<File> list;

    public boolean hasNext() {
        if (!iterator.hasNext()) {
            if (isRewindOnEndOfList) {
                if (isReReadDirectory) {
                    list = getDirectoryListing(isRandomOrder);
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

    protected List<File> getDirectoryListing(boolean isRandomOrder) {
        try {
            final List<File> list = getDirectoryListing(new File(srcDir), isRecursiveListing);

            if (isRandomOrder) {
                shuffleList(list);
            }

            return list;
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static List<File> shuffleList(List<File> list) {
        if (list != null) {
            Collections.shuffle(list, new Random(System.currentTimeMillis()));
        }
        return list;
    }

    public static List<File> getDirectoryListing(File baseDir, boolean isRecursiveListing) throws FileNotFoundException {

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
}
