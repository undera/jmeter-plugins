package com.blazemeter.jmeter;


import org.apache.jmeter.threads.JMeterContextService;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class DirectoryIterator {

    private final DirectoryListingConfig config;

    public DirectoryIterator(DirectoryListingConfig config) {
        this.config = config;
        list = config.getDirectoryListing();
        iterator = list.iterator();
    }

    private Iterator<File> iterator;
    private List<File> list;

    public boolean hasNext() {
        if (!iterator.hasNext()) {
            boolean isReRead = config.getReReadDirectoryOnTheEndOfList();
            boolean isRewindOnTheEnd = config.getRewindOnTheEnd();

            if (isReRead && isRewindOnTheEnd) {
                list = config.getDirectoryListing();
                iterator = list.iterator();
            } else if (isRewindOnTheEnd) {
                if (config.getRandomOrder()) {
                    DirectoryListingHelper.shuffleList(list);
                }
                iterator = list.iterator();
            } else {
                // if the end of list && !isRewindOnTheEnd
                nullifyAll();
                JMeterContextService.getContext().getThread().stop();
                return false;
            }
        }
        return true;
    }

    public File next() {
        return iterator.next();
    }

    private void nullifyAll() {
        list = null;
        iterator = null;
    }

    public static class DirectoryIteratorPool {

        private DirectoryIterator instance;

        public void init(DirectoryListingConfig config) {
            this.instance = new DirectoryIterator(config);
        }

        public DirectoryIterator get() {
            return instance;
        }

        public void nullify() {
            instance = null;
        }

    }

}
