package com.blazemeter.jmeter;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.lang.reflect.Field;

public class RotatingResultCollector extends ResultCollector {
    private static final Logger LOGGER = LoggingManager.getLoggerForClass();
    public static final String MAX_SAMPLES_COUNT = "maxSamplesCount";

    private int maxSamplesCount;
    private int currentSamplesCount;
    protected String filename;
    private boolean isChanging;

    @Override
    public void sampleOccurred(SampleEvent event) {
        isChanging = true;
        try {
            if (currentSamplesCount >= maxSamplesCount) {
                testEnded();
                nullifyPrintWriter();   // HACK for previous versions
                filename = getRotatedFilename(filename);
                LOGGER.info("Creating new log chunk: " + filename);
                currentSamplesCount = 0;
                testStarted();
            }

            super.sampleOccurred(event);
            if (this.isSampleWanted(event.getResult().isSuccessful())) {
                currentSamplesCount++;
            }
        } finally {
            isChanging = false;
        }
    }

    @Override
    public String getFilename() {
        if (!isChanging) {
            filename = super.getFilename();
        }
        return filename;
    }

    @Override
    public void testStarted(String host) {
        super.testStarted(host);
        this.maxSamplesCount = getMaxSamplesCountAsInt();
    }

    protected int getMaxSamplesCountAsInt() {
        try {
            return Integer.parseInt(getMaxSamplesCount());
        } catch (NumberFormatException e) {
            LOGGER.warn("Incorrect value of max samples count: " + getMaxSamplesCount());
            return Integer.MAX_VALUE;
        }
    }

    public String getMaxSamplesCount() {
        return getPropertyAsString(MAX_SAMPLES_COUNT, "");
    }

    public void setMaxSamplesCount(String maxSamplesCount) {
        setProperty(MAX_SAMPLES_COUNT, maxSamplesCount);
    }

    protected static String getRotatedFilename(String origFile) {
        String[] parts = origFile.split("[.]");
        final int length = parts.length;

        if (length > 2) {
            try {
                parts[length - 2] = String.valueOf(Integer.parseInt(parts[length - 2]) + 1);
            } catch (NumberFormatException ex) {
                LOGGER.debug("Can't cast to integer " + parts[length - 2], ex);
                parts[length - 1] = "1." + parts[length - 1];
            }
        } else {
            parts[length - 1] = "1." + parts[length - 1];
        }

        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(parts[i]);
            if (i < length - 1) {
                builder.append('.');
            }
        }

        return builder.toString();
    }

    private void nullifyPrintWriter() {
        try {
            Class<?> cls = getClass().getSuperclass();
            Field outField = cls.getDeclaredField("out");
            outField.setAccessible(true);
            // nullify out stream, that must nullify in org.apache.jmeter.reporters.ResultCollector#finalizeFileOutput()
            outField.set(this, null);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            LOGGER.error("Cannot nullify out stream", ex);
        }
    }

}
