package com.adaptive.jmeter.plugins;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ThroughputMetricsTest {

    private ThroughputMetrics metrics;

    @Before
    public void setUp() {
        metrics = new ThroughputMetrics(1000);
    }

    @Test
    public void testRecordResponseTime() {
        metrics.recordResponseTime(100);
        metrics.recordResponseTime(200);
        metrics.recordResponseTime(150);

        assertEquals(3, metrics.getSampleCount());
    }

    @Test
    public void testRecordError() {
        metrics.recordResponseTime(100);
        metrics.recordError();
        metrics.recordError();

        assertEquals(2, metrics.getErrorCount());
    }

    @Test
    public void testGetPercentile() {
        // Add response times: 10, 20, 30, 40, 50, 60, 70, 80, 90, 100
        for (int i = 1; i <= 10; i++) {
            metrics.recordResponseTime(i * 10);
        }

        // Test 50th percentile (median)
        long p50 = metrics.getPercentile(50);
        assertTrue(p50 > 0);

        // Test 90th percentile
        long p90 = metrics.get90thPercentile();
        assertTrue(p90 >= 80); // Should be around 90
    }

    @Test
    public void testErrorRate() {
        metrics.recordResponseTime(100);
        metrics.recordResponseTime(100);
        metrics.recordError();

        double errorRate = metrics.getErrorRate();
        assertEquals(33.33, errorRate, 1.0); // ~33%
    }

    @Test
    public void testReset() {
        metrics.recordResponseTime(100);
        metrics.recordError();

        assertEquals(1, metrics.getSampleCount());
        assertEquals(1, metrics.getErrorCount());

        metrics.reset();

        assertEquals(0, metrics.getSampleCount());
        assertEquals(0, metrics.getErrorCount());
    }
}
