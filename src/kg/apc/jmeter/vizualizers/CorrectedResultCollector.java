package kg.apc.jmeter.vizualizers;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleSaveConfiguration;

/**
 *
 * @author undera
 */
public class CorrectedResultCollector extends ResultCollector {

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
}
