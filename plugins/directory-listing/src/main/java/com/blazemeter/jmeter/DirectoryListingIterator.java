package com.blazemeter.jmeter;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DirectoryListingIterator implements Iterator<File>{

    private final String srcDir;
//    private final String destVar;
    private final boolean useFullPath;
    private final boolean isRandomOrder;
    private final boolean isRecursiveListing;
    private final boolean isRewindOnEndOfList;
    private final boolean isIndependentList;
    private final boolean isReReadDirectory;


    public DirectoryListingIterator(String srcDir,
//                                    String destVar,
                                    boolean useFullPath,
                                    boolean isRandomOrder,
                                    boolean isRecursiveListing,
                                    boolean isRewindOnEndOfList,
                                    boolean isIndependentList,
                                    boolean isReReadDirectory) {
        this.srcDir = srcDir;
//        this.destVar = destVar;
        this.useFullPath = useFullPath;
        this.isRandomOrder = isRandomOrder;
        this.isRecursiveListing = isRecursiveListing;
        this.isRewindOnEndOfList = isRewindOnEndOfList;
        this.isIndependentList = isIndependentList;
        this.isReReadDirectory = isReReadDirectory;

        this.list = getDirectoryListing();
        this.iterator = this.list.iterator();
    }

    private Iterator<File> iterator;
    private List<File> list;

    public boolean hasNext() {
        if (!iterator.hasNext()) {

            if (isReReadDirectory && isRewindOnEndOfList) {
                list = getDirectoryListing();
                iterator = list.iterator();
            } else if (isRewindOnEndOfList) {
                if (isRandomOrder) {
                    shuffleList(list);
                }
                iterator = list.iterator();
            } else {
                // if the end of list && !isRewindOnTheEnd
                nullifyAll();
                return false;
            }
        }
        return true;
    }

    public File next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }

    private void nullifyAll() {
        list = null;
        iterator = null;
    }

    protected List<File> getDirectoryListing() {
        try {
            return getDirectoryListing(srcDir, isRandomOrder, isRecursiveListing);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
//            LOGGER.error(ex.getMessage(), ex);
        }
    }

    public static List<File> shuffleList(List<File> list) {
        if (list != null) {
            Collections.shuffle(list, new Random(System.currentTimeMillis()));
        }
        return list;
    }

    public static List<File> getDirectoryListing(String dirPath, boolean isRandomOrder, boolean isRecursiveListing) throws FileNotFoundException {

        final List<File> list = getDirectoryListing(new File(dirPath), isRecursiveListing);

        if (isRandomOrder) {
            shuffleList(list);
        }
//        TODO: remove it
//        else {
//            Collections.sort(list, new Comparator<File>() {
//                @Override
//                public int compare(File o1, File o2) {
//                    return o1.getName().compareTo(o2.getName());
//                }
//            });
//        }

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
