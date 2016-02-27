package com.blazemeter.jmeter.reporters;

import org.apache.jmeter.reporters.ResultCollector;

public class FlushingResultCollector extends ResultCollector {
    public FlushingResultCollector() {
        super();
        getSaveConfig().setFieldNames(true);
    }

    @Override
    public void testEnded(String host) {
        super.testEnded(host);
        flushFile();
    }
}
