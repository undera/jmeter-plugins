package kg.apc.jmeter.perfmon;

import java.io.IOException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Using "null object" pattern
 */
class UnavailableAgentConnector implements PerfMonAgentConnector {

    private static final Logger log = LoggerFactory.getLogger(UnavailableAgentConnector.class);
    private IOException cause;

    UnavailableAgentConnector(IOException e) {
        cause = e;
    }

    public void setMetricType(String metric) {
        log.debug("Dropped setMetric call");
    }

    public void setParams(String params) {
        log.debug("Dropped setParams call");
    }

    public void connect() throws IOException {
        log.debug("Dropped connect call");
    }

    public void disconnect() {
        log.debug("Dropped disconnect call");
    }

    public String getLabel(boolean translateHost) {
        return cause.toString();
    }

    public void generateSamples(PerfMonSampleGenerator collector) throws IOException {
        collector.generateErrorSample(getLabel(false), cause.toString());
    }

    public void addMetric(String metric, String params, String label) {
        log.debug("Dropped addMetric call");
    }
}
