package com.blazemeter.jmeter;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DirectoryListingIteratorTest {

    @Test
    public void testReRead() throws Exception {
        File rootDir = TestDirectoryListingConfigActionTest.createFileTree();

        List<File> etalonList = DirectoryListingIterator.getDirectoryListing(rootDir.getAbsoluteFile(), true);

        DirectoryListingIterator iterator = new DirectoryListingIterator(
                rootDir.getAbsolutePath(),
                false,
                true,
                true,
                true
        );

        testFlow(etalonList, iterator);

        File.createTempFile("tmpFile3_", ".csv", rootDir);

        etalonList = DirectoryListingIterator.getDirectoryListing(rootDir.getAbsoluteFile(), true);
        testFlow(etalonList, iterator);
    }

    @Test
    public void testRewindWithRandom() throws Exception {
        File rootDir = TestDirectoryListingConfigActionTest.createFileTree();

        List<File> etalonList = DirectoryListingIterator.getDirectoryListing(rootDir.getAbsoluteFile(), true);

        DirectoryListingIterator iterator = new DirectoryListingIterator(
                rootDir.getAbsolutePath(),
                true,
                true,
                true,
                false
        );

        testRandomFlow(etalonList, iterator);
        testRandomFlow(etalonList, iterator);
    }

    private void testRandomFlow(List<File> etalonList, DirectoryListingIterator iterator) {
        List<String> filesNames = new ArrayList<>(etalonList.size());

        for (File f : etalonList) {
            filesNames.add(f.getAbsolutePath());
        }

        for (int i = 0; i <  etalonList.size(); i++) {
            assertTrue(iterator.hasNext());
            String fname = iterator.next().getAbsolutePath();
            assertTrue(filesNames.contains(fname));
            filesNames.remove(fname);
        }

        assertEquals(0, filesNames.size());
    }

    private void testFlow(List<File> etalonList, DirectoryListingIterator iterator) {
        assertNotNull(etalonList);

        for (File etalonFile : etalonList) {
            assertTrue(iterator.hasNext());

            assertEquals(etalonFile.getAbsolutePath(), iterator.next().getAbsolutePath());
        }
    }
}