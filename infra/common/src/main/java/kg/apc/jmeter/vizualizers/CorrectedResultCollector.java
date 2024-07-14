package kg.apc.jmeter.vizualizers;

import java.util.List;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorrectedResultCollector extends ResultCollector {

    public static final String EMPTY_FIELD = "EMPTY_FIELD";
    private static final Logger log = LoggerFactory.getLogger(CorrectedResultCollector.class);
    public static final String INCLUDE_SAMPLE_LABELS = "include_sample_labels";
    public static final String EXCLUDE_SAMPLE_LABELS = "exclude_sample_labels";
    public static final String INCLUDE_REGEX_CHECKBOX_STATE = "include_checkbox_state";
    public static final String EXCLUDE_REGEX_CHECKBOX_STATE = "exclude_checkbox_state";
    public static final String START_OFFSET = "start_offset";
    public static final String END_OFFSET = "end_offset";
    public static final String PREFIX_LABEL = "prefix_label";

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
        ConfigurationHelper.setupThreadCounts(conf);
    }

    public List<String> getList(String prop) {
        return ConfigurationHelper.getList(getPropertyAsString(prop));
    }

    public String getRegex(String prop) {
        return getPropertyAsString(prop);
    }

    public void setExcludeLabels(String excludeLabels) {
        setProperty(EXCLUDE_SAMPLE_LABELS, excludeLabels);
    }

    public void setIncludeLabels(String includesLabels) {
        setProperty(INCLUDE_SAMPLE_LABELS, includesLabels);
    }

    public void setEnabledIncludeRegex(boolean prop) {
        setProperty(INCLUDE_REGEX_CHECKBOX_STATE, prop);
    }

    public void setEnabledExcludeRegex(boolean prop) {
        setProperty(EXCLUDE_REGEX_CHECKBOX_STATE, prop);
    }

    public long getTimeDelimiter(String prop, long defaultValue) {
        return ConfigurationHelper.getTimeDelimiter(getPropertyAsString(prop), defaultValue);
    }

    public boolean getRegexChkboxState(String prop) {
        return getPropertyAsBoolean(prop);
    }

    public void setStartOffset(String startOffset) {
        setProperty(START_OFFSET, startOffset);
    }

    public void setEndOffset(String endOffset) {
        setProperty(END_OFFSET, endOffset);
    }

    public void setPrefixLabel(String prefixLabel) {
        setProperty(PREFIX_LABEL, prefixLabel);
    }

    public String getPrefixLabel(String prop) {
        return getPropertyAsString(prop);
    }
}
