package com.blazemeter.jmeter.reporters;

import org.apache.jmeter.reporters.ResultCollector;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class FlushingResultCollector extends ResultCollector {
    private static final Logger log = LoggerFactory.getLogger(FlushingResultCollector.class);

    public FlushingResultCollector() {
        super();
        getSaveConfig().setFieldNames(true);
    }

    @Override
    public void testEnded(String host) {
        super.testEnded(host);
        try {
            // hack for JMeter < 2.12 (BUG #56807)
            ResultCollector.class.getDeclaredMethod("flushFile");
            flushFile();
        } catch (NoSuchMethodException e) {
            log.warn("Cannot flush PrintWriter to file");
        }
    }
}
