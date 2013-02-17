package kg.apc.jmeter.vizualizers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class CorrectedResultCollector extends ResultCollector {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String INCLUDE_SAMPLE_LABELS = "include_sample_labels";
    public static final String EXCLUDE_SAMPLE_LABELS = "exclude_sample_labels";
    private static final String COMMA = ",";

    @Override
    public void testStarted() {
        setupSaving();
        super.testStarted();
    }

    private void setupSaving() {
        SampleSaveConfiguration conf = getSaveConfig();
        // please, save the threads... it's so important, but disabled by default
        conf.setThreadCounts(true);
    }

    public List<String> getList(String prop) {
        String s = getPropertyAsString(prop);
        if (s.isEmpty()) {
            return new ArrayList<String>(0);
        } else {
            return Arrays.asList(s.split(COMMA));
        }
    }

    public void setExcludeLabels(String excludeLabels) {
        setProperty(EXCLUDE_SAMPLE_LABELS, excludeLabels);
    }

    public void setIncludeLabels(String labels) {
        setProperty(CorrectedResultCollector.INCLUDE_SAMPLE_LABELS, labels);
    }
}
