package kg.apc.jmeter.vizualizers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class CorrectedResultCollector extends ResultCollector {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String INCLUDE_SAMPLE_LABELS = "include_sample_labels";
    public static final String EXCLUDE_SAMPLE_LABELS = "exclude_sample_labels";
    public static final String INCLUDE_REGEX_CHECKBOX_STATE = "include_checkbox_state";
    public static final String EXCLUDE_REGEX_CHECKBOX_STATE = "exclude_checkbox_state";
    public static final String START_OFFSET = "start_offset";
    public static final String END_OFFSET = "end_offset";
    public static final String PREFIX_LABEL = "prefix_label";
    public static final String EMPTY_FIELD = "";
    private static final String COMMA = ",";

    @Override
    public void testStarted() {
        setupSaving();
        super.testStarted();
    }

    @Override
    public void testStarted(String host) {
        setupSaving();
        super.testStarted(host);
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

    public String getRegex(String prop) {
        return getPropertyAsString(prop);
    }

    public void setExcludeLabels(String excludeLabels) {
        setProperty(CorrectedResultCollector.EXCLUDE_SAMPLE_LABELS,
                excludeLabels);
    }

    public void setIncludeLabels(String includesLabels) {
        setProperty(CorrectedResultCollector.INCLUDE_SAMPLE_LABELS,
                includesLabels);
    }

    public void setEnabledIncludeRegex(boolean prop) {
        setProperty(CorrectedResultCollector.INCLUDE_REGEX_CHECKBOX_STATE, prop);
    }

    public void setEnabledExcludeRegex(boolean prop) {
        setProperty(CorrectedResultCollector.EXCLUDE_REGEX_CHECKBOX_STATE, prop);
    }

    public long getTimeDelimiter(String prop, long indice) {
        String number = getPropertyAsString(prop);
        if (!number.isEmpty() && number.length() < 19
                && number.matches("^[1-9][0-9]*"))
            return Long.valueOf(number) * 1000;
        return indice;
    }

    public boolean getRegexChkboxState(String prop) {
        return getPropertyAsBoolean(prop);
    }

    public void setStartOffset(String startOffset) {
        setProperty(CorrectedResultCollector.START_OFFSET, startOffset);
    }

    public void setEndOffset(String endOffset) {
        setProperty(CorrectedResultCollector.END_OFFSET, endOffset);
    }

    public void setPrefixLabel(String prefixLabel) {
        setProperty(CorrectedResultCollector.PREFIX_LABEL, prefixLabel);
    }

    public String getPrefixLabel(String prop) {
        return getPropertyAsString(prop);
    }
}
