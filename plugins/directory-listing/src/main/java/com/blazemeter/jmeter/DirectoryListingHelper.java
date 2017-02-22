package com.blazemeter.jmeter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DirectoryListingHelper {

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
