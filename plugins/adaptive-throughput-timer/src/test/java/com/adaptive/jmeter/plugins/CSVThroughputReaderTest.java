package com.adaptive.jmeter.plugins;

import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.List;

import static org.junit.Assert.*;

public class CSVThroughputReaderTest {

    private String testCsvFile;
    private String testTxtFile;

    @Before
    public void setUp() throws IOException {
        // Create test CSV file with new format: stepcount,mm:ss,tps
        testCsvFile = System.getProperty("java.io.tmpdir") + File.separator + "test-throughput.csv";
        try (FileWriter fw = new FileWriter(testCsvFile)) {
            fw.write("# Test CSV file\n");
            fw.write("1,00:10,100\n");
            fw.write("2,01:00,500\n");
            fw.write("3,02:30,1000\n");
            fw.write("4,05:00,100\n");
        }

        // Create test TXT file
        testTxtFile = System.getProperty("java.io.tmpdir") + File.separator + "test-throughput.txt";
        try (FileWriter fw = new FileWriter(testTxtFile)) {
            fw.write("# Test TXT file\n");
            fw.write("1,00:10,100\n");
            fw.write("2,01:00,500\n");
        }
    }

    @Test
    public void testReadCSVFile() throws IOException {
        List<CSVThroughputEntry> entries = CSVThroughputReader.readFile(testCsvFile);
        
        assertEquals(4, entries.size());
        assertEquals(100, entries.get(0).getTargetTps());
        assertEquals(500, entries.get(1).getTargetTps());
    }

    @Test
    public void testReadTxtFile() throws IOException {
        List<CSVThroughputEntry> entries = CSVThroughputReader.readFile(testTxtFile);
        
        assertEquals(2, entries.size());
        assertEquals(100, entries.get(0).getTargetTps());
        assertEquals(500, entries.get(1).getTargetTps());
    }

    @Test
    public void testParseStepCount() throws IOException {
        List<CSVThroughputEntry> entries = CSVThroughputReader.readFile(testCsvFile);
        
        assertEquals(1, entries.get(0).getStepCount());
        assertEquals(2, entries.get(1).getStepCount());
        assertEquals(3, entries.get(2).getStepCount());
        assertEquals(4, entries.get(3).getStepCount());
    }

    @Test
    public void testParseTimeFormat() throws IOException {
        List<CSVThroughputEntry> entries = CSVThroughputReader.readFile(testCsvFile);
        
        assertEquals(0, entries.get(0).getMinutes());
        assertEquals(10, entries.get(0).getSeconds());
        assertEquals(1, entries.get(1).getMinutes());
        assertEquals(0, entries.get(1).getSeconds());
    }

    @Test
    public void testGetTargetTpsForTime() throws IOException {
        List<CSVThroughputEntry> entries = CSVThroughputReader.readFile(testCsvFile);
        
        // Before any entry - should be 0
        assertEquals(0, CSVThroughputReader.getTargetTpsForTime(entries, 5000));
        
        // At 10 seconds - should be 100
        assertEquals(100, CSVThroughputReader.getTargetTpsForTime(entries, 10000));
        
        // At 30 seconds - should still be 100 (next entry is at 60s)
        assertEquals(100, CSVThroughputReader.getTargetTpsForTime(entries, 30000));
        
        // At 60 seconds - should be 500
        assertEquals(500, CSVThroughputReader.getTargetTpsForTime(entries, 60000));
        
        // At 150 seconds (2:30) - should be 1000
        assertEquals(1000, CSVThroughputReader.getTargetTpsForTime(entries, 150000));
        
        // At 300 seconds (5:00) - should be 100
        assertEquals(100, CSVThroughputReader.getTargetTpsForTime(entries, 300000));
    }

    @Test
    public void testGetTotalTimeMs() {
        CSVThroughputEntry entry = new CSVThroughputEntry(1, 1, 30, 100);
        assertEquals(90000, entry.getTotalTimeMs()); // 1*60 + 30 = 90 seconds
    }
}
