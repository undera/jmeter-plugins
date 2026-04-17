package com.adaptive.jmeter.plugins;

import java.util.*;
import java.util.concurrent.*;

/**
 * Tracks throughput metrics and calculates percentiles
 */
public class ThroughputMetrics {
    private final ConcurrentLinkedQueue<Long> responseTimes;
    private final long windowSizeMs;
    private volatile long sampleCount;
    private volatile long errorCount;
    private volatile long lastResetTime;

    public ThroughputMetrics(long windowSizeMs) {
        this.responseTimes = new ConcurrentLinkedQueue<>();
        this.windowSizeMs = windowSizeMs;
        this.lastResetTime = System.currentTimeMillis();
    }

    /**
     * Record a response time in milliseconds
     */
    public void recordResponseTime(long responseTimeMs) {
        responseTimes.offer(responseTimeMs);
        sampleCount++;
    }

    /**
     * Record an error
     */
    public void recordError() {
        errorCount++;
    }

    /**
     * Calculate current throughput (samples per second)
     */
    public double getCurrentThroughput() {
        long elapsed = System.currentTimeMillis() - lastResetTime;
        if (elapsed == 0) return 0;
        return (sampleCount * 1000.0) / elapsed;
    }

    /**
     * Calculate Nth percentile response time
     */
    public long getPercentile(double percentile) {
        if (responseTimes.isEmpty()) {
            return 0;
        }

        List<Long> sorted = new ArrayList<>(responseTimes);
        Collections.sort(sorted);

        int index = (int) Math.ceil((percentile / 100.0) * sorted.size()) - 1;
        if (index < 0) index = 0;
        if (index >= sorted.size()) index = sorted.size() - 1;

        return sorted.get(index);
    }

    /**
     * Get 90th percentile
     */
    public long get90thPercentile() {
        return getPercentile(90);
    }

    /**
     * Reset metrics for new window
     */
    public void reset() {
        responseTimes.clear();
        sampleCount = 0;
        errorCount = 0;
        lastResetTime = System.currentTimeMillis();
    }

    public long getSampleCount() {
        return sampleCount;
    }

    public long getErrorCount() {
        return errorCount;
    }

    public double getErrorRate() {
        long total = sampleCount + errorCount;
        if (total == 0) return 0;
        return (errorCount * 100.0) / total;
    }
}
