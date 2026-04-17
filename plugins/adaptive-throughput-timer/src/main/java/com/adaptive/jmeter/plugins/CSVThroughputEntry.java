package com.adaptive.jmeter.plugins;

/**
 * Represents a single entry from the CSV file
 */
public class CSVThroughputEntry {
    private final int stepCount;
    private final int minutes;
    private final int seconds;
    private final int targetTps;

    public CSVThroughputEntry(int stepCount, int minutes, int seconds, int targetTps) {
        this.stepCount = stepCount;
        this.minutes = minutes;
        this.seconds = seconds;
        this.targetTps = targetTps;
    }

    public int getStepCount() {
        return stepCount;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getTargetTps() {
        return targetTps;
    }

    /**
     * Get total time in milliseconds
     */
    public long getTotalTimeMs() {
        return (minutes * 60 + seconds) * 1000L;
    }

    @Override
    public String toString() {
        return String.format("Step %d: %02d:%02d - %d TPS", stepCount, minutes, seconds, targetTps);
    }
}
