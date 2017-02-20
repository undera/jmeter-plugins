package com.blazemeter.jmeter;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class DirectoryListingConfigTest {

    @Test
    public void test() throws Exception {
        List<File> files = DirectoryListingConfig.getDirectoryListing("/tmp/csv", false, true);

        System.out.println();
    }
}