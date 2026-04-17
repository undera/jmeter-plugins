package com.adaptive.jmeter.plugins;

import org.junit.Before;
import org.junit.Test;
import java.io.*;

import static org.junit.Assert.*;

public class AdaptiveTimerFromCSVTest {

    private AdaptiveTimerFromCSV timer;
    private String testCsvFile;

    @Before
    public void setUp() throws IOException {
        timer = new AdaptiveTimerFromCSV();
        
        // Create test CSV file with new format
        testCsvFile = System.getProperty("java.io.tmpdir") + File.separator + "test-adaptive.csv";
        try (FileWriter fw = new FileWriter(testCsvFile)) {
            fw.write("1,00:10,100\n");
            fw.write("2,01:00,500\n");
            fw.write("3,02:00,1000\n");
        }
    }

    @Test
    public void testTimerCreation() {
        assertNotNull(timer);
    }

    @Test
    public void testCsvFilePathProperty() {
        timer.setCsvFilePath("path/to/file.csv");
        assertEquals("path/to/file.csv", timer.getCsvFilePath());
    }

    @Test
    public void testThreadProperties() {
        timer.setMinThreads(1);
        timer.setMaxThreads(50);

        assertEquals(1, timer.getMinThreads());
        assertEquals(50, timer.getMaxThreads());
    }

    @Test
    public void testAdjustmentProperties() {
        timer.setAdjustmentIntervalMs(3000);
        timer.setRampUpStep(2);
        timer.setRampDownStep(1);
        timer.setP90ThresholdMs(300);

        assertEquals(3000, timer.getAdjustmentIntervalMs());
        assertEquals(2, timer.getRampUpStep());
        assertEquals(1, timer.getRampDownStep());
        assertEquals(300, timer.getP90ThresholdMs());
    }

    @Test
    public void testDefaultValues() {
        // Check default values
        assertEquals(1, timer.getMinThreads());
        assertEquals(100, timer.getMaxThreads());
        assertEquals(5000, timer.getAdjustmentIntervalMs());
        assertEquals(1, timer.getRampUpStep());
        assertEquals(1, timer.getRampDownStep());
        assertEquals(500, timer.getP90ThresholdMs());
    }

    @Test
    public void testDelayCalculation() {
        timer.setCsvFilePath(testCsvFile);
        
        // Delay should be returned
        long delay = timer.delay();
        assertTrue(delay >= 0);
    }
}
